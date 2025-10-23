package techvibe.Model.storage;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface ResultSetExtractor <B>{

    //MAPPER:prende in inuput un result set e restituisce un generico bin, B entità da mappare
   B extract(ResultSet resultSet)throws SQLException;


}
