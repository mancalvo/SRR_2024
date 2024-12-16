import React, { useState } from "react";
import ReservaModal from "./ReservaModal"; // Importar el modal

function Esporadica({ volverSeccion }) {
  const [mostrarModal, setMostrarModal] = useState(false);
  const [listaReservas, setListaReservas] = useState([]);
  const [reservaActual, setReservaActual] = useState(null);

  const manejarMostrarModal = (reserva = null) => {
    setReservaActual(reserva);
    setMostrarModal(true);
  };

  const manejarCerrarModal = () => {
    setMostrarModal(false);
    setReservaActual(null);
  };

  const manejarAgregarReserva = (nuevaReserva) => {
    if (reservaActual) {
      // Editar reserva existente
      setListaReservas((prevLista) =>
        prevLista.map((reserva) =>
          reserva.id === reservaActual.id ? nuevaReserva : reserva
        )
      );
    } else {
      // Agregar nueva reserva
      setListaReservas((prevLista) => [
        ...prevLista,
        { ...nuevaReserva, id: Date.now() }, // Asegúrate de que nuevaReserva tenga todos los campos
      ]);
    }
    manejarCerrarModal();
  };

  const manejarEliminarReserva = (id) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar esta reserva?")) {
      setListaReservas((prevLista) =>
        prevLista.filter((reserva) => reserva.id !== id)
      );
    }
  };

  return (
    <div className="container">
      <div className="row mt-4 justify-content-center">
        <div className="col-6 text-center">
          <h2>Reservas Esporádicas</h2>
        </div>
      </div>

      <div className="row">
        <div className="col-6 d-flex justify-content-start align-items-center">
          {/* Eliminar input de búsqueda */}
        </div>
        <div className="col-6">
          <div className="d-flex justify-content-end">
            <button
              type="button"
              className="btn btn-outline-success"
              onClick={() => manejarMostrarModal()}
            >
              Agregar Reserva
            </button>
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col">
          <div className="table-responsive">
            <table className="table table-hover table-bordered">
              <thead>
                <tr className="table-dark text-center align-middle">
                  <th>Fecha</th>
                  <th>Hora Inicio</th>
                  <th>Hora Final</th>
                  <th>Aula Seleccionada</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {listaReservas.map((reserva) => (
                  <tr className="text-center" key={reserva.id}>
                    <td>{reserva.fecha}</td>
                    <td>{reserva.horaInicial}</td>
                    <td>{reserva.horaFinal}</td>
                    <td>{reserva.aulaSeleccionada ? reserva.aulaSeleccionada.nombre : "No asignada"}</td>
                    <td className="text-center">
                      <button
                        className="btn btn-warning me-2"
                        onClick={() => manejarMostrarModal(reserva)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-danger"
                        onClick={() => manejarEliminarReserva(reserva.id)}
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Mostrar la cantidad de reservas */}
      <div className="d-flex justify-content-start mt-0 mb-3">
        <span>Cantidad de Reservas: {listaReservas.length}</span>
      </div>

      {mostrarModal && (
        <ReservaModal
          reserva={reservaActual}
          onGuardar={manejarAgregarReserva}
          onCerrar={manejarCerrarModal}
        />
      )}

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
            aria-label="Guardar reserva"
          >
            Guardar Reserva
          </button>
        </div>
      </div>
    </div>
  );
}

export default Esporadica;