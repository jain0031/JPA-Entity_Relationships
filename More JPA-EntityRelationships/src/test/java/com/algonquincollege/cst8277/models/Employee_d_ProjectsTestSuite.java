/**********************************************************************egg*m******a******n********************
 * File: EmployeePhonesSuite.java
 * Course materials (19W) CST 8277
 * @author Vaibhav Jain and Shadi al khalil
 * (Modified) @date 2019 30
 *
 *
 */
package com.algonquincollege.cst8277.models;

import static com.algonquincollege.cst8277.models.TestSuiteConstants.buildEntityManagerFactory;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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


/**
* File: EmployeeProjectsTestSuite.java
* Date Created: March 24, 2019
* @author Vaibhav Jain, Shadi Al Khalil 
*/
public class Employee_d_ProjectsTestSuite implements TestSuiteConstants {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);
    private static final ch.qos.logback.classic.Logger eclipselinkSqlLogger =
        (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ECLIPSELINK_LOGGING_SQL);

    // test fixture(s)
    public static EntityManagerFactory emf;
    public static Server server;
    EntityManager em;
    

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
    @Before
    public void instantiateEntityMan() {
        em = emf.createEntityManager();
    }
    // Creating a project while there is no employees associated into that project
    @Test
    public void test_b_create_projectWhileEmpIsNull() {

        Project pro1 = new Project();
        pro1.setName("First project");;
        pro1.setDescription("starting");
        pro1.setEmployees(null);
        em.getTransaction().begin();
        em.persist(pro1);
        em.getTransaction().commit();
        logger.info("thie is the retruned value "+em.find(Project.class, pro1.getId()).getName());
       Project ppro1 = em.find(Project.class, pro1.getId());

        assertEquals(pro1, ppro1);

    }
    
    // Creating employee by executing CriteriaQuery
    @Test
    public void test_criteria_first_emp_project() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = criteriaQuery.from(Project.class);
        criteriaQuery.select(root);
        TypedQuery<Project> e = em.createQuery(criteriaQuery); 
       Project proj = e.getResultList().get(0);
       logger.info("Thie is the name of our project "+proj.id);
        assertNotNull(proj);
        
    }
     
    // Checking if empG is working on two projects only 
    @Test
    public void test_bc_NumberOfProjectThat_empG_workOn() {


        Project proF = new Project();
        Project proG = new Project();
        Project proJ = new Project();
        
        proF.setName("F project");;
        proF.setDescription("starting");
        proG.setName("G project");;
        proG.setDescription("starting");
        proJ.setName("Ffirst project");;
        proJ.setDescription("starting");
        
        List<Project> projectsF = Collections.singletonList(proJ);
        List<Project> projectsG = Arrays.asList(proF, proG);

        Employee empF = new Employee();
        Employee empG = new Employee();
          empF.setFirstName("Rob");
          empF.setLastName("Alg");
          List<Employee> employeesF = Collections.singletonList(empF);
          List<Employee> employeesG = Collections.singletonList(empG);

          empF.setSalary(500);
          empG.setFirstName("San");
          empG.setLastName("Wood");
          empG.setSalary(1000);
          empF.setProjects(projectsF);
          empG.setProjects(projectsG);
          proF.setEmployees(employeesF);
          proG.setEmployees(employeesF);
          proJ.setEmployees(employeesG);
          em.getTransaction().begin();
          em.persist(empF);
          em.persist(empG);
          em.getTransaction().commit();

          Query q = em.createQuery("select proj.name from Project proj join proj.employees e where e.id = :empId");
          q.setParameter("empId", empG.getId());
          List<String> names = q.getResultList();
          //logger.info("proF.getName() "+proF.getName()+" -" +"proG.getName() "+proG.getName()+" -" +str.get(0)+" The nname is "+str.get(1)+"cccc "+str.size());
          assertThat(names, hasSize(2));
    }
    
    // This test tests how many employees work on project f
    @Test
    public void test_get_NumberOFEmpWhoWorkOn_F_Project() {
        Project proF = new Project();
        Project proG = new Project();
        Project proJ = new Project();
        
        proF.setName("F project");;
        proF.setDescription("starting");
        proG.setName("G project");;
        proG.setDescription("starting");
        proJ.setName("Ffirst project");;
        proJ.setDescription("starting");
        
        List<Project> projectsF = Collections.singletonList(proJ);
        List<Project> projectsG = Arrays.asList(proF, proG);

        Employee empF = new Employee();
        Employee empG = new Employee();
          empF.setFirstName("Rob");
          empF.setLastName("Alg");
          List<Employee> employeesF = Collections.singletonList(empF);
          List<Employee> employeesG = Collections.singletonList(empG);

          empF.setSalary(500);
          empG.setFirstName("San");
          empG.setLastName("Wood");
          empG.setSalary(1000);
          empF.setProjects(projectsF);
          empG.setProjects(projectsG);
          proF.setEmployees(employeesF);
          proG.setEmployees(employeesF);
          proJ.setEmployees(employeesG);
          em.getTransaction().begin();
          em.persist(empF);
          em.persist(empG);
          em.getTransaction().commit();
          
        Query q = em.createQuery("Select p.employees from Project p where p.name = 'F project'");
        
        List<Employee> result = q.getResultList();
        
        assertEquals(2, result.size());
        
        
    }
    
    // testing if the employer Sab work on 2 projects
    @Test
    public void test_getProjects_by_employee() {
        Project proF = new Project();
        Project proG = new Project();
        Project proJ = new Project();
        
        proF.setName("F project");;
        proF.setDescription("starting");
        proG.setName("G project");;
        proG.setDescription("starting");
        proJ.setName("Ffirst project");;
        proJ.setDescription("starting");
        
        List<Project> projectsF = Collections.singletonList(proJ);
        List<Project> projectsG = Arrays.asList(proF, proG);

        Employee empF = new Employee();
        Employee empG = new Employee();
          empF.setFirstName("Rob");
          empF.setLastName("Alg");
          List<Employee> employeesF = Collections.singletonList(empF);
          List<Employee> employeesG = Collections.singletonList(empG);

          empF.setSalary(500);
          empG.setFirstName("Sab");
          empG.setLastName("Wood");
          empG.setSalary(1000);
          empF.setProjects(projectsF);
          empG.setProjects(projectsG);
          proF.setEmployees(employeesF);
          proG.setEmployees(employeesF);
          proJ.setEmployees(employeesG);
          em.getTransaction().begin();
          em.persist(empF);
          em.persist(empG);
          em.getTransaction().commit();
        Query q = em.createQuery("Select e.projects from Employee e where e.firstName = 'Sab'");
        
        List<Project> result = q.getResultList();
        
        assertEquals(2, result.size());

        
    }
    
    // Testing the phone number of the employees who work on project w- In our case we have only 1 employee
    @Test
    public void test_bc_whatIsThePhoneNumberOfThePersonWhoWorkOnProW() {

        Phone phoneW = new Phone();
        Project proW = new Project();
        phoneW.setAreaCode("613");
        phoneW.setPhoneNumber("5858588");
        
        proW.setName("W project");;
        proW.setDescription("Wstarting");
        
        List<Project> projectsW = Collections.singletonList(proW);

        Employee empW = new Employee();
          empW.setFirstName("Rob");
          empW.setLastName("Alg");
          List<Employee> employeesW = Collections.singletonList(empW);
          List<Phone> phonesW = Collections.singletonList(phoneW);

          empW.setSalary(500);
          proW.setEmployees(employeesW);
          empW.setProjects(projectsW);
          phoneW.setEmployee(empW);
          empW.setPhones(phonesW);
          em.getTransaction().begin();
          em.persist(empW);
          em.persist(phoneW);
          em.getTransaction().commit();
          
         logger.info("dddddddddddddddddddddd");
          Query q = em.createQuery("select p.phoneNumber From Phone p join p.employee e "
                  + "join e.projects proj where proj.id = :projid");

          q.setParameter("projid", proW.getId());
          List<List> names = q.getResultList();
          assertEquals(phoneW.getPhoneNumber(),names.get(0));
    }
    
    // Checking the salary of an emoloyee by knowing foreign key of that employee in the Project table
