import React, { useState, useEffect } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./style.css";

function RequestDocuments() {
    const [documents, setDocuments] = useState([]); // All available documents
    const [requestedDocuments, setRequestedDocuments] = useState([]); // User's requested documents
    const [isLoading, setIsLoading] = useState(false); // Loading state for the progress bar
    const { authenticatedUser } = useAuth();
    const navigate = useNavigate();

    // Fetch available documents
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

    // Fetch user's requested documents
    useEffect(() => {
        const fetchUserRequestedDocuments = async () => {
            try {
                const response = await fetch(
                    `http://localhost:8080/clients/${authenticatedUser.id}`
                );
                if (response.ok) {
                    const userData = await response.json();
                    setRequestedDocuments(userData.requestedDocuments || []); // Populate requested documents
                } else {
                    console.error("Failed to fetch user's requested documents");
                }
            } catch (error) {
                console.error("Error fetching user's requested documents:", error);
            }
        };

        if (authenticatedUser) {
            fetchUserRequestedDocuments();
        }
    }, [authenticatedUser]);

    const handleRequestedChange = async (event) => {
        try {
            const selectedValue = event.target.value; // Get selected value
            if (!selectedValue) {
                alert("Please select a valid document.");
                return;
            }

            const selectedDocument = JSON.parse(selectedValue); // Parse selected document
            if (!selectedDocument || !selectedDocument.id) {
                console.error("Invalid document selected.");
                alert("An error occurred. Please try selecting a valid document.");
                return;
            }

            // Fetch the current user data to retrieve ownedDocumentsIds
            const userResponse = await fetch(`http://localhost:8080/clients/${authenticatedUser.id}`);
            if (!userResponse.ok) {
                alert("Failed to fetch user data.");
                return;
            }

            const userData = await userResponse.json();
            const ownedDocumentsIds = userData.ownedDocumentsIds || []; // Keep owned documents unchanged
            const updatedRequestedDocumentIds = [
                ...(userData.requestedDocumentIds || []), // Existing requested documents
                selectedDocument.id, // Add the newly selected document ID
            ];

            // Call the PATCH endpoint to update requestedDocumentIds
            const patchResponse = await fetch(`http://localhost:8080/clients/${authenticatedUser.id}/documents`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    requestedDocumentIds: updatedRequestedDocumentIds,
                    ownedDocumentsIds: ownedDocumentsIds, // Keep the owned documents unchanged
                }),
            });

            if (patchResponse.ok) {
                const updatedUser = await patchResponse.json();
                setRequestedDocuments(updatedUser.requestedDocuments || []); // Update state
                alert(
                    `Document "${selectedDocument.name}" successfully added to your requests.`
                );
            } else {
                alert("Failed to update requested documents. Please try again.");
                console.error("Failed to PATCH requested documents.");
            }
        } catch (error) {
            console.error("Error updating requested documents:", error);
            alert("An error occurred while updating your requests.");
        }
    };

    const handleRequestDocuments = async () => {
        setIsLoading(true); // Start loading
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
                const data = await response.json();
                alert(`Successfully submitted your request for documents.`);
                navigate("/finish"); // Redirect to the finish page
            } else {
                console.error("Failed to submit document request");
                alert("Error submitting your document request. Please try again.");
            }
        } catch (error) {
            console.error("Error while requesting documents:", error);
            alert("An error occurred. Please try again.");
        } finally {
            setIsLoading(false); // Stop loading
        }
    };

    return (
        <div className="auth-container">
            <h1>Request Documents</h1>

            <div>
                <label>
                    Select a Document to Add to Your Requests:
                    <select onChange={handleRequestedChange}>
                        <option value="" disabled selected>
                            Select a document
                        </option>
                        {documents.map((doc) => (
                            <option key={doc.id} value={JSON.stringify(doc)}>
                                {doc.name} (Issued by: {doc.issuingOffice.name})
                            </option>
                        ))}
                    </select>
                </label>
            </div>

            <button onClick={handleRequestDocuments} disabled={isLoading}>
                {isLoading ? "Processing..." : "Make the Request"}
            </button>

            {/* Progress bar */}
            {isLoading && (
                <div className="progress-bar">
                    <div className="progress-bar-fill" />
                </div>
            )}
        </div>
    );
}

export default RequestDocuments;
