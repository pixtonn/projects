package daos;

import java.sql.*;
import java.util.*;
import Exceptions.DatabaseException;

public class Database {

    private Connection conn;

    private PersonDAO pdao;
    private UserDAO udao;
    private EventDAO edao;
    private AuthTokenDAO atdao;


    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Database(){
        try {
            openConnection();
            pdao = new PersonDAO(conn);
            udao = new UserDAO(conn);
            atdao = new AuthTokenDAO(conn);
            edao = new EventDAO(conn);
        }
        catch(DatabaseException e){
            e.printStackTrace();
        }
    }



    public void openConnection() throws DatabaseException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:FMSDB.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DatabaseException("openConnection failed", e);
        }
    }

    public void closeConnection(boolean commit){
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            System.out.println("Error closing connection.");
            e.printStackTrace();
        }
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setPdao(PersonDAO pdao) {
        this.pdao = pdao;
    }

    public void setUdao(UserDAO udao) {
        this.udao = udao;
    }

    public void setEdao(EventDAO edao) {
        this.edao = edao;
    }

    public void setAtdao(AuthTokenDAO atdao) {
        this.atdao = atdao;
    }

    public Connection getConn() {
        return conn;
    }

    public PersonDAO getPdao() {
        return pdao;
    }

    public UserDAO getUdao() {
        return udao;
    }

    public EventDAO getEdao() {
        return edao;
    }

    public AuthTokenDAO getAtdao() {
        return atdao;
    }

    public void clearTables() throws DatabaseException
    {

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Users";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM AuthTokens";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("SQL Error encountered while clearing tables");
        }
    }

    public void clearTables(String username) throws DatabaseException
    {
        try (Statement stmt = conn.createStatement()){
            StringBuilder sql = new StringBuilder("DELETE FROM Events WHERE AssociatedUsername = '");
            sql.append(username.replace("'", "''"));
            sql.append("'");
            stmt.executeUpdate(sql.toString());
            sql = new StringBuilder("DELETE FROM Persons WHERE AssociatedUsername = '");
            sql.append(username.replace("'", "''"));
            sql.append("'");
            stmt.executeUpdate(sql.toString());
            sql = new StringBuilder("DELETE FROM AuthTokens WHERE Username = '");
            sql.append(username.replace("'", "''"));
            sql.append("'");
            stmt.executeUpdate(sql.toString());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("SQL Error encountered while clearing tables");
        }
    }

    public static void main(String[] args) {
        try {
            Database db = new Database();

            db.openConnection();

            db.clearTables();;

            db.closeConnection(true);

            System.out.println("OK");
        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}