function filterEmail(email) {
  return email.replace(/[.#$\[\]]/g, "_");
}

module.exports = filterEmail;

