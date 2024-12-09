import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
import './Login.css';

function Login() {
    const [username, setUsername] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const { setAuthenticatedUser } = useAuth();

    useEffect(() => {
        const storedUser = localStorage.getItem('authenticatedUser');
        if (storedUser) {
            setAuthenticatedUser(JSON.parse(storedUser));
            navigate('/');
        }
    }, [navigate, setAuthenticatedUser]);

    const handleLogin = (e) => {
        e.preventDefault();

        if (username === 'admin123') {
            const adminUser = { id: 1, name: 'Admin', username: 'admin123' };
            setMessage(`Welcome, ${adminUser.name}!`);
            setAuthenticatedUser(adminUser);
            localStorage.setItem('authenticatedUser', JSON.stringify(adminUser));
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
