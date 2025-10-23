package techvibe.Model.storage;

import techvibe.Model.search.Condition;

import java.util.List;
import java.util.StringJoiner;

public class QueryBuilder {

    private final String table;
    private final String alias;
    private final StringBuilder query;
    private static final String QM = "?";

    public QueryBuilder(String table, String alias) {
        this.table = table;
        this.alias = alias;
        this.query = new StringBuilder();
    }

    public String generateQuery() {
        String generatedQuery = query.toString().trim();
        query.setLength(0);
        return generatedQuery;
    }

    public QueryBuilder select(String... fields) {
        query.append("SELECT ");
        if (fields.length == 0) {
            query.append("* ");
        } else {
            StringJoiner commaJoiner = new StringJoiner(", ");
            for (String field : fields) {
                commaJoiner.add(alias + "." + field);
            }
            query.append(commaJoiner).append(" ");
        }
        query.append("FROM ").append(table).append(" AS ").append(alias).append(" ");
        return this;
    }

    public QueryBuilder where(String condition) {
        query.append("WHERE ").append(condition).append(" ");
        return this;
    }

    public QueryBuilder where(){
        query.append("WHERE");
        return this;
    }

    public QueryBuilder search(List<Condition> conditions) {
        if (conditions.isEmpty()) {
            return this;
        }

        StringJoiner searchJoiner = new StringJoiner(" AND ");
        for (Condition cn : conditions) {
            // Costruisce: alias.nomeColonna OPERATORE ?
            String conditionStr = String.format("%s.%s %s %s",
                    alias,
                    cn.getName(),
                    cn.getOperator().toString(),
                    QM
            );
            searchJoiner.add(conditionStr);
        }
        query.append(" ").append(searchJoiner).append(" ");
        return this;
    }

    public QueryBuilder insert(String... fields) {
        query.append("INSERT INTO ").append(table).append(" ");
        StringJoiner commaJoiner = new StringJoiner(", ", "(", ")");
        for (String field : fields) {
            commaJoiner.add(field);
        }
        query.append(commaJoiner).append(" VALUES ");
        StringJoiner qmJoiner = new StringJoiner(", ", "(", ")");
        for (int i = 0; i < fields.length; i++) {
            qmJoiner.add(QM);
        }
        query.append(qmJoiner).append(" ");
        return this;
    }

    public QueryBuilder delete() {
        query.append("DELETE FROM ").append(table).append(" ");
        return this;
    }

    public QueryBuilder update(String... fields) {
        query.append("UPDATE ").append(table).append(" SET ");
        StringJoiner commaJoiner = new StringJoiner(", ");
        for (String field : fields) {
            commaJoiner.add(field + " = " + QM);
        }
        query.append(commaJoiner).append(" ");
        return this;
    }

    public QueryBuilder limit(boolean withOffset) {
        query.append("LIMIT ").append(QM);
        if (withOffset) {
            query.append(", ").append(QM);
        }
        query.append(" ");
        return this;
    }

    public QueryBuilder innerJoin(String joinedTable, String joinedAlias) {
        query.append("INNER JOIN ").append(joinedTable).append(" ").append(joinedAlias).append(" ");
        return this;
    }

    public QueryBuilder outerJoin(boolean isLeft, String joinedTable, String joinedAlias) {
        String direction = isLeft ? "LEFT JOIN " : "RIGHT JOIN ";
        query.append(direction).append(joinedTable).append(" ").append(joinedAlias).append(" ");
        return this;
    }

    public QueryBuilder on(String condition) {
        query.append("ON ").append(condition).append(" ");
        return this;
    }
}
