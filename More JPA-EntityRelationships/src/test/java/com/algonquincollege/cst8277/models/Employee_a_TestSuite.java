/**********************************************************************egg*m******a******n********************
 * File: EmployeeTestSuite.java
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

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.h2.tools.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * @author Vaibhav jain
 *below annotation is for ordering the test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Employee_a_TestSuite implements TestSuiteConstants {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);
    private static final ch.qos.logback.classic.Logger eclipselinkSqlLogger = (ch.qos.logback.classic.Logger) LoggerFactory
            .getLogger(ECLIPSELINK_LOGGING_SQL);

    // test fixture(s)
     EntityManager em;
    public static EntityManagerFactory emf;
    public static Server server;
    Employee emp1;

    @BeforeClass
    public static void oneTimeSetUp() {
        try {
            logger.debug("oneTimeSetUp");
            // create in-process H2 server so we can 'see' into database
            // use "jdbc:h2:tcp://localhost:9092/mem:assignment3-testing" in Db Perspective
            // (connection in .dbeaver-data-sources.xml so should be immediately useable
            server = Server.createTcpServer().start();
            emf = buildEntityManagerFactory(_thisClaz.getSimpleName());
            
        } catch (Exception e) {
            logger.error("something went wrong building EntityManagerFactory", e);
        }
    }

// instantiating entity manager
    @Before
    public void instantiateEntityMan() {
        em = emf.createEntityManager();
    }

    private static final String SELECT_EMPLOYEE_1 = "SELECT ID, FIRSTNAME, LASTNAME, SALARY, VERSION, ADDR_ID FROM EMPLOYEE WHERE (ID = ?)";

    /**
     * below test is run to check there is no employee at start mean database is empty
     */
    @Test
    public void test_a_no_Employees_at_start() {
        ListAppender<ILoggingEvent> listAppender = attachListAppender(eclipselinkSqlLogger, ECLIPSELINK_LOGGING_SQL);
        emp1 = em.find(Employee.class, 1);

        detachListAppender(eclipselinkSqlLogger, listAppender);

        assertNull(emp1);
        List<ILoggingEvent> loggingEvents = listAppender.list;
        assertEquals(1, loggingEvents.size());
        assertThat(loggingEvents.get(0).getMessage(), startsWith(SELECT_EMPLOYEE_1));

    }

    // C-R-U-D lifecycle
    /**
     * below test is for creating the frst employee
     */
    @Test
    public void test_b_create_employee() {
        Employee emp1 = new Employee();
        emp1.setFirstName("Vaibhav");
        emp1.setLastName("Jain");
        emp1.setSalary(20000);
        em.getTransaction().begin();
        em.persist(emp1);
        em.getTransaction().commit();

        ListAppender<ILoggingEvent> listAppender = attachListAppender(eclipselinkSqlLogger, ECLIPSELINK_LOGGING_SQL);
        emp1 = em.find(Employee.class, 1);

        detachListAppender(eclipselinkSqlLogger, listAppender);

        assertNotNull(emp1);

    }

    /**
     * testing wether the employee got updated or not by creating the new employee and testing on that
     */
    @Test
    public void test_update_employee_salary() {
        Employee emp1 = new Employee();
        emp1.setFirstName("Vaibhav");
        emp1.setLastName("Jain");
        emp1.setSalary(20000);
        em.getTransaction().begin();
        em.persist(emp1);
        em.getTransaction().commit();
        emp1 = em.find(Employee.class, emp1.getId());
        assertEquals(emp1.getSalary(), 20000,0.00001);

        em.getTransaction().begin();
        emp1.setSalary(10000);
        em.merge(emp1);
        emp1 = em.find(Employee.class, emp1.getId());
        em.getTransaction().commit();
        assertEquals(10000, emp1.getSalary(),0.0001);
    }

    /**
     * testing whether the first employee details entered are correct or not
     */
    @Test
    public void test_x_firstEmpl_details() {
        Query q = em.createNamedQuery("Employee.findAll");
        emp1=em.find(Employee.class, 1);
        assertEquals(q.getResultList().get(0),emp1);

    }

    /**
     * Test for deleting the employee
     */
    @Test
    public void test_deleteEmp() {
    Employee emp2=new Employee();
    emp2.setFirstName("anyone");
    emp2.setLastName("anything");
    emp2.setSalary(100);
    em.getTransaction().begin();
    em.persist(emp2);
    em.getTransaction().commit();
    
    em.getTransaction().begin();

    em.remove(emp2);
    em.getTransaction().commit();
    emp2=em.find(Employee.class,emp2.getId());
    assertNull(emp2);
    
    }
    
    /**
     * testing of employee who has the highest salary
     */
    @Test
    public void test_highSalary() {
        Employee emp6=new Employee();
        Employee emp3=new Employee();
        Employee emp4=new Employee();
        Employee emp5=new Employee();
        emp6.setSalary(100000);
        emp3.setSalary(200000);
        emp4.setSalary(300000);
        emp5.setSalary(400000);
        em.getTransaction().begin();
        em.persist(emp3);
        em.persist(emp4);
        em.persist(emp5);
        em.persist(emp6);
        em.getTransaction().commit();

        CriteriaQuery<Double> query = em.getCriteriaBuilder().createQuery(Double.class);
        Root<Employee> rootClass = query.from(Employee.class);
       
        Expression<Double> attributeFieldToCheck = rootClass.get("salary");
        query.select(em.getCriteriaBuilder().max(attributeFieldToCheck));
        Double maxValue = (double) 0;
        try {
          maxValue = em.createQuery(query).getSingleResult();
        }
        catch (NoResultException nre) {
            maxValue = (double) 0;
          }
        assertEquals(400000, maxValue,0.00001);
    }
    
    /**
     * testing for employee who has owest salary
     */
    @Test 
    public void test_minsalary() {
        
        CriteriaQuery<Double> query = em.getCriteriaBuilder().createQuery(Double.class);
        Root<Employee> rootClass = query.from(Employee.class);
       
        Expression<Double> attributeFieldToCheck = rootClass.get("salary");
        query.select(em.getCriteriaBuilder().min(attributeFieldToCheck));
         Double minValue = (double) 0;
        try {
          minValue = em.createQuery(query).getSingleResult();
        }
        catch (NoResultException nre) {
            minValue = (double) 0;
          }
        assertEquals(20000, minValue,0.00001);
   
    }
    /**
     * testing for average salary of alll employee
     */
    @Test 
    public void test_w_avgsalary() {
        double s=0;
        int i=0;
        CriteriaQuery<Double> query = em.getCriteriaBuilder().createQuery(Double.class);
        Root<Employee> rootClass = query.from(Employee.class);
       
        Expression<Double> attributeFieldToCheck = rootClass.get("salary");
        query.select(em.getCriteriaBuilder().avg(attributeFieldToCheck));
         Double avgValue = (double) 0;
        try {
          avgValue = em.createQuery(query).getSingleResult();
        }
        catch (NoResultException nre) {
            avgValue = (double) 0;
          }
        Query q = em.createNamedQuery("Employee.findAll");
        List<Employee> emp=q.getResultList();
        for(Employee e: emp) {
            s=s+e.getSalary();            
        i=i+1;
        }
        double avg=s/i;
        assertEquals(avg, avgValue,0.00001);
   
    }
    /**
     * finding employee by salary
     */
    @Test
    public void test_c_find_employee_by_salary() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
        Root<Employee> c = q.from(Employee.class);
        q.select(c).where(cb.equal(c.get(Employee_.salary), 20000));
        TypedQuery<Employee> e = em.createQuery(q);
        emp1=em.find(Employee.class, 1);
        Employee ee=e.getSingleResult();
        assertEquals(ee,emp1);
    }

    /**
     * finding employee by first name
     */
    @Test
    public void test_d_find_employee_by_firstname() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
        Root<Employee> c = q.from(Employee.class);
        q.select(c).where(cb.equal(c.get("firstName"), "Vaibhav"));
        TypedQuery<Employee> e = em.createQuery(q);
        emp1=em.find(Employee.class, 1);
        Employee ee=e.getSingleResult();
        assertEquals(ee,emp1);
    }

    /**
     * finding employee by lastname
     */
    @Test
    public void test_e_find_employee_by_lastname() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
        Root<Employee> c = q.from(Employee.class);
        q.select(c).where(cb.equal(c.get("lastName"), "Jain"));
        TypedQuery<Employee> e = em.createQuery(q);
        emp1=em.find(Employee.class, 1);
        Employee ee=e.getSingleResult();
        assertEquals(ee,emp1);
    }

    /**
     * counting emplyee whose last name is null
     */
    @Test
    public void test_z_find_employee_count_lastnameNull() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Employee> c = q.from(Employee.class);
        Expression<Employee> attributeFieldToCheck = c.get("id");

        q.multiselect(cb.count(attributeFieldToCheck)).where(cb.isNull(c.get("lastName")));
         
        Long countValue= em.createQuery(q).getSingleResult(); 
        assertEquals(4.0, countValue,0.00001);
        
        
        
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//
//        CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
//        Root<Employee> c = q.from(Employee.class);
//        q.select(c).where(cb.equal(c.get("lastName"), null));
//        TypedQuery<Employee> e = em.createQuery(q);
//        emp1=em.find(Employee.class, 3);
//        List<Employee> ee=e.getResultList();
//        for(Employee gh:ee) {
//            System.out.print(gh.getId()+"\t"+gh.getFirstName()+"\t"+gh.getLastName()+"\t"+gh.getSalary()+"\n");
//        }
//        assertEquals(ee,emp1);
    }
   /**
 *  test to count all the employee have been added till now
 */
