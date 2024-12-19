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
import CrudBedels from "./pages/CrudBedels";
import NuevaReservaPage from "./pages/NuevaReservaPage";
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
       
        <Route path="/" element={<RedirectByRole />} />

        <Route path="/login" element={<Login />} />

        <Route path="/admin" element={<AdminPage />} />

        <Route path="/bedel" element={<BedelPage />} />

        <Route path="/adminBedels" element={<CrudBedels />} />

        <Route path="/nueva-reserva" element={<NuevaReservaPage />} />

      </Routes>
    </Router>
  );
}

export default App;
