<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Checkout - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Checkout"/>
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div style="max-width: 1200px; margin: 2rem auto; padding: 0 1rem;">
  <h1 style="color: var(--primary-light); margin-bottom: 2rem; text-align: center;">Checkout</h1>

  <c:choose>
    <c:when test="${empty carrello.items}">
      <!-- Carrello vuoto -->
      <div style="text-align: center; padding: 3rem; background: #f8f9fa; border-radius: 15px;">
        <h2 style="color: #666; margin-bottom: 1rem;">Il tuo carrello è vuoto</h2>
        <p style="color: #888; margin-bottom: 2rem;">Aggiungi alcuni prodotti per procedere al checkout!</p>
        <a href="${pageContext.request.contextPath}/pages"
           style="display: inline-block; background: var(--primary-light); color: white; padding: 1rem 2rem;
                  text-decoration: none; border-radius: 8px; font-weight: 500;">
          Continua lo Shopping
        </a>
      </div>
    </c:when>

    <c:otherwise>
      <!-- Checkout Form -->
      <form id="checkout-form" style="display: grid; grid-template-columns: 1fr 400px; gap: 2rem;">

        <!-- Sezione Dati Spedizione e Pagamento -->
        <div>
          <!-- Dati di Spedizione -->
          <div style="background: white; padding: 2rem; border-radius: 12px; margin-bottom: 2rem; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
            <h3 style="margin: 0 0 1.5rem 0; color: var(--primary-light); display: flex; align-items: center;">
              <span style="background: var(--primary-light); color: white; width: 24px; height: 24px; border-radius: 50%;
                           display: flex; align-items: center; justify-content: center; margin-right: 0.75rem; font-size: 0.9rem;">1</span>
              Indirizzo di Spedizione
            </h3>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
              <div>
                <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Nome *</label>
                <input type="text" name="nome" required value="${utente.nome != null ? utente.nome : ''}"
                       style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
              </div>
              <div>
                <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Cognome *</label>
                <input type="text" name="cognome" required value="${utente.cognome != null ? utente.cognome : ''}"
                       style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
              </div>
            </div>

            <!-- SOLO INDIRIZZO -->
            <div style="display: grid; grid-template-columns: 1fr; gap: 1rem; margin-bottom: 1rem;">
              <div>
                <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Indirizzo *</label>
                <input type="text" name="indirizzo" required placeholder="Via, numero civico, città, CAP"
                       value="${utente.indirizzoSpedizione != null ? utente.indirizzoSpedizione : ''}"
                       style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
              </div>
            </div>

            <div style="margin-bottom: 1rem;">
              <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Telefono *</label>
              <input type="tel" name="telefono" required value="${utente.telefono != null ? utente.telefono : ''}"
                     style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
            </div>
          </div>

          <!-- Metodo di Pagamento -->
          <div style="background: white; padding: 2rem; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
            <h3 style="margin: 0 0 1.5rem 0; color: var(--primary-light); display: flex; align-items: center;">
              <span style="background: var(--primary-light); color: white; width: 24px; height: 24px; border-radius: 50%;
                           display: flex; align-items: center; justify-content: center; margin-right: 0.75rem; font-size: 0.9rem;">2</span>
              Metodo di Pagamento
            </h3>

            <div style="margin-bottom: 1.5rem;">
              <label style="display: flex; align-items: center; padding: 1rem; border: 1px solid #ddd; border-radius: 8px; cursor: pointer; margin-bottom: 0.75rem;"
                     onclick="selezionaMetodoPagamento('carta')">
                <input type="radio" name="metodoPagamento" value="carta" checked style="margin-right: 0.75rem;">
                <div style="flex: 1;">
                  <div style="font-weight: 500;">Carta di Credito/Debito</div>
                  <div style="font-size: 0.9rem; color: #666;">Visa, Mastercard, American Express</div>
                </div>
              </label>

              <label style="display: flex; align-items: center; padding: 1rem; border: 1px solid #ddd; border-radius: 8px; cursor: pointer;"
                     onclick="selezionaMetodoPagamento('paypal')">
                <input type="radio" name="metodoPagamento" value="paypal" style="margin-right: 0.75rem;">
                <div style="flex: 1;">
                  <div style="font-weight: 500;">PayPal</div>
                  <div style="font-size: 0.9rem; color: #666;">Paga con il tuo account PayPal</div>
                </div>
              </label>
            </div>

            <!-- Dettagli Carta (visibile solo se selezionata) -->
            <div id="dettagli-carta" style="display: block;">
              <div style="margin-bottom: 1rem;">
                <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Numero Carta *</label>
                <input type="text" name="numeroCarta" placeholder="1234 5678 9012 3456" maxlength="19"
                       style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
              </div>

              <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem;">
                <div>
                  <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Scadenza *</label>
                  <input type="text" name="scadenza" placeholder="MM/AA" maxlength="5"
                         style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
                </div>
                <div>
                  <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">CVV *</label>
                  <input type="text" name="cvv" placeholder="123" maxlength="4"
                         style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
                </div>
                <div>
                  <label style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Intestatario *</label>
                  <input type="text" name="intestatario" placeholder="Nome Cognome"
                         style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem;">
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Riepilogo Ordine -->
        <div style="background: white; padding: 2rem; border-radius: 12px; height: fit-content; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
          <h3 style="margin: 0 0 1.5rem 0; color: var(--primary-light); display: flex; align-items: center;">
            <span style="background: var(--primary-light); color: white; width: 24px; height: 24px; border-radius: 50%;
                         display: flex; align-items: center; justify-content: center; margin-right: 0.75rem; font-size: 0.9rem;">3</span>
            Riepilogo Ordine
          </h3>

          <!-- Lista Prodotti -->
          <div style="border: 1px solid #eee; border-radius: 8px; margin-bottom: 1.5rem; max-height: 300px; overflow-y: auto;">
            <c:forEach var="item" items="${carrello.items}" varStatus="status">
              <div style="display: flex; gap: 0.75rem; padding: 1rem; ${!status.last ? 'border-bottom: 1px solid #eee;' : ''}">
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
                  <img src="${imgSrc}" alt="${item.prodotto.marca} ${item.prodotto.modello}"
                       style="width: 60px; height: 60px; object-fit: cover; border-radius: 6px;">
                </div>

                <!-- Dettagli prodotto -->
                <div style="flex: 1; min-width: 0;">
                  <div style="font-weight: 500; font-size: 0.9rem; margin-bottom: 0.25rem; color: var(--primary-light);">
                      ${item.prodotto.marca} ${item.prodotto.modello}
                  </div>
                  <div style="font-size: 0.8rem; color: #666; margin-bottom: 0.25rem;">
                    <c:if test="${not empty item.prodotto.storage}">Storage: ${item.prodotto.storage}GB</c:if>
                    <c:if test="${not empty item.prodotto.colore}"> • ${item.prodotto.colore}</c:if>
                  </div>
                  <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.9rem;">
                    <span>Qta: ${item.quantita}</span>
                    <span style="font-weight: 500;">
                      <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                    </span>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>

          <!-- Calcoli -->
          <div style="border-top: 1px solid #eee; padding-top: 1rem;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
              <span>Subtotale:</span>
              <span id="checkout-subtotale">
                <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
              </span>
            </div>

            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
              <span>Spedizione:</span>
              <span id="checkout-spedizione">Gratuita</span>
            </div>

            <div style="display: flex; justify-content: space-between; font-size: 1.1rem; font-weight: bold;
                        padding-top: 0.75rem; border-top: 2px solid var(--primary-light); color: var(--primary-light);">
              <span>Totale:</span>
              <span id="checkout-totale">
                <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
              </span>
            </div>
          </div>

          <!-- Termini e Condizioni -->
          <div style="margin: 1.5rem 0;">
            <label style="display: flex; align-items: start; font-size: 0.9rem; color: #666;">
              <input type="checkbox" name="accettaTermini" required style="margin-right: 0.5rem; margin-top: 0.2rem;">
              <span>Accetto i <a href="#" style="color: var(--primary-light);">Termini e Condizioni</a>
                    e la <a href="#" style="color: var(--primary-light);">Privacy Policy</a></span>
            </label>
          </div>

          <!-- Pulsante Conferma -->
          <button type="submit" id="conferma-ordine"
                  style="width: 100%; background: var(--primary-light); color: white; border: none;
                         padding: 1rem; border-radius: 8px; font-size: 1.1rem; font-weight: 500; cursor: pointer;">
            Conferma Ordine
          </button>

          <a href="${pageContext.request.contextPath}/carrello"
             style="display: block; text-align: center; margin-top: 1rem; color: var(--primary-light);
                    text-decoration: none; font-size: 0.9rem;">
            ← Torna al Carrello
          </a>
        </div>
      </form>
    </c:otherwise>
  </c:choose>
