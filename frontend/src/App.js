// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './index.css';
import Navbar from './components/Navbar';
import Home from './components/Home';  // Componente de inicio
import NuevaReserva from './pages/NuevaReserva';  // Componente NuevaReserva
import AulasDisponibles from './pages/AulasDisponibles';
import Bedel from './pages/Bedel';
import Footer from './components/Footer';  // Componente Footer

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/nueva-reserva" element={<NuevaReserva />} />
        <Route path="/aulas" element={<AulasDisponibles />} />
        <Route path="/bedel" element={<Bedel />} />
      </Routes>
      <Footer /> 
    </Router>
  );
}

export default App;
