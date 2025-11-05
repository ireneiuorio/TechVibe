package techvibe.Model.storage;

import techvibe.Model.search.Condition;

import java.util.List;
import java.util.StringJoiner;

//Costruisce tutta la query, gli operatori e la clausola
public class QueryBuilder {

    private final String table;                         // Nome della tabella principale su cui operare (es. "utenti")
    private final String alias;                         // Alias della tabella (es. "u")
    private final StringBuilder query;                  // Accumulatore della query SQL in costruzione
    private static final String QM = "?";               // Segnaposto per parametri dei PreparedStatement

    public QueryBuilder(String table, String alias) {   // Costruttore: richiede tabella e alias
        this.table = table;                             // Assegna il nome tabella al campo interno
        this.alias = alias;                             // Assegna l'alias al campo interno
        this.query = new StringBuilder();               // Inizializza il buffer che accumula l’SQL
    }

    public String generateQuery() {                     // Restituisce la query generata e resetta il builder
        String generatedQuery = query.toString().trim();// Converte a stringa e rimuove spazi in eccesso ai bordi
        query.setLength(0);                             // Reset: svuota il buffer per riuso dell’istanza
        return generatedQuery;                          // Ritorna la query finale
    }

    public QueryBuilder select(String... fields) {      // Costruisce la SELECT
        query.append("SELECT ");                        // Aggiunge "SELECT "
        if (fields.length == 0) {                       // Se non sono stati passati campi...
            query.append("* ");                         // ...usa "*" (tutti i campi)
        } else {                                        // Altrimenti
            StringJoiner commaJoiner = new StringJoiner(", "); // Joiner con virgole e spazio
            for (String field : fields) {               // Per ogni campo richiesto
                commaJoiner.add(alias + "." + field);   // Prefissa l'alias: "u.campo"
            }
            query.append(commaJoiner).append(" ");      // Aggiunge "u.c1, u.c2, ..." e uno spazio finale
        }
        query.append("FROM ").append(table)             // Aggiunge "FROM <tabella>"
                .append(" AS ").append(alias).append(" "); // ...con "AS <alias>" e spazio finale
        return this;                                    // Ritorna il builder per chaining
    }

    public QueryBuilder where(String condition) {       // Variante WHERE con condizione già pronta
        query.append("WHERE ").append(condition).append(" "); // "WHERE <condizione> "
        return this;                                    // Chaining
    }

    public QueryBuilder where(){                        // Variante WHERE “aperta” (senza condizione)
        query.append("WHERE");                          // Aggiunge solo "WHERE" (senza spazio)
        return this;                                    // Pensata per essere seguita da search(...)
    }

    public QueryBuilder search(List<Condition> conditions) { // Costruisce condizioni AND a partire da Condition
        if (conditions.isEmpty()) {                     // Se non ci sono condizioni...
            return this;                                // ...non aggiunge nulla e ritorna
        }

        StringJoiner searchJoiner = new StringJoiner(" AND "); // Unisce condizioni con " AND "
        for (Condition cn : conditions) {               // Per ogni Condition della lista
            // Costruisce: alias.nomeColonna OPERATORE ?
            String conditionStr = String.format("%s.%s %s %s",
                    alias,                              // es. "u"
                    cn.getName(),                       // es. "email"
                    cn.getOperator().toString(),        // es. "=" o "LIKE" (stringa dell’enum)
                    QM                                  // sempre "?" per parametro
            );
            searchJoiner.add(conditionStr);             // Aggiunge la condizione al joiner
        }
        query.append(" ").append(searchJoiner).append(" "); // Spazio prima, condizioni, spazio dopo
        return this;                                    // Chaining
    }

    public QueryBuilder insert(String... fields) {      // Costruisce una INSERT
        query.append("INSERT INTO ").append(table).append(" "); // "INSERT INTO <tabella> "
        StringJoiner commaJoiner = new StringJoiner(", ", "(", ")"); // Joiner con parentesi: "(a, b, c)"
        for (String field : fields) {                   // Elenco colonne
            commaJoiner.add(field);                     // Aggiunge nome colonna così com’è
        }
        query.append(commaJoiner).append(" VALUES ");   // "... (c1, c2, c3) VALUES "
        StringJoiner qmJoiner = new StringJoiner(", ", "(", ")"); // Joiner dei "?" in parentesi
        for (int i = 0; i < fields.length; i++) {       // Uno "?" per ogni colonna
            qmJoiner.add(QM);                           // Aggiunge "?"
        }
        query.append(qmJoiner).append(" ");             // Aggiunge "(?, ?, ?)" e spazio
        return this;                                    // Chaining
    }

    public QueryBuilder delete() {                      // Costruisce un DELETE
        query.append("DELETE FROM ").append(table).append(" "); // "DELETE FROM <tabella> "
        return this;                                    // Chaining (tipicamente seguito da WHERE)
    }

    public QueryBuilder update(String... fields) {      // Costruisce un UPDATE con SET di campi = ?
        query.append("UPDATE ").append(table).append(" SET "); // "UPDATE <tabella> SET "
        StringJoiner commaJoiner = new StringJoiner(", ");     // Joiner con virgola tra assegnazioni
        for (String field : fields) {                   // Per ogni colonna da aggiornare
            commaJoiner.add(field + " = " + QM);        // "colonna = ?"
        }
        query.append(commaJoiner).append(" ");          // Aggiunge "c1 = ?, c2 = ?, ..." e spazio
        return this;                                    // Chaining (seguito da WHERE)
    }

    public QueryBuilder limit(boolean withOffset) {     // Costruisce LIMIT (e opzionalmente OFFSET in stile MySQL)
        query.append("LIMIT ").append(QM);              // "LIMIT ?"
        if (withOffset) {                               // Se serve anche l'offset
            query.append(", ").append(QM);              // Aggiunge ", ?" -> "LIMIT ?, ?"
        }
        query.append(" ");                              // Spazio finale
        return this;                                    // Chaining
    }

    public QueryBuilder innerJoin(String joinedTable, String joinedAlias) { // INNER JOIN semplice
        query.append("INNER JOIN ").append(joinedTable) // "INNER JOIN <tabellaJoin>"
                .append(" ").append(joinedAlias).append(" "); // "<alias> " e spazio
        return this;                                    // Tipicamente seguito da on("condizione")
    }

    public QueryBuilder outerJoin(boolean isLeft, String joinedTable, String joinedAlias) { // LEFT/RIGHT JOIN
        String direction = isLeft ? "LEFT JOIN " : "RIGHT JOIN "; // Determina direzione
        query.append(direction)                       // "LEFT JOIN " o "RIGHT JOIN "
                .append(joinedTable).append(" ")         // nome tabella
                .append(joinedAlias).append(" ");        // alias e spazio
        return this;                                   // Chaining
    }

    public QueryBuilder on(String condition) {         // Clausola ON per i JOIN
        query.append("ON ").append(condition).append(" "); // "ON <condizione> "
        return this;                                    // Chaining
    }
}
