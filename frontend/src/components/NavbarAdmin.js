// src/components/Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';  // Importa Link desde react-router-dom

function Navbar() {
  return (
    <nav className="navbar navbar-expand-lg bg-dark border-bottom border-body" data-bs-theme="dark">
      <div className="container">
        {/* Aquí cambiamos el <a> por un <Link> para redirigir al inicio */}
        <Link className="navbar-brand" to="/">Navbar</Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="container">
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav">
              <li className="nav-item">
                <Link className="nav-link" to="/nueva-reserva">Nueva Reserva</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/aulas">Aulas Disponibles</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/bedel">Bedel</Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
