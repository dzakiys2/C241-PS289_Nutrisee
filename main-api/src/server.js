const express = require("express");
const app = express();
const routes = require("./routes/index.js");
const admin = require("firebase-admin");
const swaggerJsdoc = require("swagger-jsdoc");
const swaggerUi = require("swagger-ui-express");
const { options } = require("./config/swagger.js");
const cors = require("cors");
const fs = require("fs");
const bodyParser = require("body-parser");
// const { nutriScoreCalculator } = require("./functions/nutriscoreCalculator.js");

app.use(express.json());

// Enable CORS for all routes
app.use(bodyParser.json());
app.use(cors());

const serviceAccount = JSON.parse(
  fs.readFileSync(process.env.FIREBASE_SERVICE_ACCOUNT_KEY, "utf8")
);

const databaseURL = process.env.DATABASE_URL;
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL,
});

app.use("/", routes);

const specs = swaggerJsdoc(options);
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(specs));

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});
