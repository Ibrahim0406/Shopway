ShopWay - E-commerce Platform
Modern e-commerce web application built with Spring Boot backend and React frontend, featuring full authentication, product management, shopping cart, and Stripe payment integration.
📋 Table of Contents

Overview
Features
Tech Stack
Architecture
Getting Started
Configuration
API Documentation
Project Structure
Contributing

🎯 Overview
ShopWay is a full-stack e-commerce application that provides a seamless shopping experience with modern UI/UX design. The platform supports product browsing, cart management, secure authentication, and payment processing through Stripe.
✨ Features
Customer Features

User Authentication

Email/Password registration with email verification
Google OAuth2 Sign-In
JWT-based authentication
Secure session management


Product Browsing

Category-based navigation (Men, Women, Kids)
Product filtering by size, color, price
Product search functionality
Detailed product pages with image galleries
Product ratings and reviews


Shopping Cart

Add/remove products
Quantity management
Real-time price calculation
Persistent cart (localStorage)


Checkout Process

Multiple address management
Delivery date selection
Payment method selection (Card, COD, UPI)
Stripe payment integration
Order confirmation


User Account

Profile management
Order history
Multiple delivery addresses
Account settings



Admin Features

Product Management

Create/Edit/Delete products
Product variant management (size, color, stock)
Image upload to Bunny CDN
Category assignment


Category Management

Create/Edit/Delete categories
Category type management



🛠️ Tech Stack
Backend

Framework: Spring Boot 4.0.0
Language: Java 21
Database: PostgreSQL
Authentication: Spring Security + JWT
OAuth2: Google Sign-In
Payment: Stripe API
Storage: AWS S3 SDK (Bunny CDN)
Email: Spring Mail (Gmail SMTP)
API Documentation: Swagger/OpenAPI 3.0

Frontend

Framework: React 19.2.0
Build Tool: Vite 7.2.4
Styling: Tailwind CSS 4.1.18
State Management: Redux Toolkit 2.11.2
Routing: React Router DOM 7.11.0
Payment UI: Stripe React
Admin Panel: React Admin 5.13.4
UI Components: Lucide React icons
Carousel: React Multi Carousel
Form Handling: React Hook Form

🏗️ Architecture
Backend Architecture
├── auth/                    # Authentication & Authorization
│   ├── config/             # JWT & Security Config
│   ├── controller/         # Auth endpoints
│   ├── entities/           # User & Authority entities
│   └── services/           # Auth business logic
├── controllers/            # REST Controllers
├── entities/              # JPA Entities
├── repositories/          # Data Access Layer
├── services/              # Business Logic
├── dto/                   # Data Transfer Objects
├── mapper/                # Entity-DTO Mappers
└── config/                # Application Configuration
Frontend Architecture
├── components/            # Reusable UI Components
│   ├── common/           # Common icons & utilities
│   ├── Filters/          # Product filters
│   └── sections/         # Page sections
├── pages/                # Page components
│   ├── Account/          # User account pages
│   ├── AdminPanel/       # Admin dashboard
│   ├── Cart/             # Shopping cart
│   └── Checkout/         # Checkout flow
├── store/                # Redux store
│   ├── features/         # Redux slices
│   └── actions/          # Redux actions
├── api/                  # API client
└── utils/                # Utility functions
🚀 Getting Started
Prerequisites

Java 21 or higher
Node.js 18+ and npm
PostgreSQL 14+
Maven 3.9+
Stripe Account
Google OAuth2 Credentials
Bunny CDN Account (or AWS S3)

Backend Setup

Clone the repository

bashgit clone <repository-url>
cd Backend/backend

Configure environment variables

Create .env file in Backend/backend/:
envDATABASE_URL=jdbc:postgresql://localhost:5432/shopway
MAIL_PASSWORD=your-gmail-app-password
JWT_KEY=your-jwt-secret-key-base64-encoded
GOOGLE_OAUTH2_CLIENT_ID=your-google-client-id
GOOGLE_OAUTH2_CLIENT_SECRET=your-google-client-secret
stripe.secret=your-stripe-secret-key
FILE_ZONE=your-bunny-storage-zone
FILE_HOST_URL=your-bunny-storage-url
FILE_UPLOAD_KEY=your-bunny-access-key
FILE_SECRET_KEY=your-bunny-secret-key

Build and run

