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



const checkboxes = document.querySelectorAll('.form-check-input');
const cuerposTarjeta = document.querySelectorAll('.card-body');

checkboxes.forEach((checkbox, index) => {
    checkbox.addEventListener('change', function() {
        if (this.checked) {
            cuerposTarjeta[index].classList.remove('disabled');
        } else {
            cuerposTarjeta[index].classList.add('disabled');
        }
    });
});



document.addEventListener("DOMContentLoaded", function() {
    function generarOpcionesDia(selectHoraInicial, selectHoraFinal) {
        selectHoraInicial.innerHTML = "";
        var opcionPredeterminada = document.createElement("option");
        opcionPredeterminada.text = "--:--";
        opcionPredeterminada.value = "0";
        selectHoraInicial.add(opcionPredeterminada);

        var horaInicio = new Date();
        horaInicio.setHours(8, 0, 0, 0); // 08:00
        var horaFin = new Date();
        horaFin.setHours(22, 0, 0, 0); // 22:00

        var horaActual = new Date(horaInicio);

        while (horaActual <= horaFin) {
            var hora = horaActual.getHours().toString().padStart(2, '0');
            var minuto = horaActual.getMinutes().toString().padStart(2, '0');
            
            var opcion = document.createElement("option");
            opcion.text = `${hora}:${minuto}`;
            opcion.value = `${hora}:${minuto}`;
            selectHoraInicial.add(opcion);
            horaActual.setMinutes(horaActual.getMinutes() + 5);
        }

        selectHoraInicial.addEventListener("change", function() {
            var horaSeleccionada = selectHoraInicial.value;
            generarOpcionesFinales(horaSeleccionada, selectHoraFinal);
        });
    }

    function generarOpcionesFinales(horaSeleccionada, selectHoraFinal) {
        selectHoraFinal.innerHTML = "";
        var opcionPredeterminada = document.createElement("option");
        opcionPredeterminada.text = "--:--";
        opcionPredeterminada.value = "0";
        selectHoraFinal.add(opcionPredeterminada);

        var [horaSeleccionadaHora, horaSeleccionadaMinuto] = horaSeleccionada.split(":").map(Number);
        var horaActual = new Date();
        horaActual.setHours(horaSeleccionadaHora, horaSeleccionadaMinuto, 0, 0);

        var horaFin = new Date();
        horaFin.setHours(22, 0, 0, 0);

        horaActual.setMinutes(horaActual.getMinutes() + 30);

        while (horaActual <= horaFin) {
            var hora = horaActual.getHours().toString().padStart(2, '0');
            var minuto = horaActual.getMinutes().toString().padStart(2, '0');
            
            var opcion = document.createElement("option");
            opcion.text = `${hora}:${minuto}`;
            opcion.value = `${hora}:${minuto}`;
            selectHoraFinal.add(opcion);
            horaActual.setMinutes(horaActual.getMinutes() + 30);
        }
    }

    // Generar opciones para cada día
    ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'].forEach(dia => {
        generarOpcionesDia(document.getElementById(`horaInicial${dia}`), document.getElementById(`horaFinal${dia}`));
    });
});



document.querySelectorAll('.table-row').forEach(row => {
    row.addEventListener('click', function() {
        document.querySelectorAll('.table-row').forEach(r => r.classList.remove('table-active'));
        this.classList.add('table-active'); // Agrega clase para resaltar
    });
});

document.querySelector('.btn-success').addEventListener('click', function() {
    const selectedRow = document.querySelector('.table-active');
    if (selectedRow) {
        const aula = selectedRow.cells[0].textContent;
        alert('Has seleccionado el ' + aula);
    } else {
        alert('Por favor, selecciona un aula.');
    }
});