//@Test
//public void test_bc_whatIsTheSalaryOfTheEmpWhoWorkOnProH() {
//
//
//    Project proF = new Project();
//    Project proG = new Project();
//    Project proH = new Project();
//    
//    proF.setName("F project");;
//    proF.setDescription("starting");
//    proG.setName("G project");;
//    proG.setDescription("starting");
//    proH.setName("Ffirst project");;
//    proH.setDescription("starting");
//    
//    List<Project> projectsH = Collections.singletonList(proH);
//    List<Project> projectsG = Arrays.asList(proF, proG);
//
//    Employee empH = new Employee();
//    Employee empG = new Employee();
//      empH.setFirstName("Fred");
//      empH.setLastName("Alg");
//      List<Employee> employeesF = Collections.singletonList(empH);
//      List<Employee> employeesG = Collections.singletonList(empG);
//
//      empH.setSalary(3500);
//      empG.setFirstName("San");
//      empG.setLastName("Wood");
//      empG.setSalary(1000);
//      empH.setProjects(projectsH);
//      empG.setProjects(projectsG);
//      proF.setEmployees(employeesF);
//      proG.setEmployees(employeesF);
//      proH.setEmployees(employeesG);
//      em.getTransaction().begin();
//      em.persist(empH);
//      em.persist(empG);
//      em.getTransaction().commit();
//      Query query = em.createNativeQuery("SELECT Employee.Salary\n" + 
//              "FROM Employee\n" + 
//              "left JOIN EMP_PROJ\n" + 
//              "ON EMP_PROJ.Emp_ID = Employee.id\n" + 
//              "left JOIN Project\n" + 
//              "ON EMP_PROJ.PROJ_ID = Project.id\n" + 
//              "where Project.id=?; ");
//      query.setParameter(1, proH.getId());
//      double i =  (double) query.getSingleResult();
//      logger.info("empH.getSalary() "+empH.getSalary()+" -"+i);
//
//      assertEquals((double)empH.getSalary(), i, 00001);
//}
    
