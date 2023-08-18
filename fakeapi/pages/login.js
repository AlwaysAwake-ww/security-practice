// pages/login.js
import React, { useState } from 'react';
import { useRouter } from 'next/router'; // Next.js의 라우터 라이브러리

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const router = useRouter(); // 라우터 객체 생성

  const handleLogin = async () => {
    try {
      const response = await fetch('http://localhost:8080/members/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();
      console.log('API Response:', data);

      if (response.ok) {
        localStorage.setItem('loginData', JSON.stringify(data));
        console.log('Access token saved to local storage.');

        // 로그인 성공 시 페이지 이동
        router.push('/loginWelcome');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div>
      <h1>Login</h1>
      <div>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div>
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <div>
        <button onClick={handleLogin}>Login</button>
      </div>
    </div>
  );
}

export default Login;
