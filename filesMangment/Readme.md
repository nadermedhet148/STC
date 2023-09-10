Files Management System 
----------------------------------------------------------------
## Used technologies 
- liquibase
- spring boot 
- graphql 
- postgresql 

## How to run 
cd infra/
docker-compose up -d
this for the first time will create the db schema and add one permission group with name admin
and 2 user permissions viewer and editor

## End points 

### Create space with admin permission group 

```bash
curl --location --request POST 'http://localhost:8080/api/items/spaces' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name" : "stc-assessments”",
    "permissionGroupName" : "admin"
}'
```

### Create folder under created space with editor@test.com user

```bash
curl --location --request POST 'http://localhost:8080/api/items/spaces/1/folders' \
--header 'Content-Type: application/json' \
--data-raw '{

    "name" : "backend",
    "userEmail" : "editor@test.com"
}'
```

### Create file under created folder with editor@test.com user

```bash
curl --location --request POST 'http://localhost:8080/api/items/folders/2/files' \
--form 'name="assessment.pdf"' \
--form 'userEmail="editor@test.com"' \
--form 'file=@"/home/nader/Downloads/STC-backend-assessment.pdf"'
```

### view file with viewer@test.com user (graph)

```bash
curl --location --request POST 'http://localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"{\n    fileById(id : 3,userEmail: \"viewer@test.com\") {\n        id,\n        name,\n        type,\n        parent  {\n            id,\n            name,\n            type,\n            parent  {\n                    id,\n                    name,\n                    type,\n                }\n        }\n        file {\n            extension\n        },\n    \n    }\n\n}","variables":{}}'
```

### Download file viewer@test.com user

```bash
curl --location --request GET 'http://localhost:8080/api/files/3?user_email=viewer@test.com' \
--form 'name="test"' \
--form 'userEmail="nader2@test.com"' \
--form 'file=@"/home/nader/Downloads/image2.png"'
```