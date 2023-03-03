import javax.swing.*;
import java.sql.*;

public class Main extends JFrame {
    public static void main( String args[] ) {
        Connection c = Database.createDb();
        Database.createTable(c);
        new MainFrame(c);
        sout"ddkldldld";
    }
}