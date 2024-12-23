import React, { useState, useEffect } from "react";
import DisponibilidadAulaEsporadica from "./DisponibilidadAulaEsporadica";

function ReservaModal({ formData, reserva, onGuardar, onCerrar }) {
  const [fecha, setFecha] = useState(reserva?.fecha || "");
  const [horaInicial, setHoraInicial] = useState("--:--");
  const [horaFinal, setHoraFinal] = useState("--:--");
  const [opcionesHoraInicial, setOpcionesHoraInicial] = useState([]);
  const [opcionesHoraFinal, setOpcionesHoraFinal] = useState([]);
  const [aulaSeleccionada, setAulaSeleccionada] = useState(null);
  const [modalAulaAbierto, setModalAulaAbierto] = useState(false);
  const [errorFecha, setErrorFecha] = useState(""); // Estado para el mensaje de error

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

    opciones.push({ text: "--:--", value: "--:--" });

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

    opciones.push({ text: "--:--", value: "--:--" });

    horaActual.setMinutes(horaActual.getMinutes() + 30);

    while (horaActual <= horaFin) {
      const hora = horaActual.getHours().toString().padStart(2, "0");
      const minuto = horaActual.getMinutes().toString().padStart(2, "0");
      opciones.push({ text: `${hora}:${minuto}`, value: `${hora}:${minuto}` });
      horaActual.setMinutes(horaActual.getMinutes() + 30);
    }
    setOpcionesHoraFinal(opciones);
  };

  const handleFechaChange = (e) => {
    const nuevaFecha = e.target.value;
    setFecha(nuevaFecha);

    const diaSemana = new Date(nuevaFecha).getDay();
    if (diaSemana === 6) {
      setErrorFecha("No se permiten reservas los domingos.");
    } else {
      setErrorFecha("");
    }
  };

  const handleHoraInicialChange = (e) => {
    const seleccion = e.target.value;
    setHoraInicial(seleccion);
    generarOpcionesFinales(seleccion);
  };

  const handleHoraFinalChange = (e) => {
    setHoraFinal(e.target.value);
  };

  const handleSeleccionarAula = () => {
    setModalAulaAbierto(true);
  };

  const manejarGuardar = () => {
    if (!fecha || horaInicial === "--:--" || horaFinal === "--:--") {
      alert("Todos los campos son obligatorios");
      return;
    }
    onGuardar({
      id: reserva?.id || Date.now(),
      fecha,
      horaInicial,
      horaFinal,
      aulaSeleccionada,
    });
  };

  const actualizarAulaSeleccionada = (aula) => {
    setAulaSeleccionada(aula);
    setModalAulaAbierto(false);
  };

  return (
    <div
      className="modal show d-block"
      style={{ backgroundColor: "rgba(0,0,0,0.5)" }}
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {reserva ? "Editar Reserva" : "Nueva Reserva Esporadica"}
            </h5>
            <button
              type="button"
              className="btn-close"
              onClick={onCerrar}
            ></button>
          </div>
          <div className="modal-body">
            <div className="mb-3">
              <label className="form-label">Fecha</label>
              <input
                type="date"
                className="form-control"
                value={fecha}
                onChange={handleFechaChange}
              />
              {errorFecha && (
                <div className="text-danger mt-2">{errorFecha}</div>
              )}
            </div>
            <div className="mb-3">
              <label className="form-label">Hora Inicial</label>
              <select
                className="form-select"
                value={horaInicial}
                onChange={handleHoraInicialChange}
              >
                {opcionesHoraInicial.map((opcion) => (
                  <option key={opcion.value} value={opcion.value}>
                    {opcion.text}
                  </option>
                ))}
              </select>
            </div>
            <div className="mb-3">
              <label className="form-label">Hora Final</label>
              <select
                className="form-select"
                value={horaFinal}
                onChange={handleHoraFinalChange}
                disabled={horaInicial === "--:--"}
              >
                {opcionesHoraFinal.map((opcion) => (
                  <option key={opcion.value} value={opcion.value}>
                    {opcion.text}
                  </option>
                ))}
              </select>
            </div>
            <div className="mb-3">
              <button
                className="btn btn-secondary mb-3"
                onClick={handleSeleccionarAula}
                disabled={horaInicial === "--:--" || horaFinal === "--:--"}
              >
                Seleccionar Aula
              </button>
              {aulaSeleccionada && (
                <p>Aula seleccionada: {aulaSeleccionada.numero}</p>
              )}
            </div>
          </div>
          <div className="modal-footer mt-0">
            <div className="mt-3">
              <div className="col-8 d-flex justify-content-between">
                <button className="btn btn-secondary me-2" onClick={onCerrar}>
                  Cancelar
                </button>
                <button
                  className="btn btn-warning"
                  onClick={manejarGuardar}
                  disabled={!!errorFecha || !aulaSeleccionada} 
                >
                  Guardar
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <DisponibilidadAulaEsporadica
        formData={formData}
        isOpen={modalAulaAbierto}
        onClose={() => setModalAulaAbierto(false)}
        actualizarAulaSeleccionada={actualizarAulaSeleccionada}
        fecha={fecha}
        horaInicio={horaInicial}
        horaFinal={horaFinal}
      />
    </div>
  );
}

export default ReservaModal;
