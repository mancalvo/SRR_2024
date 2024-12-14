import React, { useState } from "react";
import Seccion1 from "./Seccion1";
import Seccion2 from "./Seccion2";
import "../App.css";

function NuevaReservaMain() {
  const [currentSection, setCurrentSection] = useState(1);

  // Estado global para todas las secciones
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
    setCurrentSection((prev) => prev + 1);
  };

  const volverSeccion = () => {
    setCurrentSection((prev) => prev - 1);
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

      <div class="row">
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

          {currentSection === 2 && (
            <Seccion2
              formData={formData}
              setFormData={setFormData}
              volverSeccion={volverSeccion}
              guardarReserva={guardarReserva}
            />
          )}
        </form>
      </div>
    </div>
  );
}

export default NuevaReservaMain;
