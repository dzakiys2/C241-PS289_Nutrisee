const formatTimestamp = (timestamp) => {
  const date = new Date(timestamp);
  const pad = (num) => num.toString().padStart(2, "0");
  return `${pad(date.getDate())}/${pad(date.getMonth() + 1)}/${date.getFullYear().toString().slice(-2)} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
};

module.exports = formatTimestamp;
