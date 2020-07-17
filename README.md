# File Storage API

A simple File Storage REST API for demonstration purposes written using Spring Boot 2.3.1, Java 11 and MySQL 8.0.20.  
It supports creating, updating (through versioning), listing, downloading and deleting any kind of binary files.

1. [Installation](#Installation)
2. [Usage](#Usage)

## Installation
Steps to run the API locally:
```shell script
# Clone the repository
$ git clone https://github.com/fgrassals/file-storage-api.git

# CD into the project directory
$ cd file-storage-api

# Use docker-compose to build the docker images
# This will download some files and build the project so it may take a while
$ docker-compose build app

# Use docker-compose to start the application and database containers
$ docker-compose up
```

## Usage

All endpoints of this API require basic authentication. The following username and password combinations are configured by default:  
* Username: `test` password: `test`
* Username: `test2` password: `test`

### Uploading a file
Creates a file object using the uploaded binary file. The file must not exceed 50MB in size.
Returns a `Location` header specifying the location of the newly created file.

* URL: `/files`
* Method: `POST`
* Request payload:
  * `file` field in a `multipart/form-data` request  

#### Example request
```shell script
$ curl --location --request POST 'http://localhost:8080/files' -H 'Authorization: Basic dGVzdDp0ZXN0' -F 'file=@/home/test/test.txt'
```

#### Success response

* HTTP Code: `201 CREATED`
* Response headers:
  * `Location: http://localhost:8080/files/1`
* Response payload:
```json
{
    "action": "File created",
    "id": 1,
    "filename": "test.txt",
    "contentType": "text/plain",
    "uri": "http://localhost:8080/files/1"
}
```

### Updating a file
Adds a version to an existing file object using the uploaded binary file. The file must not exceed 50MB in size.
The file must have the same name and same content type as the existing file, or the request will fail.

* URL: `/files/:id`
* Method: `PATCH`
* Parameters:
  * `:id` The id of the existing file to update
* Payload: `file` field in a `multipart/form-data` request  

#### Example request
```shell script
$ curl --location --request PATCH 'http://localhost:8080/files/1' -H 'Authorization: Basic dGVzdDp0ZXN0' -F 'file=@/home/test/test.txt'
```

#### Success response

* HTTP Code: `200 OK`
* Response payload:
```json
{
    "action": "File updated",
    "id": 1,
    "filename": "test.txt",
    "contentType": "text/plain",
    "uri": "http://localhost:8080/files/1"
}
```

### Deleting a file
Deletes a file, along with all its versions.

* URL: `/files/:id`
* Method: `DELETE`
* Parameters:
  * `:id` The id of the existing file to delete

#### Example request
```shell script
$ curl --location --request DELETE 'http://localhost:8080/files/1' -H 'Authorization: Basic dGVzdDp0ZXN0'
```

#### Success response

* HTTP Code: `200 OK`
* Response payload:
```json
{
    "action": "File deleted",
    "id": 1,
    "filename": "test.txt",
    "contentType": "text/plain",
    "uri": "http://localhost:8080/files/1"
}
```

### Fetching the list of files
Gets a list of all the uploaded files for the user ordered by `filename` in ascending order.

* URL: `/files`
* Method: `GET`

#### Example request
```shell script
$ curl --location --request GET 'http://localhost:8080/files' -H 'Authorization: Basic dGVzdDp0ZXN0'
```

#### Success response

* HTTP Code: `200 OK`
* Response payload:
```json
[
    {
        "id": 5,
        "filename": "bfiletest.pdf",
        "contentType": "application/pdf",
        "sizeInBytes": 3458410,
        "createdAt": "2020-07-16T23:38:33",
        "lastModifiedAt": "2020-07-16T23:38:33"
    },
    {
        "id": 2,
        "filename": "changes.pdf",
        "contentType": "application/pdf",
        "sizeInBytes": 135273,
        "createdAt": "2020-07-16T01:23:42",
        "lastModifiedAt": "2020-07-16T01:25:03"
    },
    {
        "id": 1,
        "filename": "test.pdf",
        "contentType": "text/plain",
        "sizeInBytes": 22,
        "createdAt": "2020-07-16T01:23:31",
        "lastModifiedAt": "2020-07-16T01:23:31"
    },
    {
        "id": 10,
        "filename": "test2.txt",
        "contentType": "text/plain",
        "sizeInBytes": 15,
        "createdAt": "2020-07-17T14:23:40",
        "lastModifiedAt": "2020-07-17T14:23:40"
    }
]
```

### Fetching a specific file
Gets the information for the most recent version of a specific file for the user.

* URL: `/files/:id`
* Method: `GET`
* Parameters:
  * `:id` The id of the existing file to fetch

#### Example request
```shell script
$ curl --location --request GET 'http://localhost:8080/files/10' -H 'Authorization: Basic dGVzdDp0ZXN0'
```

#### Success response

* HTTP Code: `200 OK`
* Response payload:
```json
{
    "id": 10,
    "filename": "test2.txt",
    "contentType": "text/plain",
    "sizeInBytes": 15,
    "createdAt": "2020-07-17T14:23:40",
    "lastModifiedAt": "2020-07-17T14:23:40"
}
```

### Downloading the latest version of a file
Downloads the latest version of an existing file.
Returns a `Content-Disposition` header specifying the filename and a
`Content-Type` header specifying the file's content type.

* URL: `/files/:id/download`
* Method: `GET`
* Parameters:
  * `:id` The id of the existing file to fetch

#### Example request
```shell script
$ curl --location --request GET 'http://localhost:8080/files/10/download' -H 'Authorization: Basic dGVzdDp0ZXN0' -OJ
```

#### Success response

* HTTP Code: `200 OK`
* Response Headers:
  * `Content-Disposition: attachment; filename="test2.txt"`
  * `Content-Type: text/plain`
* Response payload:
  * Bytes representing the contents of the file


### Fetching the list of versions of a file
Gets a list of file versions for an existing file ordered by the most recent.

* URL: `/files/:id/versions`
* Method: `GET`
* Parameters:
  * `:id` The id of the existing file to fetch

#### Example request
```shell script
$ curl --location --request GET 'http://localhost:8080/files/10/versions' -H 'Authorization: Basic dGVzdDp0ZXN0'
```

#### Success response

* HTTP Code: `200 OK`
* Response payload:
```json
[
    {
        "version": "5997755f-8df7-46e8-8ad3-1a1b559c98aa",
        "filename": "test2.txt",
        "contentType": "text/plain",
        "sizeInBytes": 91,
        "createdAt": "2020-07-17T14:49:40"
    },
    {
        "version": "61965225-1847-4e0d-9e59-9bac97515a60",
        "filename": "test2.txt",
        "contentType": "text/plain",
        "sizeInBytes": 26,
        "createdAt": "2020-07-17T14:49:21"
    },
    {
        "version": "9ec08121-21ce-4da7-86e6-815d3fe1a084",
        "filename": "test2.txt",
        "contentType": "text/plain",
        "sizeInBytes": 15,
        "createdAt": "2020-07-17T14:23:40"
    }
]
```

### Fetching a specific version of a file
Gets a specific version for an existing file.

* URL: `/files/:id/versions/:uuid`
* Method: `GET`
* Parameters:
  * `:id` The id of the existing file to fetch
  * `:uuid` The uuid of the file version to fetch

#### Example request
```shell script
$ curl --location --request GET 'http://localhost:8080/files/10/versions/61965225-1847-4e0d-9e59-9bac97515a60' -H 'Authorization: Basic dGVzdDp0ZXN0'
```

#### Success response

* HTTP Code: `200 OK`
* Response payload:
```json
{
    "version": "61965225-1847-4e0d-9e59-9bac97515a60",
    "filename": "test2.txt",
    "contentType": "text/plain",
    "sizeInBytes": 26,
    "createdAt": "2020-07-17T14:49:21"
}
```

### Downloading a specific version of a file
Downloads a specific version of an existing file.
Returns a `Content-Disposition` header specifying the filename and a
`Content-Type` header specifying the file's content type.

* URL: `/files/:id/versions/:uuid/download`
* Method: `GET`
* Parameters:
  * `:id` The id of the existing file to fetch
  * `:uuid` The uuid of the file version to fetch

#### Example request
```shell script
$ curl --location --request GET 'http://localhost:8080/files/10/versions/61965225-1847-4e0d-9e59-9bac97515a60/download' -H 'Authorization: Basic dGVzdDp0ZXN0' -OJ
```

#### Success response

* HTTP Code: `200 OK`
* Response Headers:
  * `Content-Disposition: attachment; filename="test2.txt"`
* Response payload:
  * Bytes representing the contents of the file
  
  
## Error responses

Error responses will have an HTTP code and a json object (excluding 401s) describing the error.

### HTTP Codes
 * `400 Bad Request` when the parameters sent are invalid for a given request
 * `401 Unauthorized` when the user credentials sent in the Authorization header are not correct
 * `404 Not Found` when the resource specified in the request's url was not found
 * `405 Method NOt Allowed` when sending an unsupported HTTP method for a given url
 * `500 Internal Server Error` means something went wrong in the server while processing the request

### Response payload

A JSON object with the following fields:
 * `code` number corresponding to the response HTTP code
 * `error` description of the response HTTP code
 * `message` short message describing the error`
 
### Examples

File not found
```json
{
    "code": 404,
    "error": "Not Found",
    "message": "File with id '11' not found"
}
```

Trying to create a file without uploading one
```json
{
    "code": 400,
    "error": "Bad Request",
    "message": "Please upload a valid file"
}
```
