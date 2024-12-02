import React, { useEffect, useState } from "react";
import {useAuth} from "./AuthContext";
import { useNavigate } from 'react-router-dom';

function Home() {
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [documents, setDocuments] = useState([]);
    const [requestedDocuments, setRequestedDocuments] = useState([]);
    const [ownedDocuments, setOwnedDocuments] = useState([]);
    const [message, setMessage] = useState("");
    const { authenticatedUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchDocuments = async () => {
            try {
                const response = await fetch("http://localhost:8080/documents");
                if (response.ok) {
                    const data = await response.json();
                    setDocuments(data); // Store list of documents
                } else {
                    console.error("Failed to fetch documents");
                }
            } catch (error) {
                console.error("Error fetching documents:", error);
            }
        };

        fetchDocuments();
    }, []);

    const handleRequestedChange = (event) => {
        const selectedOptions = Array.from(event.target.selectedOptions, (option) => {
            const doc = JSON.parse(option.value);
            return { id: doc.id, name: doc.name };
        });
        setRequestedDocuments(selectedOptions);
    };

    const handleOwnedChange = (event) => {
        const selectedOptions = Array.from(event.target.selectedOptions, (option) => {
            const doc = JSON.parse(option.value);
            return { id: doc.id, name: doc.name };
        });
        setOwnedDocuments(selectedOptions);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const randomId = `${Date.now()}-${Math.floor(Math.random() * 1000)}`;

        const newClient = {
            id: randomId,
            name: name,
            username: username,
            requestedDocuments: requestedDocuments.map((doc) => doc.id),
            ownedDocuments: ownedDocuments.map((doc) => doc.id),
        };

        try {
            const response = await fetch("http://localhost:8080/clients", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newClient),
            });

            if (response.ok) {
                const data = await response.json();
                setMessage(`Client created successfully: ${data.username}`);
                navigate('/request');
            } else {
                const errorData = await response.json();
                setMessage(`Failed to create client: ${errorData.message || "Unknown error"}`);
            }
        } catch (error) {
            console.error("Error creating client:", error);
            setMessage("An error occurred while creating the client.");
        }
    };

    if (!authenticatedUser) {
        return (
            <div>
                <h1>Welcome to the Home Page of BiroHelp!</h1>
                <p>You need to be logged in to access the form.</p>
                <button onClick={() => navigate('/login')}>Go to Login</button>
            </div>
        );
    }

    return (
        <div>
            <h1>Welcome to the Home Page</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        Name:
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </label>
                </div>

                {/* Username field */}
                <div>
                    <label>
                        Username:
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </label>
                </div>

                {/* Dropdown for owned documents */}
                <div>
                    <label>
                        Owned Documents:
                        <select multiple onChange={handleOwnedChange}>
                            {documents.map((doc) => (
                                <option key={doc.id} value={JSON.stringify(doc)}>
                                    {doc.name}
                                </option>
                            ))}
                        </select>
                    </label>
                </div>
                <div>
                    <label>
                        Requested Documents:
                        <select multiple onChange={handleRequestedChange}>
                            {documents.map((doc) => (
                                <option key={doc.id} value={JSON.stringify(doc)}>
                                    {doc.name}
                                </option>
                            ))}
                        </select>
                    </label>
                </div>

                {/* Submit button */}
                <div>
                    <button type="submit">Create Client</button>
                </div>
            </form>

            <div>
                <h3>Selected Documents:</h3>
                <p><strong>Requested:</strong> {requestedDocuments.map((doc) => doc.name).join(", ")}</p>
                <p><strong>Owned:</strong> {ownedDocuments.map((doc) => doc.name).join(", ")}</p>
            </div>

            {message && <p>{message}</p>}
        </div>
    );
}

export default Home;