// Checking if when I delete an Employee it deletes also the project
    @Test
    public void test_bc_deletingEmpDeleteProAlso() {


        Project proD = new Project();
        
        proD.setName("D project");;
        proD.setDescription("starting");

        Employee empD = new Employee();
          empD.setFirstName("Bob");
          empD.setLastName("Algo");
          List<Employee> employeesD = Collections.singletonList(empD);
          List<Project> projectsD = Collections.singletonList(proD);

          empD.setSalary(600);
          empD.setProjects(projectsD);
          proD.setEmployees(employeesD);
          em.getTransaction().begin();
          em.persist(empD);
          em.getTransaction().commit();
          em.getTransaction().begin();
          em.remove(empD);
          em.getTransaction().commit();
          assertNull(em.find(Project.class, proD.getId()));
    }
    
    // Deleting project does not delete also the emoloyee because of cascading is not allowed
    @Test
    public void test_bc_deletingProjDoesNotDeleteEmp() {


        Project proE = new Project();
        
        proE.setName("E project");;
        proE.setDescription("starting");

        Employee empE = new Employee();
          empE.setFirstName("Ja");
          empE.setLastName("Algo");
          List<Employee> employeesD = Collections.singletonList(empE);
          List<Project> projectsD = Collections.singletonList(proE);

          empE.setSalary(600);
          empE.setProjects(projectsD);
          proE.setEmployees(employeesD);
          em.getTransaction().begin();
          em.persist(empE);
          em.getTransaction().commit();
          em.getTransaction().begin();
          em.remove(proE);
          em.getTransaction().commit();
          assertNotNull(em.find(Employee.class, empE.getId()));
    }

    // Knowing a part of the project description is enough to know and to retrieve the project from the Project table
    @Test
    public void test_baa_findingProjectUsingLike() {
        Project pro3 = new Project();
        Project pro4 = new Project();
        Project pro5 = new Project();
        pro3.setName("Third project");;
        pro4.setName("Forth project");;
        pro5.setName("Fifth project");;
        pro3.setDescription("Software");
        pro4.setDescription("Hardware");
        pro5.setDescription("Maintainance");
        em.getTransaction().begin();
        em.persist(pro3);
        em.persist(pro4);
        em.persist(pro5);
        em.getTransaction().commit();
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = criteriaQuery.from(Project.class);
        List<Project> p =em.createNativeQuery("select * from project", Project.class).getResultList();
        logger.info("The is the result "+p.toString());
        Predicate predicate = criteriaBuilder.like(root.<String>get(Project_.description), "%dware%");
        criteriaQuery.where(predicate);
        criteriaQuery.multiselect(root);
        TypedQuery<Project> query = em.createQuery(criteriaQuery);
        List<Project> result = query.getResultList();
        logger.info("Checking the size. "+result.size());
        assertEquals(pro4, result.get(0));
    }
    
    
    // Deleting a project by knowing the description of it
    @Test
    public void test_baa_deletingProject() {
        Project pro7 = new Project();
        pro7.setName("Seventh project");;
        pro7.setDescription("Software7");
        em.getTransaction().begin();
        em.persist(pro7);
        em.getTransaction().commit();
        
        CriteriaBuilder criteriaBuilder  = em.getCriteriaBuilder();
        em.getTransaction().begin();
        CriteriaDelete<Project> delete = criteriaBuilder.createCriteriaDelete((Project.class));
        Root<Project> root = delete.from(Project.class);
        Predicate pred =
                criteriaBuilder.equal(root.get(Project_.description),"Software7");
                delete.where(pred);
                em.createQuery(delete).executeUpdate();
                em.getTransaction().commit();

        assertNull(em.find(Project.class, pro7.getId()));
    }
    
    // updating the description of a project from Software7 to Software8
    @Test
    public void test_bab_updatingProjectDescription() {
        Project pro8 = new Project();
        pro8.setName("Seventh project");;
        pro8.setDescription("Software7");
        em.getTransaction().begin();
        em.persist(pro8);
        em.getTransaction().commit();

        em.getTransaction().begin();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Project> q = cb.createCriteriaUpdate(Project.class);
        Root<Project> root = q.from(Project.class);
        Predicate predicate = cb.equal(root.<Integer>get(Project_.id), pro8.getId());
        q.where(predicate);
        q.set(Project_.description, "Software8");
        Query query = em.createQuery(q);
        int i = query.executeUpdate();
        logger.info("the value of I is "+i);
        logger.info("bbbbbbb "+pro8.getDescription());
        //em.merge(pro8);
        em.getTransaction().commit();
        em.refresh(pro8);

        logger.info("bbbbbbb "+em.find(Project.class, pro8.getId()).getDescription());
        assertEquals(pro8.getDescription(), em.find(Project.class, pro8.getId()).getDescription());
    }
    
    // Updating the project name from Sixth project to be pro6 is the Sixth project
    @Test
    public void test_l_updatingTheProjectName() {
        Project pro6 = new Project();
        pro6.setName("Sixth project");
        pro6.setDescription("Software");
        em.getTransaction().begin();
        em.persist(pro6);
        em.getTransaction().commit();
        em.getTransaction().begin();
        pro6.setName("pro6 is the Sixth project");
        em.merge(pro6);
        em.getTransaction().commit();
        assertEquals(pro6.getName(), em.find(Project.class, pro6.getId()).getName());
        
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
