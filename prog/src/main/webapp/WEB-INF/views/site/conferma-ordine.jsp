<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Ordine Confermato - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Ordine Confermato"/>
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div style="max-width: 700px; margin: 2rem auto; padding: 0 1rem;">
  <div style="text-align: center; background: white; padding: 3rem 2rem; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">

    <!-- Icona di successo -->
    <div style="background: linear-gradient(135deg, #28a745, #20c997); color: white; width: 100px; height: 100px;
                    border-radius: 50%; display: flex; align-items: center; justify-content: center;
                    margin: 0 auto 2rem; font-size: 3rem; box-shadow: 0 4px 20px rgba(40, 167, 69, 0.3);">
      ✓
    </div>

    <h1 style="color: var(--primary-light); margin-bottom: 1rem; font-size: 2.5rem;">Ordine Confermato!</h1>

    <p style="color: #666; font-size: 1.2rem; margin-bottom: 2.5rem; line-height: 1.6;">
      Grazie per il tuo acquisto! Il tuo ordine è stato elaborato con successo.
    </p>

    <!-- Box numero ordine -->
    <div style="background: linear-gradient(135deg, #f8f9fa, #e9ecef); padding: 2rem; border-radius: 12px;
                    margin-bottom: 2.5rem; border-left: 5px solid var(--primary-light);">

      <div style="text-align: center;">
        <div style="font-weight: 600; color: #495057; margin-bottom: 1rem; font-size: 1.1rem;">
          Numero Ordine
        </div>
        <div style="color: var(--primary-light); font-size: 2rem; font-weight: bold; margin-bottom: 1rem;">
          #<c:out value="${param.ordineId}" default="N/A"/>
        </div>
        <div style="color: #28a745; font-weight: 600; font-size: 1.1rem;">
          Stato: Confermato
        </div>
      </div>
    </div>

    <!-- Informazioni importanti -->
    <div style="background: #e3f2fd; padding: 2rem; border-radius: 10px; margin-bottom: 2.5rem; text-align: left;">
      <h3 style="color: var(--primary-light); margin-bottom: 1.5rem; text-align: center;">
        Cosa succede ora?
      </h3>
      <div style="display: grid; grid-template-columns: 1fr; gap: 1rem;">
        <div style="display: flex; align-items: center; padding: 0.75rem; background: white; border-radius: 8px;">
          <div style="background: var(--primary-light); color: white; width: 30px; height: 30px; border-radius: 50%;
                                display: flex; align-items: center; justify-content: center; margin-right: 1rem; font-size: 0.9rem; font-weight: bold;">1</div>
          <span style="color: #495057;">Riceverai una email di conferma entro pochi minuti</span>
        </div>
        <div style="display: flex; align-items: center; padding: 0.75rem; background: white; border-radius: 8px;">
          <div style="background: var(--primary-light); color: white; width: 30px; height: 30px; border-radius: 50%;
                                display: flex; align-items: center; justify-content: center; margin-right: 1rem; font-size: 0.9rem; font-weight: bold;">2</div>
          <span style="color: #495057;">Il tuo ordine verrà processato entro 24 ore</span>
        </div>
        <div style="display: flex; align-items: center; padding: 0.75rem; background: white; border-radius: 8px;">
          <div style="background: var(--primary-light); color: white; width: 30px; height: 30px; border-radius: 50%;
                                display: flex; align-items: center; justify-content: center; margin-right: 1rem; font-size: 0.9rem; font-weight: bold;">3</div>
          <span style="color: #495057;">Ti invieremo tutti gli aggiornamenti sulla spedizione</span>
        </div>
        <div style="display: flex; align-items: center; padding: 0.75rem; background: white; border-radius: 8px;">
          <div style="background: var(--primary-light); color: white; width: 30px; height: 30px; border-radius: 50%;
                                display: flex; align-items: center; justify-content: center; margin-right: 1rem; font-size: 0.9rem; font-weight: bold;">4</div>
          <span style="color: #495057;">Consegna prevista in 2-5 giorni lavorativi</span>
        </div>
      </div>
    </div>

    <!-- Pulsanti azione -->
    <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap; margin-bottom: 2rem;">
      <a href="${pageContext.request.contextPath}/utente/profile"
         style="background: var(--primary-light); color: white; padding: 1rem 2rem; text-decoration: none;
                      border-radius: 8px; font-weight: 600; transition: all 0.3s ease; display: inline-block;
                      box-shadow: 0 3px 10px rgba(0, 123, 255, 0.3); min-width: 150px;">
        Il Mio Profilo
      </a>

      <a href="${pageContext.request.contextPath}/pages"
         style="background: #6c757d; color: white; padding: 1rem 2rem; text-decoration: none;
                      border-radius: 8px; font-weight: 600; transition: all 0.3s ease; display: inline-block;
                      box-shadow: 0 3px 10px rgba(108, 117, 125, 0.3); min-width: 150px;">
        Continua lo Shopping
      </a>
    </div>

    <!-- Messaggio di ringraziamento -->
    <div style="background: linear-gradient(135deg, #fff3cd, #ffeaa7); padding: 1.5rem; border-radius: 10px; margin-bottom: 2rem;">
      <p style="color: #856404; font-weight: 500; margin: 0; line-height: 1.6;">
        <strong>Grazie per aver scelto TechVibe!</strong><br>
        La tua fiducia è importante per noi. Ti garantiamo prodotti di qualità e un servizio eccellente.
      </p>
    </div>

    <!-- Supporto -->
    <div style="padding-top: 1.5rem; border-top: 1px solid #dee2e6;">
      <p style="color: #6c757d; font-size: 0.9rem; margin: 0;">
        Hai domande sul tuo ordine?
        <a href="#" style="color: var(--primary-light); text-decoration: none; font-weight: 500;">Contatta il supporto</a>
      </p>
    </div>
  </div>
</div>

<%@include file="../partials/site/footer.jsp"%>

<style>
  @media (max-width: 768px) {
    div[style*="display: flex; gap: 1rem; justify-content: center"] {
      flex-direction: column !important;
      align-items: center;
    }

    h1 {
      font-size: 2rem !important;
    }

    div[style*="width: 100px; height: 100px"] {
      width: 80px !important;
      height: 80px !important;
      font-size: 2.5rem !important;
    }

    div[style*="font-size: 2rem"] {
      font-size: 1.5rem !important;
    }
  }

  /* Hover effects */
  a[style*="background: var(--primary-light)"]:hover {
    background: #0056b3 !important;
    transform: translateY(-2px);
  }

  a[style*="background: #6c757d"]:hover {
    background: #545b62 !important;
    transform: translateY(-2px);
  }

  /* Animazione icona successo */
  @keyframes checkmark {
    0% { transform: scale(0); }
    50% { transform: scale(1.2); }
    100% { transform: scale(1); }
  }

  div[style*="background: linear-gradient(135deg, #28a745, #20c997)"] {
    animation: checkmark 0.6s ease-out;
  }
</style>

</body>
</html>