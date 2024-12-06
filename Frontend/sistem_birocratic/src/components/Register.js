import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

function Register() {
    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [ownedDocuments, setOwnedDocuments] = useState([]);
    const [availableDocuments, setAvailableDocuments] = useState([]);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchDocuments = async () => {
            try {
                const response = await fetch('http://localhost:8080/documents');
                if (response.ok) {
                    const data = await response.json();
                    setAvailableDocuments(data);
                } else {
                    console.error('Failed to fetch available documents.');
                }
            } catch (error) {
                console.error('Error fetching available documents:', error);
            }
        };

        fetchDocuments();
    }, []);

    const handleRegister = async (e) => {
        e.preventDefault();

        const clientData = {
            name,
            username,
            requestedDocumentIds: [],
            ownedDocumentsIds: ownedDocuments,
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
                setTimeout(() => navigate('/login'), 2000);
            } else {
                setMessage('Registration failed. Please try again.');
            }
        } catch (error) {
            setMessage('An error occurred. Please try again later.');
        }
    };

    const handleOwnedDocumentsChange = (e) => {
        const selectedOptions = Array.from(e.target.selectedOptions);
        const selectedIds = selectedOptions.map((option) => option.value);
        setOwnedDocuments(selectedIds);
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
                <label>
                    Select Owned Documents:
                    <select
                        multiple
                        value={ownedDocuments}
                        onChange={handleOwnedDocumentsChange}
                    >
                        {availableDocuments.map((doc) => (
                            <option key={doc.id} value={doc.id}>
                                {doc.name} (Issued by: {doc.issuingOffice.name})
                            </option>
                        ))}
                    </select>
                </label>
                <br />
                <button type="submit">Register</button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
}

export default Register;
