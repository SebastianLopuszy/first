import javax.swing.*;
import java.sql.*;

public class Main extends JFrame {
    public static void main( String args[] ) {
        Connection c = Database.createDb();
        Database.createTable(c);
        //Database.dropTable(c);
        new MainFrame(c);

        /*Connection c = Database.createDb();
        Database.createTable(c);
        Database.dropTable(c);
        new FlightReservationSystem(c);
        //new RegistrationAndLogin(c);*/
    }

}