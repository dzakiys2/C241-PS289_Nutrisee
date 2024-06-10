const express = require('express');
const app = express();
const routes = require('./routes');
const admin = require('firebase-admin');
const multer = require('multer');
const upload = multer();
const { nutriScoreCalculator } = require('./functions/nutriscoreCalculator.js');

app.use(express.json());
const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
    credential: admin.credential.cert(process.env.FIREBASE_SERVICE_ACCOUNT_KEY),
    databaseURL
});

app.use('/', routes);

app.get('/products', async (req, res) => {
    try {
        const dbRef = admin.database().ref('products');
        const snapshot = await dbRef.once('value');
        const products = snapshot.val();
  
        if (!products) {
            return res.status(404).json({ error: 'No products found' });
        }
        
        // Convert products object into an array of objects
        const productsArray = Object.keys(products).map(key => ({
            id: key,
            ...products[key]
        }));
        
        // Organize products into an object with numeric keys
        const productsObject = {};
        productsArray.forEach((product, index) => {
            productsObject[index] = product;
        });

        res.status(200).json(productsObject);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

nutriScoreCalculator();

const port = process.env.PORT || 6000;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});