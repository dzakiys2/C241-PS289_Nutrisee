const express = require('express');
const app = express();
// const routes = require('./routes');

app.use(express.json());

// app.use('/', upload);
app.get('/', (req, res) => {
  res.send('Hello World!')
})

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});