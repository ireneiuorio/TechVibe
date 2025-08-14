package techvibe.http;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "PageServlet", value = "/pages")
public class PageServlet extends Controller implements ErrorHandler {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch (path) {
                case "dashboard":
                    authorize(request.getSession(false));
                    request.getRequestDispatcher("/WEB-INF/views/crm/home.jsp").forward(request, response);
                    break;

                case "/":
                    request.getRequestDispatcher("/WEB-INF/views/site/home.jsp");
                    break;
                default:
                    notFound();

            }

        } catch (InvalidRequestException ex) {
            log(ex.getMessage());
            ex.handle(request, response);
        }
    }
    }

