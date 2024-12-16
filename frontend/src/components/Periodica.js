import React from "react";
import CartaDia from "./CartaDia";
import "../App.css";

const Seccion2 = ({ volverSeccion, guardarReserva }) => {
  const dias = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];

  return (
    <div className="form-container encuadro mb-0 mt-2 " style={{ width: "auto", overflowX: "auto" }}>
      <div id="seccion2">
        <h5 className="text-center">RESERVA PERIODICA</h5>

        {/* Contenedor de las cartas */}
        <div
          className="d-flex justify-content-center align-items-center"
          style={{
            gap: "7px",
            padding: "5px",
            maxWidth: "1900px", // Ajustar el máximo ancho
            flexWrap: "nowrap",
          }}
        >
          {dias.map((dia, index) => (
            <div className="d-flex justify-content-center" key={index}>
              <CartaDia dia={dia} id={index + 1} />
            </div>
          ))}
        </div>

        {/* Contenedor de los botones */}
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
              onClick={guardarReserva}
              aria-label="Guardar reserva"
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
