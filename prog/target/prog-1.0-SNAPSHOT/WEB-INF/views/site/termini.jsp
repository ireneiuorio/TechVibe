<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="it">
<head>
  <title>Termini e Condizioni - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Termini e Condizioni"/>
    <jsp:param name="styles" value="site,privacy"/>
  </jsp:include>
</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>


  <section class="privacy container">
    <h1>Termini e Condizioni</h1>


    <div class="card privacy">
      <h2>1. Oggetto</h2>
      <p>
        I presenti Termini e Condizioni disciplinano l’uso del sito <strong>TechVibe</strong> e l’acquisto dei prodotti
        offerti. Accedendo o utilizzando il sito, l’utente accetta integralmente le presenti condizioni.
      </p>
    </div>

    <div class="card privacy">
      <h2>2. Registrazione e account</h2>
      <p>
        Per effettuare acquisti potrebbe essere richiesta la registrazione. L’utente si impegna a fornire dati veritieri
        e a custodire con diligenza le proprie credenziali, restando responsabile delle attività svolte tramite l’account.
      </p>
    </div>

    <div class="card privacy">
      <h2>3. Prodotti e disponibilità</h2>
      <p>
        Il catalogo viene aggiornato regolarmente; tuttavia la disponibilità può variare. In caso di sopravvenuta
        indisponibilità, l’ordine potrà essere annullato con tempestiva comunicazione all’utente e rimborso di quanto pagato.
      </p>
    </div>

    <div class="card privacy">
      <h2>4. Prezzi e pagamenti</h2>
      <p>
        I prezzi sono espressi in Euro e, salvo diversa indicazione, comprensivi di IVA. I pagamenti avvengono tramite
        i metodi indicati sul sito in modalità sicura. In presenza di anomalie o sospetti di frode, l’ordine potrà essere rifiutato o annullato.
      </p>
    </div>

    <div class="card privacy">
      <h2>5. Spedizioni e consegne</h2>
      <p>
        Le spedizioni sono affidate a corrieri convenzionati. I tempi di consegna sono indicativi e possono variare in base
        a destinazione e disponibilità. Eventuali ritardi imputabili ai vettori non costituiscono responsabilità di TechVibe.
      </p>
    </div>

    <div class="card privacy">
      <h2>6. Diritto di recesso</h2>
      <p>
        Il consumatore ha diritto di recedere dall’acquisto entro 14 giorni dalla ricezione dei beni, ai sensi del D.Lgs. 206/2005.
        Il rimborso avverrà dopo la verifica dell’integrità dei prodotti resi. Possono essere esclusi dal recesso i beni sigillati
        aperti dopo la consegna quando non si prestano a essere restituiti per motivi igienici o connessi alla protezione della salute.
      </p>
    </div>

    <div class="card privacy">
      <h2>7. Garanzia legale di conformità</h2>
      <p>
        I prodotti sono coperti dalla garanzia legale di 24 mesi per difetti di conformità prevista dal Codice del Consumo.
        Per attivarla è necessaria la prova d’acquisto e la segnalazione del difetto entro i termini di legge.
      </p>
    </div>

    <div class="card privacy">
      <h2>8. Limitazioni di responsabilità</h2>
      <p>
        Salvo i casi di dolo o colpa grave, TechVibe non risponde di danni indiretti, consequenziali o perdita di profitto
        derivanti dall’uso dei prodotti o dall’impossibilità di utilizzarli.
      </p>
    </div>

    <div class="card privacy">
      <h2>9. Privacy</h2>
      <p>
        Il trattamento dei dati personali è disciplinato dalla nostra
        <a href="${pageContext.request.contextPath}/site/privacy">Privacy Policy</a>.
      </p>
    </div>

    <div class="card privacy">
      <h2>10. Legge applicabile e foro competente</h2>
      <p>
        I presenti Termini sono regolati dalla legge italiana. Per le controversie con consumatori è competente il foro del luogo
        di residenza o domicilio del consumatore; per acquisti professionali è competente il Foro di Napoli.
      </p>
    </div>

    <div class="card privacy">
      <h2>11. Modifiche ai Termini</h2>
      <p>
        Potremo aggiornare i presenti Termini in qualsiasi momento. Le modifiche saranno pubblicate su questa pagina
        con indicazione della data di aggiornamento.
      </p>
    </div>
  </section>

  <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
