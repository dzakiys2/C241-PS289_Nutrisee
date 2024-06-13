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
