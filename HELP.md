
#  Developer Help Guide

Welcome! This backend is structured to help developers understand, extend, and test a Twitter-like microblogging API.

---

##  Technologies

- Java 17
- Spring Boot 3
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- Maven

---

## Authentication Flow

1. User registers via `/api/auth/register`
2. User logs in via `/api/auth/login`
3. Server responds with a JWT token
4. Token is passed in `Authorization: Bearer <token>` for protected endpoints

---

##  Recommended Reading

- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [JWT Intro (Auth0)](https://jwt.io/introduction)
- [Spring Security Guide](https://spring.io/guides/topicals/spring-security-architecture)

---

##  Testing Strategy

- Unit tests should cover services and validation
- Controllers can be tested using MockMvc or integration tests
- For CI, connect with GitHub Actions or Jenkins for automated test runs

---

##  Troubleshooting

- Use Postman or curl to debug endpoints
- For 403 errors, ensure the JWT token is correct and included
- Use H2 console at `/h2-console` (if enabled) to inspect DB

---

Clean code. Modular structure. Scalable systems !!
