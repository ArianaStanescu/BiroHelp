import React, { useState, useEffect } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./style.css";

function RequestDocuments() {
    const [documents, setDocuments] = useState([]);
    const [requestedDocuments, setRequestedDocuments] = useState([]);
    const [ownedDocuments, setOwnedDocuments] = useState([]);
    const [selectedDocumentIds, setSelectedDocumentIds] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const { authenticatedUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchDocuments = async () => {
            try {
                const response = await fetch("http://localhost:8080/documents");
                if (response.ok) {
                    const data = await response.json();
                    setDocuments(data);
                } else {
                    console.error("Failed to fetch documents");
                }
            } catch (error) {
                console.error("Error fetching documents:", error);
            }
        };

        fetchDocuments();
    }, []);

    useEffect(() => {
        if (authenticatedUser) {
            const fetchUserDocuments = async () => {
                try {
                    const response = await fetch(
                        `http://localhost:8080/clients/${authenticatedUser.id}`
                    );
                    if (response.ok) {
                        const userData = await response.json();
                        setOwnedDocuments(userData.ownedDocuments || []);
                        setRequestedDocuments(userData.requestedDocuments || []);
                    } else {
                        console.error("Failed to fetch user's documents");
                    }
                } catch (error) {
                    console.error("Error fetching user's documents:", error);
                }
            };
            fetchUserDocuments();
        } else {
            navigate("/login");
        }
    }, [authenticatedUser, navigate]);

    const handleAddSelectedDocuments = async () => {
        const newRequestedDocumentIds = selectedDocumentIds.filter(
            (id) =>
                !requestedDocuments.some((doc) => doc.id === id) &&
                !ownedDocuments.some((doc) => doc.id === id)
        );

        if (newRequestedDocumentIds.length === 0) {
            alert("No new valid documents selected.");
            return;
        }

        try {
            setIsLoading(true);
            const response = await fetch(
                `http://localhost:8080/clients/${authenticatedUser.id}/documents`,
                {
                    method: "PATCH",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        requestedDocumentIds: newRequestedDocumentIds,
                    }),
                }
            );

            if (response.ok) {
                const updatedUser = await response.json();
                setRequestedDocuments(updatedUser.requestedDocuments || []);
                setOwnedDocuments(updatedUser.ownedDocuments || []);
                setSelectedDocumentIds([]);
                alert("Selected documents successfully added to your requests.");
            } else {
                console.error("Failed to update requested documents.");
                alert("An error occurred while updating your document requests.");
            }
        } catch (error) {
            console.error("Error updating requested documents:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleRequestDocuments = async () => {
        setIsLoading(true);
        try {
            const response = await fetch(
                `http://localhost:8080/clients/${authenticatedUser.id}/request-documents`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            if (response.ok) {
                alert("Successfully submitted your request for documents.");
                navigate("/finish");
            } else {
                alert("Error submitting your document request.");
            }
        } catch (error) {
            console.error("Error while requesting documents:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleDocumentChange = (e) => {
        const documentId = parseInt(e.target.value, 10);
        setSelectedDocumentIds((prev) =>
            prev.includes(documentId)
                ? prev.filter((id) => id !== documentId)
                : [...prev, documentId]
        );
    };

    return (
        <div className="auth-container">
            <h1>Request Documents</h1>

            <div>
                <label>
                    Select Documents to Add to Your Requests:
                    <div className="checkbox-group">
                        {documents.map((doc) => (
                            <div key={doc.id} className="checkbox-item">
                                <label>
                                    <input
                                        type="checkbox"
                                        value={doc.id}
                                        onChange={handleDocumentChange}
                                    />
                                    {doc.name} (Issued by: {doc.issuingOffice.name})
                                </label>
                            </div>
                        ))}
                    </div>
                </label>
            </div>

            <button onClick={handleAddSelectedDocuments} disabled={isLoading}>
                Add Selected Documents
            </button>

            <button onClick={handleRequestDocuments} disabled={isLoading}>
                {isLoading ? "Processing..." : "Submit Request"}
            </button>

            {isLoading && (
                <div className="progress-bar">
                    <div className="progress-bar-fill" />
                </div>
            )}
        </div>
    );
}

export default RequestDocuments;
