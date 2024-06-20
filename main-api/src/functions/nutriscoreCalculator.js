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
      const energy = extractNumericValue(product.nutrisi.energi) * factor * 4.184;
      console.log(`energi: ${energy}`);
      const sugar =
      extractNumericValue(product.nutrisi.karbohidrat.gula) * factor;
      console.log(`sugar: ${sugar}`);
      const satFat = extractNumericValue(product.nutrisi.lemak.jenuh) * factor;
      console.log(satFat);
      const salt = extractNumericValue(product.nutrisi.sodium) * factor;
      console.log(salt);
      const protein = extractNumericValue(product.nutrisi.protein) * factor;
      console.log(protein);
      const fiber =
        extractNumericValue(product.nutrisi.karbohidrat.serat || "0g") * factor;
      console.log(fiber);
      const fruitsVegetables = 
      extractNumericValue(product.nutrisi.fvl || "0g") * factor;
      const type = product.type;
      const sub_type = product.sub_type;

      // NutriScore calculation
      const { totalNPoints, totalPPoints, nutritionalScore, NPoints, PPoints } =
        calculateNutriScore(energy, sugar, satFat, salt, protein, fiber, fruitsVegetables, type);

      // Determine NutriScore letter
      const nutriScoreLetter = determineNutriScoreLetter(
        nutritionalScore,
        type,
        sub_type
      );
      console.log(nutriScoreLetter);

      // Generate NutriScore badge link
      const nutriscore = `https://storage.googleapis.com/${bucketName}/nutriscores/nutriscore_${nutriScoreLetter}.png`
      const nutrient_profiling_class = `${nutriScoreLetter}`;
      const nutriscore_label_description = getNutriScoreDescription(nutriScoreLetter);

      const totalFatPercentage = ((extractNumericValue(product.nutrisi.lemak.total) / servingSize) * 100).toFixed(2); // Daily value for total fat is 70g
      const satFatPercentage = ((extractNumericValue(product.nutrisi.lemak.jenuh) / servingSize) * 100).toFixed(2); // Daily value for saturated fat is 20g
      const sugarPercentage = ((extractNumericValue(product.nutrisi.karbohidrat.gula) / servingSize) * 100).toFixed(2); // Daily value for sugars is 90g
      const saltPercentage = (((extractNumericValue(product.nutrisi.sodium) / servingSize) / 1000) * 100).toFixed(2); // Daily value for salt is 6g
      
      const thresholds = {
        totalFat: { low: 15, moderate: 20 },
        satFat: { low: 7, moderate: 12 },
        sugar: { low: 3, moderate: 5 },
        salt: { low: 0.7, moderate: 1.2 },
      };

      const classifyPercentage = (percentage, thresholds) => {
        if (percentage <= thresholds.low) return "low";
        if (percentage <= thresholds.moderate) return "moderate";
        return "high";
      };
      
      const getStatusImageUrl = (status) => {
        switch (status) {
          case "low":
            return "https://storage.googleapis.com/bucket_nutrisee/status/eclipse-green.png";
          case "moderate":
            return "https://storage.googleapis.com/bucket_nutrisee/status/eclipse-yellow.png";
          case "high":
            return "https://storage.googleapis.com/bucket_nutrisee/status/eclipse-red.png";
          default:
            return "https://storage.googleapis.com/bucket_nutrisee/status/eclipse-grey.png";
        }
      };

      const total_fat_status = classifyPercentage(totalFatPercentage, thresholds.totalFat);
      const total_fat_percentage = totalFatPercentage;
      const total_fat_summary = `Total fat in ${total_fat_status} quantity (${total_fat_percentage}%)`;
      const  sat_fat_status = classifyPercentage(satFatPercentage, thresholds.satFat);
      const  sat_fat_percentage = satFatPercentage;
      const  sat_fat_summary = `Total saturated fat in ${sat_fat_status} quantity (${sat_fat_percentage}%)`;
      const  sugar_status = classifyPercentage(sugarPercentage, thresholds.sugar);
      const  sugar_percentage = sugarPercentage;
      const  sugar_summary = `Total sugar in ${sugar_status} quantity (${sugar_percentage}%)`;
      const  salt_status = classifyPercentage(saltPercentage, thresholds.salt);
      const  salt_percentage = saltPercentage;
      const salt_summary = `Total salt in ${salt_status} quantity (${salt_percentage}%)`;
      const summary = {
        total_fat_status: total_fat_status,
        total_fat_percentage: totalFatPercentage,
        total_fat_summary: total_fat_summary,
        total_fat_status_url: getStatusImageUrl(total_fat_status),
        sat_fat_status: sat_fat_status,
        sat_fat_percentage: satFatPercentage,
        sat_fat_summary: sat_fat_summary,
        sat_fat_status_url: getStatusImageUrl(sat_fat_status),
        sugar_status: classifyPercentage(sugarPercentage, thresholds.sugar),
        sugar_percentage: sugarPercentage,
        sugar_summary: sugar_summary,
        sugar_status_url: getStatusImageUrl(sugar_status),
        salt_status: classifyPercentage(saltPercentage, thresholds.salt),
        salt_percentage: saltPercentage,
        salt_summary: salt_summary,
        salt_status_url: getStatusImageUrl(salt_status),
      };

      await admin
        .database()
        .ref("products")
        .child(productId)
        .update({ nutriscore, nutrient_profiling_class, nutriscore_label_description,
        summary,
        barcode_url: "https://storage.googleapis.com/bucket_nutrisee/dummybarcode.png",
      });

      console.log(`NutriScore badge generated for product ${productId}`);
    }

    console.log("NutriScore badges generated for all products.");
  } catch (error) {
    console.error("Error generating NutriScore badges:", error);
  }
};

