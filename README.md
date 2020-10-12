# Typical Scala Webapp

A typical structure to be used in later production.

## Goal

- Create easy-to-test template. 
- Use Reader monad for all things config, repo(inMem or realDb), executor, controller. Bring it up to application level.
    - server, http clients don't need to be in reader. 

## Notes 
- [don't make useless  trait](https://github.com/alexandru/scala-best-practices/blob/master/sections/2-language-rules.md#24-should-not-define-useless-traits)

Always have a reason for making it. Repo needs trait as it implements at least one for default and for for in-mem. Service needs trait for default and for unstable implementations. 
    - Unstable implementation can be removed in realistic application. Network error, etc is already encapsulated in db query so we can do error type matching. 


- package object

Think of it as index.html in a Node dir. It bridges its content with outside by giving the stuff inside type alias.
Benefit: to avoid confusion when importing. Imports can have naming collision like me.mbcu.repo.user.SomeImpl and mbcu.repo.purchase.SomeImpl. 
Now user.SomeImpl can be aliased with UserSomeImpl whose definition is stored in this file.   

- executor

This project uses FokJoinPool as it only has in-memory db. In production scheduler should be IO. 

- network error, etc

In realistic application handling should be done on repository level (i.e result should be `[A Either Error]` `where error can be NotFound or NetworkError). Currently it's not handled as it's using unstable service which simulates these errors.  

- using first order type F[_]

The benefit is unclear as realistically we can only expect F to be Future or Task. Plus the config will create specific environment for each (ExecutionContect for Future or Monix's Scheduler)

- EitherT only plays nice with Future not Task
