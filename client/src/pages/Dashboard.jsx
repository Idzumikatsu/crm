import React, { useEffect, useState } from 'react';

export default function Dashboard() {
  const [text, setText] = useState('Loading...');

  useEffect(() => {
    const token = localStorage.getItem('token');
    fetch('/api/protected', {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(r => r.json())
      .then(data => {
        if (data.user) {
          setText(`You are authenticated as ${data.user.userId} with role ${data.user.role}`);
        } else {
          setText('Error');
        }
      })
      .catch(() => setText('Error'));
  }, []);

  return <div>{text}</div>;
}
