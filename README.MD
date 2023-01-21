# Pro JPA 2 in Java EE 8: An In-Depth Guide to Java Persistence APIs

## Entity

An entity is a Java representation of the database table that has characteristics like persistability, identity, transactionality, and granularity.

## Entity Metadata

- Every JPA entity has some associated metadata (even if a very small amount) that describes it.
- It enables the persistence layer to recognize, interpret, and properly manage the entity from the time it is loaded through to its runtime invocation.
- Entity metadata may be specified in two ways:
  - Annotations
  - XML
  
### Annotations

Annotation metadata is a language feature introduced in Java SE 5 that allows structured and typed metadata to be attached to the source code. Although annotations are not required by JPA, they are a convenient way to learn and use the API.

### XML

For those who prefer to use traditional XML, this option is still available.

## Creating an Entity

By adding a couple of annotations, almost any class with a no-arg constructor can become an entity.
- Must be annotated with the `javax.persistence.Entity` annotation
- Must have a public or protected no-argument constructor (it may have other constructors)
- Must not be declared final (therefore, no methods or persistent instance variables can be declared final)
- Must implement the `Serializable` interface (in case the entity instance is passed by value as a detached object via a session bean’s remote business interface)
- May extend both entity and non-entity classes, and non-entity classes may extend entity classes
- Persistent instance variables must be declared private, protected, or package-private (they can be accessed directly only by the entity class methods)

## Entity Manager

- Entity Manager is the one who is actually doing the real work of persistence.
- When an entity manager obtains a reference to an entity, that object is said to be managed by the entity manager.
- The set of managed entity instances within an entity manager at a given time is called its `persistence context`.
- Only one entity instance with the same persistent identity may exist in `persistence context` at any time.
  - For example,
  - If an `Employee` with a persistent identity (or ID) of 158 exists in the `persistent context`, then other `Employee` object with its ID set to 158 may exist within the same `persistence context`.
        
### Summary of API Objects
- `Persistence`          - Bootstrap class used to obtain an entity manager factory
- `EntityManagerFactory` - Configured factory object used to obtain entity managers
- Persistence Unit       - Named configuration declaring the entry classes and data store info
- `EntityManager`        - Main API object used to perform operations and queries on entities
- Persistence Context    - Set of all entity instances managed by a specific entity manager

#### Example
##### create and EntityManagerFactory
```
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeService");
```
- The name of the specified persistent unit is `EmployeeService` 
- Persistent unit name identifies the given persistent unit configuration that determines such things as the connection parameters that entity managers generated from this factory will use when connecting to the database.
##### create en EntityManager
```
  EntityManager em = emf.createEntityManager();
```
- With this entity manager we are in a position to start working with persistent entities.
##### Persist an Entity
```
Employee emp = new Employee(158);
em.persist(emp);
```
- Calling `persist()` is all that is required to initiate it being persisted in the database.
- If the entity manager encounters a problem doing this, it will throw and unchecked `PersistenceException`
- When the `persist()` call completes, `emp` will have become a managed entity within the entity manager's persistent context.
##### Finding an Entity
```
Employee emp = em.find(Employee.class, 158);
```
- We are passing
  - class of the entity
  - ID or primary key that identities the particular entity
- When the call completes, the employee object that gets returned will be a managed entity, that is it will exist in the current persistent context associated with the entity manager.
- If the ID or primary key we are looking for is not found, then `find()` call simply return `null`.
##### Removing an Entity
```
Employee emp = em.find(Employee.class, 158);
em.remove(emp);
```
- In order to remove an entity, the entity itself must be managed, meaning that it is present in the persistence context.
##### Updating an Entity
```
Employee emp = em.find(Employee.class, 158);
emp.setSalary(emp.getSalary() + 1000);
```
- In this case, we are not calling into the entity manager to modify the object, but directly calling the object itself. 
- For this reason, it is important that the entity be a managed instance; 
- Otherwise, the persistence provider will have no means of detecting the change, and no changes will be made to the persistent representation of the employee.

## Transactions
In a Java SE environment the transaction service `javax.persistence.EntityTransaction` should be used
```
em.getTransaction().begin();
Employee emp = new Employee(158);
em.persist(emp);
em.getTransaction().commit();
```
- a transaction is started by calling getTransaction() on the entity manager to get the EntityTransaction and then invoking begin() on it. 
- Likewise, to commit the transaction, the commit() call is invoked on the EntityTransaction object obtained from the entity manager.

In a Java EE container environment is that the standard Java Transaction API (JTA) is used automatically.

## Queries
- In JPA, we are querying over entities and using a language called Java Persistence Query Language (JPQL).
- A query is implemented in code as a `Query` or `TypedQuery<X>` object.
- A query can be defined either 
  - statically
    - A static query is typically defined in either annotation or XML metadata,  called a `named query`
  - (or)
  - dynamically.
    - A dynamic query can be issued at runtime by supplying the JPQL query criteria or a criteria object. 
    - They may be a little more expensive to execute this, because the persistence provider cannot do any query preparation beforehand
### Example
```
TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
List<Employee> emps = query.getResultList();
```







