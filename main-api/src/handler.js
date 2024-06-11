const {Storage} = require('@google-cloud/storage');
require('dotenv').config();
const axios = require('axios');
const filterEmail = require('./helper/emailFilter');
const serviceAccount = process.env.SERVICE_ACCOUNT_KEY;
const projectId = process.env.PROJECT_ID;

const admin = require('firebase-admin');

const FIREBASE_API_KEY = process.env.FIREBASE_API_KEY;

const storage = new Storage({
    projectId,
    serviceAccount
});

const bucketName = process.env.BUCKET_NAME;

const mlAPI = process.env.ML_API_URL;

const uploadImage = async (req, res) => {
  try {
    console.log('Request Headers:', req.headers);
    console.log('Request Body:', req.body);
    console.log('Request File:', req.file);
    if (!req.file) {
      return res.status(400).json({error: 'Gambar tidak ditemukan!'});
    }
    
        const email = req.user.email;
        console.log('User Email:', email);
        const filteredEmail = filterEmail(email);
        console.log('Filtered Email:', filteredEmail);
        const productImage = req.file;
        const currentDate = new Date().toISOString().replace(/:/g, '-');
        const filename = `${filteredEmail}/${currentDate}.jpg`;
        console.log('Filename:', filename);
        await storage.bucket(bucketName).file(filename).save(productImage.buffer, {
            metadata: {
            contentType: 'image/jpeg',
        },
    });

        const imageUrl = `https://storage.googleapis.com/${bucketName}/${filename}`;
        console.log('Image URL:', imageUrl);
        result = await axios.post(mlAPI, {imageUrl})
        const classificationResult = result.data.product_name;
        const confidenceResult = (result.data.confidence * 100).toFixed(2);
        console.log('Classification Result:', classificationResult);
        console.log('Confidence Result:', confidenceResult);
        // Retrieve product data from Firebase
        const dbRef = admin.database().ref('products').child(classificationResult);
        const snapshot = await dbRef.once('value');
        const productData = snapshot.val();

        if (!productData) {
            return res.status(404).json({ error: 'Product not found' });
        }
    
        const data = {
          merek: productData.merek,
          nama_produk: productData.nama_produk,
          ukuran_porsi: productData.ukuran_porsi,
          nutriscore: productData.nutriscore,
          nutrisi: {
            energi: productData.nutrisi.energi,
            karbohidrat: {
              gula: productData.nutrisi.karbohidrat.gula,
              total: productData.nutrisi.karbohidrat.total
            },
            lemak: {
              jenuh: productData.nutrisi.lemak.jenuh,
              total: productData.nutrisi.lemak.total,
              trans: productData.nutrisi.lemak.trans
            },
            protein: productData.nutrisi.protein,
            sodium: productData.nutrisi.sodium
          },
          nutrition_fact_image: 'url dari bucket', // You need to generate this
          product_image: imageUrl, // Assuming imageUrl is generated earlier in your code
          ringkasan: productData.ringkasan,
          sajian: productData.sajian,
          type: productData.type,
          sub_type: productData.sub_type,
          confidence: confidenceResult,
        };
    
        // Save history data to Firebase
        const historyRef = admin.database().ref('history').child(filteredEmail).push();
        await historyRef.set(data);

        res.status(200).json({data});
  } catch (error) {
        console.error(error);
        res.status(500).json({error: 'Internal server error'});
  }
};


/**
 * @swagger
 * /get-token:
 *   post:
 *     summary: Get Firebase access token
 *     description: Sign in with email and password to get an access token for testing.
 *     tags:
 *       - Auth
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               email:
 *                 type: string
 *                 example: atmin.nutrisee@gmail.com
 *               password:
 *                 type: string
 *                 example: password
 *     responses:
 *       200:
 *         description: Access token generated successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 token:
 *                   type: string
 *       400:
 *         description: Bad request - invalid email or password.
 */
const getToken = async (req, res) => {
  const { email, password } = req.body;

  try {
    // Firebase REST API endpoint for verifying password
    const url = `https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=${FIREBASE_API_KEY}`;

    // Request payload
    const payload = {
      email,
      password,
      returnSecureToken: true
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



module.exports = { uploadImage, getToken };