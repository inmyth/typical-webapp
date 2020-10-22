## Proto Certivied

- application config
- hash for id
- s3
    - file / documents
- sql
    - source
- dynamodb
    - whitelist email 
    - role
    - view
- crud for admin


## How To Start

- Create IAM user with policy that 
    - allows put object
    - restricts to one dev bucket  


## Notes

- Conf doesn't store credentials. Use IDEA's AWS Tools to store them. 
    - credentials are only used in development, in production IAM role is used
    - AWS client is smart enough to look for these credentials

## TODOS

- [x] reconfigure config
- [x] restructured domain (user to certiv)
- [x] AWS conf-type converter (ConfUtils)
- [x] S3 put
- [] implement server
- [] implement crud controller
- [] implement req / resp
- [] in mem file repo 
- [] selection between in-mem or real happens in repositories config
 