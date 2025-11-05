<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Il tuo Carrello - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Carrello"/>
    <jsp:param name="styles" value="site,carrello"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div class="contenitore-carrello">
  <h1 class="titolo-carrello">Il tuo Carrello</h1>

  <c:choose>
    <c:when test="${empty carrello.items}">
      <!-- Carrello vuoto -->
      <div class="carrello-vuoto">
        <h2>Il tuo carrello è vuoto</h2>
        <p>Aggiungi alcuni prodotti per iniziare!</p>
        <a href="${pageContext.request.contextPath}/pages" class="btn-continua">
          Continua lo Shopping
        </a>
      </div>
    </c:when>
    <c:otherwise>

      <!-- Carrello con prodotti -->
      <div class="griglia-carrello">

        <!-- Lista prodotti -->
        <div>
          <c:forEach var="item" items="${carrello.items}">
            <div class="card-prodotto" data-prodotto-id="${item.prodotto.idProdotto}">

              <!-- Immagine prodotto -->
              <div>
                <c:choose>
                  <c:when test="${not empty item.prodotto.immagine1}">
                    <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}" />
                  </c:when>
                  <c:otherwise>
                    <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
                  </c:otherwise>
                </c:choose>
                <img src="${imgSrc}"
                     alt="${item.prodotto.marca} ${item.prodotto.modello}"
                     class="img-prodotto">
              </div>

              <!-- Dettagli prodotto -->
              <div class="dettagli-prodotto">
                <h3>${item.prodotto.marca} ${item.prodotto.modello}</h3>

                <div class="info-prodotto">
                  <c:if test="${not empty item.prodotto.storage}">
                    <div>Storage: ${item.prodotto.storage}GB</div>
                  </c:if>
                  <c:if test="${not empty item.prodotto.colore}">
                    <div>Colore: ${item.prodotto.colore}</div>
                  </c:if>
                </div>

                <!-- Prezzo unitario -->
                <div class="prezzo-unitario">
                  <fmt:formatNumber value="${item.prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>

                <!-- Controlli quantità e rimozione -->
                <div class="controlli-prodotto">
                  <div>
                    <label class="etichetta-quantita">Quantità:</label>
                    <input type="number"
                           class="quantita-carrello"
                           data-prodotto-id="${item.prodotto.idProdotto}"
                           value="${item.quantita}"
                           min="1">
                  </div>

                  <button class="remove-from-cart" data-id="${item.prodotto.idProdotto}">
                    Rimuovi
                  </button>
                </div>
              </div>

              <!-- Totale item -->
              <div class="totale-item">
                <div class="totale-riga">
                  <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>
                <div class="dettaglio-riga">
                    ${item.quantita} x
                  <fmt:formatNumber value="${item.prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>

        <!-- Riepilogo ordine -->
        <div class="riepilogo-ordine">

          <h3 class="riepilogo-titolo">Riepilogo Ordine</h3>

          <!-- Subtotale -->
          <div class="riga-subtotale">
            <span>Subtotale (<span id="numero-prodotti">${sessionScope.carrello.numeroTotaleArticoli}</span> prodotti):</span>
            <span id="carrello-totale">
              <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
            </span>
          </div>

          <!-- Spedizione -->
          <div class="riga-spedizione">
            <span>Spedizione:</span>
            <span>Gratuita</span>
          </div>

          <!-- Totale finale -->
          <div class="riga-totale">
            <span>Totale:</span>
            <span id="totale-finale">
              <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
            </span>
          </div>

          <!-- Pulsanti azione -->
          <div class="azioni-carrello">
            <c:choose>
              <c:when test="${not empty sessionScope.utenteSession}">
                <a href="${pageContext.request.contextPath}/checkout" class="btn-primario">
                  Procedi al Checkout
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/pages/accediutente?redirect=checkout" class="btn-primario">
                  Accedi per Continuare
                </a>
              </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/pages" class="btn-secondario">
              Continua lo Shopping
            </a>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@include file="../partials/site/footer.jsp"%>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const contextPath = '<%= request.getContextPath() %>';//Ottiene il percorso dell'applicazione

    // Gestione rimozione prodotto //Trova tutti i bottoni con questa classe
    document.querySelectorAll('.remove-from-cart').forEach(button => {
      button.addEventListener('click', function() {
        const prodottoId = this.getAttribute('data-id'); //itera su ogni bottone
        rimuoviProdotto(prodottoId); //chiama la funzione rimuovi prodotto
      });
    });

    // Gestione aggiornamento quantità, triva gli input quantità carrello
    document.querySelectorAll('.quantita-carrello').forEach(input => {
      input.addEventListener('change', function() {
        const prodottoId = this.getAttribute('data-prodotto-id');
        const nuovaQuantita = parseInt(this.value); //converte il valore da stringa a numero

        if (nuovaQuantita < 1) {
          this.value = 1;
          return;
        }

        aggiornaQuantita(prodottoId, nuovaQuantita);
      });
    });


  //CHIAMATA AJAX
    function rimuoviProdotto(prodottoId) {
      fetch(contextPath + '/carrello/rimuovi', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},//Fromato dati
        body: 'prodottoId=' + prodottoId
      })
              .then(response => response.json()) //Converte la risposta del server in json
              .then(data => {
                if (data.success) {
                  //Trova il card HTML del prodotto usando ll'id
                  const cardProdotto = document.querySelector('.card-prodotto[data-prodotto-id="' + prodottoId + '"]');
                  if (cardProdotto) {
                    //Elimina l'elemento del DOM
                    cardProdotto.remove();



                    //Se il carrello è vuoto ricarica la pagina
                    if (data.count === 0) {
                      location.reload();
                    } else {
                      //Aggiorna il riepilogo
                      aggiornaRiepilogo(data.count, data.totale);
                      // Aggiorna anche il badge nell'header
                      if (typeof aggiornaConteggioBadge === 'function') {
                        aggiornaConteggioBadge();
                      }
                    }
                  }
                } else {
                  alert(data.error || 'Errore durante la rimozione');
                }
              })
              .catch(error => {
                console.error('Errore:', error);
                alert('Errore di connessione');
              });
    }

    function aggiornaQuantita(prodottoId, nuovaQuantita) {
      fetch(contextPath + '/carrello/aggiorna', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'prodottoId=' + prodottoId + '&quantita=' + nuovaQuantita
      })
              .then(response => response.json())
              .then(data => {
                if (data.success) {
                  //Aggiorna il totale della riga prodotto
                  aggiornaItemTotale(prodottoId, nuovaQuantita);
                  //Aggiorna il riepilogo generale
                  aggiornaRiepilogo(data.count, data.totale);
                  // Aggiorna anche il badge nell'header
                  if (typeof aggiornaConteggioBadge === 'function') {
                    aggiornaConteggioBadge();
                  }
                } else {
                  alert(data.error || 'Errore durante aggiornamento');
                  location.reload();
                }
              })
              .catch(error => {
                console.error('Errore:', error);
                alert('Errore di connessione');
              });
    }


    function aggiornaItemTotale(prodottoId, nuovaQuantita) {
      const card = document.querySelector('.card-prodotto[data-prodotto-id="' + prodottoId + '"]'); //query selector il primo elemento html
      if (!card) return; //Trova la card del prodotto se non esiste esce

      //Legge il prezzo unitario del DOM
      const prezzoText = card.querySelector('.prezzo-unitario').textContent;
      const prezzo = parseFloat(prezzoText.replace('€', '').replace(',', '.').trim());

      //Calcola il nuovo totale
      const nuovoTotale = prezzo * nuovaQuantita;


      //Aggiorna il totale nella riga
      card.querySelector('.totale-riga').textContent = formatCurrency(nuovoTotale);
      //Aggiorna il dettaglio
      card.querySelector('.dettaglio-riga').innerHTML = nuovaQuantita + ' x ' + formatCurrency(prezzo);
    }

    function aggiornaRiepilogo(count, totale) {
      const numeroProdotti = document.getElementById('numero-prodotti');
      if (numeroProdotti) {
        numeroProdotti.textContent = count;
      }

      //Aggiorna il numero totale di prodotti nel carrello

      const carrelloTotale = document.getElementById('carrello-totale');
      if (carrelloTotale) {
        carrelloTotale.textContent = formatCurrency(totale);
      }

      const totaleFinale = document.getElementById('totale-finale');
      if (totaleFinale) {
        totaleFinale.textContent = formatCurrency(totale);
      }
    }

    //Formattazione
    function formatCurrency(value) {
      return new Intl.NumberFormat('it-IT', { //formatta i numeri in italiano
        style: 'currency', //come valuta euro
        currency: 'EUR',
        minimumFractionDigits: 2 //sempre due decimali
      }).format(value);
    }
  });
</script>

</body>
</html>