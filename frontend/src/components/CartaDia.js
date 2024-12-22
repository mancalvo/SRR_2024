import React, { useState, useEffect } from "react";
import "../App.css";
import DisponibilidadAulaEsporadica from "./DisponibilidadAulaEsporadica";

const CartaDia = ({ formData,dia, id , agregarDia }) => {
  const [horaInicial, setHoraInicial] = useState("--:--");
  const [horaFinal, setHoraFinal] = useState("--:--");
  const [opcionesHoraInicial, setOpcionesHoraInicial] = useState([]);
  const [opcionesHoraFinal, setOpcionesHoraFinal] = useState([]);
  const [isChecked, setIsChecked] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [aulaSeleccionada, setAulaSeleccionada] = useState(null);

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);

  const actualizarAulaSeleccionada = (aula) => {
    setAulaSeleccionada(aula);
    agregarDia(dia, horaInicial, horaFinal, aula);
  };

  

  useEffect(() => {
    generarOpcionesIniciales();
  }, []);

  const generarOpcionesIniciales = () => {
    const opciones = [];
    const horaInicio = new Date();
    horaInicio.setHours(8, 0, 0, 0); // 08:00
    const horaFin = new Date();
    horaFin.setHours(22, 0, 0, 0); // 22:00
    const horaActual = new Date(horaInicio);

    opciones.push({ text: "--:--", value: "0" });

    while (horaActual <= horaFin) {
      const hora = horaActual.getHours().toString().padStart(2, "0");
      const minuto = horaActual.getMinutes().toString().padStart(2, "0");
      opciones.push({ text: `${hora}:${minuto}`, value: `${hora}:${minuto}` });
      horaActual.setMinutes(horaActual.getMinutes() + 5);
    }
    setOpcionesHoraInicial(opciones);
  };

  const generarOpcionesFinales = (horaSeleccionada) => {
    const opciones = [];
    const [horaSeleccionadaHora, horaSeleccionadaMinuto] = horaSeleccionada
      .split(":")
      .map(Number);
    const horaActual = new Date();
    horaActual.setHours(horaSeleccionadaHora, horaSeleccionadaMinuto, 0, 0);
    const horaFin = new Date();
    horaFin.setHours(22, 0, 0, 0);

    opciones.push({ text: "--:--", value: "0" });

    horaActual.setMinutes(horaActual.getMinutes() + 30);

    while (horaActual <= horaFin) {
      const hora = horaActual.getHours().toString().padStart(2, "0");
      const minuto = horaActual.getMinutes().toString().padStart(2, "0");
      opciones.push({ text: `${hora}:${minuto}`, value: `${hora}:${minuto}` });
      horaActual.setMinutes(horaActual.getMinutes() + 30);
    }
    setOpcionesHoraFinal(opciones);
  };

  const handleHoraInicialChange = (e) => {
    const seleccion = e.target.value;
    setHoraInicial(seleccion);
    generarOpcionesFinales(seleccion);
  };

  const handleHoraFinalChange = (e) => {
    const seleccion = e.target.value;
    setHoraFinal(seleccion);
  };

  const handleCheckboxChange = (e) => {
    setIsChecked(e.target.checked);

    // Limpia los valores si se desactiva
    if (!e.target.checked) {
      setHoraInicial("--:--");
      setHoraFinal("--:--");
      setOpcionesHoraFinal([]);
      setAulaSeleccionada(null);
    }
  };

  const isButtonDisabled = !isChecked || horaInicial === "--:--" || horaFinal === "--:--";
  return (
    <div className="col-auto mt-3">
      <div className="card text-center" id={`carta${id}`}>
        <div className="card-header custom-header ">
          <div className="form-check custom-form-check ">
            <input
              className="form-check-input"
              type="checkbox"
              id={`habilitar${id}`}
              checked={isChecked}
              onChange={handleCheckboxChange}
            />
            <label className="form-check-label" htmlFor={`habilitar${id}`}>
              {dia.toUpperCase()}
            </label>
          </div>
        </div>
        <div
          className={`card-body ${!isChecked ? "disabled" : ""}`}
          id={`cardBody${id}`}
        >
          <div className="d-flex align-items-center justify-content-center">
            <div>
              <div className="row align-items-center justify-content-center">
                <div className="col-auto d-flex align-items-center justify-content-center">
                  <label
                    htmlFor={`horaInicial${id}`}
                    className="form-label me-2"
                  >
                    Hora Inicial
                  </label>
                  <div className="custom-select-container">
                    <select
                      id={`horaInicial${id}`}
                      className="form-select"
                      onChange={handleHoraInicialChange}
                      value={horaInicial}
                      disabled={!isChecked}
                    >
                      {opcionesHoraInicial.map((opcion) => (
                        <option key={opcion.value} value={opcion.value}>
                          {opcion.text}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>

              <div className="row align-items-center">
                <div className="col-auto d-flex align-items-center">
                  <label htmlFor={`horaFinal${id}`} className="form-label">
                    Hora Final
                  </label>
                  <div className="custom-select-container">
                    <select
                      id={`horaFinal${id}`}
                      className="form-select"
                      onChange={handleHoraFinalChange}
                      value={horaFinal}
                      disabled={!isChecked || horaInicial === "--:--"}
                    >
                      {opcionesHoraFinal.map((opcion) => (
                        <option key={opcion.value} value={opcion.value}>
                          {opcion.text}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <button
            className="btn btn-outline-secondary btn-sm mt-1"
            onClick={handleOpenModal}
            disabled={isButtonDisabled}
          >
            Seleccionar Aula
          </button>
        </div>
        <div
          className="card-footer text-body-secondary aula-asignada text-center align-items-center justify-content-center mt-auto"
          id={`aula${id}`}
        >
          {aulaSeleccionada ? (
            <p className="mb-0">Aula {aulaSeleccionada.numero}</p>

          ) : (
            <p className="mb-0"> </p>
          )}
        </div>
        
        <DisponibilidadAulaEsporadica
          formData={formData}
          dia={dia}
          isOpen={isModalOpen}
          onClose={handleCloseModal}
          actualizarAulaSeleccionada={actualizarAulaSeleccionada}
          horaInicio={horaInicial}
          horaFinal={horaFinal}
        />
      </div>
    </div>
  );
};

export default CartaDia;
