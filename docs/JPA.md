to show the objectDB tables in the UI we have 2 ways:
 1. open finder and enter to the directory objectdb-2.8.1 --> bin --> and run the script ./explorer.sh
 2. open finder and enter to the directory objectdb-2.8.1  --> bin --> and right click on the file explorer.jar  
    click to open with and select jar launcher option.
    
 To store an object to the database - you need a database!
 With ObjectDB database can be created automatically with the first object you persist to it, but you have to fetch a databased connection, with the following steps:
 Step 1 – create EntityManagerFactory with the static method of Persistence class:
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/mydb1.odb");
 // $objectdb represents the ObjectDB home directory
 // (by default - the directory in which ObjectDB is installed).
 Note: for ORM providers, the connection to the database is set in the persistence.xml file, in javax.persistence.jdbc properties
 
 Step 2 – create EntityManager to work with:
     EntityManager em = emf.createEntityManager();
 Notes:
 1. EntityManagerFactory and EntityManager shall be closed when not used anymore, by calling: em.close() and emf.close()
 2. EntityManagerFactory represents the Database and should be one per database in an application. EntityManager represents a connection to the Database and you may have many in a single application pointing to the same Database (e.g. one per thread).
 
 Step 3 – open transaction:
     em.getTransaction().begin();
 Step 4 – persist the object:
     em.persist(person); // we assume there is a person object
 Step 5 – commit the transaction:
     em.getTransaction().commit();
     
 Since in this example we didn’t set an ID for person, we will get all persons named ‘Mo’ with a JPQL (Java Persistence Query Language):
 
 TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.name = 'Mo'", Person.class);
     List<Person> results = query.getResultList();
     for(Person p : results) {
         System.out.println(p);
     }
     
 Here is another possible JPQL query:
 
 Query q1 = em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.name='Mo'");
 System.out.println("Number of persons called 'Mo' = " + q1.getSingleResult());
 
 We will later review in greater details JPQL:
 - Typed queries
 - Many additional JPQL options

We will now add to Person the following field:
@Id
long ID;
Now that person has an ID (managed by the application) we can retrieve a person by her ID:
Person person = em.find(Person.class, 123);
// using EntityManager method: <T> T find(Class<T>, Object primaryKey) // If the object is not found in the database null is returned.
