function siguienteSeccion() {
    document.getElementById('seccion1').style.display = 'none';
    document.getElementById('seccion2').style.display = 'block';

    var contenedorFormulario = document.querySelector('.form-container');
    contenedorFormulario.classList.remove('col-6');
    contenedorFormulario.classList.add('col-12');
}

function volverSeccion() {
    document.getElementById('seccion2').style.display = 'none';
    document.getElementById('seccion1').style.display = 'block';
    
    var contenedorFormulario = document.querySelector('.form-container');
    contenedorFormulario.classList.remove('col-12');
    contenedorFormulario.classList.add('col-6');
}

// Obtener los checkboxes y los card-body de cada día
const checkboxes = document.querySelectorAll('.form-check-input');
const cuerposTarjeta = document.querySelectorAll('.card-body');

// Agregar un evento de cambio a cada checkbox
checkboxes.forEach((checkbox, index) => {
    checkbox.addEventListener('change', function() {
        // Verificar si el checkbox está marcado
        if (this.checked) {
            // Si está marcado, habilitar el card-body correspondiente
            cuerposTarjeta[index].classList.remove('disabled');
        } else {
            // Si no está marcado, deshabilitar el card-body correspondiente
            cuerposTarjeta[index].classList.add('disabled');
        }
    });
});




// ========================================================================================================
// ========================================================================================================

document.addEventListener("DOMContentLoaded", function() {
    // Función para generar las opciones de hora inicial y final para un día de la semana
    function generarOpcionesDia(selectHoraInicial, selectHoraFinal) {
        // Limpiar las opciones actuales del select de hora inicial
        selectHoraInicial.innerHTML = "";

        // Agregar la opción "--:--" al principio del select de hora inicial
        var opcionPredeterminada = document.createElement("option");
        opcionPredeterminada.text = "--:--";
        opcionPredeterminada.value = "0";
        selectHoraInicial.add(opcionPredeterminada);

        // Generar las opciones del select de hora inicial de 08:00 a 22:00 cada 5 minutos
        var horaInicio = new Date();
        horaInicio.setHours(8, 0, 0, 0); // 08:00
        var horaFin = new Date();
        horaFin.setHours(22, 0, 0, 0); // 22:00

        var horaActual = new Date(horaInicio);

        while (horaActual <= horaFin) {
            var hora = horaActual.getHours().toString().padStart(2, '0'); // Asegurar dos dígitos antes de los dos puntos
            var minuto = horaActual.getMinutes().toString().padStart(2, '0'); // Asegurar dos dígitos después de los dos puntos
            
            var opcion = document.createElement("option");
            opcion.text = `${hora}:${minuto}`;
            opcion.value = `${hora}:${minuto}`;
            selectHoraInicial.add(opcion);
            horaActual.setMinutes(horaActual.getMinutes() + 5); 
        }

        // Event listener para el cambio en el select de hora inicial
        selectHoraInicial.addEventListener("change", function() {
            var horaSeleccionada = selectHoraInicial.value;
            generarOpcionesFinales(horaSeleccionada, selectHoraFinal); // Generar las opciones del select de hora final a partir del horario seleccionado en el primer select
        });
    }

    // Función para generar las opciones de hora final a partir de la hora inicial seleccionada
    function generarOpcionesFinales(horaSeleccionada, selectHoraFinal) {
        // Limpiar las opciones actuales del select de hora final
        selectHoraFinal.innerHTML = "";

        // Agregar la opción "--:--" al principio del select de hora final
        var opcionPredeterminada = document.createElement("option");
        opcionPredeterminada.text = "--:--";
        opcionPredeterminada.value = "0";
        selectHoraFinal.add(opcionPredeterminada);

        // Calcular el próximo horario disponible agregando 30 minutos al horario seleccionado en el primer select
        var horaSeleccionadaHora = parseInt(horaSeleccionada.split(":")[0]);
        var horaSeleccionadaMinuto = parseInt(horaSeleccionada.split(":")[1]);

        var horaActual = new Date();
        horaActual.setHours(horaSeleccionadaHora, horaSeleccionadaMinuto, 0, 0);

        var horaFin = new Date();
        horaFin.setHours(22, 0, 0, 0); // 22:00

        horaActual.setMinutes(horaActual.getMinutes() + 30); // Agregar 30 minutos al horario seleccionado en el primer select

        while (horaActual <= horaFin) {
            var hora = horaActual.getHours().toString().padStart(2, '0'); // Asegurar dos dígitos antes de los dos puntos
            var minuto = horaActual.getMinutes().toString().padStart(2, '0'); // Asegurar dos dígitos después de los dos puntos
            
            var opcion = document.createElement("option");
            opcion.text = `${hora}:${minuto}`;
            opcion.value = `${hora}:${minuto}`;
            selectHoraFinal.add(opcion);
            horaActual.setMinutes(horaActual.getMinutes() + 30); // Generar opciones cada 30 minutos
        }
    }

    // Ejecutar la función para generar las opciones para cada día de la semana
    generarOpcionesDia(document.getElementById("horaInicialLunes"), document.getElementById("horaFinalLunes"));
    generarOpcionesDia(document.getElementById("horaInicialMartes"), document.getElementById("horaFinalMartes"));
    generarOpcionesDia(document.getElementById("horaInicialMiercoles"), document.getElementById("horaFinalMiercoles"));
    generarOpcionesDia(document.getElementById("horaInicialJueves"), document.getElementById("horaFinalJueves"));
    generarOpcionesDia(document.getElementById("horaInicialViernes"), document.getElementById("horaFinalViernes"));
});
