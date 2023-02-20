import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FlightSearch extends JPanel {

    private JPanel searchPanel;
    private JLabel nameLabel, dateLabel, fromLabel, toLabel;
    private JTextField nameField, dateField, fromField, toField;
    private JButton searchButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public FlightSearch(Connection c) {
        //super("Flight Search");

        searchPanel = new JPanel(new GridLayout(5, 2));
        nameLabel = new JLabel("Nazwa linii:");
        nameField = new JTextField();
        dateLabel = new JLabel("Data:");
        dateField = new JTextField();
        fromLabel = new JLabel("Od:");
        fromField = new JTextField();
        toLabel = new JLabel("Do:");
        toField = new JTextField();

        searchButton = new JButton("Szukaj");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = c.createStatement();

                    String sql = "SELECT * FROM flights WHERE 1=1";

                    String name = nameField.getText();
                    String date = dateField.getText();
                    String from = fromField.getText();
                    String to = toField.getText();

                    if (!name.isEmpty()) {
                        sql += " AND nazwa LIKE '%" + name + "%'";
                    }

                    if (!date.isEmpty()) {
                        sql += " AND dataLotu LIKE '%" + date + "%'";
                    }

                    if (!from.isEmpty()) {
                        sql += " AND from_location LIKE '%" + from + "%'";
                    }

                    if (!to.isEmpty()) {
                        sql += " AND to_location LIKE '%" + to + "%'";
                    }

                    ResultSet rs = stmt.executeQuery(sql);

                    while (tableModel.getRowCount() > 0) {
                        tableModel.removeRow(0);
                    }

                    while (rs.next()) {
                        String[] row = new String[5];
                        row[0] = rs.getString("flightID");
                        row[1] = rs.getString("nazwa");
                        row[2] = rs.getString("dataLotu");
                        row[3] = rs.getString("from_location");
                        row[4] = rs.getString("to_location");

                        tableModel.addRow(row);
                    }

                    rs.close();
                    stmt.close();

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        String[] columns = {"Numer Lotu", "Nazwa Linii", "Data", "Od", "Do"};
        tableModel = new DefaultTableModel(columns, 0);
        resultsTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(resultsTable);

        searchPanel.add(nameLabel);
        searchPanel.add(nameField);
        searchPanel.add(dateLabel);
        searchPanel.add(dateField);
        searchPanel.add(fromLabel);
        searchPanel.add(fromField);
        searchPanel.add(toLabel);
        searchPanel.add(toField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        /*setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);*/
    }
}