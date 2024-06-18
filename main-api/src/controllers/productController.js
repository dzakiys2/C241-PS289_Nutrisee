const admin = require("firebase-admin");

const getProducts = async (res) => {
    try {
      const dbRef = admin.database().ref("products");
      const snapshot = await dbRef.once("value");
      const products = snapshot.val();
  
      if (!products) {
        return res.status(404).json({ error: "No products found" });
      }
  
      // Convert products object into an array of objects
      const productsArray = Object.keys(products).map((key) => ({
        id: key,
        ...products[key],
      }));
  
      // Organize products into an object with numeric keys
      const productsObject = {};
      productsArray.forEach((product, index) => {
        productsObject[index] = product;
      });
  
      res.status(200).json(productsObject);
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: "Internal server error" });
    }
  };
  
  const getProductByBarcode = async (req, res) => {
  const barcode = req.params.barcode;

  try {
    const productRef = admin.database().ref("products").child(barcode);
    const snapshot = await productRef.once("value");
    const productData = snapshot.val();

    if (!productData) {
      return res.status(404).json({ error: "Product not found" });
    }

    res.status(200).json({ data: productData });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal server error" });
  }
};

module.exports = {
    getProducts,
    getProductByBarcode,
  };
  