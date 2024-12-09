import React, { useState, useEffect } from "react";

const Documents = () => {
  const [offices, setOffices] = useState([]); // List of offices
  const [allDocuments, setAllDocuments] = useState([]); // List of all documents
  const [documentsByOffice, setDocumentsByOffice] = useState([]); // Documents grouped by office
  const [newDocumentName, setNewDocumentName] = useState("");
  const [selectedOfficeId, setSelectedOfficeId] = useState("");
  const [selectedDependencies, setSelectedDependencies] = useState({}); // Track selected dependency for each document

  useEffect(() => {
    fetchOffices();
  }, []);

  useEffect(() => {
    if (offices.length > 0) {
      fetchAllDocuments();
    }
  }, [offices]);

  const fetchOffices = async () => {
    const res = await fetch("http://localhost:8080/offices");
    const data = await res.json();
    setOffices(data);
  };

  const fetchAllDocuments = async () => {
    const res = await fetch("http://localhost:8080/documents");
    const allDocs = await res.json();
    setAllDocuments(allDocs);

    // Group documents by issuing office
    const grouped = offices.map((office) => ({
      office,
      documents: allDocs.filter(
        (doc) => doc.issuingOffice && doc.issuingOffice.id === office.id
      ),
    }));
    setDocumentsByOffice(grouped);
  };

  const addDocument = async () => {
    if (!newDocumentName || !selectedOfficeId) {
      alert("Document name and office are required!");
      return;
    }

    await fetch(`http://localhost:8080/documents/${selectedOfficeId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newDocumentName }),
    });

    setNewDocumentName("");
    setSelectedOfficeId("");
    fetchAllDocuments();
  };

  const deleteDocument = async (documentId) => {
    await fetch(`http://localhost:8080/documents/${documentId}`, {
      method: "DELETE",
    });
    fetchAllDocuments();
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
    <div>
      <h1>Edit Documents</h1>

      {/* Add New Document */}
      <div>
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
              {office.name} (ID: {office.id})
            </option>
          ))}
        </select>
        <button onClick={addDocument}>Add Document</button>
      </div>

      {/* List Documents by Office */}
      <div>
        <h2>Documents by Office</h2>
        {documentsByOffice.map((group) => (
          <div key={group.office.id}>
            <h3>
              {group.office.name} (ID: {group.office.id})
            </h3>
            <ul>
              {group.documents.map((doc) => (
                <li key={doc.id}>
                  <strong>{doc.name} (ID: {doc.id})</strong>{" "}
                  <button onClick={() => deleteDocument(doc.id)}>Delete</button>

                  {/* Add Dependency */}
                  <div>
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
                        .filter((d) => d.id !== doc.id) // Exclude current document
                        .map((d) => (
                          <option key={d.id} value={d.id}>
                            {d.name} (ID: {d.id})
                          </option>
                        ))}
                    </select>
                    <button onClick={() => addDocumentDependency(doc.id)}>
                      Add Dependency
                    </button>
                  </div>

                  {/* Display Dependencies */}
                  {doc.necessaryDocuments &&
                    doc.necessaryDocuments.length > 0 && (
                      <div>
                        <h4>Dependencies:</h4>
                        <ul>
                          {doc.necessaryDocuments.map((dependency) => (
                            <li key={dependency.id}>
                              {dependency.name} (ID: {dependency.id}){" "}
                              <button
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
