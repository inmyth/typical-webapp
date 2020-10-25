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
- DynamoDB IAM test
    - put a empty entry to the table and delete it
    - requires a put permission on the table and delete permission on the test item

```
{
    "Sid": "VisualEditor0",
    "Effect": "Allow",
    "Action": "dynamodb:DeleteItem",
    "Resource": "arn:aws:dynamodb:us-east-1:162207319863:table/proto-lambda-scalajs",
    "Condition": {
        "ForAllValues:StringEquals": {
            "dynamodb:LeadingKeys": "thisisiamtestitem"
        }
    }
},
{
    "Sid": "VisualEditor1",
    "Effect": "Allow",
    "Action": [
        "dynamodb:PutItem"
    ],
    "Resource": [
        "arn:aws:dynamodb:us-east-1:162207319863:table/proto-lambda-scalajs",
    ]
}
``` 

- complete sample IAM and conf
application.conf
```
env-config {
  repo-mode {
      type : "real"
  }
}

executors-config {
    computation-scheduler {
        parallellism : 1
    }
}

repository-config {
    s-3-config {
        region : "us-east-1"
        bucket : "dev-certiv"
        iam-test-target-key : "thisisiamtestfiletocreateanddelete.txt"
        iam-test-file: "README.md"
    }
    dynamo-config {
        region : "us-east-1"
        table-name : "proto-lambda-scalajs"
        iam-test-key : "thisisiamtestitem"
    }
    sql-config {
        region : "us-east-1"
    }
    in-mem-config{
        failure-probability : 0.5
    }
}
```

IAM group policy
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "VisualEditor0",
      "Effect": "Allow",
      "Action": "dynamodb:DeleteItem",
      "Resource": "arn:aws:dynamodb:us-east-1:162207319863:table/proto-lambda-scalajs",
      "Condition": {
        "ForAllValues:StringEquals": {
          "dynamodb:LeadingKeys": "thisisiamtestitem"
        }
      }
    },
    {
      "Sid": "VisualEditor1",
      "Effect": "Allow",
      "Action": [
        "dynamodb:PutItem",
        "s3:GetBucketCORS",
        "s3:GetBucketVersioning",
        "s3:GetBucketAcl",
        "s3:GetBucketLocation"
      ],
      "Resource": [
        "arn:aws:dynamodb:us-east-1:162207319863:table/proto-lambda-scalajs",
        "arn:aws:s3:::dev-certiv"
      ]
    },
    {
      "Sid": "VisualEditor2",
      "Effect": "Allow",
      "Action": [
        "s3:PutObject",
        "s3:GetObjectAcl",
        "s3:PutObjectVersionAcl",
        "s3:GetObjectVersion"
      ],
      "Resource": "arn:aws:s3:::dev-certiv/*"
    },
    {
      "Sid": "VisualEditor3",
      "Effect": "Allow",
      "Action": "s3:DeleteObject",
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
- [x] in mem file repo 
- [x] selection between in-mem or real happens in repositories config
 