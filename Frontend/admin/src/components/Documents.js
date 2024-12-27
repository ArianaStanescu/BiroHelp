import React, { useState, useEffect } from "react";
import "./Documents.css";

const Documents = () => {
  const [offices, setOffices] = useState([]);
  const [allDocuments, setAllDocuments] = useState([]);
  const [documentsByOffice, setDocumentsByOffice] = useState([]);
  const [newDocumentName, setNewDocumentName] = useState("");
  const [selectedOfficeId, setSelectedOfficeId] = useState("");
  const [selectedDependencies, setSelectedDependencies] = useState({});


  const fetchAllDocuments = async () => {
    try {
      const res = await fetch("http://localhost:8080/documents");
      const allDocs = await res.json();
      setAllDocuments(allDocs);

      const grouped = offices.map((office) => ({
        office,
        documents: allDocs.filter(
            (doc) => doc.issuingOffice && doc.issuingOffice.id === office.id
        ),
      }));
      setDocumentsByOffice(grouped);
    } catch (error) {
      console.error("Error fetching documents:", error);
    }
  };


  useEffect(() => {
    fetchOffices();
  }, []);


  useEffect(() => {
    if (offices.length > 0) {
      fetchAllDocuments();
    }
  }, [offices]);


  const fetchOffices = async () => {
    try {
      const res = await fetch("http://localhost:8080/offices");
      if (!res.ok) {
        throw new Error('Failed to fetch offices');
      }
      const data = await res.json();
      setOffices(data);
    } catch (error) {
      console.error("Error fetching offices:", error);
    }
  };


  const addDocument = async () => {
    if (!newDocumentName || !selectedOfficeId) {
      alert("Document name and office are required!");
      return;
    }

    try {
      await fetch(`http://localhost:8080/documents/${selectedOfficeId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: newDocumentName }),
      });

      setNewDocumentName("");
      setSelectedOfficeId("");
      fetchAllDocuments();
    } catch (error) {
      console.error("Error adding document:", error);
      alert("Failed to add document.");
    }
  };

  const deleteDocument = async (documentId) => {
    try {
      const response = await fetch(`http://localhost:8080/documents/${documentId}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
      });

      if (!response.ok) {
        throw new Error(`Failed to delete document (ID: ${documentId})`);
      }

      fetchAllDocuments();
    } catch (error) {
      console.error("Error deleting document:", error);
      alert(`Failed to delete document. ${error.message}`);
    }
  };


  const addDocumentDependency = async (documentId) => {
    const dependencyId = selectedDependencies[documentId];
    if (!dependencyId) {
      alert("Please select a dependency document!");
      return;
    }

    await fetch(`http://localhost:8080/documents/dependencies/${documentId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify([parseInt(dependencyId)]),
    });

    setSelectedDependencies({ ...selectedDependencies, [documentId]: "" });
    fetchAllDocuments();
  };


  const deleteDocumentDependency = async (documentId, dependencyId) => {
    await fetch(`http://localhost:8080/documents/dependencies/${documentId}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify([dependencyId]),
    });
    fetchAllDocuments();
  };

  return (
      <div className="documents-container">
        <h1>Edit Documents</h1>

        <div className="add-document-card">
          <h2>Add New Document</h2>
          <input
              type="text"
              placeholder="Document Name"
              value={newDocumentName}
              onChange={(e) => setNewDocumentName(e.target.value)}
          />
          <select
              value={selectedOfficeId}
              onChange={(e) => setSelectedOfficeId(e.target.value)}
          >
            <option value="">Select Office</option>
            {offices.map((office) => (
                <option key={office.id} value={office.id}>
                  {office.name}
                </option>
            ))}
          </select>
          <button className="add-btn" onClick={addDocument}>Add Document</button>
        </div>

        <div className="documents-list">
          <h2>Documents by Office</h2>
          {documentsByOffice.map((group) => (
              <div key={group.office.id} className="office-card">
                <h3>{group.office.name} </h3>
                <ul>
                  {group.documents.map((doc) => (
                      <li key={doc.id} className="document-card">
                        <strong>{doc.name} </strong>
                        <button className="delete-btn" onClick={() => deleteDocument(doc.id)}>Delete</button>

                        <div className="dependency-section">
                          <label>Add Dependency: </label>
                          <select
                              value={selectedDependencies[doc.id] || ""}
                              onChange={(e) =>
                                  setSelectedDependencies({
                                    ...selectedDependencies,
                                    [doc.id]: e.target.value,
                                  })
                              }
                          >
                            <option value="">Select Document</option>
                            {allDocuments
                                .filter((d) => d.id !== doc.id)
                                .map((d) => (
                                    <option key={d.id} value={d.id}>
                                      {d.name}
                                    </option>
                                ))}
                          </select>
                          <button
                              className="add-dep-btn"
                              onClick={() => addDocumentDependency(doc.id)}
                          >
                            Add Dependency
                          </button>
                        </div>

                        {doc.necessaryDocuments && doc.necessaryDocuments.length > 0 && (
                            <div className="dependencies-list">
                              <h4>Dependencies:</h4>
                              <ul>
                                {doc.necessaryDocuments.map((dependency) => (
                                    <li key={dependency.id}>
                                      {dependency.name} {" "}
                                      <button
                                          className="remove-dep-btn"
                                          onClick={() =>
                                              deleteDocumentDependency(doc.id, dependency.id)
                                          }
                                      >
                                        Remove Dependency
                                      </button>
                                    </li>
                                ))}
                              </ul>
                            </div>
                        )}
                      </li>
                  ))}
                </ul>
              </div>
          ))}
        </div>
      </div>
  );
};

export default Documents;
