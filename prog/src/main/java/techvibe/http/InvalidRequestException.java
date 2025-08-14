package techvibe.http;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import techvibe.components.Alert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InvalidRequestException extends RuntimeException {

    private final List<String> errors;
    private final int errorCode;

    public InvalidRequestException(String mesage, List<String> errors, int errorCode) {
        super(mesage);
        this.errors = errors;
        this.errorCode = errorCode;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (errorCode) {
            case HttpServletResponse.SC_BAD_REQUEST:
                request.setAttribute("alert", new Alert(errors, "danger"));
                String backPath = (String) request.getAttribute("back");
                response.setStatus(errorCode);
                request.getRequestDispatcher(backPath).forward(request, response);
                break;

            default:
                response.sendError(errorCode, errors.get(0));

        }


    }

    public List<String> getErrors(){
        return errors;
    }

}
