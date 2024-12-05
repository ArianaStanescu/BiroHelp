import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

function Register() {
    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();

        const clientData = {
            name,
            username,
            requestedDocumentIds: [], // Initialize as empty array
            ownedDocumentsIds: [], // Initialize as empty array
        };

        try {
            const response = await fetch('http://localhost:8080/clients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(clientData),
            });

            if (response.ok) {
                setMessage('Registration successful! You can now log in.');
                setTimeout(() => navigate('/login'), 2000); // Redirect to login after 2 seconds
            } else {
                setMessage('Registration failed. Please try again.');
            }
        } catch (error) {
            setMessage('An error occurred. Please try again later.');
        }
    };

    return (
        <div className="auth-container">
            <h1>Register</h1>
            <form onSubmit={handleRegister}>
                <label>
                    Name:
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </label>
                <br />
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
                <button type="submit">Register</button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
}

export default Register;
