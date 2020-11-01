# Typical Scala Webapp
A typical structure to be used in web-app project.

## Goal
- Create easy-to-test template. 
- Restrict the most FP stuff to use
    - Reader
    - dedicated thread pool
    - (from here not so important)
    - Implicit conversion
    - Either, EitherT
    - Writer (this is monadic logging)
- Adheres to basic DDD
- Simulates unstable network
    - Unstable implementation can be removed in realistic application. Network error, etc is already encapsulated in db query so we can do error type matching. 
- Has in-memory repository for testing
- Has the following desired structure

All hierarchy of application is represented as chain of Reader. Server is outside as it's not tested and ideally should be easy to change. 
Controller however is usually integrated with server framework, it's probably not possible to abstract it. 

```
// ideal
- application
  - controller
  - services    
    - repo
      - config 
        - executor
        - env
    - config(idem)
- server
```

```
// realistic
- application
  - services    
    - repo  
      - config 
        - executor
        - env
    - config(idem)
- server
  - controller

```


## Notes 
- Reader Monad

Why Reader ? It helps organize dependency graph which means easier testing and easier time to visualize the software structure. The latter often becomes a problem when working with dependency injection.  
Use Reader for all things config, repo(inMem or realDb), executor, (hopefully) controller. Bring configs up to application level (server, http clients don't need to use reader and are the final consumer).

- [don't make useless  trait](https://github.com/alexandru/scala-best-practices/blob/master/sections/2-language-rules.md#24-should-not-define-useless-traits)

Always have a reason for making it. Repo needs trait as it implements at least one for default and for for in-mem. Service needs trait for default and for unstable implementations. 

- package object

WARNING: package object will be removed in Scala 3.0.

Think of it as index.html in a Node dir. It bridges its content with outside by giving the stuff inside type alias.
Benefit: to avoid confusion when importing. Imports can have naming collision like me.mbcu.repo.user.SomeImpl and mbcu.repo.purchase.SomeImpl. 
Now user.SomeImpl can be aliased with UserSomeImpl whose definition is stored in this file.   

- executor

Ideally concurrent process will use a variety of schedulers / execution context. ExecutorConfig will store ec definitions to be used in services or repo. 
The current template only has in-memory db which probably requires only computation ec. 

- network error, etc

Handling should be done on repository level (i.e result should be `[A Either Error]` where error can be NotFound or NetworkError). Currently it's not handled as it's using unstable service which simulates these errors.  

- using first order type F[_]

The benefit is unclear as realistically we can only expect F to be Future or Task. Each one requires specific environment (ExecutionContect for Future or Monix's Scheduler) which cannot be abstracted. 

- [ddd](https://web.archive.org/web/20201014145232/https://terasolunaorg.github.io/guideline/1.0.x/en/Overview/ApplicationLayering.html) 
    - controller should belong in application
    - messaging should go to infra
    - application is a thin layer connecting UI and domain 
    - domain contains business rules
    

    
