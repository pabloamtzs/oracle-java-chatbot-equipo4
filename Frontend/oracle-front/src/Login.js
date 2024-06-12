import React, { useState } from 'react';
import API_LIST from './API';
//import Cookies from 'js-cookie';

function Login({ setToken }) {
    const [email, setUsername] = useState('');
    const [contrasena, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch(API_LIST+ '/auth/login', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
              'Access-Control-Allow-Headers': 'X-Requested-With'
            },
            body: JSON.stringify({ email, contrasena }),
            mode: 'cors' 
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Login failed');
            }
            return response.json();
        })
        .then(data => {
            setToken(data.token);
            localStorage.setItem('authToken', data.token);
            //Cookies.set('authToken', data.token);
        })
        .catch(error => {
            setError(error.message);
        });
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={email}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Username"
            />
            <input
                type="password"
                value={contrasena}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
            <button type="submit">Login</button>
            {error && <p>{error}</p>}
        </form>
    );
}

export default Login