bash./mvnw clean install
./mvnw spring-boot:run
Backend will start on http://localhost:8080
Frontend Setup

Navigate to frontend directory

bashcd frontend

Install dependencies

bashnpm install

Configure environment

Create .env file:
envVITE_API_BASE_URL=http://localhost:8080
VITE_STRIPE_PUBLIC_KEY=your-stripe-public-key

Start development server

bashnpm run dev
Frontend will start on http://localhost:5173
⚙️ Configuration
Database Configuration
Update application.properties:
propertiesspring.datasource.url=${DATABASE_URL}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
Email Configuration
Gmail SMTP settings in application.properties:
propertiesspring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=${MAIL_PASSWORD}
Note: Use App Password for Gmail, not regular password.
JWT Configuration
propertiesjwt.auth.app=shopway
jwt.auth.secret_key=${JWT_KEY}
jwt.auth.expires_in=3600
OAuth2 Configuration
Google OAuth2 in application.properties:
propertiesspring.security.oauth2.client.registration.google.client-id=${GOOGLE_OAUTH2_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_OAUTH2_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google
```

## 📚 API Documentation

Once the backend is running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
Key Endpoints
Authentication

POST /api/auth/register - Register new user
POST /api/auth/login - User login
POST /api/auth/verify - Verify email with code
GET /oauth2/authorization/google - Google OAuth2 login

Products

GET /api/products - Get all products (with filters)
GET /api/products/{id} - Get product by ID
POST /api/products - Create product (Admin)
PUT /api/products/{id} - Update product (Admin)

Categories

GET /api/category - Get all categories
GET /api/category/{id} - Get category by ID
POST /api/category - Create category (Admin)
PUT /api/category/{id} - Update category (Admin)

Cart & Orders

POST /api/order - Place order
POST /api/order/update-payment - Update payment status
GET /api/order/user - Get user orders

User

GET /api/user/profile - Get user profile
POST /api/address - Add delivery address
DELETE /api/address/{id} - Delete address

📁 Project Structure
Key Backend Classes

JWTTokenHelper: JWT token generation and validation
JWTAuthenticationFilter: JWT authentication filter
WebSecurityConfig: Security configuration
ProductService: Product management logic
OrderService: Order processing logic
PaymentIntentService: Stripe payment integration
FileUploadService: File upload to CDN

Key Frontend Components

ProductListPage: Product catalog with filters
ProductDetails: Detailed product view
Cart: Shopping cart management
Checkout: Checkout flow
AdminPanel: Admin dashboard with React Admin

Redux Store Structure
javascript{
  productState: { products: [] },
  cartState: { cart: [] },
  categoryState: { categories: [] },
  commonState: { loading: false },
  userState: { userInfo: {}, orders: [] }
}
🔒 Security Features

JWT Authentication: Secure token-based authentication
Password Encryption: BCrypt password hashing
CORS Configuration: Configurable CORS policy
OAuth2: Google Sign-In integration
Email Verification: Account activation via email
Protected Routes: Frontend route protection
SQL Injection Prevention: JPA/Hibernate parameterized queries

🎨 Design Features

Responsive Design: Mobile-first approach
Tailwind CSS: Modern utility-first styling
Snow Animation: Seasonal effects with react-snowfall
Image Carousels: Product showcases
Loading States: Smooth loading indicators
Modal Dialogs: Confirmation dialogs
Form Validation: Client and server-side validation

🧪 Testing
Run Backend Tests
bashcd Backend/backend
./mvnw test
Run Frontend Tests
bashcd frontend
npm run test
📦 Deployment
Backend Deployment

Build JAR

bash./mvnw clean package

Run JAR

bashjava -jar target/shopway-0.0.1-SNAPSHOT.jar
Frontend Deployment

Build for production

bashnpm run build

Deploy dist/ folder to hosting service (Vercel, Netlify, etc.)

Environment Variables for Production
Ensure all environment variables are set in your production environment:

Database connection strings
API keys (Stripe, Google OAuth2, Bunny CDN)
JWT secret key
Email credentials

🐛 Known Issues & Limitations

AddressService Security: deleteAddress doesn't verify address ownership
PaymentIntentService: Amount is hardcoded to 10 instead of using order.getTotalAmount()
Browser Storage: localStorage/sessionStorage not supported in React Admin artifacts
Error Handling: Some endpoints lack comprehensive error handling


👥 Authors

Ibrahim Bišić
Ahmed Skopljak
