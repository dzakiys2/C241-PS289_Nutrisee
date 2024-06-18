const axios = require("axios");
const FIREBASE_API_KEY = process.env.FIREBASE_API_KEY;

const generateToken = async (req, res) => {
    const { email, password } = req.body;
  
    try {
      // Firebase REST API endpoint for verifying password
      const url = `https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=${FIREBASE_API_KEY}`;
  
      // Request payload
      const payload = {
        email,
        password,
        returnSecureToken: true,
      };
  
      // Make the HTTP POST request to Firebase Authentication REST API
      const response = await axios.post(url, payload);
  
      // Extract the ID token from the response
      const idToken = response.data.idToken;
  
      // Send the ID token in the response
      res.json({ token: `Bearer ${idToken}` });
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  };
  
  module.exports = {
    generateToken,
  };
  