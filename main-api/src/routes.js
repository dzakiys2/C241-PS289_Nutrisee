const express = require('express');
const router = express.Router();
const multer = require('multer');
const { uploadImage, getToken } = require('./handler');
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

module.exports = router;