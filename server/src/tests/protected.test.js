const request = require('supertest');
require('./setup');
const app = require('../index');
const { createUser } = require('../models/user');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

describe('Protected endpoint', () => {
  test('no token -> 401', async () => {
    const res = await request(app).get('/api/protected');
    expect(res.status).toBe(401);
  });

  test('invalid token -> 401', async () => {
    const res = await request(app).get('/api/protected').set('Authorization', 'Bearer invalid');
    expect(res.status).toBe(401);
  });

  test('valid token -> 200', async () => {
    const hashed = await bcrypt.hash('pass', 10);
    const user = await createUser({ email: 'a@a.com', password: hashed, role: 'TEACHER' });
    const token = jwt.sign({ userId: user.id, role: user.role }, process.env.JWT_SECRET, { expiresIn: '1h' });
    const res = await request(app).get('/api/protected').set('Authorization', `Bearer ${token}`);
    expect(res.status).toBe(200);
    expect(res.body.user.userId).toEqual(user.id);
    expect(res.body.user.role).toEqual('TEACHER');
  });
});
