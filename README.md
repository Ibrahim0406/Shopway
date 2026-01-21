Shopway E-Commerce Platform
A modern full-stack e-commerce application built with React (frontend) and Spring Boot (backend). This platform offers a complete shopping experience with user authentication, product management, cart functionality, and secure payment processing via Stripe.

📋 Table of Contents

Features
Tech Stack
Prerequisites
Installation
Configuration
Running the Application
Project Structure
API Documentation
Admin Panel
Contributing
License


✨ Features
User Features

🔐 Authentication & Authorization

Email/Password registration with email verification
Google OAuth2 Sign-In
JWT-based authentication


🛍️ Shopping Experience

Browse products by categories (Men, Women, Kids)
Advanced filtering (price, size, color, category)
Product details with variants (size, color)
Shopping cart with quantity management
Wishlist functionality


💳 Checkout & Payments

Multiple delivery addresses
Stripe integration for card payments
Cash on Delivery (COD) option
Order tracking and history


👤 User Profile

Manage personal information
Add/edit/delete delivery addresses
View order history
Account settings



Admin Features

📦 Product Management

Create/Edit/Delete products
Upload product images to CDN (Bunny CDN)
Manage product variants (sizes, colors, stock)
Category and subcategory management


📊 Dashboard

React Admin panel for easy management
Product listings with search and filters




🛠️ Tech Stack
Frontend

React 19 - UI library
Redux Toolkit - State management
React Router v7 - Routing
Tailwind CSS 4 - Styling
React Admin - Admin panel
Stripe React - Payment integration
Axios - HTTP client
Vite - Build tool

Backend

Spring Boot 4.0 - Application framework
Spring Security - Authentication & authorization
Spring Data JPA - Database access
PostgreSQL - Database
JWT - Token-based authentication
OAuth2 - Google Sign-In
Stripe Java SDK - Payment processing
AWS S3 SDK - File storage (Bunny CDN)
JavaMail - Email verification
Maven - Dependency management


📦 Prerequisites
Before running this application, ensure you have:

Java 21 or higher
Node.js 18+ and npm/yarn
PostgreSQL database
Maven (or use the included Maven wrapper)
Stripe Account (for payment processing)
Google OAuth2 Credentials (for social login)
CDN Storage (e.g., Bunny CDN or AWS S3)


🚀 Installation
1. Clone the Repository
bashgit clone https://github.com/yourusername/shopway.git
cd shopway
2. Backend Setup
Navigate to backend directory
bashcd Backend/backend
Create .env file
Create a .env file in the Backend/backend directory with the following variables:
envDATABASE_URL=jdbc:postgresql://localhost:5432/shopway

MAIL_PASSWORD=your_gmail_app_password

JWT_KEY=your_base64_encoded_secret_key

GOOGLE_OAUTH2_CLIENT_ID=your_google_client_id
GOOGLE_OAUTH2_CLIENT_SECRET=your_google_client_secret

stripe.secret=your_stripe_secret_key

FILE_ZONE=your_cdn_bucket_name
FILE_HOST_URL=https://your-cdn-url.com
FILE_UPLOAD_KEY=your_cdn_access_key
FILE_SECRET_KEY=your_cdn_secret_key
Install dependencies and build
bash./mvnw clean install
3. Frontend Setup
Navigate to frontend directory
bashcd ../../frontend
Install dependencies
bashnpm install

⚙️ Configuration
Database Setup

Create a PostgreSQL database:

sqlCREATE DATABASE shopway;

The application uses Hibernate DDL auto-update, so tables will be created automatically on first run.
(Optional) Insert initial data for categories and roles:

java// Run AuthorityService.createAuthority() to create roles
// USER role is required for registration
Email Configuration
Configure Gmail SMTP in application.properties:
propertiesspring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=${MAIL_PASSWORD}
Note: Use an App Password instead of your actual Gmail password.
Google OAuth2 Setup

Go to Google Cloud Console
Create a new project or select existing
Enable Google+ API
Create OAuth2 credentials
Add authorized redirect URI: http://localhost:8080/login/oauth2/code/google
Copy Client ID and Secret to .env

Stripe Setup

Sign up at Stripe
Get your Test API Keys from the Dashboard
Add to .env: stripe.secret=sk_test_...
Update frontend with your Publishable Key in PaymentPage.jsx

CDN/File Storage Setup
For Bunny CDN (or any S3-compatible storage):

Create a storage zone
Get Access Key and Secret
Add credentials to .env
Update AdminPanel.jsx with your CDN URL


