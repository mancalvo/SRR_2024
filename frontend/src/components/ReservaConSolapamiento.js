import React from 'react';

const ReservaConSolapamiento = ({ conflictos, aulasDisponibles, onClose }) => {
  console.log(conflictos); // Verifica el contenido de los conflictos

  return (
    <div className="modal fade show d-block" tabIndex="-1" aria-modal="true" role="dialog">
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">Existen conflictos con reservas</h5>
            <button className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            <div className="table-responsive mt-3">
              <table className="table table-bordered table-hover text-center">
                <thead className="table-dark">
                  <tr>
                    <th>Aula</th>
                    <th>Apellido y Nombre</th>
                    <th>Cátedra</th>
                    <th>Correo</th>
                    <th>Inicio</th>
                    <th>Final</th>
                  </tr>
                </thead>
                <tbody>
                  {conflictos.map((conflicto, index) => {
                    const aula = aulasDisponibles[index];

                    return (
                      <>
                        {/* Recorrer conflictosEsporadicos si existen */}
                        {conflicto.conflictosEsporadicos &&
                          conflicto.conflictosEsporadicos.map((conflictoEsporadico, idx) => (
                            <tr key={`esporadico-${index}-${idx}`}>
                              <td>{aula ? `Aula ${aula.numero}` : 'No disponible'}</td>
                              <td>{conflictoEsporadico.docente}</td>
                              <td>{conflictoEsporadico.curso}</td>
                              <td>{conflictoEsporadico.email}</td>
                              <td>{conflictoEsporadico.horaInicio}</td>
                              <td>{conflictoEsporadico.horaFinal}</td>
                            </tr>
                          ))}

                        {/* Recorrer conflictosPeriodicos si existen */}
                        {conflicto.conflictosPeriodicos &&
                          conflicto.conflictosPeriodicos.map((conflictoPeriodico, idx) => (
                            <tr key={`periodico-${index}-${idx}`}>
                              <td>{aula ? `Aula ${aula.numero}` : 'No disponible'}</td>
                              <td>{conflictoPeriodico.docente}</td>
                              <td>{conflictoPeriodico.curso}</td>
                              <td>{conflictoPeriodico.email}</td>
                              <td>{conflictoPeriodico.horaInicio}</td>
                              <td>{conflictoPeriodico.horaFinal}</td>
                            </tr>
                          ))}
                      </>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReservaConSolapamiento;
