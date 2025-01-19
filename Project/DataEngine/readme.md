## Data engine structure

```
data-engine/
│
├── data/
│   ├── orders.json
│   └── users.json
│
├── engine/
│   ├── orders.py
│   └── users.py
│
├── main.py
└── requirements.txt
```

## Overview

This data engine generates mock data. It creates two main types of data:
- User accounts with authentication details
- Order records with shipping and payment information

## Features

- Generates realistic mock data using Faker library
- Creates consistent relationships between users and orders
- Supports custom data generation configurations
- Outputs data in JSON format
- Includes pre-configured admin and test user accounts

## Data Models

### User Model
- `id`: Unique identifier
- `fullName`: User's full name
- `userName`: Unique username
- `email`: Unique email address
- `password`: User password (default: "pass")
- `isAdmin`: Boolean flag for admin status

### Order Model
- `id`: Unique identifier
- `timestamp`: Order creation time
- `statusDate`: Last status update time
- `addressLine`: Shipping address
- `city`: Shipping city
- `country`: Shipping country
- `poBox`: Post office box number
- `nameOnCard`: Name on payment card
- `expiryYear`: Card expiry year (2022-2030)
- `expiryMonth`: Card expiry month (1-12)
- `cvv`: Card CVV
- `cardNumber`: 16-digit card number
- `phoneNumber`: Contact phone number
- `userId`: Reference to user ID
- `status`: Order status (Shipping/Processing/Delivered)

## Usage

1. Install the required packages:
   ```bash
   pip install -r requirements.txt
   ```

2. Run the data generation:
   ```bash
   python main.py
   ```

3. Generated data will be available in:
   - `data/users.json`
   - `data/orders.json`

## Configuration

The number of records to generate can be configured in `main.py` by modifying the `n` parameter in `file_func_map`:

```python
file_func_map = {
    "data/users.json": partial(generate_users, n=50),
    "data/orders.json": partial(generate_orders, n=50)
}
```

