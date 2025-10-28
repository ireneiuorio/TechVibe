// ==== Setup base ====
if (!window.contextPath) {
    window.contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
}

// ==== Badge - Aggiorna contatore carrello nell'header ====
function aggiornaConteggioBadge() {
    fetch(window.contextPath + '/carrello/count')
        .then(r => r.json())
        .then(data => {
            const badge = document.getElementById('cart-count');
            if (badge) {
                const c = data && typeof data.count === 'number' ? data.count : 0;
                badge.textContent = c;
                if (c >=0) {
                    badge.style.display = 'flex';
                }
            }
        })
        .catch(err => console.error('Errore badge:', err));
}

// Aggiorna badge al caricamento di ogni pagina
document.addEventListener('DOMContentLoaded', function () {
    aggiornaConteggioBadge();
});

// ==== Aggiungi al carrello (per pagine prodotto) ====
function aggiungiAlCarrello(prodottoId, quantita = 1) {
    if (!prodottoId) return;
    const button = document.querySelector(`[data-id="${prodottoId}"]`);
    if (button && button.disabled) return;

    if (button) {
        button.disabled = true;
        button.textContent = 'Aggiungendo...';
    }

    const params = new URLSearchParams();
    params.append('prodottoId', prodottoId);
    params.append('quantita', quantita);

    fetch(window.contextPath + '/carrello/aggiungi', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
        .then(r => r.json())
        .then(data => {
            if (data.success) {
                aggiornaConteggioBadge();
            } else {
                alert(data.error || 'Errore durante l\'aggiunta');
            }
        })
        .catch(err => {
            console.error('Errore aggiunta:', err);
            alert('Errore di connessione');
        })
        .finally(() => {
            if (button) {
                button.disabled = false;
                button.textContent = 'Aggiungi al carrello';
            }
        });
}

// Click su pulsante "Aggiungi al carrello" (pagine prodotto)
document.addEventListener('click', function (e) {
    const t = e.target;

    if (t.classList.contains('add-to-cart')) {
        e.preventDefault();
        const prodottoId = t.getAttribute('data-id');
        const quantita = t.getAttribute('data-qty') || 1;
        if (prodottoId) aggiungiAlCarrello(parseInt(prodottoId, 10), parseInt(quantita, 10));
    }
});

// Pagina prodotto - funzione con input quantità personalizzata
function aggiungiAlCarrelloConQuantita(prodottoId) {
    const quantitaInput = document.getElementById('quantity');
    const quantita = quantitaInput ? parseInt(quantitaInput.value, 10) || 1 : 1;
    aggiungiAlCarrello(prodottoId, quantita);
}