const express = require("express");
const router = express.Router();
const productRoutes = require("./productRoutes");
const historyRoutes = require("./historyRoutes");
const uploadRoutes = require("./uploadRoutes");
const tokenRoutes = require("./tokenRoutes");

router.use(productRoutes);
router.use(historyRoutes);
router.use(uploadRoutes);
router.use(tokenRoutes);

module.exports = router;