import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaExample");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Persons instance
//        Person amjed = new Person("123", "amjed", getBirthday("04/12/1987"),
//                new Person(),
//                new ArrayList(Arrays.asList(new Person(), new Person(), new Person())));

        Person amjed = new Person("123", "amjed", getBirthday("04/12/1987"));

        //Person almog = new Person("456", "almog", getBirthday("30/12/1978"));
        //Person greg = new Person("789", "greg", getBirthday("25/09/1977"));
        //Person nesia = new Person("852", "nesia", getBirthday("1/01/1968"));


        addPerson(amjed, em);
//        addPerson(almog, em);
//        addPerson(greg, em);
        getById(amjed.getId(), em);
        getByName(amjed.getName(), em);
        getByBirthday(amjed.getBirthday(), em);
        getByMonth(amjed.getBirthday(), em);
        updatePersonName(amjed.getId(), "test", em);

        //TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.name = 'amjed' AND NOT EXISTS ",Person.class);
        //TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.name = 'amjed'",Person.class);

        em.close();
        emf.close();
    }

    private static Date getBirthday(String dateStr) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateformat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return date;
        }
        return new Date();
    }

    private static void getByBirthday(Date birthday, EntityManager em) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.birthday = :birthday" ,Person.class).setParameter("birthday", birthday);
        List<Person> result = query.getResultList();
        if (result.isEmpty()){
            System.out.println("no result with Birthday :'" + birthday + "'");
            return;
        }
        System.out.println("result for Birthday '"+ birthday +"'  ");
        printList(result);
    }

    private static void getByMonth(Date birthday, EntityManager em) {
        //TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE  p.birthday.getMonth() = :birthday ", Person.class).setParameter("birthday", birthday.getMonth());
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE  EXTRACT(MONTH FROM p.birthday) = EXTRACT(MONTH FROM birthday) ", Person.class);

        List<Person> result = query.getResultList();
        if (result.isEmpty()){
            System.out.println("no result with Month :'" + Integer.parseInt(String.valueOf(birthday.getMonth() + 1)) + "'");
            return;
        }
        System.out.println("result for Month '"+ Integer.parseInt(String.valueOf(birthday.getMonth() + 1)) + "' ");
        printList(result);
    }

    private static void getByName(String name, EntityManager em) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.name = '" + name + "'",Person.class);
        List<Person> result = query.getResultList();
        if (result.isEmpty()){
            System.out.println("no result with name :'" + name + "'\n");
            return;
        }
        System.out.println("result for name '"+ name +"'  ");
        printList(result);
    }

    private static void printList(List<Person> result) {
        for(Person p : result){
            System.out.println(p);
        }
        System.out.println("\n");
    }

    private static void getById(String id, EntityManager em) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.id = '" + id + "'",Person.class);
        try{
            Person result = query.getSingleResult();
            System.out.println("\n" + "result for id '"+ id +"'  ");
            System.out.println(result + "\n");
        } catch (NoResultException nre) {
            System.out.println("the id '" + id + "' not exist");
        }
    }

    private static void addPerson(Person person, EntityManager em) {
        boolean isObjectExist = IsObjectExist(person, em);
        if (!isObjectExist){
            em.persist(person);
            em.getTransaction().commit();
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p ",Person.class);
            List<Person> result = query.getResultList();
            System.out.println("person '"+ person.getName() +"' added successfully ");
            printList(result);
        }
    }

    private static void updatePersonName(String id, String name, EntityManager em) {

        boolean IsObjectExistById = IsObjectExistById(id, em);
        if (IsObjectExistById){
//            Query query = em.createQuery("UPDATE Person p SET p.name = :name WHERE p.id = :id");
//            query.setParameter("name", name);
//            query.setParameter("id", id);
//            int rowCount = query.executeUpdate();
//            em.getTransaction().commit();
            SessionFactory sessionFactory = null;
            try{
                loadSessionFactory(sessionFactory);
            }catch(Exception e){
                System.err.println("Exception while initializing hibernate util.. ");
                e.printStackTrace();
            }
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Person m = session.get(Person.class, id);
            m.setName(name);
            session.update(m);
            TypedQuery<Person> query1 = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> result = query1.getResultList();
            printList(result);
            return;
        }
        em.close();

        System.out.println("the object you want to update not exist in Data Base:");
    }


    public static void loadSessionFactory(SessionFactory sessionFactory){

        Configuration configuration = new Configuration();
        configuration.configure("/Users/ah108f/Desktop/courses/java/JPA Hibernate and Spring Data Training/JPA+Hibernate/src/main/resources/META-INF/persistence.xml");
        configuration.addAnnotatedClass(Person.class);
        ServiceRegistry srvcReg = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(srvcReg);
    }
    private static boolean IsObjectExist(Person person, EntityManager em) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p ",Person.class);
        List<Person> result = query.getResultList();
        for(int i = 1; i <= result.size(); i++) {
            Person foundPerson = em.find(Person.class, i);
            if (foundPerson != null && foundPerson.getId().equals(person.getId())){
               System.out.println("the person: '" + person + "' is exist in data base");
               return true;
            }
        }
        return false;
    }

    private static boolean IsObjectExistById(String id, EntityManager em) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p",Person.class);
        List<Person> result = query.getResultList();
        for(int i = 1; i <= result.size(); i++) {
            Person foundPerson = em.find(Person.class, "123");
            if (foundPerson != null && foundPerson.getId().equals(id)){
                System.out.println("the id: '" + id + "' is exist in data base");
                return true;
            }
        }
        return false;
    }
}