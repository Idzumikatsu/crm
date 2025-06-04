const { Pool } = require('pg');

let pool = new Pool({ connectionString: process.env.DATABASE_URL || 'postgresql://localhost/postgres' });

function setPool(p) {
  pool = p;
}

async function init() {
  await pool.query(`CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    first_name TEXT,
    last_name TEXT,
    role TEXT DEFAULT 'STUDENT'
  )`);
}

function query(text, params) {
  return pool.query(text, params);
}

module.exports = { init, query, setPool };
