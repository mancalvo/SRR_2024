import React from 'react';

const ReservaSinSolapamiento = ({ aulas, aulaSeleccionada, handleSelection, handleConfirm, cargando, error, onClose }) => {
  
  const obtenerTextoAula = (tipoAula) => {
    switch (tipoAula) {
      case 'INFORMATICA':
        return 'Aula Informática';
      case 'MULTIMEDIOS':
        return 'Aula Multimedios';
      case 'SINRECURSOS':
        return 'Aula sin recursos';
      default:
        return tipoAula; 
    }
  };

  return (
    <div className="modal fade show d-block" tabIndex="-1" aria-modal="true" role="dialog">
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">SELECCIONAR AULA</h5>
            <button className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            {cargando && <p>Cargando aulas disponibles...</p>}
            {error && <p className="text-danger">{error}</p>}
            {!cargando && !error && (
              <form>
                <div className="table-responsive mt-3">
                  <table className="table table-bordered table-hover text-center">
                    <thead className="table-dark">
                      <tr>
                        <th>Selec</th>
                        <th>Aula</th>
                        <th>Tipo</th>
                        <th>Capacidad</th>
                      </tr>
                    </thead>
                    <tbody>
                      {aulas.map((aula) => (
                        <tr key={aula.numero} onClick={() => handleSelection(aula.numero)}>
                          <td>
                            <input
                              type="radio"
                              name="aulaSeleccionada"
                              checked={aulaSeleccionada === aula.numero}
                              onChange={() => handleSelection(aula.numero)}
                            />
                          </td>
                          <td>{`Aula ${aula.numero}`}</td>
                          <td>{obtenerTextoAula(aula.tipoAula)}</td> 
                          <td>{aula.capacidad}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </form>
            )}
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cerrar</button>
            <button
              type="button"
              className="btn btn-warning"
              onClick={handleConfirm}
              disabled={!aulaSeleccionada}
            >
              Seleccionar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReservaSinSolapamiento;
