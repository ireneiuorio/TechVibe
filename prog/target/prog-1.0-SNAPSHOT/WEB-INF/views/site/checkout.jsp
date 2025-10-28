<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Checkout - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Checkout"/>
    <jsp:param name="styles" value="site,checkout"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div class="contenitore-checkout">
  <h1 class="titolo-checkout">Checkout</h1>

  <c:choose>
    <c:when test="${empty carrello.items}">
      <!-- Carrello vuoto -->
      <div class="box-carrello-vuoto">
        <h2>Il tuo carrello è vuoto</h2>
        <p>Aggiungi alcuni prodotti per procedere al checkout!</p>
        <a href="${pageContext.request.contextPath}/pages" class="link-continua">
          Continua lo Shopping
        </a>
      </div>
    </c:when>

    <c:otherwise>
      <!-- Checkout Form -->
      <form id="checkout-form" class="form-checkout">

        <!-- Sezione Dati Spedizione e Pagamento -->
        <div>
          <!-- Dati di Spedizione -->
          <div class="card-sezione">
            <h3 class="titolo-sezione">
              <span class="badge-step">1</span>
              Indirizzo di Spedizione
            </h3>

            <div class="griglia-2">
              <div>
                <label class="etichetta">Nome *</label>
                <!--Se è presente mostro il valore del nome altrimenti null-->
                <input type="text" name="nome" required value="${utente.nome != null ? utente.nome : ''}" class="campo">
              </div>
              <div>
                <label class="etichetta">Cognome *</label>
                <input type="text" name="cognome" required value="${utente.cognome != null ? utente.cognome : ''}" class="campo">
              </div>
            </div>

            <!-- Solo Indirizzo -->
            <div class="griglia-1">
              <div>
                <label class="etichetta">Indirizzo *</label>
                <input type="text" name="indirizzo" required placeholder="Via, numero civico, città, CAP"
                       value="${utente.indirizzoSpedizione != null ? utente.indirizzoSpedizione : ''}" class="campo">
              </div>
            </div>

            <div>
              <label class="etichetta">Telefono *</label>
              <input type="tel" name="telefono" required value="${utente.telefono != null ? utente.telefono : ''}" class="campo">
            </div>
          </div>

          <!-- Metodo di Pagamento -->
          <div class="card-sezione">
            <h3 class="titolo-sezione">
              <span class="badge-step">2</span>
              Metodo di Pagamento
            </h3>

            <div style="margin-bottom: 1.5rem;">
              <label class="opzione-pagamento selezionata" onclick="selezionaMetodoPagamento('carta')">
                <input type="radio" name="metodoPagamento" value="carta" checked>
                <div class="testo-opzione">
                  <div class="titolo-opzione">Carta di Credito/Debito</div>
                  <div class="sottotitolo-opzione">Visa, Mastercard, American Express</div>
                </div>
              </label>

              <label class="opzione-pagamento" onclick="selezionaMetodoPagamento('paypal')">
                <input type="radio" name="metodoPagamento" value="paypal">
                <div class="testo-opzione">
                  <div class="titolo-opzione">PayPal</div>
                  <div class="sottotitolo-opzione">Paga con il tuo account PayPal</div>
                </div>
              </label>
            </div>

            <!-- Dettagli Carta (visibile solo se selezionata) -->
            <div id="dettagli-carta" style="display: block;">
              <div>
                <label class="etichetta">Numero Carta *</label>
                <input type="text" name="numeroCarta" placeholder="1234 5678 9012 3456" maxlength="19" class="campo">
              </div>

              <div class="griglia-3" style="margin-top: 1rem;">
                <div>
                  <label class="etichetta">Scadenza *</label>
                  <input type="text" name="scadenza" placeholder="MM/AA" maxlength="5" class="campo">
                </div>
                <div>
                  <label class="etichetta">CVV *</label>
                  <input type="text" name="cvv" placeholder="123" maxlength="4" class="campo">
                </div>
                <div>
                  <label class="etichetta">Intestatario *</label>
                  <input type="text" name="intestatario" placeholder="Nome Cognome" class="campo">
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Riepilogo Ordine -->
        <div class="card-sezione riepilogo-colonna">
          <h3 class="titolo-sezione">
            <span class="badge-step">3</span>
            Riepilogo Ordine
          </h3>

          <!-- Lista Prodotti -->
          <div class="lista-prodotti">
            <c:forEach var="item" items="${carrello.items}" varStatus="status">
              <div class="prodotto-riga">
                <!-- Immagine prodotto piccola -->
                <div style="flex-shrink: 0;">
                  <c:choose>
                    <c:when test="${not empty item.prodotto.immagine1}">
                      <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}" />
                    </c:when>
                    <c:otherwise>
                      <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
                    </c:otherwise>
                  </c:choose>
                  <img src="${imgSrc}" alt="${item.prodotto.marca} ${item.prodotto.modello}" class="img-prodotto-mini">
                </div>

                <!-- Dettagli prodotto -->
                <div class="dettagli-mini">
                  <div class="nome-mini">${item.prodotto.marca} ${item.prodotto.modello}</div>
                  <div class="meta-mini">
                    <c:if test="${not empty item.prodotto.storage}">Storage: ${item.prodotto.storage}GB</c:if>
                    <c:if test="${not empty item.prodotto.colore}"> • ${item.prodotto.colore}</c:if>
                  </div>
                  <div class="riga-qta-prezzo">
                    <span>Qta: ${item.quantita}</span>
                    <span class="prezzo-riga">
                      <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                    </span>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>

          <!-- Calcoli -->
          <div class="calcoli">
            <div class="riga-calcolo">
              <span>Subtotale:</span>
              <span id="checkout-subtotale">
                <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
              </span>
            </div>

            <div class="riga-calcolo">
              <span>Spedizione:</span>
              <span id="checkout-spedizione">Gratuita</span>
            </div>

            <div class="riga-totale">
              <span>Totale:</span>
              <span id="checkout-totale">
                <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
              </span>
            </div>
          </div>

          <!-- Termini e Condizioni -->
          <div class="box-termini">
            <label style="display: flex; align-items: start; font-size: 0.9rem; color: #666;">
              <input type="checkbox" name="accettaTermini" required style="margin-right: 0.5rem; margin-top: 0.2rem;">
              <span>Accetto i <a href="${pageContext.request.contextPath}/pages/termini" class="link-primario">Termini e Condizioni</a>
                    e la <a href="${pageContext.request.contextPath}/pages/privacy" class="link-primario">Privacy Policy</a></span>
            </label>
          </div>

          <!-- Pulsante Conferma -->
          <button type="submit" id="conferma-ordine" class="btn-conferma">
            Conferma Ordine
          </button>

          <a href="${pageContext.request.contextPath}/carrello" class="link-indietro">
            ← Torna al Carrello
          </a>
        </div>
      </form>
    </c:otherwise>
  </c:choose>
