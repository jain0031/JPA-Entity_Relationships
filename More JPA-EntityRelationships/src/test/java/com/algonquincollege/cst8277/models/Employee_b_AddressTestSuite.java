/**********************************************************************egg*m******a******n********************
 * File: EmployeeAddressTestSuite.java
 * Course materials (19W) CST 8277
 * @author Vaibhav Jain and shadi al khalil
 *
 *
 */
package com.algonquincollege.cst8277.models;

import static com.algonquincollege.cst8277.models.TestSuiteConstants.attachListAppender;
import static com.algonquincollege.cst8277.models.TestSuiteConstants.buildEntityManagerFactory;
import static com.algonquincollege.cst8277.models.TestSuiteConstants.detachListAppender;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.algonquincollege.cst8277.models.Address_;
import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.h2.tools.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * @author Vaibhav jain
 *  fixing the order of test to be run
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Employee_b_AddressTestSuite implements TestSuiteConstants {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);
    private static final ch.qos.logback.classic.Logger eclipselinkSqlLogger =
        (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ECLIPSELINK_LOGGING_SQL);

    // test fixture(s)
    EntityManager em;
    public static EntityManagerFactory emf;
    public static Server server;
    Employee emp1;
    Employee emp4;

    /**
     * instating the entity manager 
     */
    @Before
    public  void instantiateEntityMan() {
        em = emf.createEntityManager();
        emp4=new Employee();
    }
    @BeforeClass
    public static void oneTimeSetUp() {
        try {
            logger.debug("oneTimeSetUp");
            // create in-process H2 server so we can 'see' into database
            // use "jdbc:h2:tcp://localhost:9092/mem:assignment3-testing" in Db Perspective
            // (connection in .dbeaver-data-sources.xml so should be immediately useable
            server = Server.createTcpServer().start();
            emf = buildEntityManagerFactory(_thisClaz.getSimpleName());
        }
        catch (Exception e) {
            logger.error("something went wrong building EntityManagerFactory", e);
        }
    }
    /**
     * testing no address at start menas to it should be empty database 
     */
    @Test
    public void test_a_no_Address_at_start() {
        ListAppender<ILoggingEvent> listAppender = attachListAppender(eclipselinkSqlLogger, ECLIPSELINK_LOGGING_SQL);
        Address add = em.find(Address.class, 1);

        detachListAppender(eclipselinkSqlLogger, listAppender);

        assertNull(add);
       

    }
    /**
     * testing whether the first employee is gone correct or not
     */
    @Test
    public void test_create_emp_adddress() {        
        Employee emp1= new Employee();
        emp1.setFirstName("Vaibhav");
        emp1.setLastName("Jain");
        emp1.setSalary(20000);
        Address add1= new Address(emp1,"Ottawa","Canada","K2C0B6","Ontario","1699 Baseline road");
        emp1.setEmp_address(add1);
        em.getTransaction().begin();
        em.persist(emp1);
        em.getTransaction().commit();
        add1=em.find(Address.class, 1);
       emp1=em.find(Employee.class,emp1.getId());
       
       assertNotNull(add1);
       assertNotNull(emp1);
//       Query q = em.createNamedQuery("Employee.findAll");
//       List<Employee> emp=q.getResultList();
//       for(Employee e: emp) {
//           System.out.print(e.getId()+"\t"+e.getLastName()+"\t"+e.getFirstName());
//           
//       }
    }
    /**
     * testing whether update is working with databse by changing the city
     */
    @Test
    public void test_update_employee_address() {
        Employee emp2 = new Employee();
        emp2.setFirstName("sdj");
        emp2.setLastName("lihgblka");
        emp2.setSalary(200);
        Address add2= new Address(emp2,"Ottawa","Canada","K2C0B6","Ontario","1699 Baseline road");
        emp2.setEmp_address(add2);
        em.getTransaction().begin();
        em.persist(emp2);
        em.getTransaction().commit();
        add2 = em.find(Address.class, emp2.getEmp_address().getId());
        assertEquals(add2.getCity(),"Ottawa");

        em.getTransaction().begin();
        add2.setCity("Toronto");
        em.merge(add2);
        em.getTransaction().commit();
        add2 = em.find(Address.class, emp2.getEmp_address().getId());

        assertEquals(add2.getCity(),"Toronto");
    }
    /**
     * testing the delete the address and corresponding to it whether the employee or not
     */
    @Test
    public void test_deleteEmp_Address() {
    Employee emp3=new Employee();
    emp3.setFirstName("anyone");
    emp3.setLastName("anything");
    emp3.setSalary(100);
    Address add3= new Address(emp3,"Ottawa","Canada","K2C0B6","Ontario","1699 Baseline road");
    emp3.setEmp_address(add3);
    em.getTransaction().begin();
    em.persist(emp3);
    em.getTransaction().commit();
    int i = add3.getId();
    int e=emp3.getId();
    em.getTransaction().begin();

    em.remove(emp3);
    em.getTransaction().commit();
    emp3=em.find(Employee.class,e);
    
      assertNull(emp3);
      add3=em.find(Address.class,i);
      assertNull(add3);
    
    }
    /**
     * reading the first employee address and checking whether is correct or not
     */
    @Test
    public void test_first_EmplAddress() {
        emp1=new Employee();
        Address addr3=new Address();
        addr3=em.find(Address.class,1);
        Query q = em.createNamedQuery("Employee.findAll");
        emp1=em.find(Employee.class, addr3.getId() );
        assertEquals(q.getResultList().get(0),emp1);
    }
    
    /**
     * testing employee address by using criteria builder
     */
    @Test
    public void test_first_EmplAddressby_criteria() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Address> q = cb.createQuery(Address.class);
        Root<Address> c = q.from(Address.class);
        q.select(c);
        TypedQuery<Address> e = em.createQuery(q); 
       Address address = e.getResultList().get(0);
        assertEquals("Ottawa", address.getCity());
        
    }
    
    /**
     * finding employee by city
     */
    @Test
    public void test_find_emplbyCity() {
         emp4=new Employee();
        Address add4=new Address();
        emp4.setFirstName("anyone");
        emp4.setLastName("anything");
        emp4.setSalary(100);
        add4= new Address(emp4,"Ottawa","Canada","K2C0B6","Ontario","1699 Baseline road");
        emp4.setEmp_address(add4);
        em.getTransaction().begin();
        em.persist(emp4);
        em.getTransaction().commit();
        int i=emp4.getId();
//        int a=add4.getId();
//        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Address> q = cb.createQuery(Address.class);
        Root<Address> c = q.from(Address.class);
        q.select(c).where(cb.equal(c.get(Address_.city), "Ottawa"));
        TypedQuery<Address> e = em.createQuery(q); 
       Address address = e.getResultList().get(1);
        assertEquals(i, address.getEmployee().getId());
//
//         add4=em.find(Address.class,a);
//         emp4=em.find(Employee.class, i);
//         assertEquals("anyone", address.getEmployee().getFirstName());
         
//       for(Address   ad: address) {
//          System.out.print(ad.getId()+"\t"+ad.getCity()+"\t"+ad.getState()+"\t"+ad.getEmployee().getFirstName()+"\n");
//  }        
    }
    /**finding the country of employee 
     * 
     */
    @Test
    public void test_g_findCountry_of_empl() {
        Address add4=new Address();
        emp4.setFirstName("anyone");
        emp4.setLastName("anything");
        emp4.setSalary(100);
        add4= new Address(emp4,"Ottawa","Canada","K2C0B6","Ontario","1699 Baseline road");
        emp4.setEmp_address(add4);
        em.getTransaction().begin();
        em.persist(emp4);
        em.getTransaction().commit();
        int i=emp4.getId();
        emp4=em.find(Employee.class, i);
        Address add= emp4.getEmp_address();
        assertEquals(add.getCountry(), "Canada" );
        
    }
    /**
     * testing ho many employee are living in ottawa city
     */
    @Test
    public void test_y_Ottawa_emplCount() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Address> c = q.from(Address.class);
        Expression<Integer> attributeFieldToCheck = c.get(Address_.id);

        q.multiselect(cb.count(attributeFieldToCheck)).where(cb.equal(c.get(Address_.city), "Ottawa"));
         
        Long countValue= em.createQuery(q).getSingleResult(); 
        assertEquals(4, countValue.intValue());
    }
    
    /**
      * testing ho many employee are living in country canada
     */
    @Test
    public void test_x_Canada_emplCount() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Address> c = q.from(Address.class);
        Expression<Address> attributeFieldToCheck = c.get("id");

        q.multiselect(cb.count(attributeFieldToCheck)).where(cb.equal(c.get("country"), "Canada"));
         
        Long countValue= em.createQuery(q).getSingleResult(); 
        assertEquals(5.0, countValue,0.00001);
    }
    
    /**
     * counting employee address 
     */
    @Test
    public void test_z_empladdressCount() {

        CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
        Root<Address> from = cq.from(Address.class);
       
        Expression<Address> attributeFieldToCheck = from.get("id");
        cq.multiselect(em.getCriteriaBuilder().count(attributeFieldToCheck));
        Long countValue = em.createQuery(cq).getSingleResult();

        assertEquals(7.0, countValue,0.00001);
    }
    /**
     * finding employee salary by city
     */
    @Test
    public void test_find_w_Emplsalaray_bycity() {
        Employee emp5=new Employee();
        emp5.setFirstName("kyger");
        emp5.setLastName("kfjgh");
        emp5.setSalary(500);
        Address add5=new Address();
        add5= new Address(emp5,"Vancouver","Australia","K2C0B6","British Columbia","1699 Baseline road");
        emp5.setEmp_address(add5);
        em.getTransaction().begin();
        em.persist(emp5);
        em.getTransaction().commit();
        double salary=emp5.getSalary();
//      
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Address> q = cb.createQuery(Address.class);
      Root<Address> c = q.from(Address.class);
      q.select(c).where(cb.equal(c.get("city"), "Vancouver"));
      TypedQuery<Address> e = em.createQuery(q); 
     Address address = e.getSingleResult();
      assertEquals(salary, address.getEmployee().getSalary(),0.00001);
        
//      for(Address sd: address) {
//          System.out.print(sd.getId()+"\t"+sd.getPostal()+"\t"+sd.getEmployee().getFirstName()+"\t"+sd.getEmployee().getId()+"\n");
//        
//      }    
    }