🏃 Running the Application
Start Backend
bashcd Backend/backend
./mvnw spring-boot:run
Backend runs on http://localhost:8080
Start Frontend
bashcd frontend
npm run dev
```
Frontend runs on **http://localhost:5173**

### Access Points
- **Shop:** http://localhost:5173
- **Admin Panel:** http://localhost:5173/admin
- **API Documentation:** http://localhost:8080/swagger-ui.html
- **API Base URL:** http://localhost:8080/api

---

## 📁 Project Structure

### Backend Structure
```
Backend/backend/
├── src/main/java/com/shopway/shopway/
│   ├── auth/                    # Authentication & user management
│   │   ├── config/              # JWT, Security config
│   │   ├── controller/          # Auth endpoints
│   │   ├── entities/            # User, Authority
│   │   └── services/            # Auth business logic
│   ├── config/                  # App configuration (S3, Swagger)
│   ├── controllers/             # REST controllers
│   ├── dto/                     # Data Transfer Objects
│   ├── entities/                # JPA entities
│   ├── repositories/            # Data access layer
│   ├── services/                # Business logic
│   └── ShopwayApplication.java  # Main application
└── src/main/resources/
    └── application.properties   # Configuration
```

### Frontend Structure
```
frontend/
├── src/
│   ├── api/                     # API calls
│   ├── components/              # Reusable components
│   │   ├── Filters/             # Product filters
│   │   ├── Navigation/          # Header navigation
│   │   └── common/              # Shared UI components
│   ├── pages/                   # Page components
│   │   ├── Account/             # User profile pages
│   │   ├── AdminPanel/          # Admin dashboard
│   │   ├── Cart/                # Shopping cart
│   │   ├── Checkout/            # Checkout flow
│   │   ├── Login/               # Authentication
│   │   └── ProductListPage/     # Product listings
│   ├── store/                   # Redux store
│   │   ├── features/            # Redux slices
│   │   └── actions/             # Redux actions
│   ├── utils/                   # Utility functions
│   ├── routes.jsx               # Route definitions
│   └── main.jsx                 # App entry point
└── public/                      # Static assets

📡 API Documentation
Authentication Endpoints

POST /api/auth/register - Register new user
POST /api/auth/login - Login with credentials
POST /api/auth/verify - Verify email with code
GET /oauth2/authorization/google - Google OAuth2 login

Product Endpoints

GET /api/products - Get all products (with filters)
GET /api/products/{id} - Get product by ID
GET /api/products?slug={slug} - Get product by slug
POST /api/products - Create product (Admin)
PUT /api/products/{id} - Update product (Admin)

Category Endpoints

GET /api/category - Get all categories
GET /api/category/{id} - Get category by ID
POST /api/category - Create category (Admin)
PUT /api/category/{id} - Update category (Admin)

Order Endpoints

POST /api/order - Create order
POST /api/order/update-payment - Update payment status
GET /api/order/user - Get user orders
POST /api/order/cancel/{orderId} - Cancel order

User Endpoints

GET /api/user/profile - Get user profile
POST /api/address - Add address
DELETE /api/address/{id} - Delete address

For detailed API documentation, visit Swagger UI at http://localhost:8080/swagger-ui.html after starting the backend.

👨‍💼 Admin Panel
Access the admin panel at http://localhost:5173/admin (requires admin role).
Default Admin Setup
To create an admin user:

Register normally through the UI
Manually update the database to add ADMIN role:

sql-- First, create ADMIN authority if it doesn't exist
INSERT INTO auth_authority (id, role_code, role_description)
VALUES (gen_random_uuid(), 'ADMIN', 'Administrator');

-- Then assign it to your user
INSERT INTO auth_user_authority (user_id, authority_id)
VALUES (
    (SELECT id FROM auth_users_details WHERE email = 'your@email.com'),
    (SELECT id FROM auth_authority WHERE role_code = 'ADMIN')
);
Admin Features

Product CRUD operations
Image upload to CDN
Category management
Variant management (sizes, colors, stock)

🐛 Known Issues & TODOs

 Address deletion doesn't verify user ownership
 Stripe amount is hardcoded to 10 (should use order total)
 Missing order refund logic for cancellations
 No password reset functionality
 Limited error handling on frontend
 No product inventory management on checkout

👥 Authors

Ibrahim Bišić - Backend Development
Ahmed Skopljak - Frontend Development

📧 Contact
For questions or support, please open an issue on GitHub or contact:

Email: ibrahimbisic12@gmail.com

React and Redux communities
Stripe for payment processing
All open-source contributors
