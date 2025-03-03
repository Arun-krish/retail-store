# Retail Store Reward Program
This application is used to generate Reward points for Purchase orders.

# Features
- Save Customers
- Save Purchase orders based on Customers
- Bulk process Purchase orders
- Calculate Reward Points based on Purchase Orders
- Fetch Customer based Order history

# Tech Stack
- Java 17
- Spring Boot 3.4
- MongoDB 5.0

# API Details
- **To save Customer** -'http://localhost:8080/api/customer/saveCustomer' \
  --data '{
    "name":"AK1",
    "mobile":"9876543210" }'
- **To save Purchase Order** -'http://localhost:8080/api/purchaseOrder/savePurchaseOrder'
    --data '{
    "customerId": "677be0ef6ffe6034a2e95c63",
    "orderTotal": 100,
    "orderDate": "2025-01-01"
  }'  
- **To Bulk process Purchase Orders** -http://localhost:8080/api/purchaseOrder/bulkProcessPurchaseOrders
- **To Fetch Order history** --location 'http://localhost:8080/api/purchaseOrder/fetchOrderHistory' \
      --header 'customerId: 677be0ef6ffe6034a2e95c63' \
      --header 'fromDate: 2025-01-01' \
      --header 'toDate: 2025-01-10' \
      --header 'lastThreeMonths: true'
    - If only From date is chosen data will populate from the date to current date.
    - If Both From and To date is chosen ,data will populate between the range.
    - If lastThreeMonths tag is given as true then the last three months data is populated.
    - If none of the options are chosen by default it populates all orders for the customer
    - **Response**

```
{
    "status": "SUCCESS",
    "statusDescription": "Details Fetched Successfully!",
    "data": {
        "Customer Name": "ak",
        "Customer Mobile": "9876543210",
        "Total Orders": 2,
        "Total Order Value": 230.0,
        "Total Reward Points": 250.0,
        "Orders": [
            {
                "id": "677e6519a2993a67a18112b2",
                "customerId": "677e6480a2993a67a18112a8",
                "orderId": "O4",
                "orderDate": "2025-01-01",
                "orderTotal": 30.0,
                "totalRewards": 0.0,
                "createdOn": "2025-01-08",
                "createdBy": "ADMIN_USER"
            },
            {
                "id": "677e6519a2993a67a18112b3",
                "customerId": "677e6480a2993a67a18112a8",
                "orderId": "O5",
                "orderDate": "2025-01-10",
                "orderTotal": 200.0,
                "totalRewards": 250.0,
                "createdOn": "2025-01-08",
                "createdBy": "ADMIN_USER"
            }
        ]
    }
}
```

# Unit Test Results
![Unit Test Results](https://github.com/Arun-krish/retail-store/blob/master/src/main/resources/testresults/TestResults.png)
