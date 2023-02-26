import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.*;

public class ReservedFlights extends JFrame {
    private int userID;
    private String userSes;
    public ReservedFlights(Connection c, String userSes) {
        setTitle("Rezerwacje " + userSes);
        setLayout(new BorderLayout());

        this.userSes = userSes;

        try {
            String query1 = "SELECT userID FROM users WHERE username = ?";
            PreparedStatement stmt1 = c.prepareStatement(query1);
            stmt1.setString(1, userSes);
            ResultSet rsUserID = stmt1.executeQuery();
            while (rsUserID.next()){
                this.userID = rsUserID.getInt("userID");
                System.out.println("username: " + userSes + " userID: " + this.userID);
            }
            Statement stmt2 = c.createStatement();
            //////////////////////////////////////////////////////////////////////////////////
            String query2 = "SELECT flights.nazwa AS 'Nazwa linii', flights.dataLotu AS 'Data', flights.from_location AS 'Od', flights.to_location AS 'Do' " +
                    "FROM flights " +
                    "JOIN reservations ON reservations.flightID = flights.flightID " +
                    "WHERE reservations.userID = " + userID;
            ResultSet rs = stmt2.executeQuery(query2);
            JTable table = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setSize(600, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
                dim.width/2 -getSize().width/2,
                dim.height/2-getSize().height/2
        );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private static TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        int columnCount = metaData.getColumnCount();

        DefaultTableModel tableModel = new DefaultTableModel();

        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            tableModel.addColumn(metaData.getColumnLabel(columnIndex));
        }

        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row[columnIndex - 1] = rs.getObject(columnIndex);
            }
            tableModel.addRow(row);
        }

        return tableModel;
    }
}