const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { createUser, findByEmail } = require('../models/user');

const router = express.Router();

router.post('/register', async (req, res) => {
  const { email, password, firstName, lastName } = req.body;
  const existing = await findByEmail(email);
  if (existing) return res.status(400).json({ message: 'Email already used' });
  const hashed = await bcrypt.hash(password, 10);
  const user = await createUser({ email, password: hashed, firstName, lastName });
  res.status(201).json({ message: 'User registered', userId: user.id });
});

router.post('/login', async (req, res) => {
  const { email, password } = req.body;
  const user = await findByEmail(email);
  if (!user) return res.status(401).json({ message: 'Invalid credentials' });
  const match = await bcrypt.compare(password, user.password);
  if (!match) return res.status(401).json({ message: 'Invalid credentials' });
  const token = jwt.sign({ userId: user.id, role: user.role }, process.env.JWT_SECRET || 'secret', { expiresIn: '1h' });
  res.json({ token });
});

module.exports = router;
