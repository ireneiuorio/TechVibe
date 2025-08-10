package techvibe.utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/utente/*")
public class UtenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

        switch (path) {


            case "/secret":
                request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
                return;
            case "/":
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().println("OK /utente/");
                return;



            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found: " + path);
        }
    }



@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path =(request.getPathInfo()!= null)?request.getPathInfo():"/";
        switch(path)
        {
            case "/signin": //login cliente ricerca nel db
                break;
            case "/secret":
                break;
            case "/create":
                break;
            case "/update":
                break;
            case "/logout":
                break;
            case "/signup":
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"Operazione non consentita");

        }
    }
}
