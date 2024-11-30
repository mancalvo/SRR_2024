import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./pages/Login";
import AdminPage from "./pages/AdminPage";
import BedelPage from "./pages/BedelPage";
import "./App.css";

function App() {
  // Componente para redirigir basado en tipo de usuario
  const RedirectByRole = () => {
    const tipoUsuario = localStorage.getItem("tipoUsuario"); // Obtiene el rol del usuario desde localStorage
    if (tipoUsuario === "ADMINISTRADOR") {
      return <Navigate to="/admin" />;
    } else {
      if (tipoUsuario === "BEDEL") {
        return <Navigate to="/bedel" />;
      } else {
        return <Navigate to="/login" />;
      }
    }
  };

  return (
    <Router>
      <Routes>
        {/* Ruta predeterminada: verifica tipo de usuario y redirige */}
        <Route path="/" element={<RedirectByRole />} />

        {/* Ruta para login */}
        <Route path="/login" element={<Login />} />

        {/* Ruta para Administradores */}
        <Route path="/admin" element={<AdminPage />} />

        {/* Ruta para Bedeles */}
        <Route path="/bedel" element={<BedelPage />} />
        
      </Routes>
    </Router>
  );
}

export default App;