document.addEventListener('DOMContentLoaded', function() {
    var modal = document.getElementById('asignarAula');
    var aulaSeleccionada = null;
    var botonSeleccionarAula = modal.querySelector('.btn-warning');

    botonSeleccionarAula.addEventListener('click', function() {
        if (aulaSeleccionada) {
            var carta = document.querySelector(`#${aulaSeleccionada}`);
            var aulaFooter = carta.querySelector('.aula-asignada');
            aulaFooter.textContent = `Aula ${aulaSeleccionada} asignada`;
        }
        var modalInstance = bootstrap.Modal.getInstance(modal);
        modalInstance.hide();
    });

    modal.addEventListener('show.bs.modal', function(event) {
        var button = event.relatedTarget;
        var day = button.getAttribute('data-day');
        aulaSeleccionada = day;
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const botonesSeleccionarAula = document.querySelectorAll('a[data-bs-toggle="modal"]');

    botonesSeleccionarAula.forEach(boton => {
        boton.addEventListener('click', function(event) {
            event.preventDefault();
            const diaSeleccionado = this.getAttribute('data-day');
            console.log('Día seleccionado:', diaSeleccionado);

            const modal = new bootstrap.Modal(document.getElementById('asignarAula'));
            modal.show();
        });
    });
});


let selectedDay; // Para almacenar el día que seleccionará el aula

// Función que se activa al abrir el modal de "Seleccionar Aula"
document.querySelectorAll('[data-bs-target="#asignarAula"]').forEach(button => {
    button.addEventListener('click', function() {
        selectedDay = this.getAttribute('data-day'); // Obtener el día del botón clickeado (Lunes, Martes, etc.)
    });
});

// Función para asignar el aula seleccionada
document.querySelector('#asignarAula .btn-warning').addEventListener('click', function() {
    // Obtener el aula seleccionada de la tabla
    const selectedRow = document.querySelector('#asignarAula tbody .table-row.selected');
    if (!selectedRow) {
        alert('Por favor, selecciona un aula de la tabla');
        return;
    }

    // Obtener la información del aula
    const aulaSeleccionada = selectedRow.cells[0].innerText;
    const pisoSeleccionado = selectedRow.cells[1].innerText;
    const tipoAulaSeleccionada = selectedRow.cells[2].innerText;
    const capacidadSeleccionada = selectedRow.cells[3].innerText;

    // Actualizar la tarjeta correspondiente al día seleccionado
    const aulaDia = document.querySelector(`#aula${selectedDay}`);
    aulaDia.innerHTML = `
        <strong>Aula:</strong> ${aulaSeleccionada} <br>
        <strong>Piso:</strong> ${pisoSeleccionado} <br>
        <strong>Tipo:</strong> ${tipoAulaSeleccionada} <br>
        <strong>Capacidad:</strong> ${capacidadSeleccionada}
    `;

    // Cerrar el modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('asignarAula'));
    modal.hide();
});

// Función para seleccionar una fila de la tabla
document.querySelectorAll('#asignarAula tbody .table-row').forEach(row => {
    row.addEventListener('click', function() {
        // Quitar la clase 'selected' de las otras filas
        document.querySelectorAll('#asignarAula tbody .table-row').forEach(r => r.classList.remove('selected'));

        // Agregar la clase 'selected' a la fila clickeada
        this.classList.add('selected');
    });
});



// Función para seleccionar una fila de la tabla y el radio button correspondiente
document.querySelectorAll('#asignarAula tbody .table-row').forEach(row => {
    row.addEventListener('click', function() {
        // Quitar la clase 'selected' de las otras filas
        document.querySelectorAll('#asignarAula tbody .table-row').forEach(r => r.classList.remove('selected'));

        // Agregar la clase 'selected' a la fila clickeada
        this.classList.add('selected');

        // Seleccionar el radio button correspondiente
        const radioButton = this.querySelector('input[type="radio"]');
        radioButton.checked = true;
    });

    // Asegurarse de que al hacer clic en el radio button no deseleccione la fila
    const radioButton = row.querySelector('input[type="radio"]');
    radioButton.addEventListener('click', function(event) {
        event.stopPropagation(); // Evita que el clic en el radio button deseleccione la fila
    });
});

