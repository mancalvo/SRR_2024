import React, {  } from "react";
import CartaDia from "./CartaDia";
import "../App.css";

const Seccion2 = ({ formData, setFormData, volverSeccion, agregarDia, enviarReserva }) => {
  const dias = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO"];

  
  console.log("DESDE PERIODICA ",formData);

   
  
  const manejarGuardarReserva = () => {
    enviarReserva();
  };



  return (
    <div className="form-container encuadro mb-0 mt-2 " style={{ width: "auto", overflowX: "auto" }}>
      <div id="seccion2">
        <h5 className="text-center">RESERVA PERIODICA</h5>

    
        <div
          className="d-flex justify-content-center align-items-center"
          style={{
            gap: "7px",
            padding: "5px",
            maxWidth: "1900px", 
            flexWrap: "nowrap",
          }}
        >
          {dias.map((dia, index) => (
            <div className="d-flex justify-content-center" key={index}>
              <CartaDia 
              formData={formData}
              dia={dia} 
              id={index + 1} 
              agregarDia={agregarDia} />
            </div>
          ))}
        </div>

        <div className="mt-3 d-flex justify-content-center">
          <div className="col-8 d-flex justify-content-between">
            <button
              type="button"
              className="btn btn-secondary w-50 me-2"
              onClick={volverSeccion}
              aria-label="Volver a la sección anterior"
            >
              Volver
            </button>
            <button
              type="button"
              className="btn btn-warning w-50"
              aria-label="Guardar reserva"
              onClick={manejarGuardarReserva}
            >
              Guardar Reserva
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Seccion2;
