import React from "react";
import CartaDia from "./CartaDia";
import "../App.css";

const Seccion2 = ({ formData, setFormData, volverSeccion, agregarDia, enviarReserva }) => {
  const dias = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO"];

  const manejarGuardarReserva = () => {
    // Validación robusta
    const detallesValidos = formData.detallesReserva.filter((detalle) => {
      const tieneDia = detalle.diaSemana && dias.includes(detalle.diaSemana.toUpperCase());
      const tieneHorario = detalle.horarioInicio && detalle.horarioFinal;
      const tieneAula = detalle.aulaId;
      const horarioValido =
        tieneHorario &&
        detalle.horarioInicio < detalle.horarioFinal; // Hora de inicio debe ser menor a la final

      return tieneDia && horarioValido && tieneAula;
    });

    if (detallesValidos.length === 0) {
      alert(
        "Debe seleccionar al menos un día con un horario válido y un aula configurada.\n" +
        "Formato válido: Día, Hora Inicio < Hora Fin, Aula seleccionada."
      );
      return;
    }

    // Si pasa la validación, se procede a enviar la reserva
    enviarReserva();
  };

  return (
    <div className="form-container encuadro mb-0 mt-2" style={{ width: "auto", overflowX: "auto" }}>
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
                agregarDia={agregarDia} 
              />
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
