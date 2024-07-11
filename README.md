[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/K6MEbnQdqEgQE7qSJFetp9/MKj6ds9vRoXpubQ1PBLwf5/tree/main.svg?style=svg&circle-token=CCIPRJ_YLXqPYUvSerJXQnVbHzXu9_0920b5813739ecef71fc05953932d4273dc226d2)](https://dl.circleci.com/status-badge/redirect/circleci/K6MEbnQdqEgQE7qSJFetp9/MKj6ds9vRoXpubQ1PBLwf5/tree/main)
[![codecov](https://codecov.io/gh/MiroslavKolosnjaji/AutoPartsEstoreSystem/graph/badge.svg?token=0Z63EFNDGY)](https://codecov.io/gh/MiroslavKolosnjaji/AutoPartsEstoreSystem)
# AutoPartsEstoreSystem

<<<<<<< HEAD
Description:
AutoPartsEstoreSystem is a web application aimed at providing a platform for ordering automobile parts for various makes and models.
The application serves as an online store where users can browse, search, and purchase car parts.
The main functionality includes a search system based on selecting the brand, model, and engine type of the vehicle to narrow down the parts selection.
Users can add items to their shopping purchaseOrder, view details of selected parts, and proceed to checkout.

## Key Features:

Brand, model, and engine-based part search system
Part group selection to refine search results (e.g., braking system, engine components)
Shopping purchaseOrder functionality for adding and managing selected items
User registration and login system
Secure checkout process with various payment methods
=======
## Description:
AutoPartsEstoreSystem is a server-side web application in development, designed to serve as an online platform for ordering automobile parts.
Users can browse, search, and purchase parts based on vehicle make, model, and engine type. 
Key functionalities include advanced search capabilities, part grouping for refined results, order management, secure payment processing, and invoice generation.

## Key Features:

- Advanced search system based on vehicle brand, model, and engine type
- Part grouping to refine search results (e.g., braking system, engine components)
- Order management for tracking and processing purchases
- Secure payment processing with various payment methods
- Invoice generation for order transactions
- User registration and login system
- Secure checkout process supporting various payment methods
>>>>>>> a2d143fba91d2a680769a9837ce44d64d073b1fb

## Technologies Used:
- __Spring Framework:__ Core Framework for building enterprise Java applications.
- __Spring Boot DevTools:__ Tools for improving development efficiency with features like automatic restarts and live reload.
- __Spring Boot Validation:__ Ensures that the data being processed adheres to specified rules and constraints.
- __Spring Security:__ Provides authentication and authorization for Spring applications
- __Spring Crypto:__ Provides utilities for cryptographic operations in Spring applications
- __Project Lombok:__ Library for reducing boilerplate code in Java.
- __MapStruct:__ Used for mapping between domain entities and DTOs.
- __JUnit5:__ Testing framework for unit and integration testing in Java.
- __Mockito:__ Framework for creating mock objects in automated testing. 
- __Spring Boot Test:__ Provides testing support for Spring Boot applications.
- __CircleCI:__ Platform for continuous integration and automated testing.
- __H2 Database:__ In-memory relational database for development and testing purposes.
- __JSON:__ Data interchange format for communication between the client and the server.

## Project Goals:
<<<<<<< HEAD
The main goal of the project is to create a fully functional web application for ordering automobile parts.
The application will allow users to search for parts by selecting the car brand, model, and engine type, add parts to a shopping purchaseOrder, and proceed to checkout.
=======
The primary objective of this project is to create a fully functional server-side web application for purchasing automobile parts.
It aims to provide users with a seamless experience for finding and buying parts based on specific vehicle details, processing payments securely, and generating invoices.
>>>>>>> a2d143fba91d2a680769a9837ce44d64d073b1fb

### Project Phases:
- Project setup and initialization
- Database design and entity modeling
<<<<<<< HEAD
- Implementing CRUD operations for managing parts, brands, and models
- Developing search functionality based on brand, model, and engine type
- Adding shopping purchaseOrder and checkout features
- User authentication and registration system
- Testing and debugging
=======
- Implementation of CRUD operations for managing parts, brands, and models
- Development of advanced search functionality based on vehicle specifications
- Integration of order management, payment processing, and invoice generation
- Implementation of user authentication and registration system
- Testing, debugging, and optimization
>>>>>>> a2d143fba91d2a680769a9837ce44d64d073b1fb
- Integration with CircleCI for continuous integration and automated testing
- Deployment and final adjustments

### Risks and Challenges:
The integration of complex search functionalities poses challenges in terms of performance optimization and data retrieval.
Ensuring data consistency in a multi-user environment requires careful management of concurrency issues.
Robust implementation of user authentication, payment processing, and security measures is essential to prevent unauthorized access and ensure data protection.
