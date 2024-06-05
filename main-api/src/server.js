const express = require('express');
const app = express();
const routes = require('./routes');
const admin = require('firebase-admin');
const multer = require('multer');
const upload = multer();

app.use(express.json());
const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
    credential: admin.credential.cert(process.env.FIREBASE_SERVICE_ACCOUNT_KEY),
    databaseURL
});

app.use('/', routes);

app.post('/get-nutrition', upload.none(), async (req, res) => {
  try {
      const { productName } = req.body;
      console.log('Request Body:', req.body);
      if (!productName) {
          return res.status(400).json({ error: 'Product name is required' });
      }

      const dbRef = admin.database().ref('products').child(productName);
      const snapshot = await dbRef.once('value');
      const nutritionData = snapshot.val();

      if (!nutritionData) {
          return res.status(404).json({ error: 'Product not found' });
      }

      res.status(200).json({ productName, size: "100", nutrition: nutritionData });
  } catch (error) {
      console.error(error);
      res.status(500).json({ error: 'Internal server error' });
  }
});

const port = process.env.PORT || 6000;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});