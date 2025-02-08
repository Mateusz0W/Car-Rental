# Car Rental Management System

The **Car Rental Management System** is a JavaFX-based application integrated with PostgreSQL to efficiently manage a car rental business.  
The system supports CRUD operations, data import from CSV files, client and car management, reservation handling, opinion collection, payment processing, and detailed statistical reporting.

## Features

### 1. CRUD Operations
Supports Create, Read, Update, and Delete operations for:
- Clients  
- Cars  
- Reservations  
- Payments  
- Opinions  
- Services  
- Insurance  

### 2. CSV Data Import
- Allows bulk data entry by importing CSV files.  

### 3. Car and Client Management
- Register new clients with personal details.  
- Add and manage cars with details like model, brand, year, etc.  

### 4. Reservation System
- Enable clients to reserve cars.  
- Manage active and completed reservations.  

### 5. Customer Feedback
- Clients can submit opinions and ratings for rented cars.  
- Aggregates average ratings per car.  

### 6. Payment Processing
- Support for payment methods (credit card, cash).  

### 7. Car Service Management
- Store service details including description and cost.  

### 8. Insurance Management
- Purchase and management of car insurance.  

### 9. Statistics & Reports  

#### **Opinions Analysis**  
- Calculate the average opinion rating per car.  

#### **Service Statistics**  
- Summary cost per car for services.  
- Most expensive service with price and description.  

#### **Monthly Income Reports**  
- Display income plots based on rental transactions.  

## Technologies Used
- **Backend:** Java (JDBC for PostgreSQL)  
- **Frontend:** JavaFX  
- **Database:** PostgreSQL  
- **Advanced Database Features:**  
  - Virtual tables  
  - Functions and triggers  
  - Aggregation functions  
  - ENUM data types  
