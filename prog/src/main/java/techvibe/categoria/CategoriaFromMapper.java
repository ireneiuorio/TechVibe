package techvibe.categoria;

import jakarta.servlet.http.HttpServletRequest;

public class CategoriaFromMapper {

    public Categoria map(HttpServletRequest request, boolean update)
    {
        Categoria categoria=new Categoria();

        if(update){
            categoria.setIdCategoria(Integer.parseInt(request.getParameter("id")));

        }
        categoria.setNomeCategoria(request.getParameter("label"));

        return categoria;
    }
}
