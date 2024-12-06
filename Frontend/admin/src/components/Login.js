import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from './AuthContext';
import './Login.css';

function Login() {
    const [username, setUsername] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const { setAuthenticatedUser } = useAuth();

    const handleLogin = (e) => {
        e.preventDefault();

        if (username === 'admin123') {
            // Simulating a successful login
            const adminUser = { id: 1, name: 'Admin', username: 'admin123' };
            setMessage(`Welcome, ${adminUser.name}!`);
            setAuthenticatedUser(adminUser);
            navigate('/');
        } else {
            setMessage('Access denied. Only "admin123" can log in.');
        }
    };

    return (
        <div className="auth-container">
            <h1>Login admin</h1>
            <form onSubmit={handleLogin}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </label>
                <br />
                <button type="submit">Login</button>
            </form>
            {message && <p className="message">{message}</p>}
            <p>
                Admin page
            </p>
        </div>
    );
}

export default Login;
