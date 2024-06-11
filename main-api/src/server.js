const express = require('express');
const app = express();
const routes = require('./routes');
const admin = require('firebase-admin');
const multer = require('multer');
const upload = multer();
const swaggerJsdoc = require("swagger-jsdoc")
const swaggerUi = require("swagger-ui-express");
const { swaggerDefinition, options } = require('./swagger.js');
const cors = require('cors');

const { nutriScoreCalculator } = require('./functions/nutriscoreCalculator.js');

app.use(express.json());

// Enable CORS for all routes
app.use(cors());


const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
    credential: admin.credential.cert(process.env.FIREBASE_SERVICE_ACCOUNT_KEY),
    databaseURL
});

app.use('/', routes);

  const specs = swaggerJsdoc(options);
  app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(specs));


/**
 * @swagger
 * /products:
 *   get:
 *     summary: Get all products
 *     description: Retrieve a list of all products from the database.
 *     responses:
 *       200:
 *         description: A list of products.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 0:
 *                   type: object
 *                   description: A product object.
 *                   properties:
 *                     id:
 *                       type: string
 *                       description: The product ID.
 *                     merek:
 *                       type: string
 *                       description: The brand of the product.
 *                     nama_produk:
 *                       type: string
 *                       description: The name of the product.
 *                     ukuran_porsi:
 *                       type: string
 *                       description: The serving size of the product.
 *                     nutriscore:
 *                       type: string
 *                       description: The Nutri-Score of the product.
 *                     nutrisi:
 *                       type: object
 *                       description: The nutritional information of the product.
 *                     ringkasan:
 *                       type: string
 *                       description: The summary of the product.
 *                     sajian:
 *                       type: string
 *                       description: The servings of the product.
 *                     type:
 *                       type: string
 *                       description: The type of the product.
 *                     sub_type:
 *                       type: string
 *                       description: The sub-type of the product.
 *       404:
 *         description: No products found.
 *       500:
 *         description: Internal server error.
 */
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

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});