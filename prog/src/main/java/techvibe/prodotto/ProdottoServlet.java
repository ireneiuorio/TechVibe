package techvibe.prodotto;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/prodotti/*")
public class ProdottoServlet extends HttpServlet {

    private String getPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        return (path == null) ? "/" : path;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = getPath(request);

        switch (path) {
            case "/":
            case "/prodotti":
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotti.jsp").forward(request, response);
                break;
            case "/show":
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotto.jsp").forward(request, response);
                break;
            case "/create":
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotto.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotti.jsp").forward(request, response);
                break;
        }
    }
}
