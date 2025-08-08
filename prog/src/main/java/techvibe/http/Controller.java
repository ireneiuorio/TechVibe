package techvibe.http;


import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;


import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.print.DocFlavor;
import javax.sql.DataSource;
import java.io.File;

public abstract class Controller extends HttpServlet {

    @Resource(name = "jdbc/Grocer")

    protected static DataSource source;

    protected String getPath(HttpServletRequest req)
    {
        return req.getPathInfo()!=null?req.getPathInfo():"/";
    }

    protected String view(String viewPath)
    {
        String basePath = getServletContext().getInitParameter("basePath");
        String engine= getServletContext().getInitParameter("engine");
        return basePath+viewPath+engine;
    }

    protected String back(HttpServletRequest request)
    {
        return request.getServletPath()+ request.getPathInfo();

    }

    /*
    protected void validate(RequestValidator validator) throws InvalidRequestException{
        if(validator.hasErrors())
        {
            throw new InvalidRequestException("Validation Error", validator.getErrors());
        }
    }
*/
    protected String getUploadPath()
    {
        return System.getenv("CATALINA_HOME")+ File.separator+"webapps"+File.separator+"TechVibe"+File.separator+"uploads"+File.separator;

    }
}
