# Fivvy Backend Challenge

This is a simple challenge to test your skills on building APIs. The Fivvy services use mainly Java and Springboot technologies.

# What to do

Create a simple API to manage disclaimer acceptance of terms and conditions

Entities:

-**Disclaimer**

    Fields

      -   id
    
     -   name
    
     -   text
    
     -   version
    
     -   create_at
    
    -   update_at
    

-**Acceptance**

  Fields

    -   disclaimer_id
    
    -   user_id
    
    -   create_at
    
**Must create a CRUD for a Disclaimer entity**

-   The LIST endpoint must be able to filter the disclaimers by field “text” as an optional parameter.

**Must create CREATE and LIST endpoints for the Acceptance entity**

-   The LIST endpoint must be able to filter the acceptances by the “user_id” field as an optional parameter

# Requirements

-   All API responses must be JSON
    
-   Provide a README.md file with usage instructions (how to run, endpoints etc)
    
-   Specify a docker image in Readme or include docker compose file.
    
-   The use of non-relational databases like DynamoDB will be taken into account as a plus.
    

# Recommendations

-   TDD
    
-   SOLID
    
-   Code and commits in english (methods, classes, variables, etc)
    

# Evaluation

-   Project structure, architecturing and organization (50%)
    
-   Programming good practices (30%)
    
-   Testing strategy (20%)
    

# Delivery

You must fork this repository and commit the solution in the solution folder. Your repository must be public. After that, send the repository link to recruiter.