const { query } = require('../db');

async function createUser({ email, password, firstName, lastName, role = 'STUDENT' }) {
  const result = await query(
    'INSERT INTO users(email, password, first_name, last_name, role) VALUES ($1,$2,$3,$4,$5) RETURNING *',
    [email, password, firstName, lastName, role]
  );
  return result.rows[0];
}

async function findByEmail(email) {
  const { rows } = await query('SELECT * FROM users WHERE email = $1', [email]);
  return rows[0] || null;
}

module.exports = { createUser, findByEmail };
