# Environment Variables

1. Go to the **main-api** folder:
    ```
    cd main-api
    ```
2. Create a `.env` file in the **main-api** folder:
    ```
    nano .env
    ```
3. Copy and paste this into your `.env` file and change the value:
    ```
    PORT=<your_port>
    BUCKET_NAME=<your_bucket_name>
    SERVICE_ACCOUNT_KEY='<path/to/your/service account key>
    PROJECT_ID='<your_project_id>'
    DATABASE_URL='<your_firebase_url>'
    FIREBASE_SERVICE_ACCOUNT_KEY='<path/to/your/firebase service account key>'
    ML_API_URL='<your running ml-api url>'
    FIREBASE_API_KEY='<your_firebase_web_api_key>'
    ```
4. Re-deploy the **Main API** and **ML API**.
5. Use the **Main API URL Endpoints** for client.

## Obtaining Environment Variable Values

1. **Service Account Keys**: 
   - Go to the Google Cloud Console.
   - Navigate to `IAM & Admin` > `Service Accounts`.
   - Select your project and create a new service account if you don't have one.
   - Assign the necessary roles (e.g., Cloud Storage Object Creator).
   - Generate a new key (JSON) and download it.
   - Rename and place it in the specified directory.

2. **Firebase Database URL**:
   - Go to your Firebase project in the Firebase Console.
   - Navigate to `Realtime Database`.
   - Copy the Database URL from the database settings.

3. **Firebase API Key**:
   - In the Firebase Console, go to `Project Settings`.
   - Under the `General` tab, find your web API key.

4. **Project ID**:
   - The Project ID can be found in the Google Cloud Console under `Home` > `Dashboard`.

# API Development and Testing Documentation

## Setting Up

1. Ensure you have all environment variables set up as described above.
2. Start the **Main API**:
    ```
    npm start
    ```
3. Start the **ML API** similarly if it's not running already.

## Using Postman for API Testing

### Authorization

1. Obtain a token from the `/get-token` endpoint:
    - Endpoint: `POST /get-token`
    - Body:
      ```json
      {
        "email": "<your_email>",
        "password": "<your_password>"
      }
      ```
    - Response: 
      ```json
      {
        "token": "Bearer <your_token>"
      }
      ```

2. Use the token for authorized requests:
    - In Postman, under the `Authorization` tab, select `Bearer Token`.
    - Paste your token into the `Token` field.

### Endpoints

1. **Upload Image**
    - Endpoint: `POST /upload-image`
    - Headers:
      ```
      Authorization: Bearer <your_token>
      ```
    - Body (form-data):
      ```
      Key: image
      Value: <your_image_file>
      ```
    - Response: 
      ```json
      {
        "data": { ... }
      }
      ```

2. **Get History By ID**
    - Endpoint: `GET /history/:historyId`
    - Headers:
      ```
      Authorization: Bearer <your_token>
      ```
    - Response:
      ```json
      {
        "data": { ... }
      }
      ```

3. **Get History By User**
    - Endpoint: `GET /history`
    - Headers:
      ```
      Authorization: Bearer <your_token>
      ```
    - Response:
      ```json
      {
        "data": [ ... ]
      }
      ```

4. **Get Product By ID**
    - Endpoint: `GET /product/:productId`
    - Headers:
      ```
      Authorization: Bearer <your_token>
      ```
    - Response:
      ```json
      {
        "data": { ... }
      }
      ```

## Using Swagger for API Testing

1. Open Swagger UI (ensure your API server is running):
    - Go to `http://localhost:<your_port>/api-docs`
2. You can test endpoints directly from the Swagger UI:
    - Navigate to the desired endpoint.
    - Click `Try it out`.
    - Fill in the necessary fields.
    - Execute the request.

### Example for `/get-token`

1. Select the `POST /get-token` endpoint.
2. Click `Try it out`.
3. Fill in the body parameters:
    ```json
    {
      "email": "<your_email>",
      "password": "<your_password>"
    }
    ```
4. Click `Execute`.
5. The response will provide you with a token to use for other endpoints.

### Making API Private by Verifying Token

1. Ensure your middleware checks for authorization:
    ```javascript
    const verifyToken = (req, res, next) => {
      const bearerHeader = req.headers['authorization'];
      if (typeof bearerHeader !== 'undefined') {
        const bearer = bearerHeader.split(' ');
        const bearerToken = bearer[1];
        req.token = bearerToken;
        next();
      } else {
        res.sendStatus(403);
      }
    };
    ```
2. Apply the middleware **verifyToken** to routes requiring authentication:
    ```javascript
    app.post('/upload-image', verifyToken, uploadImage);
    app.get('/history/:historyId', verifyToken, getHistoryById);
    app.get('/history', verifyToken, getHistoryByUser);
    app.get('/product/:productId', verifyToken, getProductById);
    ```

## Troubleshooting

1. **Ensure all services are running**:
    - Main API
    - ML API
    - Firebase

2. **Check environment variables**:
    - Ensure all required variables are set and correctly referenced in your code.

3. **Verify network configurations**:
    - Ensure no conflicts in port usage.
    - Verify that your local server is accessible.

4. **Review error logs**:
    - Check console and server logs for error details.

5. **Postman and Swagger**:
    - Ensure headers and body parameters are correctly set.
    - Verify authorization tokens are correctly applied.

By following this documentation, you should be able to set up, develop, and test the API effectively. Make sure to update any changes in endpoints or configurations promptly.