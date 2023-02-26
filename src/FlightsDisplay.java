import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FlightsDisplay extends JPanel {
    private JTable flightsTable;

    public FlightsDisplay(Connection c) {

        flightsTable = new JTable(new DefaultTableModel(new Object[]{"Numer lotu", "Nazwa linii", "Data", "Od", "Do", "Wolnych miejsc", "Cena"}, 0));

        JScrollPane scrollPane = new JScrollPane(flightsTable);

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        add(panel);

        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM flights");

            DefaultTableModel model = (DefaultTableModel) flightsTable.getModel();

            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flightID");
                String name = resultSet.getString("nazwa");
                String flightDate = resultSet.getString("dataLotu");
                String fromLocation = resultSet.getString("from_location");
                String toLocation = resultSet.getString("to_location");
                Integer numberOfFreeSeats = resultSet.getInt("numberOfFreeSeats");
                Double price = resultSet.getDouble("seatPrice");

                model.addRow(new Object[]{flightNumber, name, flightDate, fromLocation, toLocation, numberOfFreeSeats, price});
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}