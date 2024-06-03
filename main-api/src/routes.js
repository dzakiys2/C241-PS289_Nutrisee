const express = require('express');
const router = express.Router();
const multer = require('multer');
const uploadImage = require('./handler');
const upload = multer();

router.post('/upload', upload.single('image'), uploadImage);

module.exports = router;