</div>

<%@include file="../partials/site/footer.jsp"%>

<script>
  // Attiva/disattiva dettagli carta
  function selezionaMetodoPagamento(metodo) {
    const dettagliCarta = document.getElementById('dettagli-carta');
    const opzioni = document.querySelectorAll('.opzione-pagamento');
    opzioni.forEach(l => l.classList.remove('selezionata'));

    if (metodo === 'carta') {
      dettagliCarta.style.display = 'block';
      opzioni[0]?.classList.add('selezionata');
      document.querySelector('input[name="metodoPagamento"][value="carta"]').checked = true;
    } else {
      dettagliCarta.style.display = 'none';
      opzioni[1]?.classList.add('selezionata');
      document.querySelector('input[name="metodoPagamento"][value="paypal"]').checked = true;
    }
  }

  // Gestione submit form
  document.getElementById('checkout-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const confermaBtn = document.getElementById('conferma-ordine');

    confermaBtn.disabled = true;
    confermaBtn.textContent = 'Elaborazione...';

    fetch('${pageContext.request.contextPath}/checkout/conferma', {
      method: 'POST',
      body: formData
    })
            .then(r => r.json())
            .then(data => {
              if (data.success) {
                window.location.href = '${pageContext.request.contextPath}/checkout/conferma?ordineId=' + data.ordineId;
              } else {
                alert('Errore durante l\'elaborazione dell\'ordine: ' + (data.error || 'Errore sconosciuto'));
                confermaBtn.disabled = false;
                confermaBtn.textContent = 'Conferma Ordine';
              }
            })
            .catch(err => {
              console.error('Errore:', err);
              alert('Errore durante l\'elaborazione dell\'ordine');
              confermaBtn.disabled = false;
              confermaBtn.textContent = 'Conferma Ordine';
            });
  });

  // Formattazione numero carta
  document.querySelector('input[name="numeroCarta"]')?.addEventListener('input', function(e) {
    let v = e.target.value.replace(/\s/g, '').replace(/\D/g, '');
    let f = v.replace(/(.{4})/g, '$1 ').trim();
    if (f.length > 19) f = f.substr(0, 19);
    e.target.value = f;
  });

  // Formattazione scadenza
  document.querySelector('input[name="scadenza"]')?.addEventListener('input', function(e) {
    let v = e.target.value.replace(/\D/g, '');
    if (v.length >= 2) v = v.substr(0, 2) + '/' + v.substr(2, 2);
    e.target.value = v;
  });

  // Solo numeri per CVV
  document.querySelector('input[name="cvv"]')?.addEventListener('input', function(e) {
    e.target.value = e.target.value.replace(/\D/g, '');
  });

  // Cambio opzione pagamento
  document.querySelectorAll('input[name="metodoPagamento"]').forEach(r => {
    r.addEventListener('change', function() {
      selezionaMetodoPagamento(this.value);
    });
  });
</script>

</body>
</html>
