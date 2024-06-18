const express = require("express");
const router = express.Router();
const { getProducts, getProductByBarcode } = require("../controllers/productController");
const { verifyToken } = require("../middleware/verifyToken");

/**
 * @swagger
 * /products:
 *   get:
 *     summary: Get all products
 *     description: Retrieve a list of all products from the database.
 *     tags:
 *       - Products
 *     security:
 *       - BearerAuth: []
 *     responses:
 *       200:
 *         description: A list of products.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               additionalProperties:
 *                 type: object
 *                 properties:
 *                   id:
 *                     type: string
 *                     description: The product ID.
 *                   merek:
 *                     type: string
 *                     description: The brand of the product.
 *                   nama_produk:
 *                     type: string
 *                     description: The name of the product.
 *                   ukuran_porsi:
 *                     type: string
 *                     description: The serving size of the product.
 *                   nutriscore:
 *                     type: string
 *                     description: The Nutri-Score of the product.
 *                   nutrisi:
 *                     type: object
 *                     description: The nutritional information of the product.
 *                   ringkasan:
 *                     type: string
 *                     description: The summary of the product.
 *                   sajian:
 *                     type: string
 *                     description: The servings of the product.
 *                   type:
 *                     type: string
 *                     description: The type of the product.
 *                   sub_type:
 *                     type: string
 *                     description: The sub-type of the product.
 *       404:
 *         description: No products found.
 *       500:
 *         description: Internal server error.
 */
router.get("/products", verifyToken, getProducts);

/**
 * @swagger
 * /products/barcode/{barcode}:
 *   get:
 *     summary: Get product by ID or barcode
 *     description: Retrieve specific product details by product ID or barcode.
 *     tags:
 *       - Products
 *     parameters:
 *       - in: path
 *         name: productId
 *         schema:
 *           type: string
 *         required: true
 *         description: The ID of the product entry
 *     security:
 *       - BearerAuth: []
 *     responses:
 *       200:
 *         description: Product details retrieved successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 data:
 *                   type: object
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
 *         description: Product not found.
 *       500:
 *         description: Internal server error.
 */
router.get("/products/barcode/:barcode", verifyToken, getProductByBarcode);
module.exports = router;
