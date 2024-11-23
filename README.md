# order_management
Here’s a `README.md` file for the **Order Management System** project with descriptions of the project, setup instructions, and API usage:

```markdown
# Order Management System

The **Order Management System** (OMS) is a RESTful API designed to manage products, orders, and their relationships in an e-commerce system. The system allows for CRUD operations on products and orders, as well as managing the many-to-many relationship between orders and products through the `OrderProduct` entity.

## Project Overview

This project allows you to:
- Create, read, update, and delete products.
- Create, read, update, and delete orders.
- Manage the relationship between products and orders using the `OrderProduct` entity.

The backend is built using **Spring Boot**, and the data is stored in a **MySQL** database. All API endpoints follow RESTful conventions.

## Prerequisites

- **Java 11 or higher**
- **MySQL** database setup
- **Maven** for building and managing the project dependencies
- **Spring Boot 2.5.x**

## Project Setup

### 1. Clone the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/your-username/order-management-system.git
```

### 2. Install Dependencies

Make sure Maven is installed and navigate to the project directory:

```bash
cd order-management-system
```

Then, install the dependencies:

```bash
mvn install
```

### 3. Configure the MySQL Database

Set up your MySQL database and create a schema. Modify the `application.properties` file with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/order_management
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 4. Run the Application

Start the application with the following Maven command:

```bash
mvn spring-boot:run
```

The application will be running at `http://localhost:8080`.

## API Documentation

### Base URL
All endpoints are prefixed with `/api`.

---

### 1. Product APIs

#### **Create a New Product**
- **URL**: `/api/products`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "name": "Product Name",
    "description": "Product Description",
    "price": 100.0
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "name": "Product Name",
    "description": "Product Description",
    "price": 100.0
  }
  ```

---

#### **Update an Existing Product**
- **URL**: `/api/products/{id}`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "name": "Updated Product Name",
    "description": "Updated Description",
    "price": 150.0
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "name": "Updated Product Name",
    "description": "Updated Description",
    "price": 150.0
  }
  ```

---

#### **Delete a Product**
- **URL**: `/api/products/{id}`
- **Method**: `DELETE`
- **Response**:
  ```json
  {
    "message": "Product deleted successfully."
  }
  ```

---

### 2. Order APIs

#### **Create a New Order**
- **URL**: `/api/orders`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "orderProducts": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "orderProducts": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }
  ```

---

#### **Update an Existing Order**
- **URL**: `/api/orders/{id}`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "orderProducts": [
      {
        "productId": 1,
        "quantity": 3
      },
      {
        "productId": 3,
        "quantity": 1
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "orderProducts": [
      {
        "productId": 1,
        "quantity": 3
      },
      {
        "productId": 3,
        "quantity": 1
      }
    ]
  }
  ```

---

#### **Delete an Order**
- **URL**: `/api/orders/{id}`
- **Method**: `DELETE`
- **Response**:
  ```json
  {
    "message": "Order deleted successfully."
  }
  ```

---

#### **Get All Orders**
- **URL**: `/api/orders`
- **Method**: `GET`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "orderProducts": [
        {
          "productId": 1,
          "quantity": 2
        },
        {
          "productId": 2,
          "quantity": 1
        }
      ]
    }
  ]
  ```

---

#### **Get Order by ID**
- **URL**: `/api/orders/{id}`
- **Method**: `GET`
- **Response**:
  ```json
  {
    "id": 1,
    "orderProducts": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }
  ```

---

## Error Handling

All errors will follow the standard error response format:

```json
{
  "timestamp": "2024-11-22T18:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input provided.",
  "path": "/api/orders"
}
```

## Database Relationships

- **Order** ↔ **OrderProduct** (One-to-Many)
- **Product** ↔ **OrderProduct** (Many-to-One)

## Contribution

Feel free to fork this repository and submit pull requests if you'd like to contribute to the development of this project. Please ensure that you follow best practices for Java development and maintain the coding style used in the project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
