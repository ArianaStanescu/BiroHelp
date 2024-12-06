import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import Home from './components/Home';
import Login from './components/Login';
import Documents from './components/Documents';
import Offices from './components/Offices';
import Counters from './components/Counters';
import Clients from './components/Clients';

function App() {
  return (
      <Router>
        <div>
          <NavBar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
              <Route path="/documents" element={<Documents />} />
              <Route path="/offices" element={<Offices />} />
              <Route path="/counters" element={<Counters />} />
              <Route path="/clients" element={<Clients />} />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