// testing if empv lives in Kempvil
    
    @Test
    public void test_find_employee_by_City() {
        Employee empv= new Employee();
        empv.setFirstName("Vaibhav2");
        empv.setLastName("Jain2");
        empv.setSalary(22000);
        Address addv= new Address(empv,"Kempvil","Canada","K2C0B7","Ontario","1697 Clyde road");
        empv.setEmp_address(addv);
        em.getTransaction().begin();
        em.persist(empv);
        em.getTransaction().commit();
        Query q = em.createQuery("Select e from Employee e where e.address.city = :c").setParameter("c", "Kempvil");
        List<Employee> l = q.getResultList();
        logger.info("Sizeeeeee "+ l.size());         
        Employee ex = l.get(0);
            assertTrue(ex.equals(empv));
        
        
    }
    
    // Testing if empg lives in the Brazil country
    @Test
    public void test_find_employees_by_country() {
        Employee empg= new Employee();
        empg.setFirstName("Vaibhav3");
        empg.setLastName("Jain3");
        empg.setSalary(21000);
        Address add3= new Address(empg,"Ottawa","Brazil","K2C0B4","Ontario","1695 WQoodek road");
        empg.setEmp_address(add3);
        em.getTransaction().begin();
        em.persist(empg);
        em.getTransaction().commit();
        
        Query q = em.createQuery("Select e from Employee e where e.address.country = :count").setParameter("count", "Brazil");
        List<Employee> c = q.getResultList();
        
        assertEquals(c.get(0), empg); 
        }
    @AfterClass
    public static void oneTimeTearDown() {
//        em = emf.createEntityManager();
//
//       em.getTransaction().begin();
//       em.createNativeQuery("Delete  From Employee").executeUpdate();
//      em.createNativeQuery("Delete  From Address").executeUpdate();
//       em.createNativeQuery("Alter table Address Alter column id restart with 1").executeUpdate();
//       em.getTransaction().commit();
      
        logger.debug("oneTimeTearDown");
        if (emf != null) {
            emf.close();
        }
        if (server != null) {
            server.stop();
        }
    }
    
    @After
    public void closingEntityMan() {
        Query q = em.createNamedQuery("Address.findAll");
        List<Address> emp=q.getResultList();
        for(Address e: emp) {
            System.out.print(e.getId()+"\t"+e.getPostal()+"\t"+e.getEmployee().getFirstName()+"\t"+e.getEmployee().getId()+"\n");
            
        }
        em.close();
    
    }

}