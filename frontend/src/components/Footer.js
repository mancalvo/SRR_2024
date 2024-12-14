// src/components/Footer.js
import React from 'react';
import "../App.css";

function Footer({ customClass }) {
  return (
    <footer id="main-footer" className={customClass}>
      <div className="footer">
        Trabajo Practico Diseño de Sistemas 2024 - UTN Santa Fe
      </div>
    </footer>
  );
}

export default Footer;
