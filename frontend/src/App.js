import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import AdminPage from './pages/AdminPage';
import BedelPage from './pages/BedelPage';
import './App.css'; 

// Ruta protegida según tipo de usuario
function PrivateRoute({ children, allowedRole }) {
  const tipoUsuario = localStorage.getItem('tipoUsuario');
  if (!tipoUsuario) {
    return <Navigate to="/login" />;
  }
  if (tipoUsuario !== allowedRole) {
    return <Navigate to="/unauthorized" />;
  }
  return children;
}

function App() {
  return (
    <Router>
      <Routes>
        {/* Ruta predeterminada que redirige a login */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* Login Público */}
        <Route path="/login" element={<Login />} />

        {/* Ruta para Administradores */}
        <Route
          path="/admin"
          element={
            <PrivateRoute allowedRole="ADMINISTRADOR">
              <AdminPage />
            </PrivateRoute>
          }
        />

        {/* Ruta para Bedeles */}
        <Route
          path="/bedel"
          element={
            <PrivateRoute allowedRole="BEDEL">
              <BedelPage />
            </PrivateRoute>
          }
        />

        {/* Página no autorizada */}
        <Route path="/unauthorized" element={<div>No tienes permiso para acceder a esta página.</div>} />
      </Routes>
    </Router>
  );
}


export default App;
