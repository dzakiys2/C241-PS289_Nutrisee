const express = require('express');
const app = express();
const routes = require('./routes');

app.use(express.json());

app.use('/', routes);

const port = process.env.PORT || 6000;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});