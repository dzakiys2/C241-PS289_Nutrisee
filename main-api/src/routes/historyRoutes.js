const express = require("express");
const router = express.Router();
const { getHistoryById, getHistoryByUser, deleteHistoryById } = require("../controllers/historyController");
const { verifyToken } = require("../middleware/verifyToken");
/**
 * @swagger
 * /history:
 *   get:
 *     summary: Get user history
 *     description: Retrieve the user's history sorted by the newest entries.
 *     tags:
 *       - History
 *     security:
 *       - BearerAuth: []
 *     responses:
 *       200:
 *         description: Successfully retrieved user history.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 data:
 *                   type: object
 *                   additionalProperties:
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
router.get("/history", verifyToken, getHistoryByUser);

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
 *               properties:
 *                 data:
 *                   type: object
 *                   additionalProperties:
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
 *       404:
 *         description: History not found.
 *       401:
 *         description: Unauthorized - invalid or missing token.
 *       500:
 *         description: Internal server error.
 */
router.get("/history/:historyId", verifyToken, getHistoryById);

/**
 * @swagger
 * /history/{historyId}:
 *   delete:
 *     summary: Delete history entry by ID
 *     description: Delete a specific history entry by its ID.
 *     tags:
 *       - History
 *     parameters:
 *       - in: path
 *         name: historyId
 *         schema:
 *           type: string
 *         required: true
 *         description: The ID of the history entry to delete
 *     responses:
 *       204:
 *         description: History entry deleted successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   description: Confirmation message of successful deletion.
 *       404:
 *         description: History entry not found.
 *       401:
 *         description: Unauthorized - invalid or missing token.
 *       500:
 *         description: Internal server error.
 */
router.delete("/history/:historyId", verifyToken, deleteHistoryById);
module.exports = router;
