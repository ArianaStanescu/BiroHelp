import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
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

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/clients');
            if (response.ok) {
                const clients = await response.json();
                const user = clients.find(client => client.username === username);

                if (user) {
                    setMessage(`Welcome, ${user.name}!`);
                    setAuthenticatedUser(user);
                    localStorage.setItem('authenticatedUser', JSON.stringify(user)); // Save the user in localStorage
                    navigate('/');
                } else {
                    setMessage('User not found');
                }
            } else {
                setMessage('Failed to fetch clients');
            }
        } catch (error) {
            setMessage('An error occurred. Please try again later.');
        }
    };

    return (
        <div className="auth-container">
            <h1>Login</h1>
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
                Don't have an account? <Link to="/register">Register here</Link>.
            </p>
        </div>
    );
}

export default Login;
