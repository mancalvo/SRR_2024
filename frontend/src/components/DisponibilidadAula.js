import React, { useState } from "react";

const DisponibilidadAula = ({ isOpen, onClose, actualizarAulaSeleccionada }) => {
  const aulas = [
    { id: 1, nombre: "Aula 101", piso: 1, tipo: "Aula Multimedios", capacidad: 40 },
    { id: 2, nombre: "Aula 202", piso: 2, tipo: "Aula Informática", capacidad: 30 },
    { id: 3, nombre: "Aula 303", piso: 3, tipo: "Aula Sin Recursos", capacidad: 20 },
  ];

  const [aulaSeleccionada, setAulaSeleccionada] = useState(null);

  const handleSelection = (id) => {
    setAulaSeleccionada(id);
  };

  const handleConfirm = () => {
    const aula = aulas.find((aula) => aula.id === aulaSeleccionada);
    actualizarAulaSeleccionada(aula); // Llamamos al callback con el aula seleccionada
    onClose(); // Cerramos el modal
  };

  if (!isOpen) return null;

  return (
    <div className="modal fade show d-block" tabIndex="-1" aria-modal="true" role="dialog">
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">SELECCIONAR AULA</h5>
            <button className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            <form>
              <div className="table-responsive mt-3">
                <table className="table table-bordered table-hover text-center">
                  <thead className="table-dark">
                    <tr>
                      <th>Selec</th>
                      <th>Aula</th>
                      <th>Piso</th>
                      <th>Tipo</th>
                      <th>Capacidad</th>
                    </tr>
                  </thead>
                  <tbody>
                    {aulas.map((aula) => (
                      <tr key={aula.id} onClick={() => handleSelection(aula.id)}>
                        <td>
                          <input
                            type="radio"
                            name="aulaSeleccionada"
                            checked={aulaSeleccionada === aula.id}
                            onChange={() => handleSelection(aula.id)}
                          />
                        </td>
                        <td>{aula.nombre}</td>
                        <td>{aula.piso}</td>
                        <td>{aula.tipo}</td>
                        <td>{aula.capacidad}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </form>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cerrar
            </button>
            <button
              type="button"
              className="btn btn-warning"
              onClick={handleConfirm}
              disabled={!aulaSeleccionada}
            >
              Confirmar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DisponibilidadAula;
