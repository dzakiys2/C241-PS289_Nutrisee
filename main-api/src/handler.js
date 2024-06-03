const {Storage} = require('@google-cloud/storage');
require('dotenv').config();
const filterEmail = require('./helper/emailFilter');
const serviceAccount = process.env.SERVICE_ACCOUNT_KEY;
const projectId = 'capstone-project-424614';

const storage = new Storage({
    projectId,
    serviceAccount
});

const bucketName = process.env.BUCKET_NAME;

const mlAPI = process.env.ML_API_URL;

const uploadImage = async (req, res) => {
  try {

    if (!req.file) {
      return res.status(400).json({error: 'Gambar tidak ditemukan!'});
    }
        const userEmail = "dummy123@gmail.com";
        const filteredEmail = filterEmail(userEmail);
        const productImage = req.file;
        const currentDate = new Date().toISOString().replace(/:/g, '-');
        const filename = `${filteredEmail}/${currentDate}.jpg`;

        await storage.bucket(bucketName).file(filename).save(productImage.buffer, {
            metadata: {
            contentType: 'image/jpeg',
        },
    });

        const imageUrl = `https://storage.googleapis.com/${bucketName}/${filename}`;

        res.status(200).json({imageUrl});
  } catch (error) {
        console.error(error);
        res.status(500).json({error: 'Internal server error'});
  }
};

module.exports = uploadImage;