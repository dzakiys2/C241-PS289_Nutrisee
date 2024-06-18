const admin = require("firebase-admin");
require("dotenv").config();

const verifyToken = async (req, res, next) => {
  try {
    const token = req.headers.authorization;

    if (!token) {
      return res.status(401).json({ error: "Token tidak ditemukan!" });
    }

    const idToken = token.split("Bearer ")[1];
    const decodedToken = await admin.auth().verifyIdToken(idToken);
    req.user = { email: decodedToken.email, uid: decodedToken.uid };
    next();
  } catch (error) {
    console.error(error);
    res.status(401).json({ error: "Unauthorized" });
  }
};

module.exports = { verifyToken };
