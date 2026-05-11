export function convertirHTML_PDF(elementoDIV) {
    const elemento = document.getElementById(elementoDIV);

    // Aplicar borde fino SOLO para el PDF
    elemento.querySelectorAll('th, td').forEach(celda => {
        celda.style.border = "1px solid black";
    });

    const opciones = {
        filename: 'inventario CIFPP',
        image: { type: 'pdf', quality: 0.98 },
        html2canvas: {scale: 2 },
        jsPDF: {
            unit : 'in',
            format: 'a4',
            orientation: 'portrait' //'landscape', //para a paisado  
        }
    }
    
    html2pdf().set(opciones).from(elemento).save();


}

// Hacer la función accesible desde el HTML
window.convertirHTML_PDF = convertirHTML_PDF;