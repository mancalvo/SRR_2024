import React, { useEffect, useState } from "react";
import ReservaSinSolapamiento from './ReservaSinSolapamiento'; 
import ReservaConSolapamiento from './ReservaConSolapamiento'; 

const DisponibilidadAula = ({
  formData,
  dia,
  isOpen,
  onClose,
  actualizarAulaSeleccionada,
  fecha,
  horaInicio,
  horaFinal,
}) => {
  const [aulas, setAulas] = useState([]); // Lista de aulas disponibles
  const [conflictos, setConflictos] = useState(null); // Estado para los conflictos
  const [aulaSeleccionada, setAulaSeleccionada] = useState(null);
  const [cargando, setCargando] = useState(false); // Estado para manejar la carga de datos
  const [error, setError] = useState(null); // Estado para manejar errores
  
  const tipoAulaMap = {
    "Aula Multimedios": "MULTIMEDIOS",
    "Aula Informática": "INFORMATICA",
    "Aula sin recursos": "SINRECURSOS",
  };

  useEffect(() => {
    const tipoAulaBackend = tipoAulaMap[formData.tipoAula];
    
    if (!tipoAulaBackend) {
      console.error(`Tipo de aula "${formData.tipoAula}" no encontrado en tipoAulaMap`);
      setError(`El tipo de aula "${formData.tipoAula}" no es válido`);
      return;
    }

    if (isOpen) {
      const fetchAulas = async () => {
        setCargando(true);
        setError(null);

        try {
          const fechaObj = fecha ? new Date(fecha) : null;

          const params = new URLSearchParams({
            tipoAula: tipoAulaBackend,
            capacidad: formData.cantAlumnos,
            tipoReserva: formData.tipoReserva === "Esporadica" ? "ESPORADICA" : "PERIODICA",
            tipoPeriodo: formData.periodo || '',
            fecha: fechaObj ? fechaObj.toISOString().split('T')[0] : '',
            dia: dia || '',
            horaInicio,
            horaFinal,
          });
          console.log(params);
          const response = await fetch(`http://localhost:8080/aulas/disponibles?${params.toString()}`, {
            method: "GET",
          });

          if (!response.ok) {
            throw new Error("Error al obtener las aulas disponibles");
          }

          const data = await response.json();
          setAulas(data.aulasDisponibles || []);
          setConflictos(data.conflictos);
        } catch (err) {
          setError(err.message || "Error desconocido");
        } finally {
          setCargando(false);
        }
      };

      fetchAulas();
    }
  }, [isOpen, formData, fecha, horaInicio, horaFinal]);

  const handleSelection = (numero) => {
    console.log('Aula seleccionada Disponibilidad:', numero);
    setAulaSeleccionada(numero);
  };

  const handleConfirm = () => {
    const aula = aulas.find((aula) => aula.numero === aulaSeleccionada);
    console.log('Aula confirmada Disponibilidad:', aula);
    if (aula) {
      actualizarAulaSeleccionada(aula);
      onClose();
    } else {
      console.log("No se encontró el aula seleccionada.");
    }
  };

  if (!isOpen) return null;

  return (
    <div>
      {conflictos && conflictos.length > 0 ? (
        <ReservaConSolapamiento
        conflictos={conflictos} 
        aulasDisponibles={aulas} 
        onClose={onClose}
      />
      ) : (
        <ReservaSinSolapamiento
          aulas={aulas}
          aulaSeleccionada={aulaSeleccionada}
          handleSelection={handleSelection}
          handleConfirm={handleConfirm}
          cargando={cargando}
          error={error}
          onClose={onClose}
        />
      )}
    </div>
  );
};

export default DisponibilidadAula;

