import java.sql.*;

public class Database {
    public static Connection createDb(){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Database created successfully");

        return c;
    }

    public static void createTable(Connection c){
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sqlFlights = "CREATE TABLE IF NOT EXISTS flights " +
                        "(flightID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " nazwa CHAR(50) NOT NULL, " +
                        " dataLotu DATE NOT NULL, " +
                        " from_location CHAR(50), " +
                        " to_location CHAR(50), " +
                        " numberOfFreeSeats INTEGER(2), " +
                        " price DECIMAL(5, 2)" +
                    ")";
            String sqlUsers =
                    "CREATE TABLE IF NOT EXISTS users " +
                        "(userID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " username VARCHAR(15) NOT NULL, " +
                        " password TEXT NOT NULL, " +
                        " imie TEXT, " +
                        " nazwisko TEXT, " +
                        " ulica TEXT, " +
                        " nr_domu TEXT, " +
                        " miejscowosc TEXT, " +
                        " kod_pocztowy TEXT, " +
                        " nr_tel TEXT, " +
                        " portfel DECIMAL(10, 2)" +
                    ")";
            String sqlReservations =
                    "CREATE TABLE IF NOT EXISTS reservations " +
                            "(reservationID INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                            " userID INTEGER, " +
                            " flightID INTEGER, " +
                            " FOREIGN KEY(userID) REFERENCES users(userID), " +
                            " FOREIGN KEY(flightID) REFERENCES flights(flightID)" +
                    ")";
            stmt.executeUpdate(sqlFlights);
            stmt.close();
            stmt.executeUpdate(sqlUsers);
            stmt.close();
            stmt.executeUpdate(sqlReservations);
            stmt.close();
            /*stmt.execute("ALTER TABLE flights ADD COLUMN numberOfFreeSeats INTEGER(2)");
            stmt.execute("ALTER TABLE flights ADD COLUMN seatPrice DECIMAL(5, 2)");
            stmt.execute("ALTER TABLE users ADD COLUMN portfel DECIMAL(10, 2)");
            stmt.close();*/
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
    public static void dropTable(Connection c){
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sqlFlights = "DROP TABLE flights";
            String sqlUsers = "DROP TABLE users";
            stmt.executeUpdate(sqlFlights);
            stmt.executeUpdate(sqlUsers);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table deleted successfully");
    }

}
