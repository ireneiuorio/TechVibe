<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="it">
<head>
    <title>Privacy Policy - TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Privacy Policy"/>
        <jsp:param name="styles" value="site,privacy"/>
    </jsp:include>
    <meta name="robots" content="noindex">
</head>
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <section class="privacy container">
        <h1>Privacy</h1>


        <div class="card privacy">
            <h2>1. Titolare del trattamento</h2>
            <p>
                <strong>TechVibe</strong> – Via xxxxxxxxxxxxxx, Italia<br>
                Email: mailto:privacy@techvibe.it – PEC <em>pec@techvibe.it</em><br>
                Telefono:xxxxxxxxxx
            </p>
        </div>

        <div class="card privacy">
            <h2>2. Tipologie di dati trattati</h2>
            <p>
                Trattiamo diverse categorie di dati personali, tra cui:
                <strong>dati identificativi</strong> (nome, cognome, indirizzo, telefono),
                <strong>dati di contatto</strong> (email ed eventuali account social utilizzati per il login),
                <strong>dati di acquisto</strong> (prodotti, quantità, importi e metodo di pagamento; non conserviamo i numeri completi delle carte, ma solo token o ID di transazione),
                <strong>dati di spedizione e fatturazione</strong>,
                <strong>dati tecnici</strong> (log, indirizzo IP, user-agent e cookie/ID dispositivo)
                e <strong>contenuti volontari</strong> forniti dall’utente, come messaggi inviati tramite il form contatti o recensioni.
            </p>
        </div>

        <div class="card privacy">
            <h2>3. Finalità e basi giuridiche (art. 6 GDPR)</h2>
            <p>
                I dati personali vengono trattati per diverse finalità sulla base delle condizioni di liceità previste dal GDPR:
                per la <strong>gestione degli ordini e delle consegne</strong>, in quanto necessario all’esecuzione di un contratto;
                per l’<strong>assistenza clienti</strong>, anche in fase precontrattuale o contrattuale;
                per adempiere a <strong>obblighi legali</strong> di natura fiscale e contabile;
                per attività di <strong>prevenzione delle frodi e sicurezza</strong>, basate sul legittimo interesse del titolare;
                per l’invio di <strong>comunicazioni promozionali</strong>, esclusivamente previo consenso esplicito e sempre revocabile;
                e infine per <strong>analisi statistiche aggregate</strong> effettuate su dati anonimizzati, anch’esse fondate sul legittimo interesse.
            </p>
        </div>


        <div class="card privacy">
            <h2>4. Modalità del trattamento</h2>
            <p>I dati sono trattati con strumenti elettronici e misure tecnico‑organizzative adeguate per garantire riservatezza, integrità e disponibilità.</p>
        </div>

        <div class="card privacy">
            <h2>5. Conservazione dei dati</h2>
            <p>
                I dati vengono conservati per periodi differenti in base alla finalità del trattamento:
                i <strong>dati relativi ad account e ordini</strong> sono mantenuti per tutta la durata del rapporto e fino a 10 anni per adempiere agli obblighi civilistici e fiscali;
                i <strong>dati di assistenza</strong> vengono conservati fino a 24 mesi dalla chiusura della richiesta;
                i <strong>dati utilizzati per finalità di marketing</strong> sono conservati fino a revoca del consenso oppure per un massimo di 24 mesi dall’ultimo contatto significativo;
                infine, i <strong>log tecnici</strong> sono conservati da 6 a 24 mesi, salvo esigenze particolari di sicurezza o antifrode.
            </p>
        </div>

        <div class="card privacy">
            <h2>6. Destinatari e categorie di destinatari</h2>
            <p>Potremmo comunicare i dati a: corrieri/spedizionieri, provider di pagamento, hosting/cloud, CRM, consulenti contabili/legali, prevenzione frodi, piattaforme newsletter (se usate). Tali soggetti operano come <em>Responsabili del trattamento</em> ex art. 28 o autonomi Titolari quando applicabile.</p>
        </div>

        <div class="card privacy">
            <h2>7. Trasferimenti extra UE</h2>
            <p>Se alcuni fornitori hanno sedi extra SEE, il trasferimento avviene secondo gli artt. 44‑49 GDPR (es. Clausole Contrattuali Standard) e con misure supplementari quando necessarie. Informazioni dettagliate sono disponibili su richiesta.</p>
        </div>

        <div class="card privacy">
            <h2>8. Cookie e tecnologie simili</h2>
            <p>Utilizziamo cookie tecnici e, previo consenso, cookie analitici/profilazione. Per dettagli e preferenze consulta la <a href="${pageContext.request.contextPath}/site/cookie">Cookie Policy</a> o <a href="#" id="open-consent">gestisci i consensi</a>.</p>
            <div class="cookie-table">
            </div>
        </div>

        <div class="card privacy">
            <h2>9. Diritti dell’interessato (artt. 15–22 GDPR)</h2>
            <p>Hai diritto di accesso, rettifica, cancellazione, limitazione, portabilità, opposizione e revoca del consenso in ogni momento, senza pregiudicare la liceità del trattamento basata sul consenso prima della revoca.</p>
            <p>Per esercitare i diritti: scrivi a <a href="mailto:privacy@techvibe.it">privacy@techvibe.it</a>. Hai anche diritto di proporre reclamo al <a href="https://www.garanteprivacy.it/" target="_blank" rel="nofollow noopener">Garante per la protezione dei dati personali</a>.</p>
        </div>

        <div class="card privacy">
            <h2>10. Sicurezza</h2>
            <p>Adottiamo misure quali cifratura in transito (HTTPS), segregazione degli ambienti, controllo accessi e backup periodici.</p>
        </div>

        <div class="card privacy">
            <h2>11. Minori</h2>
            <p>Il sito non è destinato a minori di 14 anni. Se ritieni che un minore ci abbia fornito dati, contattaci per rimozione.</p>
        </div>

        <div class="card privacy">
            <h2>12. Modifiche alla presente informativa</h2>
            <p>Potremmo aggiornare la presente informativa. Le modifiche saranno pubblicate su questa pagina con data di aggiornamento.</p>
        </div>


    </section>

    <%@ include file="../partials/site/footer.jsp" %>
</main>


</body>
</html>
