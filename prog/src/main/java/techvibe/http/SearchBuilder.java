package techvibe.http;

import jakarta.servlet.http.HttpServletRequest;
import techvibe.Model.search.Condition;

import java.util.List;

public interface SearchBuilder {
    List<Condition> buildSearch(HttpServletRequest request);
}
