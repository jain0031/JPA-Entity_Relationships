/**********************************************************************egg*m******a******n********************
 * File: EmployeePhonesSuite.java
 * Course materials (19W) CST 8277
 * @author Vaibhav Jain and Shadi al khalil
 * (Modified) @date 2019 30
 *
 *
 */
package com.algonquincollege.cst8277.models;
/**
* File: EmployeePhonesTestSuite.java
* Date Created: March 24, 2019
* @author Vaibhav Jain, Shadi Al Khalil 
*/
import static com.algonquincollege.cst8277.models.TestSuiteConstants.attachListAppender;
import static com.algonquincollege.cst8277.models.TestSuiteConstants.buildEntityManagerFactory;
import static com.algonquincollege.cst8277.models.TestSuiteConstants.detachListAppender;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.h2.tools.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class Employee_c_PhonesTestSuite implements TestSuiteConstants {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);
    private static final ch.qos.logback.classic.Logger eclipselinkSqlLogger =
        (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ECLIPSELINK_LOGGING_SQL);

    // test fixture(s)
    EntityManager em;
    public static EntityManagerFactory emf;
    public static Server server;

    

    @Before
    public void instantiateEntityMan() {
        em = emf.createEntityManager();
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

    private static final String SELECT_PHONE_1 = "SELECT ID, AREACODE, PHONENUMBER, VERSION, owning_emp_id FROM PHONE WHERE (ID = ?)";
    
    // Making sure that there is no phone in the phone table by checking the first primary key 
    //if it is exist so the expected return is to be null
    
    @Test
    public void test_a_no_Phones_at_start() {
        Phone phone1=new Phone();
        ListAppender<ILoggingEvent> listAppender = attachListAppender(eclipselinkSqlLogger, ECLIPSELINK_LOGGING_SQL);
        phone1 = em.find(Phone.class, 1);

        detachListAppender(eclipselinkSqlLogger, listAppender);

        assertNull(phone1);
        List<ILoggingEvent> loggingEvents = listAppender.list;
        assertEquals(1, loggingEvents.size());
        assertThat(loggingEvents.get(0).getMessage(), startsWith(SELECT_PHONE_1));

    }
    
    // Creating first phone and its employee owner and checking if it was inserted in the database
    @Test
    public void test_b_create_emp_phone() {

        Employee emp = new Employee();
        emp.setFirstName("shadi");
        emp.setLastName("al");
        Phone phone1 = new Phone();
        phone1.setAreaCode("613");
        phone1.setPhoneNumber("555555");
        phone1.setEmployee(emp);
        em.getTransaction().begin();
        em.persist(emp);
        em.persist(phone1);
        em.getTransaction().commit();

        ListAppender<ILoggingEvent> listAppender = attachListAppender(eclipselinkSqlLogger, ECLIPSELINK_LOGGING_SQL);
        phone1 = em.find(Phone.class, phone1.getId());
        emp = em.find(Employee.class, phone1.getId());
        detachListAppender(eclipselinkSqlLogger, listAppender);

        assertEquals(phone1, em.find(Phone.class, phone1.getId()));
        assertEquals(emp, em.find(Employee.class, phone1.getId()));

    }
    
    // changing the phone number and save it and check if new new number in the database is the number that was inserted
    @Test
    public void test_ca_update_phone() {
        Employee emp3 = new Employee();
        emp3.setFirstName("shadi");
        emp3.setLastName("al");
        Phone phone3 = new Phone();
        phone3.setAreaCode("613");
        phone3.setPhoneNumber("555555");
        phone3.setEmployee(emp3);
        em.getTransaction().begin();
        em.persist(emp3);
        em.persist(phone3);
        em.getTransaction().commit();

        em.getTransaction().begin();
        phone3.setPhoneNumber("444444");;
        em.merge(phone3);
        em.getTransaction().commit();
        phone3 = em.find(Phone.class, phone3.getId());

        assertEquals(phone3.getPhoneNumber(),phone3.getPhoneNumber());
    }
    
    //inserting new phones to the table and then checking the total number
    @Test
    public void test_cf_count_phone() {
        Employee emp8 = new Employee();
        emp8.setFirstName("Mark");
        emp8.setLastName("Clark");
        Phone phone8 = new Phone();
        Phone phone9 = new Phone();
        phone8.setAreaCode("613");
        phone8.setPhoneNumber("888888");
        phone8.setAreaCode("613");
        phone9.setPhoneNumber("999999");
        phone8.setEmployee(emp8);
        phone9.setEmployee(emp8);
        em.getTransaction().begin();
        em.persist(emp8);
        em.persist(phone8);
        em.persist(phone9);
        em.getTransaction().commit();
        em.getTransaction().begin();
        em.getTransaction().commit();
        Long count= (Long) em.createNativeQuery("SELECT count(*) FROM Phone").getSingleResult();  
        assertEquals(6.0,count, 0.0002);
    }
    
    // Giving employee10 2 phone numbers and then checking that number of phones in the table
    @Test
    public void test_cg_howManyPhoneNumberForEmp10() {
        Employee emp10 = new Employee();
        emp10.setFirstName("Mark");
        emp10.setLastName("Clark");
        Phone phone8 = new Phone();
        Phone phone9 = new Phone();
        phone8.setAreaCode("613");
        phone8.setPhoneNumber("888888");
        phone8.setAreaCode("613");
        phone9.setPhoneNumber("999999");
        phone8.setEmployee(emp10);
        phone9.setEmployee(emp10);
        em.getTransaction().begin();
        em.persist(emp10);
        em.persist(phone8);
        em.persist(phone9);
        em.getTransaction().commit();
        em.getTransaction().begin();
        em.getTransaction().commit();
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Phone> root = criteriaQuery.from(Phone.class);
        Expression<Phone> attributeFieldToCheck = root.get("id");
        criteriaQuery.multiselect(criteriaBuilder.count(attributeFieldToCheck))
        .where(criteriaBuilder.equal(root.get("employee"), emp10));
        Long count= em.createQuery(criteriaQuery).getSingleResult(); 
        assertEquals(2.0, count,0.00001);
    }
    
    //Checking if the deleted phone number is removed from the table and 
    // therefore the value should be null
    @Test
    public void test_cb_delete_phone() {
        Employee emp4 = new Employee();
        emp4.setFirstName("shadi");
        emp4.setLastName("al");
        emp4.setSalary(900);
        Phone phone4 = new Phone();
        phone4.setAreaCode("613");
        phone4.setPhoneNumber("555444");
        phone4.setEmployee(emp4);
        em.getTransaction().begin();
        em.persist(emp4);
        em.persist(phone4);
        em.getTransaction().commit();

        em.getTransaction().begin();
        em.remove(phone4);
        em.getTransaction().commit();
        phone4 = em.find(Phone.class, phone4.getId());

        assertNull(phone4);
    }
    
    //deleting the phone first then deleting the owner of the phone
    @Test
    public void test_cc_delete_emp_after_deleting_phone() {
        Employee emp5 = new Employee();
        emp5.setFirstName("Someone");
        emp5.setLastName("al");
        emp5.setSalary(900);
        Phone phone5 = new Phone();
        phone5.setAreaCode("613");
        phone5.setPhoneNumber("555446");
        phone5.setEmployee(emp5);
        em.getTransaction().begin();
        em.persist(emp5);
        em.persist(phone5);
        em.getTransaction().commit();

        em.getTransaction().begin();
        em.remove(phone5);
        em.getTransaction().commit();
        em.getTransaction().begin();
        em.remove(emp5);
        em.getTransaction().commit();
        phone5 = em.find(Phone.class, phone5.getId());
        emp5 = em.find(Employee.class, emp5.getId());
        assertNull(phone5);
        assertNull(emp5);
    }
    // Finding employee by knowing the phone id
    @Test
    public void test_first_emp_phoneByPhoneID() {
        Employee emp6=new Employee();
        Phone phone6=new Phone();
        phone6=em.find(Phone.class,1);
        Query query = em.createNamedQuery("Employee.findAll");
        emp6=em.find(Employee.class, phone6.getId() );
        assertEquals(query.getResultList().get(0),emp6);
    }
    
    // assuring the phone number that we expect by knowing the phone id
    @Test
    public void test_criteria_first_emp_phone() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Phone> criteriaQuery = criteriaBuilder.createQuery(Phone.class);
        Root<Phone> root = criteriaQuery.from(Phone.class);
        criteriaQuery.select(root).orderBy(criteriaBuilder.desc(root.get(Phone_.phoneNumber)));
        TypedQuery<Phone> e = em.createQuery(criteriaQuery); 
       Phone phone = e.getResultList().get(0);
        assertEquals("999999", phone.getPhoneNumber());
        
    }
    // finding phone number using the employment id
    @Test
    public void test_find_phoneNumberByEmployeeId() {
        List<Phone> phs=new ArrayList<Phone>();
        Employee emp7=new Employee();
        Phone phone7=new Phone();
        emp7.setFirstName("Mike");
        emp7.setLastName("Prof");
        emp7.setSalary(10000);
        phone7.setAreaCode("613");
        phone7.setPhoneNumber("111111");
        phone7.setEmployee(emp7);
        phs.add(phone7);
        emp7.setPhones(phs);
        em.getTransaction().begin();
        em.persist(emp7);
        em.persist(phone7);
        em.getTransaction().commit();
//        int a=add4.getId();
//        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.multiselect(root).where(criteriaBuilder.equal(root.get("id"), 1));
        TypedQuery<Employee> typedQuery = em.createQuery(criteriaQuery); 
        emp7 = typedQuery.getResultList().get(0);
        assertEquals("111111", emp7.getPhones().get(0).getPhoneNumber());
//
//         add4=em.find(Address.class,a);
//         emp4=em.find(Employee.class, i);
//         assertEquals("anyone", address.getEmployee().getFirstName());
         
//       for(Address   ad: address) {
//          System.out.print(ad.getId()+"\t"+ad.getCity()+"\t"+ad.getState()+"\t"+ad.getEmployee().getFirstName()+"\n");
//  }        
    }
    
    
