const { newDb } = require('pg-mem');
let init, setPool;

let db;

beforeAll(async () => {
  process.env.JWT_SECRET = 'testsecret';
  db = newDb();
  const { Pool: MockPool } = db.adapters.createPg();
  ({ init, setPool } = require('../db'));
  setPool(new MockPool());
  await init();
});

afterEach(async () => {
  await db.public.none('TRUNCATE TABLE users');
});

afterAll(async () => {
  // no-op
});
