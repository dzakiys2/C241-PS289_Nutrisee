const admin = require('firebase-admin');
require('dotenv').config();

const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
    databaseURL
});

const createDummyUsers = async () => {
  try {
      const user1 = await admin.auth().createUser({
          email: 'zakeh.gt@gmail.com',
          password: 'password123'
      });

      const user2 = await admin.auth().createUser({
          email: 'testuser2@example.com',
          password: 'password123'
      });

      console.log('Dummy users created:', user1.uid, user2.uid);
  } catch (error) {
      console.error('Error creating dummy users:', error);
  }
};

createDummyUsers();

const verifyToken = async (req, res, next) => {
  try {

    const token = req.headers.authorization;

    if (!token) {
          return res.status(401).json({error: 'Token tidak ditemukan!'});
        }
    
    const idToken = token.split('Bearer ')[1];
    const decodedToken = await admin.auth().verifyIdToken(idToken);
    req.user = {email: decodedToken.email, uid: decodedToken.uid};
    next();

    
  } catch (error) {
    console.error(error);
    res.status(401).json({error: 'Unauthorized'});
  }
};

module.exports = {verifyToken};