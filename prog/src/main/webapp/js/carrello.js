
    // ==== Setup base ====
    if (!window.contextPath) {
    window.contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
}

    let isUpdating = false;
    let quantitaTimeout = null;

    document.addEventListener('DOMContentLoaded', function () {
    // Prepara dataset prezzo unitario per ogni riga (se possibile) e normalizza UI iniziale
    inizializzaRigheCarrello();
    aggiornaConteggioBadge();
    aggiornaVisualizzazioneTotale(); // allinea subtotale (X prodotti) al numero pezzi
});

    // ==== Util ====
    function parseEuroToFloat(txt) {
    // estrae "1.234,56" o "1234.56" e lo converte in float
    if (!txt) return NaN;
    const m = String(txt).match(/([\d\.\,]+)/);
    if (!m) return NaN;
    let num = m[1].trim();

    // Gestione decimali europei: se contiene sia '.' che ',', assume ',' come decimale
    if (num.includes(',') && num.includes('.')) {
    num = num.replace(/\./g, ''); // rimuovi separatori migliaia
    num = num.replace(',', '.');  // usa punto come decimale
} else if (num.includes(',')) {
    // solo virgola => decimale
    num = num.replace(',', '.');
}
    return parseFloat(num);
}

    function formatEuro(value) {
    if (isNaN(value)) value = 0;
    return '€' + value.toFixed(2);
}

    function sommaPezziVisibili() {
    let totalePezzi = 0;
    document.querySelectorAll('.quantita-carrello').forEach(inp => {
    const q = parseInt(inp.value, 10);
    if (!isNaN(q) && q > 0) totalePezzi += q;
});
    return totalePezzi;
}

    // Trova i sotto-elementi "totale riga" e "dettaglio qty x prezzo" nella riga
    function getElementiRiga(container) {
    // container = div[data-prodotto-id="..."]
    // Struttura tua: l'ultimo <div> nella riga contiene:
    //   - primo <div> = totale riga grande
    //   - secondo <div> = "Q x €prezzo"
    const rightCol = container.querySelector(':scope > div:last-child');
    const rigaTotaleEl = rightCol ? rightCol.querySelector(':scope > div:first-child') : null;
    const qtyxPrezzoEl = rightCol ? rightCol.querySelector(':scope > div:last-child') : null;
    return { rigaTotaleEl, qtyxPrezzoEl };
}

    function inizializzaRigheCarrello() {
    document.querySelectorAll('[data-prodotto-id]').forEach(container => {
        const input = container.querySelector('.quantita-carrello');
        const { rigaTotaleEl, qtyxPrezzoEl } = getElementiRiga(container);
        if (!input || !rigaTotaleEl || !qtyxPrezzoEl) return;

        const qty = parseInt(input.value, 10) || 0;

        // recupera prezzo unitario:
        // 1) prova a leggerlo da "Q x €prezzo"
        let unit = NaN;
        const txt = qtyxPrezzoEl.textContent;
        if (txt && txt.includes('x')) {
            // es. "2 x €399,90"
            const parts = txt.split('x');
            if (parts[1]) unit = parseEuroToFloat(parts[1]);
        }
        // 2) se non funziona, deduci da totale riga / qty
        if ((isNaN(unit) || unit <= 0) && qty > 0) {
            const tot = parseEuroToFloat(rigaTotaleEl.textContent);
            if (!isNaN(tot)) unit = tot / qty;
        }

        if (!isNaN(unit) && unit > 0) {
            container.dataset.prezzo = String(unit);
        }

        // Normalizza testo "Q x €prezzo" (utile se il server ha formattazioni varie)
        if (!isNaN(unit) && unit > 0) {
            qtyxPrezzoEl.textContent = `${qty} x ${formatEuro(unit)}`;
        }
    });
}

    // ==== Badge ====
    function aggiornaConteggioBadge() {
    fetch(window.contextPath + '/carrello/count')
        .then(r => r.json())
        .then(data => {
            const badge = document.getElementById('cart-count');
            if (badge) {
                const c = data && typeof data.count === 'number' ? data.count : 0;
                badge.textContent = c;
                badge.style.display = c > 0 ? 'block' : 'none';
            }
        })
        .catch(err => console.error('Errore badge:', err));
}

    // ==== Aggiungi al carrello ====
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
    const badge = document.getElementById('cart-count');
    if (badge) {
    badge.textContent = data.count;
    badge.style.display = data.count > 0 ? 'block' : 'none';
}
    // opzionale: mostra un toast/snackbar
}
})
    .catch(err => console.error('Errore aggiunta:', err))
    .finally(() => {
    if (button) {
    button.disabled = false;
    button.textContent = 'Aggiungi al carrello';
}
});
}

    // ==== Rimuovi dal carrello ====
    function rimuoviDalCarrello(prodottoId) {
    if (!confirm('Rimuovere questo prodotto dal carrello?')) return;

    const params = new URLSearchParams();
    params.append('prodottoId', prodottoId);

    fetch(window.contextPath + '/carrello/rimuovi', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params
})
    .then(r => r.json())
    .then(data => {
    if (data.success) {
    // rimuovi riga
    const riga = document.querySelector(`[data-prodotto-id="${prodottoId}"]`);
    if (riga) riga.remove();

    aggiornaConteggioBadge();
    aggiornaVisualizzazioneTotale();
}
})
    .catch(err => console.error('Errore rimozione:', err));
}

    // ==== Svuota carrello ====
    function svuotaCarrello() {
    if (!confirm('Svuotare il carrello?')) return;

    fetch(window.contextPath + '/carrello/svuota', {
    method: 'POST'
})
    .then(r => r.json())
    .then(data => {
    if (data.success) {
    // rimuovi tutte le righe
    document.querySelectorAll('[data-prodotto-id]').forEach(n => n.remove());
    aggiornaConteggioBadge();
    aggiornaVisualizzazioneTotale();
    // opzionale: redirect alla pagina prodotti se vuoi
    // window.location.href = window.contextPath + '/pages';
}
})
    .catch(err => console.error('Errore svuota:', err));
}

    // ==== Aggiorna quantità (API) ====
    function aggiornaQuantitaCarrello(prodottoId, nuovaQuantita) {
    if (isUpdating) return;
    isUpdating = true;

    const params = new URLSearchParams();
    params.append('prodottoId', prodottoId);
    params.append('quantita', nuovaQuantita);

    fetch(window.contextPath + '/carrello/aggiorna', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params
})
    .then(r => r.json())
    .then(data => {
    if (data.success) {
    // badge
    aggiornaConteggioBadge();

    // sync input
    const input = document.querySelector(`input.quantita-carrello[data-prodotto-id="${prodottoId}"]`);
    if (input) input.value = nuovaQuantita;

    // aggiorna riga & riepilogo
    aggiornaRigaERiepilogo(prodottoId, nuovaQuantita);

    // aggiorna totali & label subtotale
    aggiornaVisualizzazioneTotale();

    // rimuovi riga se 0
    if (nuovaQuantita === 0) {
    const riga = document.querySelector(`[data-prodotto-id="${prodottoId}"]`);
    if (riga) riga.remove();
}
}
})
    .catch(err => console.error('Errore aggiornamento:', err))
    .finally(() => {
    setTimeout(() => { isUpdating = false; }, 300);
});
}

    // ==== Aggiorna riga e riepilogo (locale) ====
    function aggiornaRigaERiepilogo(prodottoId, nuovaQuantita) {
    const container = document.querySelector(`[data-prodotto-id="${prodottoId}"]`);
    if (!container) return;

    // prezzo unitario
    let unit = parseFloat(container.dataset.prezzo);
    if (isNaN(unit) || unit <= 0) {
    // tenta di ricalcolare da UI
    const input = container.querySelector('.quantita-carrello');
    const { rigaTotaleEl, qtyxPrezzoEl } = getElementiRiga(container);
    if (qtyxPrezzoEl && qtyxPrezzoEl.textContent.includes('x')) {
    const parts = qtyxPrezzoEl.textContent.split('x');
    if (parts[1]) unit = parseEuroToFloat(parts[1]);
}
    if ((isNaN(unit) || unit <= 0) && rigaTotaleEl && input) {
    const tot = parseEuroToFloat(rigaTotaleEl.textContent);
    const q = parseInt(input.value, 10) || 1;
    unit = q ? tot / q : tot; // fallback
}
    if (!isNaN(unit) && unit > 0) container.dataset.prezzo = String(unit);
}

    const { rigaTotaleEl, qtyxPrezzoEl } = getElementiRiga(container);
    if (rigaTotaleEl && !isNaN(unit)) {
    const nuovoTotRiga = Math.max(0, unit * (nuovaQuantita || 0));
    rigaTotaleEl.textContent = formatEuro(nuovoTotRiga);
}
    if (qtyxPrezzoEl && !isNaN(unit)) {
    qtyxPrezzoEl.textContent = `${nuovaQuantita} x ${formatEuro(unit)}`;
}
}

    // ==== Totali & riepilogo ====
    function aggiornaVisualizzazioneTotale() {
    fetch(window.contextPath + '/carrello/totale')
        .then(r => r.json())
        .then(data => {
            const totale = data && typeof data.totale === 'number' ? data.totale : 0;

            // 1) Subtotale (X prodotti) => conta PEZZI totali SOLO nel riepilogo carrello
            const subtotaleMoney = document.getElementById('carrello-totale'); // esiste solo in carrello.jsp
            const containerSub = subtotaleMoney
                ? subtotaleMoney.closest('div[style*="border-bottom: 1px solid #eee"]')
                : null;

            if (containerSub) {
                const labelSpan = containerSub.querySelector('span:first-child');
                if (labelSpan) {
                    const pezzi = sommaPezziVisibili();
                    const label = pezzi === 1 ? 'prodotto' : 'prodotti';
                    labelSpan.textContent = `Subtotale (${pezzi} ${label}):`;
                }
            }




            // 3) Totale finale (selettore: l'ultima riga nel riepilogo: border-top + span:last-child)
            const totaleFinale = document.querySelector('div[style*="border-top: 2px solid"] span:last-child');
            if (totaleFinale) {
                totaleFinale.textContent = formatEuro(totale);
            }
        })
        .catch(err => console.error('Errore totale:', err));
}

    // ==== Eventi globali ====
    function handleQuantityChange(target) {
    if (quantitaTimeout) clearTimeout(quantitaTimeout);
    const prodottoId = target.getAttribute('data-prodotto-id');
    const nuovaQuantita = parseInt(target.value, 10);

    quantitaTimeout = setTimeout(() => {
    if (prodottoId && !isNaN(nuovaQuantita) && nuovaQuantita >= 0) {
    // clamp minimo 0 (consenti rimozione con 0)
    aggiornaQuantitaCarrello(parseInt(prodottoId, 10), nuovaQuantita);
}
}, 250);
}

    // Click (add/remove/svuota)
    document.addEventListener('click', function (e) {
    const t = e.target;

    if (t.classList.contains('add-to-cart')) {
    e.preventDefault();
    const prodottoId = t.getAttribute('data-id');
    const quantita = t.getAttribute('data-qty') || 1;
    if (prodottoId) aggiungiAlCarrello(parseInt(prodottoId, 10), parseInt(quantita, 10));
}

    if (t.classList.contains('remove-from-cart')) {
    e.preventDefault();
    const prodottoId = t.getAttribute('data-id');
    if (prodottoId) rimuoviDalCarrello(parseInt(prodottoId, 10));
}

    if (t.classList.contains('svuota-carrello')) {
    e.preventDefault();
    svuotaCarrello();
}
});

    // Input/Change quantità (supporta increment/decrement e digitazione)
    document.addEventListener('input', function (e) {
    if (e.target.classList.contains('quantita-carrello')) {
    handleQuantityChange(e.target);
}
});
    document.addEventListener('change', function (e) {
    if (e.target.classList.contains('quantita-carrello')) {
    handleQuantityChange(e.target);
}
});

    // Pagina prodotto (se presente)
    function aggiungiAlCarrelloConQuantita(prodottoId) {
    const quantitaInput = document.getElementById('quantity');
    const quantita = quantitaInput ? parseInt(quantitaInput.value, 10) || 1 : 1;
    aggiungiAlCarrello(prodottoId, quantita);
}

