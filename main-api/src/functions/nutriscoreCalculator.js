const admin = require("firebase-admin");
const extractNumericValue = require("../helper/extractNumber.js");
const bucketName = process.env.BUCKET_NAME; // Your Google Cloud Storage bucket name

const nutriScoreCalculator = async () => {
  try {
    // Retrieve all products from Firebase
    const snapshot = await admin.database().ref("products").once("value");
    const products = snapshot.val();

    // Iterate through each product
    for (const productId in products) {
      const product = products[productId];
      console.log(product);
      // Extract numeric values from product data
      const servingSize = extractNumericValue(product.ukuran_porsi); // Assuming serving size is in grams
      const factor = 100 / servingSize;
      const energy = extractNumericValue(product.nutrisi.energi) * factor;
      console.log(energy);
      const sugar =
        extractNumericValue(product.nutrisi.karbohidrat.gula) * factor;
      console.log(sugar);
      const satFat = extractNumericValue(product.nutrisi.lemak.jenuh) * factor;
      console.log(satFat);
      const salt = extractNumericValue(product.nutrisi.sodium) * factor;
      console.log(salt);
      const protein = extractNumericValue(product.nutrisi.protein) * factor;
      console.log(protein);
      const fiber =
        extractNumericValue(product.nutrisi.karbohidrat.serat || "0g") * factor;
      console.log(fiber);
      const type = product.type;
      const sub_type = product.sub_type;

      // NutriScore calculation
      const { totalNPoints, totalPPoints, nutritionalScore } =
        calculateNutriScore(energy, sugar, satFat, salt, protein, fiber);

      // Determine NutriScore letter
      const nutriScoreLetter = determineNutriScoreLetter(
        nutritionalScore,
        type,
        sub_type
      );
      console.log(nutriScoreLetter);

      // Generate NutriScore badge link
      const nutriscore = `https://storage.googleapis.com/${bucketName}/nutriscores/nutriscore_${nutriScoreLetter}.png`;

      // Update product data in Firebase with the generated NutriScore badge link
      await admin
        .database()
        .ref("products")
        .child(productId)
        .update({ nutriscore, totalNPoints, totalPPoints });

      console.log(`NutriScore badge generated for product ${productId}`);
    }

    console.log("NutriScore badges generated for all products.");
  } catch (error) {
    console.error("Error generating NutriScore badges:", error);
  }
};

const calculateNutriScore = (energy, sugar, satFat, salt, protein, fiber) => {
  const getNPoints = (value, thresholds) => {
    for (let i = 0; i < thresholds.length; i++) {
      if (i === 0 && value <= thresholds[i]) return i;
      if (i > 0 && value <= thresholds[i] * 1000) return i;
    }
    return thresholds.length;
  };

  const NPoints = {
    energy: getNPoints(
      energy,
      [120, 240, 360, 480, 600, 720, 840, 960, 1080, 1200]
    ),
    sugars: getNPoints(sugar, [3.4, 6.8, 10, 14, 17, 20, 24, 27, 31, 34]),
    satFat: getNPoints(satFat, [10, 16, 22, 28, 34, 40, 46, 52, 58, 64]),
    salt: getNPoints(salt, [0.2, 0.4, 0.6, 0.8, 1, 1.2, 1.4, 1.6, 1.8, 2]),
  };

  const PPoints = {
    protein: getNPoints(protein, [2.4, 4.8, 7.2, 9.6, 12, 14, 17]),
    fiber: getNPoints(fiber, [3, 4.1, 5.2, 6.3, 7.4]),
  };

  const totalNPoints =
    NPoints.energy + NPoints.sugars + NPoints.satFat + NPoints.salt;
  const totalPPoints = PPoints.protein + PPoints.fiber;

  let nutritionalScore;
  if (totalNPoints < 11) {
    nutritionalScore = totalNPoints - totalPPoints;
  } else {
    nutritionalScore = totalNPoints - PPoints.fiber;
  }

  return { totalNPoints, totalPPoints, nutritionalScore };
};

// Example function for determining NutriScore letter
const determineNutriScoreLetter = (score, type, sub_type) => {
  if (type === "beverages") {
    if (score === 0 || sub_type === "water") return "A"; // Assuming 'waters' criteria can be identified with score 0
    if (score <= 2) return "B";
    if (score <= 6) return "C";
    if (score <= 9) return "D";
    return "E";
  } else if (
    type === "vegetable fats" ||
    type === "animal fats" ||
    type === "seeds" ||
    type === "nuts"
  ) {
    if (score <= -6) return "A";
    if (score <= 2) return "B";
    if (score <= 10) return "C";
    if (score <= 18) return "D";
    return "E";
  } else {
    if (score <= 0) return "A";
    if (score <= 2) return "B";
    if (score <= 10) return "C";
    if (score <= 18) return "D";
    return "E";
  }
};

module.exports = {
  calculateNutriScore,
  determineNutriScoreLetter,
  nutriScoreCalculator,
};
