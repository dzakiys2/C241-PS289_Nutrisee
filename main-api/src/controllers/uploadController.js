const { Storage } = require("@google-cloud/storage");
require("dotenv").config();
const axios = require("axios");
const filterEmail = require("../helper/emailFilter");
const serviceAccount = process.env.SERVICE_ACCOUNT_KEY;
const projectId = process.env.PROJECT_ID;
const { v4: uuidv4 } = require("uuid");
const moment = require("moment-timezone");
const admin = require("firebase-admin");

const storage = new Storage({
  projectId,
  serviceAccount,
});

const bucketName = process.env.BUCKET_NAME;

const mlAPI = process.env.ML_API_URL;
console.log(mlAPI);
const uploadImage = async (req, res) => {
  try {
    console.log("Request Headers:", req.headers);
    console.log("Request Body:", req.body);
    console.log("Request File:", req.file);
    if (!req.file) {
      return res.status(400).json({ error: "Gambar tidak ditemukan!" });
    }

    const email = req.user.email;
    console.log("User Email:", email);
    const filteredEmail = filterEmail(email);
    console.log("Filtered Email:", filteredEmail);
    const productImage = req.file;
    const currentDate = new Date().toISOString().replace(/:/g, "-");
    const filename = `${filteredEmail}/${currentDate}.jpg`;
    console.log("Filename:", filename);
    await storage
      .bucket(bucketName)
      .file(filename)
      .save(productImage.buffer, {
        metadata: {
          contentType: "image/jpeg",
        },
      });

    const imageUrl = `https://storage.googleapis.com/${bucketName}/${filename}`;
    console.log("Image URL:", imageUrl);
    result = await axios.post(mlAPI, { imageUrl });
    const classificationResult = result.data.product_name;
    const confidenceResult = (result.data.confidence * 100).toFixed(2);
    console.log("Classification Result:", classificationResult);
    console.log("Confidence Result:", confidenceResult);
    // Retrieve product data from Firebase
    const dbRef = admin.database().ref("products").child(classificationResult);
    const snapshot = await dbRef.once("value");
    const productData = snapshot.val();

    if (!productData) {
      return res.status(404).json({ error: "Product not found" });
    }

    let statusLogoUrl = null;
    if (productData.isHalal) {
      statusLogoUrl = `https://storage.googleapis.com/${bucketName}/logo-halal.png`; // Adjust the path as necessary
    } else {
      statusLogoUrl = `https://storage.googleapis.com/${bucketName}/logo-haram.png`;
    }

    // Get the user's timezone from the request headers or body
    const userTimezone = req.headers['timezone'] || req.body.timezone || 'Asia/Jakarta';
    console.log(`User Timezone: ${userTimezone}`);
    
    // Format the timestamp in the user's timezone
    const formattedTimestamp = moment().tz(userTimezone).format('DD/MM/YYYY HH:mm:ss');
    const data = {
      history_id: uuidv4(),
      product_image: imageUrl, // Assuming imageUrl is generated earlier in your code
      merek: productData.merek,
      nama_produk: productData.nama_produk,
      confidence: confidenceResult,
      barcode: productData.barcode,
      barcode_url: productData.barcode_url,
      ukuran_porsi: productData.ukuran_porsi,
      nutrient_profiling_class: productData.nutrient_profiling_class,
      halal_description: productData.halal_description,
      isHalal: productData.isHalal, // Indicate if the product is halal
      status_logo_url: statusLogoUrl, // URL of the halal or haram logo
      nutriscore: productData.nutriscore,
      nutriscore_label_description: productData.nutriscore_label_description,
      sajian: productData.sajian,
      summary: {
        salt_status: productData.summary.salt_status,
        salt_percentage: productData.summary.salt_percentage,
        salt_summary: productData.summary.salt_summary,
        salt_status_url: productData.summary.salt_status_url,
        sat_fat_percentage: productData.summary.sat_fat_percentage,
        sat_fat_status: productData.summary.sat_fat_status,
        sat_fat_summary: productData.summary.sat_fat_summary,
        sat_fat_status_url: productData.summary.sat_fat_status_url,
        sugar_percentage: productData.summary.sugar_percentage,
        sugar_status: productData.summary.sugar_status,
        sugar_summary: productData.summary.sugar_summary,
        sugar_status_url: productData.summary.sugar_status_url,
        total_fat_percentage: productData.summary.total_fat_percentage,
        total_fat_status: productData.summary.total_fat_status,
        total_fat_summary: productData.summary.total_fat_summary,
        total_fat_status_url: productData.summary.total_fat_status_url,
      },
      nutrisi: {
        energi: productData.nutrisi.energi,
        karbohidrat: {
          gula: productData.nutrisi.karbohidrat.gula,
          total: productData.nutrisi.karbohidrat.total,
          fiber: productData.nutrisi.karbohidrat.fiber,
        },
        lemak: {
          jenuh: productData.nutrisi.lemak.jenuh,
          total: productData.nutrisi.lemak.total,
          trans: productData.nutrisi.lemak.trans,
        },
        protein: productData.nutrisi.protein,
        sodium: productData.nutrisi.sodium,
      },
      nutrition_fact_image: productData.nutrition_fact_image, // You need to generate this
      type: productData.type,
      sub_type: productData.sub_type,
      display_timestamp: formattedTimestamp,
      db_timestamp: admin.database.ServerValue.TIMESTAMP,
    };

    // Save history data to Firebase
    const historyRef = admin
      .database()
      .ref("history")
      .child(filteredEmail)
      .child(data.history_id);
    await historyRef.set(data);

    res.status(200).json({ data });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal server error" });
  }
};

module.exports = {
    uploadImage,
  };
  