const calculateNutriScore = (energy, sugar, satFat, salt, protein, fiber, fruitsVegetables, type) => {
  const getNPoints = (value, thresholds) => {
    for (let i = 0; i < thresholds.length; i++) {
      if (i === 0 && value <= thresholds[i]) return i;
      if (i > 0 && value <= thresholds[i] * 1000) return i;
    }
    return thresholds.length;
  };

  const NPoints = {};
  if (type === "beverages") {
    NPoints.energy = getNPoints(energy, [30, 90, 150, 240, 270, 300, 330, 360, 390, 400]);
    NPoints.sugars = getNPoints(sugar, [0.5, 2, 3.5, 6, 7, 8, 9, 10, 11, 12]);
    NPoints.satFat = getNPoints(satFat, [1, 2, 4, 5, 6, 7, 8, 9, 10, 11]);
    NPoints.salt = getNPoints(salt, [0.2, 0.4, 0.8, 1, 1.2, 1.4, 1.6, 1.8, 2, 2.2, 2.4, 2.6, 2.8, 3, 3.2, 3.4, 3.6, 3.8, 4]);
  } else if
    (type === "animals fats" || type === "vegetable fats" || type === "nuts" || type === "seeds" ) {
    NPoints.energy = getNPoints(energy, [120, 240, 360, 480, 600, 720, 840, 960, 1080, 1200]);
    NPoints.sugars = getNPoints(sugar, [3.4, 6.8, 10, 14, 17, 20, 24, 27, 31, 34, 37, 41, 44, 48, 51]);
    NPoints.satFat = getNPoints(satFat, [10, 16, 22, 28, 34, 40, 46, 52, 58, 64]);
    NPoints.salt = getNPoints(salt, [0.2, 0.4, 0.6, 0.8, 1, 1.2, 1.4, 1.6, 1.8, 2, 2.2, 2.4, 2.6, 2.8, 3, 3.2, 3.4, 3.6, 3.8, 4]);
  } else {
    NPoints.energy = getNPoints(energy, [335, 670, 1005, 1340, 1675, 2010, 2345, 2680, 3015, 3350]);
    NPoints.sugars = getNPoints(sugar, [3.4, 6.8, 10, 14, 17, 20, 24, 27, 31, 34, 37, 41, 44, 48, 51]);
    NPoints.satFat = getNPoints(satFat, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);
    NPoints.salt = getNPoints(salt, [0.2, 0.4, 0.6, 0.8, 1, 1.2, 1.4, 1.6, 1.8, 2, 2.2, 2.4, 2.6, 2.8, 3, 3.4, 3.8, 4]);
  }

 
  const PPoints = {};
  if (type === "beverages") {
    PPoints.protein = getNPoints(protein, [1.2, 1.5, 1.8, 2.1, 2.4, 2.7, 3.0]);
    PPoints.fiber = getNPoints(fiber, [3, 4.1, 5.2, 6.3, 7.4]);
  } else if (type === "animal fats" || type === "vegetable fats" || type === "nuts" || type === "seeds" ) {
    PPoints.protein = getNPoints(protein, [2.4, 4.8, 7.2, 9.6, 12, 14, 17]);
    PPoints.fiber = getNPoints(fiber, [3, 4.1, 5.2, 6.3, 7.4]);
  } else {
    PPoints.protein = getNPoints(protein, [2.4, 4.8, 7.2, 9.6, 12, 14, 17]);
    PPoints.fiber = getNPoints(fiber, [3, 4.1, 5.2, 6.3, 7.4]);
  }
  PPoints.fruitsVegetables = getNPoints(fruitsVegetables, [40, 60, 80]);


  const totalNPoints =
    NPoints.energy + NPoints.sugars + NPoints.satFat + NPoints.salt;
  const totalPPoints = PPoints.protein + PPoints.fiber + PPoints.fruitsVegetables;

  let nutritionalScore;
  if (totalNPoints < 11) {
    nutritionalScore = totalNPoints - totalPPoints;
  } else {
    nutritionalScore = totalNPoints - PPoints.fiber;
  }
  console.log(`ppoints: ${totalPPoints}`);
  console.log(`npoints: ${totalNPoints}`);
  console.log(`sugar points: ${NPoints.sugars}`);
  console.log(`salt points: ${NPoints.salt}`);
  console.log(`energy points: ${NPoints.energy}`);
  console.log(`protein points: ${PPoints.protein}`);
  console.log(`serat points: ${PPoints.fiber}`);
  return { totalNPoints, totalPPoints, nutritionalScore, NPoints, PPoints };
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

const getNutriScoreDescription = (letter) => {
  switch (letter) {
    case "A":
      return "Very good nutritional quality";
    case "B":
      return "Good nutritional quality";
    case "C":
      return "Average nutritional quality";
    case "D":
      return "Poor nutritional quality";
    case "E":
      return "Bad nutritional quality";
  }
};
module.exports = {
  calculateNutriScore,
  determineNutriScoreLetter,
  nutriScoreCalculator,
};
