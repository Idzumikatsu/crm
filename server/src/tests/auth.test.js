const request = require('supertest');
require('./setup');
const app = require('../index');
const { createUser, findByEmail } = require('../models/user');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

describe('Auth Endpoints', () => {
  test('register hashes password', async () => {
    const res = await request(app).post('/api/auth/register').send({
      email: 'a@a.com',
      password: 'pass',
      firstName: 'A',
      lastName: 'B'
    });
    expect(res.status).toBe(201);
    const user = await findByEmail('a@a.com');
    expect(user).not.toBeNull();
    const match = await bcrypt.compare('pass', user.password);
    expect(match).toBe(true);
  });

  test('duplicate email handling', async () => {
    await createUser({ email: 'a@a.com', password: '123' });
    const res = await request(app).post('/api/auth/register').send({
      email: 'a@a.com',
      password: 'pass',
      firstName: 'A',
      lastName: 'B'
    });
    expect(res.status).toBe(400);
  });

  test('login with valid credentials returns token', async () => {
    const hashed = await bcrypt.hash('pass', 10);
    const user = await createUser({ email: 'a@a.com', password: hashed });
    const res = await request(app).post('/api/auth/login').send({
      email: 'a@a.com',
      password: 'pass'
    });
    expect(res.status).toBe(200);
    expect(res.body.token).toBeDefined();
    const decoded = jwt.verify(res.body.token, process.env.JWT_SECRET);
    expect(decoded.userId).toEqual(user.id);
  });

  test('login with invalid credentials returns 401', async () => {
    await createUser({ email: 'a@a.com', password: await bcrypt.hash('pass', 10) });
    const res = await request(app).post('/api/auth/login').send({
      email: 'a@a.com',
      password: 'wrong'
    });
    expect(res.status).toBe(401);
  });
});
