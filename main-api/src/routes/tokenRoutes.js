const express = require("express");
const router = express.Router();
const { generateToken } = require("../controllers/tokenController");
/**
 * @swagger
 * /token:
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

router.post("/token", generateToken);

module.exports = router;