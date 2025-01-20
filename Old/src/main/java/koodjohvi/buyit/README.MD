# BuyIt API Documentation

---

## 1. User API

### **Base URL**: `/api/users`

### Endpoints

#### **1.1 Create User**
- **Method**: `POST`
- **URL**: `/api/users`
- **Request Body**:
    ```json
    {
      "name": "Alice",
      "email": "alice@example.com",
      "password": "password123",
      "role": "client",
      "avatar": "https://example.com/avatar/alice.png"
    }
    ```

---

#### **1.2 Get All Users**
- **Method**: `GET`
- **URL**: `/api/users/all`

---

#### **1.3 Get User by Email**
- **Method**: `GET`
- **URL**: `/api/users/email/{email}`

---

#### **1.4 Get User by ID**
- **Method**: `GET`
- **URL**: `/api/users/id/{id}`

---

#### **1.5 Update User**
- **Method**: `PUT`
- **URL**: `/api/users/{id}`
- **Request Body**:
    ```json
    {
      "name": "Alice Updated",
      "email": "alice.updated@example.com",
      "password": "newpassword123",
      "role": "client",
      "avatar": "https://example.com/avatar/alice-updated.png"
    }
    ```

---

#### **1.6 Delete User**
- **Method**: `DELETE`
- **URL**: `/api/users/{id}`

---

## 2. Product API

### **Base URL**: `/api/products`

#### In get requests, the ID must be of the owner of the product.

### Endpoints

#### **2.1 Create Product**
- **Method**: `POST`
- **URL**: `/api/products`
- **Request Body**:
    ```json
    {
      "name": "Laptop",
      "description": "A high-performance laptop",
      "price": 1500.00,
      "quantity": 10,
      "userId": "2"
    }
    ```

---

#### **2.2 Get All Products**
- **Method**: `GET`
- **URL**: `/api/products/all`

---

#### **2.3 Get Product by ID**
- **Method**: `GET`
- **URL**: `/api/products/{id}`

---

#### **2.4 Update Product**
- **Method**: `PUT`
- **URL**: `/api/products/{id}`
- **Request Body**:
    ```json
    {
      "name": "Gaming Laptop",
      "description": "A gaming laptop",
      "price": 1800.00,
      "quantity": 8,
      "userId": "2"
    }
    ```

---

#### **2.5 Delete Product**
- **Method**: `DELETE`
- **URL**: `/api/products/{id}`

---

## 3. Media API

### **Base URL**: `/api/media`

#### In get requests, the ID must be of the owner of the media.

### Endpoints

#### **3.1 Upload Media**
- **Method**: `POST`
- **URL**: `/api/media`
- **Request Body**:
    ```json
    {
      "imagePath": "https://example.com/images/image1.jpg",
      "productId": "1"
    }
    ```

---

#### **3.2 Get Media by Product ID**
- **Method**: `GET`
- **URL**: `/api/media/product/{productId}`

---

#### **3.3 Delete Media**
- **Method**: `DELETE`
- **URL**: `/api/media/{id}`

---

## Notes
1. **User Roles**: Users can have roles (`client` or `seller`) which determine their permissions.
2. **Validation**: Ensure valid `userId` and `productId` when creating or updating entities.
3. **Error Handling**: API will return appropriate status codes for errors (e.g., `404 Not Found`, `400 Bad Request`).

---

Let me know if you need further clarification or additions! 🚀