</div>

<%@include file="../partials/site/footer.jsp"%>

<style>
  /* Responsive per mobile */
  @media (max-width: 768px) {
    #checkout-form {
      grid-template-columns: 1fr !important;
    }

    div[style*="grid-template-columns: 1fr 1fr"] {
      grid-template-columns: 1fr !important;
    }

    /* il blocco indirizzo è già 1 colonna */
    div[style*="grid-template-columns: 1fr 1fr 1fr"] {
      grid-template-columns: 1fr !important;
    }
  }

  /* Hover effects */
  button[type="submit"]:hover {
    background: #0056b3 !important;
  }

  input:focus, textarea:focus {
    outline: none;
    border-color: var(--primary-light) !important;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
  }

  label:has(input[type="radio"]):hover {
    border-color: var(--primary-light) !important;
  }

  label:has(input[type="radio"]:checked) {
    border-color: var(--primary-light) !important;
    background: rgba(0, 123, 255, 0.05);
  }
</style>

<script>
  // Gestione metodi di pagamento
  function selezionaMetodoPagamento(metodo) {
    const dettagliCarta = document.getElementById('dettagli-carta');
    if (metodo === 'carta') {
      dettagliCarta.style.display = 'block';
    } else {
      dettagliCarta.style.display = 'none';
    }
  }

  // Gestione submit form
  document.getElementById('checkout-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const confermaBtn = document.getElementById('conferma-ordine');

    // Disabilita pulsante
    confermaBtn.disabled = true;
    confermaBtn.textContent = 'Elaborazione...';

    // Invia richiesta al servlet
    fetch('${pageContext.request.contextPath}/checkout/conferma', {
      method: 'POST',
      body: formData
    })
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                window.location.href = '${pageContext.request.contextPath}/checkout/conferma?ordineId=' + data.ordineId;
              } else {
                alert('Errore durante l\'elaborazione dell\'ordine: ' + (data.error || 'Errore sconosciuto'));
                confermaBtn.disabled = false;
                confermaBtn.textContent = 'Conferma Ordine';
              }
            })
            .catch(error => {
              console.error('Errore:', error);
              alert('Errore durante l\'elaborazione dell\'ordine');
              confermaBtn.disabled = false;
              confermaBtn.textContent = 'Conferma Ordine';
            });
  });

  // Formattazione automatica carta di credito
  document.querySelector('input[name="numeroCarta"]')?.addEventListener('input', function(e) {
    let value = e.target.value.replace(/\s/g, '').replace(/\D/g, '');
    let formattedValue = value.replace(/(.{4})/g, '$1 ').trim();
    if (formattedValue.length > 19) formattedValue = formattedValue.substr(0, 19);
    e.target.value = formattedValue;
  });

  // Formattazione scadenza
  document.querySelector('input[name="scadenza"]')?.addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length >= 2) {
      value = value.substr(0, 2) + '/' + value.substr(2, 2);
    }
    e.target.value = value;
  });

  // Solo numeri per CVV
  document.querySelector('input[name="cvv"]')?.addEventListener('input', function(e) {
    e.target.value = e.target.value.replace(/\D/g, '');
  });

  // Gestione radio buttons
  document.querySelectorAll('input[name="metodoPagamento"]').forEach(radio => {
    radio.addEventListener('change', function() {
      selezionaMetodoPagamento(this.value);
    });
  });
</script>

</body>
</html>