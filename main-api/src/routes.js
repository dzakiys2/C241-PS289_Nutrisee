const express = require('express');
const router = express.Router();
const multer = require('multer');
const { uploadImage, getToken, getHistoryById, getProductById, getHistoryByUser } = require('./handler');
const upload = multer();
const {verifyToken} = require('./middleware');

/**
 * @swagger
 * /upload:
 *   post:
 *     summary: Upload an image
 *     description: Upload an image file to the server.
 *     security:
 *       - BearerAuth: []
 *     consumes:
 *       - multipart/form-data
 *     requestBody:
 *       required: true
 *       content:
 *         multipart/form-data:
 *           schema:
 *             type: object
 *             properties:
 *               image:
 *                 type: string
 *                 format: binary
 *                 description: The image file to upload.
 *     responses:
 *       200:
 *         description: Image uploaded successfully.
 *       400:
 *         description: Bad request - no image file provided.
 *       500:
 *         description: Internal server error.
 */
router.post('/upload', verifyToken, upload.single('image'), uploadImage);

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
 *                 example: user@example.com
 *               password:
 *                 type: string
 *                 example: yourpassword
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

router.post('/get-token', getToken);



/**
 * @swagger
 * /history/{historyId}:
 *   get:
 *     summary: Get history by ID
 *     description: Retrieve specific history details by history ID.
 *     tags:
 *       - History
 *     parameters:
 *       - in: path
 *         name: historyId
 *         schema:
 *           type: string
 *         required: true
 *         description: The ID of the history entry
 *     responses:
 *       200:
 *         description: History details retrieved successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *       404:
 *         description: History not found.
 */
router.get('/history/:historyId', getHistoryById, verifyToken);
  
  /**
   * @swagger
   * /products/{productId}:
   *   get:
   *     summary: Get product by ID
   *     description: Retrieve specific product details by product ID.
   *     tags:
   *       - Products
   *     parameters:
   *       - in: path
   *         name: productId
   *         schema:
   *           type: string
   *         required: true
   *         description: The ID of the product entry
   *     responses:
   *       200:
   *         description: Product details retrieved successfully.
   *         content:
   *           application/json:
   *             schema:
   *               type: object
   *       404:
   *         description: Product not found.
   */
  router.get('/products/:productId', getProductById, verifyToken);

  /**
 * @swagger
 * /history:
 *   get:
 *     summary: Get user history
 *     description: Retrieve the user's history sorted by the newest entries.
 *     tags:
 *       - History
 *     security:
 *       - bearerAuth: []
 *     responses:
 *       200:
 *         description: Successfully retrieved user history.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *                     properties:
 *                       id:
 *                         type: string
 *                         description: Unique identifier for the history entry.
 *                       brand:
 *                         type: string
 *                         description: Brand of the product.
 *                       product_name:
 *                         type: string
 *                         description: Name of the product.
 *                       serving_size:
 *                         type: string
 *                         description: Serving size of the product.
 *                       nutriscore:
 *                         type: string
 *                         description: Nutri-Score of the product.
 *                       nutrition:
 *                         type: object
 *                         properties:
 *                           energi:
 *                             type: number
 *                             description: Energy content of the product.
 *                           karbohidrat:
 *                             type: object
 *                             properties:
 *                               gula:
 *                                 type: number
 *                                 description: Sugar content of the product.
 *                               total:
 *                                 type: number
 *                                 description: Total carbohydrate content of the product.
 *                           lemak:
 *                             type: object
 *                             properties:
 *                               jenuh:
 *                                 type: number
 *                                 description: Saturated fat content of the product.
 *                               total:
 *                                 type: number
 *                                 description: Total fat content of the product.
 *                               trans:
 *                                 type: number
 *                                 description: Trans fat content of the product.
 *                           protein:
 *                             type: number
 *                             description: Protein content of the product.
 *                           sodium:
 *                             type: number
 *                             description: Sodium content of the product.
 *                       nutrition_fact_image:
 *                         type: string
 *                         description: URL to the nutrition fact image.
 *                       product_image:
 *                         type: string
 *                         description: URL to the product image.
 *                       summary:
 *                         type: string
 *                         description: Summary of the product.
 *                       servings:
 *                         type: number
 *                         description: Number of servings.
 *                       type:
 *                         type: string
 *                         description: Type of the product.
 *                       subtype:
 *                         type: string
 *                         description: Subtype of the product.
 *                       confidence:
 *                         type: number
 *                         description: Confidence score of the product classification.
 *                       halal_status:
 *                         type: boolean
 *                         description: Halal status of the product.
 *                       status_logo_url:
 *                         type: string
 *                         description: URL of the status logo (halal or haram).
 *                       timestamp:
 *                         type: integer
 *                         format: int64
 *                         description: Timestamp of the history entry.
 *       401:
 *         description: Unauthorized - invalid or missing token.
 *       500:
 *         description: Internal server error.
 */
  router.get('/history', getHistoryByUser, verifyToken);
module.exports = router;