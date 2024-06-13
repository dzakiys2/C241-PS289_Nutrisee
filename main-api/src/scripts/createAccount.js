const admin = require("firebase-admin");

const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
  credential: admin.credential.cert(
    "/root/C241-PS289_Nutrisee/main-api/capstone-project-424614-firebase-adminsdk-wezef-44b1e1ad4c.json"
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
const password = "password";

// Create the user
createUser(email, password);
