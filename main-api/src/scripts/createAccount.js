const admin = require("firebase-admin");

const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
  credential: admin.credential.cert(
    process.env.FIREBASE_SERVICE_ACCOUNT_KEY
  ),
  databaseURL,
});
// Function to create a user
const createUser = async (email, password) => {
  try {
    const userRecord = await admin.auth().createUser({
      email: email,
      password: password,
    });
    console.log("Successfully created new user:", userRecord.uid);
  } catch (error) {
    console.error("Error creating new user:", error);
  }
};

// Replace with the email and password you want to create
const email = "atmin.nutrisee@gmail.com";
const password = "yourpassword";

// Create the user
createUser(email, password);

// to start the script, use node <path to createAccount.js>