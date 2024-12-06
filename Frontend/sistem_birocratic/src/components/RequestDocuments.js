import React, { useState, useEffect } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./style.css";

function RequestDocuments() {
    const [documents, setDocuments] = useState([]);
    const [requestedDocuments, setRequestedDocuments] = useState([]);
    const [ownedDocuments, setOwnedDocuments] = useState([]);
    const [selectedDocuments, setSelectedDocuments] = useState([]);
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

        if (authenticatedUser) {
            fetchUserDocuments();
        }
    }, [authenticatedUser]);

    const handleAddSelectedDocuments = async () => {
        try {
            const newRequestedDocuments = selectedDocuments.filter(
                (doc) =>
                    !requestedDocuments.some((d) => d.id === doc.id) &&
                    !ownedDocuments.some((d) => d.id === doc.id)
            );

            if (newRequestedDocuments.length === 0) {
                alert("No new valid documents selected.");
                return;
            }

            const updatedRequestedDocuments = [...requestedDocuments, ...newRequestedDocuments];
            const updatedRequestedDocumentIds = updatedRequestedDocuments.map((doc) => doc.id);

            const updatedOwnedDocumentIds = ownedDocuments.map((doc) => doc.id);

            const patchResponse = await fetch(
                `http://localhost:8080/clients/${authenticatedUser.id}/documents`,
                {
                    method: "PATCH",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        requestedDocumentIds: updatedRequestedDocumentIds,
                        ownedDocumentsIds: updatedOwnedDocumentIds,
                    }),
                }
            );

            if (patchResponse.ok) {
                const updatedUser = await patchResponse.json();
                setRequestedDocuments(updatedUser.requestedDocuments || []);
                setOwnedDocuments(updatedUser.ownedDocuments || []);
                alert("Selected documents successfully added to your requests.");
                setSelectedDocuments([]);
            } else {
                console.error("Failed to update requested documents.");
                alert("An error occurred while updating your document requests. Please try again.");
            }
        } catch (error) {
            console.error("Error updating requested documents:", error);
            alert("An error occurred while processing your request. Please try again.");
        }
    };

    const handleSelectionChange = (event) => {
        const selectedOptions = Array.from(event.target.selectedOptions);
        const selectedDocs = selectedOptions.map((option) => JSON.parse(option.value));
        setSelectedDocuments(selectedDocs);
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
                console.error("Failed to submit document request");
                alert("Error submitting your document request. Please try again.");
            }
        } catch (error) {
            console.error("Error while requesting documents:", error);
            alert("An error occurred. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <h1>Request Documents</h1>

            <div>
                <label>
                    Select Documents to Add to Your Requests:
                    <select
                        multiple
                        value={selectedDocuments.map((doc) => JSON.stringify(doc))}
                        onChange={handleSelectionChange}
                    >
                        {documents.map((doc) => (
                            <option key={doc.id} value={JSON.stringify(doc)}>
                                {doc.name} (Issued by: {doc.issuingOffice.name})
                            </option>
                        ))}
                    </select>
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
