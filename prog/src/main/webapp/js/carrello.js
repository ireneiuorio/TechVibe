//Trova il percorso del sito
if (!window.contextPath) {
    window.contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
}

// Aggiorna il contaggio del badge del carrello
function aggiornaConteggioBadge() {
    fetch(window.contextPath + '/carrello/count') //Server: dammi il numero dei prodotti el carrello e aggiorna il numero
        .then(r => r.json())//Il server risponde e questo then trasforma la risposta in un oggetto JS
        .then(data => { //Aggiorna il badge visivo
            const badge = document.getElementById('cart-count');
            if (badge) { //Se il server mi ha dato un numero usalo altrimenti usa 0
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




// Aggiungi al carrello (per pagine prodotto)
function aggiungiAlCarrello(prodottoId, quantita = 1) {
    if (!prodottoId) return; //Se non ho specificato un prodotto non fare nulla

    const button = document.querySelector(`[data-id="${prodottoId}"]`);//Trova il pulsante con attributo id=
    if (button && button.disabled) return;//Se e disabilitato esci

    if (button) {
        button.disabled = true;
        button.textContent = 'Aggiungendo...';
    }

    const params = new URLSearchParams(); //Prepara i dati da inviare
    params.append('prodottoId', prodottoId); //Carica i parametri da inviare al Server
    params.append('quantita', quantita);


    //Invia la richiesta al server
    fetch(window.contextPath + '/carrello/aggiungi', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })

        //Gestisci la risposta
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

        //Riabilita il pulsante
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

    if (t.classList.contains('add-to-cart')) { //Elemento cliccato add to cart
        e.preventDefault();
        const prodottoId = t.getAttribute('data-id');
        const quantita = t.getAttribute('data-qty') || 1;
        if (prodottoId) aggiungiAlCarrello(parseInt(prodottoId, 10), parseInt(quantita, 10));//Converte i numeri e chiama la funzione aggiungi al carrello
    }
});

// Pagina prodotto - funzione con input quantità personalizzata
function aggiungiAlCarrelloConQuantita(prodottoId) {
    const quantitaInput = document.getElementById('quantity');
    const quantita = quantitaInput ? parseInt(quantitaInput.value, 10) || 1 : 1;
    aggiungiAlCarrello(prodottoId, quantita);
}

/*Cosa succede quando clicchi:
Click sul pulsante → JavaScript intercetta
Pulsante diventa grigio con testo "Aggiungendo..."
Richiesta al server → Aggiungi prodotto 5, quantità 2
Server risponde → `{"success": true}`
Badge si aggiorna → da `0` a `2`
Pulsante torna normale → "Aggiungi al carrello"*/