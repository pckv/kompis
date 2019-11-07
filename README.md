# Kompis | backend

[![Build Status](https://travis-ci.com/pckv/kompis.svg?branch=master)](https://travis-ci.com/pckv/kompis)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a56c6b3d8d6441ba9ac9a2564e6f00f5)](https://www.codacy.com/manual/pc_3/kompis?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pckv/kompis&amp;utm_campaign=Badge_Grade)

## This is Kompis

The Kompis project aims to make it easy to for everyone to get home after a night out. 
We will provide a platform where users can ask for someone to drive them home, or drivers to advertise when they are available.

## API

#### Users
- [Create user](#create-user) : `POST /users`
- [Authorize](#authorize) : `POST /users/authorize`
- [Get user](#get-specific-user) : `GET /users/{id:number}`
- [Get current user](#get-current-user) : `GET /users/current`
- [Delete current user](#delete-current-user) : `DELETE /users/current`

## Documentation

### Users

#### Create user
```
POST /users
```

Creates a new user that can be logged into.

**Request**

Headers: 
```
Content-Type: application/json
```
 
Body:
```json
{
  "email": "roger@example.com",
  "password": "password",
  "displayName": "Roger Doger"
}
```

**Responses**

- `200 OK` on success

Headers:
```
Content-Type: application/json
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

- `409 CONFLICT` if a user with the given email exists

---
#### Authorize
```
POST /users/authorize
```

Receive authorization for use with endpoints requiring authorization.

**Request**

Headers:
```
Content-Type: application/json
```
 
Body:
```json
{
  "email": "roger@example.com",
  "password": "password"
}
```

**Responses**

- `200 OK` on success

Headers:
```
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

- `409 CONFLICT` if a user with the given email exists

---
#### Get specific user
```
GET /users/{id:number}
```

Get the user with the given ID.

**Request**

Headers:
```
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

- `200 OK` on success

Headers:
```
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

- `404 NOT FOUND` if user with the given id was not found
- `403 FORBIDDEN` if `Authorization` header is invalid

---
#### Get current user
```
GET /users/current
```

Get the current authorized user.

**Request**

Headers:
```
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

- `200 OK` on success

Headers:
```
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

- `403 FORBIDDEN` if `Authorization` header is invalid

---
#### Delete current user
```
DELETE /users/current
```

Delete the current authorized user. The client should get rid of the `Authorization` token manually.

**Request**

Headers:
```
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

- `200 OK` on success

Headers:
```
Authorization: Bearer xxx.yyy.zzz
```

- `403 FORBIDDEN` if `Authorization` header is invalid

---