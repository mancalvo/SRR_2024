import React, { useState } from "react";
import Seccion1 from "./Seccion1";
import Periodica from "./Periodica";
import Esporadica from "./Esporadica"; 
import "../App.css";

function NuevaReservaMain() {
  const [currentSection, setCurrentSection] = useState(1);

  const [formData, setFormData] = useState({
    solicitante: "",
    correo: "",
    catedra: "",
    cantAlumnos: "",
    tipoReserva: "", 
    tipoAula: "",
    dias: [],
  });

  const siguienteSeccion = () => {
    if (formData.tipoReserva === "4") {
      setCurrentSection(2); // Sección 2 para Esporádica
    } else {
      setCurrentSection(3); // Sección 3 para Periódica
    }
  };

  const volverSeccion = () => {
    setCurrentSection(1); // Vuelve siempre a la primera sección
  };

  const guardarReserva = () => {
    console.log("Reserva enviada:", formData);
    alert("Reserva simulada en consola");
  };

  return (
    <div className="container">
      <div className="row mt-4 justify-content-center">
        <div className="col-6 text-center">
          <h1>REGISTRO DE RESERVAS</h1>
        </div>
      </div>

      <div className="row">
        <form
          id="formulario"
          onSubmit={(e) => {
            e.preventDefault();
          }}
        >
          {currentSection === 1 && (
            <Seccion1
              formData={formData}
              setFormData={setFormData}
              siguienteSeccion={siguienteSeccion}
            />
          )}

          {currentSection === 3 && (
            <Periodica
              formData={formData}
              setFormData={setFormData}
              volverSeccion={volverSeccion}
              
            />
          )}

          {currentSection === 2 && (
            <Esporadica
              formData={formData}
              setFormData={setFormData}
              volverSeccion={volverSeccion}
              
            />
          )}
        </form>
      </div>
    </div>
  );
}

export default NuevaReservaMain;
