# eCommerce Project

This project is an eCommerce application that provides RESTful APIs for managing products, categories, roles, users, and orders. Users can view products, add them to the cart, and place orders. Admin users have additional privileges such as creating new products and categories, managing orders, and granting authorizations.

## Features

- CRUD operations for products, categories, roles, users, and orders
- User functionalities:
    - User authentication (login, logout, register)
    - Authorization for accessing public and protected endpoints
- Data Modeling:
    - Establishing relationships among tables/entities
- Request and Response Handling:
    - Customizing data requests and responses to ensure clean and organized data using MapStruct
- Swagger documentation 
- Dockerization:
    - Docker image available on Docker Hub: [thoanguyen2907/thoa-ecommerce-project:v3](https://hub.docker.com/r/thoanguyen2907/thoa-ecommerce-project)

## Getting Started

To get started with the eCommerce project, follow the steps below:

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven
- Docker (optional)

### Installation

1. Clone the repository:

git clone https://github.com/your-username/ecommerce-project.git


### Usage

1. Start the application:


2. The application should now be running and accessible via [http://localhost:8080](http://localhost:8080).

### Docker

If you prefer to use Docker, you can pull the Docker image from Docker Hub


The application will be accessible at [http://localhost:8080](http://localhost:8080).

## Documentation

For detailed API documentation and examples, please refer to the [API Documentation](http://localhost:8080/swagger-ui/index.html) file.


