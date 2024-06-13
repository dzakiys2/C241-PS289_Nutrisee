const extractNumericValue = (str) => {
  // Check if str is a string
  if (typeof str !== "string") {
    return NaN; // Return NaN if str is not a string
  }

  // Remove non-numeric characters (except for commas and periods)
  let numStr = str.replace(/[^\d,.-]/g, "");
  // Replace commas with periods if commas are used as decimal separators
  if (numStr.includes(",")) {
    numStr = numStr.replace(/,/g, ".");
  }
  // Parse as float
  return parseFloat(numStr);
};

module.exports = extractNumericValue;
