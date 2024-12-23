import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Seccion1 from "./Seccion1";
import Periodica from "./Periodica";
import Esporadica from "./Esporadica";
import axios from "axios";
import "../App.css";

function NuevaReservaMain() {
  const [currentSection, setCurrentSection] = useState(1);
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    solicitante: "",
    correo: "",
    catedra: "",
    cantAlumnos: "",
    tipoReserva: "",
    tipoAula: "", 
    periodo: null, 
    detallesReserva: [],
  });

  const tipoAulaMap = {
    "Aula Multimedios": "MULTIMEDIOS",
    "Aula Informática": "INFORMATICA",
    "Aula sin recursos": "SIN_RECURSO",
  };

  const periodoMap = {
    "1er Cuatrimestre": "PRIMER_CUATRIMESTRE",
    "2do Cuatrimestre": "SEGUNDO_CUATRIMESTRE",
    Anual: "ANUAL",
  };
  const siguienteSeccion = () => {
    // Validar correo electrónico con expresión regular
    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!correoRegex.test(formData.correo)) {
      alert("Por favor ingresa un correo electrónico válido. El formato debería ser: usuario@dominio.com");
      return;
    }

    if (!formData.tipoReserva) {
      alert("Por favor selecciona un tipo de reserva");
      return;
    }

    // Mapeamos el periodo según lo que seleccionó el usuario
    const periodoSeleccionado =
      periodoMap[formData.tipoReserva] || "ESPORADICA";
    setFormData((prevData) => ({
      ...prevData,
      periodo: periodoSeleccionado,
    }));
    if (formData.tipoReserva.toUpperCase() === "ESPORADICA") {
      setCurrentSection(2); // Navegamos a la sección de reservas esporádicas
    } else {
      setCurrentSection(3); // Navegamos a la sección de reservas periódicas
    }
  };



  const volverSeccion = () => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      detallesReserva: [],
    }));
    setCurrentSection(1);
  };

  const enviarReserva = () => {
    console.log("Antes de Enviar: ", formData);

    const nombreUsuario = localStorage.getItem("nombreUsuario");
    const tipoAulaBackend = tipoAulaMap[formData.tipoAula];

    const reservaDTO = {
      nombreUsuario: nombreUsuario || "UsuarioDemo",
      solicitante: formData.solicitante.toUpperCase(),
      correo: formData.correo,
      catedra: formData.catedra.toUpperCase(),
      fechaRealizada: new Date().toISOString().split("T")[0],
      tipoReserva:
        formData.tipoReserva === "Esporadica" ? "ESPORADICA" : "PERIODICA",
      periodo: formData.periodo,
      tipoAula: tipoAulaBackend,
      cantidadAlumnos: parseInt(formData.cantAlumnos),
      detalleReserva: formData.detallesReserva.map((detalle) => ({
        diaSemana: detalle.diaSemana,
        fecha: detalle.fecha,
        horarioInicio: detalle.horarioInicio,
        horarioFinal: detalle.horarioFinal,
        aulaId: detalle.aulaId,
      })),
    };

    console.log("Esto se envia: ", reservaDTO);

    axios
      .post("http://localhost:8080/reservas", reservaDTO, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        console.log("Reserva enviada con éxito:", response);
        alert("La reserva se ha guardado correctamente.");
        navigate("/bedel");
      })
      .catch((error) => {
        console.error("Error al enviar la reserva:", error);

        // Verificamos si el backend envió un mensaje de error
        const errorMessage =
          error.response && error.response.data && error.response.data.message
            ? error.response.data.message
            : "Hubo un error al enviar la reserva. Intenta de nuevo.";

        alert(errorMessage);
      });
  };

  const agregarFecha = (fecha, horarioInicio, horarioFinal, aulaId) => {
    setFormData((prev) => {
      const nuevosDetalles = [
        ...prev.detallesReserva,
        { fecha, horarioInicio, horarioFinal, aulaId },
      ];
      return { ...prev, detallesReserva: nuevosDetalles };
    });
  };

  const agregarDia = (dia, horarioInicio, horarioFinal, aulaId) => {
    setFormData((prev) => ({
      ...prev,
      detallesReserva: [
        ...prev.detallesReserva,
        { diaSemana: dia, horarioInicio, horarioFinal, aulaId: aulaId.numero },
      ],
    }));
  };

  return (
    <div className="container">
      <div className="row mt-4 justify-content-center">
        <div className="col-6 text-center">
          <h1>REGISTRO DE RESERVAS</h1>
        </div>
      </div>

      <div className="row">
        <form
          id="formulario"
          onSubmit={(e) => {
            e.preventDefault();
          }}
        >
          {currentSection === 1 && (
            <Seccion1
              formData={formData}
              setFormData={setFormData}
              siguienteSeccion={siguienteSeccion}
            />
          )}

          {currentSection === 3 && (
            <Periodica
              formData={formData}
              setFormData={setFormData}
              volverSeccion={volverSeccion}
              agregarDia={agregarDia}
              enviarReserva={enviarReserva}
            />
          )}

          {currentSection === 2 && (
            <Esporadica
              formData={formData}
              setFormData={setFormData}
              volverSeccion={volverSeccion}
              agregarFecha={agregarFecha}
              enviarReserva={enviarReserva}
            />
          )}
        </form>
      </div>
    </div>
  );
}

export default NuevaReservaMain;