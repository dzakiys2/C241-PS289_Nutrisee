const express = require('express');
const router = express.Router();
const multer = require('multer');
const uploadImage = require('./handler');
const upload = multer();
const {verifyToken} = require('./middleware');

router.post('/upload', verifyToken, upload.single('image'), uploadImage);

module.exports = router;