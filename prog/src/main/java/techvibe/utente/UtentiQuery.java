package techvibe.utente;

import techvibe.storage.TableQuery;

class UtentiQuery extends TableQuery {

    UtentiQuery(String table) {
        super(table);
    }

    String allUtenti() {
        return String.format("SELECT*FROM %s LIMIT ?,?;", this.table); //come una printf in c
        //limit quante righe al massimo voglio prendere
    }

    String selectUtente() {
        return String.format("SELECT * FROM %s where Email=?;", table);
    }

    String insertUtente() {
        return String.format("INSERT INTO %s (Nome,Cognome,Email,Password,Telefono,IndirizzoSpedizione,Ruolo)VALUES (?,?,?,?,?,?,?);", table);


    }

    String updateUtente() {
        return String.format("UPDATE FROM %s SET Nome=?,Cognome=?,Telefono=?,IndirizzoSpedizione=? WHERE Email=?", table);

    }

    String deleteUtente() {
        return String.format("DELETE FROM %s WHERE Email=?", table);

    }
}
