const express = require("express");
const router = express.Router();
const multer = require("multer");
const upload = multer();
const { verifyToken } = require("../middleware/verifyToken");
const { uploadImage } = require("../controllers/uploadController");

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
router.post("/upload", verifyToken, upload.single("image"), uploadImage);
module.exports = router;
