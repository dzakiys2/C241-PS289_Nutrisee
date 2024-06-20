const filterEmail = require("../helper/emailFilter");
const admin = require("firebase-admin");

const getHistoryByUser = async (req, res) => {
    try {
      console.log("User Email:", req.user.email);
      const email = req.user.email;
      const filteredEmail = filterEmail(email);
  
      const historyRef = admin
        .database()
        .ref("history")
        .child(filteredEmail)
        .orderByChild("db_timestamp");
      const snapshot = await historyRef.once("value");
      const historyData = [];
  
      snapshot.forEach((childSnapshot) => {
        historyData.push({ history_id: childSnapshot.key, ...childSnapshot.val() });
      });
  
      const historyObject = {};
      historyData.reverse().forEach((history, index) => {
        historyObject[index] = history;
      });
  
      res.status(200).json({ data: historyObject });
    } catch (error) {
      console.error("Error retrieving history:", error);
      res.status(500).json({ error: "Internal server error" });
    }
  };
  
  const getHistoryById = async (req, res) => {
    const historyId = req.params.historyId;
    const email = req.user.email; //req.user.email
    console.log(email);
    const filteredEmail = filterEmail(email);
  
    try {
      const historyRef = admin
        .database()
        .ref("history")
        .child(filteredEmail)
        .child(historyId);
      const snapshot = await historyRef.once("value");
      const historyData = snapshot.val();
  
      if (!historyData) {
        return res.status(404).json({ error: "History not found" });
      }
  
      //  // Convert history data into an object with numeric keys
      //  const historyArray = [{ history_id: historyId, ...historyData }];
      //  const historyObject = {};
      //  historyArray.forEach((history, index) => {
      //    historyObject[index] = history;
      //  });
   
       res.status(200).json({ data: historyData });
     } catch (error) {
       console.error(error);
       res.status(500).json({ error: "Internal server error" });
     }
   };
   
   const deleteHistoryById = async (req, res) => {
    const historyId = req.params.historyId;
    const email = req.user.email; //req.user.email
    console.log(email);
    const filteredEmail = filterEmail(email);
  
    try {
      const historyRef = admin.database().ref("history").child(filteredEmail).child(historyId); // Use db reference here
      const snapshot = await historyRef.once("value");
      const historyData = snapshot.val();
  
      if (!historyData) {
        return res.status(404).json({ error: "History not found" });
      }
  
      await historyRef.remove(); // Remove history entry from Firebase
  
      res.status(204).json({ message: "History successfully deleted" });
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: "Internal server error" });
    }
  };

  
module.exports = {
    getHistoryByUser,
    getHistoryById,
    deleteHistoryById
  };
  