package techvibe.Model.storage;

public abstract class TableQuery {

    protected final String table;


    protected TableQuery(String table) {
        this.table = table;
    }
}
