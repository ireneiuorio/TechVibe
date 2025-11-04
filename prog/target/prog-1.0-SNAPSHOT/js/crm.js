
//Lato crm:quando l'utente clicca sul pulsante hamburger esegui questa funzione
const hamburger = document.getElementById("hamburger");
hamburger.addEventListener("click", function(){
    const sidebar = document.getElementsByClassName("sidebar")[0];//Trova il primo elemento con classe sidebar
    const content = document.getElementsByClassName("content")[0];//Trova il primo elemento con classe content
    sidebar.classList.toggle("collapse");//Se la classe collapse esiste la rimuove se non esiste la aggiunge
    content.classList.toggle("full-width");
});

