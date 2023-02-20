import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FlightSearchLogged extends JPanel {

    private JPanel searchPanel;
    private JLabel nameLabel, dateLabel, fromLabel, toLabel;
    private JTextField nameField, dateField, fromField, toField;
    private JButton searchButton, reserveButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public FlightSearchLogged(Connection c, String userSes, MainFrameLogged mainFrameLogged){
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
            public void actionPerformed(ActionEvent e){
                try {
                    Statement stmt = c.createStatement();

                    String sql = "SELECT * FROM flights WHERE 1=1";

                    String name = nameField.getText();
                    String date = dateField.getText();
                    String from = fromField.getText();
                    String to = toField.getText();

                    if(!name.isEmpty()){
                        sql += " AND nazwa LIKE '%" + name + "%'";
                    }

                    if(!date.isEmpty()){
                        sql += " AND dataLotu LIKE '%" + date + "%'";
                    }

                    if(!from.isEmpty()){
                        sql += " AND from_location LIKE '%" + from + "%'";
                    }

                    if(!to.isEmpty()){
                        sql += " AND to_location LIKE '%" + to + "%'";
                    }

                    ResultSet rs = stmt.executeQuery(sql);

                    while (tableModel.getRowCount() > 0) {
                        tableModel.removeRow(0);
                    }

                    while(rs.next()){
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
                } catch(SQLException ex){
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

        resultsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event){
                if (event.getValueIsAdjusting()){
                    int userID = mainFrameLogged.getUserID(c, userSes);
                    String flightID = resultsTable.getValueAt(resultsTable.getSelectedRow(), 0).toString();
                    String name = resultsTable.getValueAt(resultsTable.getSelectedRow(), 1).toString();
                    String flightDate = resultsTable.getValueAt(resultsTable.getSelectedRow(), 2).toString();
                    String fromLocation = resultsTable.getValueAt(resultsTable.getSelectedRow(), 3).toString();
                    String toLocation = resultsTable.getValueAt(resultsTable.getSelectedRow(), 4).toString();
                    System.out.println("Row selected " + resultsTable.getSelectedRow());
                    SelectedFlightReservation sfr = new SelectedFlightReservation(c, userID, flightID, name, flightDate, fromLocation, toLocation);
                }
            }
        });
    }
}