// serching for a phone using like clause. 
@Test
public void test_f_findingPhoneUsingLike() {
    Employee emp9 = new Employee();
    emp9.setFirstName("Paul");
    emp9.setLastName("Algo");
    emp9.setSalary(500);
    em.getTransaction().begin();
    em.persist(emp9);
    em.getTransaction().commit();
    Phone phone10 = new Phone();
    Phone phone11 = new Phone();
    phone10.setAreaCode("613");
    phone10.setPhoneNumber("888810");
    phone11.setAreaCode("613");
    phone11.setPhoneNumber("999911");
    phone10.setEmployee(emp9);
    phone11.setEmployee(emp9);
    em.getTransaction().begin();
    em.persist(phone10);
    em.persist(phone11);
    em.getTransaction().commit();
    
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaQuery<Phone> criteriaQuery = criteriaBuilder.createQuery(Phone.class);
    Root<Phone> root = criteriaQuery.from(Phone.class);
    List<Phone> p =em.createNativeQuery("select * from Phone", Project.class).getResultList();
    logger.info("The is the result "+p.toString());
    Predicate predicate = criteriaBuilder.like(root.<String>get(Phone_.phoneNumber), "%881%");
    criteriaQuery.where(predicate);
    criteriaQuery.multiselect(root);
    TypedQuery<Phone> query = em.createQuery(criteriaQuery);
    List<Phone> result = query.getResultList();
    assertEquals(phone10, result.get(0));
}
    
    @After
    public void closingEntityMan() {
        em.close();
    }

    @AfterClass
    public static void oneTimeTearDown() {

        logger.debug("oneTimeTearDown");
        if (emf != null) {
            emf.close();
        }
        if (server != null) {
            server.stop();
        }
    }

}
