package techvibe.categoria;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import techvibe.components.Alert;
import techvibe.components.Paginator;
import techvibe.http.CommonValidator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.ordine.Ordine;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CategoriaServlet", value = "/categorie/*")
public class CategoriaServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;


  private CategoriaDao<SQLException> categoriaDao;

  public void init() throws ServletException{
      super.init();
      categoriaDao= new SqlCategoriaDao(source);
  }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

            switch(path){
                case"/":
                    authorize(request.getSession(false));
                    int intPage=parsePage(request);
                    Paginator paginator =new Paginator(intPage,10);

                    List<Categoria> categorie=categoriaDao.fetchCategorie(paginator);
                    int size=categoriaDao.countAll();
                    request.setAttribute("categorie",categorie);
                    request.setAttribute("pages",paginator.getPages(size));
                    request.getRequestDispatcher("/WEB-INF/views/crm/categorie.jsp").forward(request,response);
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


      try{
          String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
          switch (path){
              case "/create":
                  authorize(request.getSession(false));
                  request.setAttribute("back","/WEB-INF/views/crm/categorie.jsp");
                  validate(CategoriaValidator.validatorForm(request,false));
                  Categoria categoria=new CategoriaFromMapper().map(request,false);
                  if(categoriaDao.createCategoria(categoria)){
                      request.setAttribute("alert",new Alert(List.of("Categoria Creata"),"success"));
                      request.getRequestDispatcher("/WEB-INF/views/crm/categorie.jsp").forward(request,response);
                  }
                  else {
                      internalError();
                  }
                  break;

              case "/update":
                  authorize(request.getSession(false));
                  request.setAttribute("back","/WEB-INF/views/crm/categorie.jsp");
                  Categoria updateCategoria=new CategoriaFromMapper().map(request,true);
                  request.setAttribute("categoria",updateCategoria);
                  validate(CategoriaValidator.validatorForm(request,true));
                  if(categoriaDao.updateCategoria(updateCategoria))
                  {
                      request.setAttribute("alert",new Alert(List.of("Categoria Aggiornata"),"success"));
                      request.getRequestDispatcher("/WEB-INF/views/crm/categorie.jsp").forward(request,response);

                  }else{
                      internalError();
                  }break;
              default:
                  notAllowed();

          }


      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
    }
}