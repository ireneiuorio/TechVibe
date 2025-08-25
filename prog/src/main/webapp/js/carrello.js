
    // Base URL dal contesto JSP (verrà risolto lato server)
    const BASE = "${pageContext.request.contextPath}";

    // --- RIFERIMENTI DOM ---
    const carIcon   = document.getElementById("car");           // icona <img id="car">
    const cartBadge = document.getElementById("cart-count");    // <span id="cart-count"> nel markup suggerito
const cartLink  = document.querySelector('.menu a[href*="/pages/carrello"]'); // link testuale "Carrello"

// --- STATO (persistente in sessione) ---
const STORAGE_KEY = "cart_count";
function getCount() {
        const n = parseInt(sessionStorage.getItem(STORAGE_KEY), 10);
        return Number.isFinite(n) && n >= 0 ? n : 0;
    }
    function setCount(n) {
        sessionStorage.setItem(STORAGE_KEY, String(n));
        renderBadge(n);
    }

    // --- UI badge ---
function renderBadge(n) {
        if (!cartBadge) return;
        if (n > 0) {
        cartBadge.textContent = n;
        cartBadge.style.display = "inline-block";
        cartBadge.setAttribute("aria-label", `Articoli nel carrello: ${n}`);
    } else {
        cartBadge.textContent = "0";
        cartBadge.style.display = "none";
        cartBadge.removeAttribute("aria-label");
    }
    }

    // --- NAVIGAZIONE al carrello ---
function goToCart() {
        // se hai una pagina carrello dedicata:
        window.location.href = BASE + "/pages/carrello";
        // in alternativa, se fosse una sezione nella stessa pagina:
        // document.getElementById("carrello")?.scrollIntoView({ behavior: "smooth" });
    }

    // click su icona e sul link testuale
if (carIcon)  carIcon.addEventListener("click", goToCart);
if (cartLink) cartLink.addEventListener("click", (e) => {
        // se vuoi forzare stessa navigazione
        e.preventDefault();
        goToCart();
    });

// --- AGGIUNTA PRODOTTI ---
function addToCart(quantity = 1) {
        const current = getCount();
        const next = current + (Number.isFinite(quantity) ? quantity : 1);
        setCount(next);
    }

    // Aggancio automatico a tutti i bottoni .add-to-cart
// Supporta sia pulsanti statici che aggiunti dopo (event delegation)
document.addEventListener("click", (e) => {
        const btn = e.target.closest(".add-to-cart");
        if (!btn) return;

        // Leggi eventualmente una quantità dal data-attribute: data-qty="2"
        const qtyAttr = btn.getAttribute("data-qty");
        const qty = qtyAttr ? parseInt(qtyAttr, 10) : 1;
        addToCart(Number.isFinite(qty) && qty > 0 ? qty : 1);
    });

// --- INIT all'avvio ---
renderBadge(getCount());