@Test
    public void test_y_emplpyeeCount() {

        CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
        Root<Employee> from = cq.from(Employee.class);
       
        Expression<Employee> attributeFieldToCheck = from.get("id");
        cq.multiselect(em.getCriteriaBuilder().count(attributeFieldToCheck));
        Long countValue = em.createQuery(cq).getSingleResult();

        assertEquals(6.0, countValue,0.00001);
    }
    
    /**
     * shutting everything down
     */
    @AfterClass
    public static void oneTimeTearDown() {
//        em = emf.createEntityManager();
//
//       em.getTransaction().begin();
//       em.createNativeQuery("Delete  From Employee").executeUpdate();
//       em.createNativeQuery("Alter table Employee Alter column id restart with 1").executeUpdate();
//       em.getTransaction().commit();
       
        logger.debug("oneTimeTearDown");
        if (emf != null) {
            emf.close();
        }
        if (server != null) {
            server.stop();
        }

    }

    /**
     * closing entity manager
     */
    @After
    public void closingEntityMan() {
        Query q = em.createNamedQuery("Employee.findAll");
        List<Employee> emp=q.getResultList();
        for(Employee e: emp) {
            System.out.print(e.getId()+"\t"+e.getFirstName()+"\t"+e.getLastName()+"\t"+e.getSalary()+"\n");
            
        }
        em.close();

    }

}