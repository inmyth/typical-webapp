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
- S3 IAM test
    - to test if put object permission is ok, we put a file and delete it. 
    - the IAM policy needs to have delete object permission for this file only.
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor1",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
            ],
            "Resource": "arn:aws:s3:::dev-certiv/*"
        },
        {
            "Sid": "VisualEditor2",
            "Effect": "Allow",
            "Action": [
                "s3:DeleteObject"
            ],
            "Resource": "arn:aws:s3:::dev-certiv/thisisiamtestfiletocreateanddelete.txt"
        }
    ]
}
``` 
 
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
 