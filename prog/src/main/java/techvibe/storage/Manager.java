package techvibe.storage;

import javax.sql.DataSource;

public abstract class Manager {

    protected final DataSource source;

    protected Manager(DataSource source) {
        this.source = source;
    }
}
