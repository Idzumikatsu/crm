const express = require('express');
const authRoutes = require('./routes/auth');
const authenticateJWT = require('./middleware/authenticateJWT');
const { init } = require('./db');

const app = express();
app.use(express.json());

app.use('/api/auth', authRoutes);

app.get('/api/protected', authenticateJWT, (req, res) => {
  res.json({ message: 'You are authenticated', user: req.user });
});

const PORT = process.env.PORT || 3000;

if (process.env.NODE_ENV !== 'test') {
  init().then(() => {
    app.listen(PORT, () => console.log(`Server running on ${PORT}`));
  }).catch(err => console.error(err));
}

module.exports = app;
