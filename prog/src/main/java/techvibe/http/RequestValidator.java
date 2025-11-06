package techvibe.http;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpServletRequest;

public class RequestValidator {

    private final List<String> errors;
    private final HttpServletRequest request;

    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$"); //numero intero positivo
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^(-)?(0|[1-9]\\d+)\\.\\d+$"); //numero decimale

    public RequestValidator(HttpServletRequest request) { //prende la request per accedere ai parameteri
        this.errors = new ArrayList<>();
        this.request = request;

    }

    //Controlla se la lista errors non è più vuota
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    //Ti da la lista di tutti i messaggi di errore accumulati
    public List<String> getErrors() {
        return errors;
    }

    //Verifica una condizione
    private boolean gatherError(boolean condition, String message) {
        if (condition) {
            return true;
        } else {
            errors.add(message);
            return false;
        }
    }

    //Valore richiesto
    private boolean required(String value) {
        return value != null && !value.isBlank();
    }

    //prende il nome di un parametro prende un pattern controlla che il campop non sia vuoto e che rispetti le regole
    public boolean assertMatch(String value, Pattern regexp, String msg) {
        String param = request.getParameter(value);
        boolean condition = required(param) && regexp.matcher(param).matches();
        return gatherError(condition, msg);
    }

    public boolean assertInt(String value, String msg) {
        return assertMatch(value, INT_PATTERN, msg);

    }

    public boolean assertDouble(String value, String msg) {
        return assertMatch(value, DOUBLE_PATTERN, msg);
    }

    public boolean assertEmail(String value, String msg) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
        return assertMatch(value, pattern, msg);
    }









}