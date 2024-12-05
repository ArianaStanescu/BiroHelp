import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import Home from './components/Home';
import Login from './components/Login';
import Register from "./components/Register";
import RequestDocuments from "./components/RequestDocuments";
import Finish from "./components/Finish";
import Settings from "./components/Settings";

function App() {
  return (
      <Router>
        <div>
          <NavBar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/request-documents" element={<RequestDocuments />} />
              <Route path="/finish" element={<Finish />} />
              <Route path="/settings" element={<Settings />} />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
