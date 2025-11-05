
document.addEventListener("DOMContentLoaded", () => {
    const hamburger = document.getElementById("hamburger");
    const sidebar   = document.querySelector(".sidebar");
    const content   = document.querySelector(".content");

    if (!hamburger || !sidebar || !content) return; // evita l'errore del type-checker

    hamburger.addEventListener("click", () => {
        sidebar.classList.toggle("collapse");
        content.classList.toggle("full-width");
    });
});
