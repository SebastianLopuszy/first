import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectedFlightReservation extends JFrame {

    private JLabel titleLabel, nameLabel, flightDateLabel, fromLocationLabel, toLocationLabel;

    private JButton reserveButton, cancelButton;

    private Connection c;
    private Integer userID;
    private String userSes, flightNumber, name, flightDate, fromLocation, toLocation;

    public SelectedFlightReservation(Connection c, Integer userID, String flightNumber, String name, String flightDate, String fromLocation, String toLocation) {
        setTitle("Rezerwacja lotu");
        setLayout(null);

        this.c = c;
        this.userID = userID;
        this.flightNumber = flightNumber;
        this.name = name;
        this.flightDate = flightDate;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;

        titleLabel = new JLabel("Zarezerwuj lot");
        titleLabel.setBounds(150, 20, 400, 20);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        add(titleLabel);

        nameLabel = new JLabel("Nazwa lotu: " + name);
        nameLabel.setBounds(100, 60, 150, 20);
        add(nameLabel);

        flightDateLabel = new JLabel("Data: " + flightDate);
        flightDateLabel.setBounds(250, 60, 100, 20);
        add(flightDateLabel);

        fromLocationLabel = new JLabel("Z: " + fromLocation);
        fromLocationLabel.setBounds(100, 90, 100, 20);
        add(fromLocationLabel);

        toLocationLabel = new JLabel("Do: " + toLocation);
        toLocationLabel.setBounds(250, 90, 100, 20);
        add(toLocationLabel);

        reserveButton = new JButton("Rezerwuj");
        reserveButton.setBounds(250, 120, 100, 20);
        add(reserveButton);

        cancelButton = new JButton("Anuluj");
        cancelButton.setBounds(100, 120, 100, 20);
        add(cancelButton);

        reserveButton.addActionListener(e -> reserveFlight());
        cancelButton.addActionListener(e -> dispose());

        setSize(450, 300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
                dim.width/2 -getSize().width/2,
                dim.height/2-getSize().height/2
        );
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void reserveFlight(){
        try{
            double portfel = -1, seatPrice = 0;
            String query2 = "SELECT portfel FROM users WHERE userID = ?";
            PreparedStatement p4 = c.prepareStatement(query2);
            p4.setInt(1, userID);
            ResultSet rsPortfel = p4.executeQuery();
            while(rsPortfel.next()){
                portfel = rsPortfel.getDouble(1);
                System.out.println("portfel = " + portfel);
            }
            String query3 = "SELECT seatPrice FROM flights WHERE flightID = ?";
            PreparedStatement p5 = c.prepareStatement(query3);
            p5.setString(1, flightNumber);
            ResultSet rsSeatPrice = p5.executeQuery();
            while(rsSeatPrice.next()){
                seatPrice = rsSeatPrice.getDouble(1);
                System.out.println("seatPrice = " + seatPrice);
            }
            if(portfel >= seatPrice ){
                double newPortfel = portfel - seatPrice;
                String query = "SELECT numberOfFreeSeats FROM flights WHERE flightID = ?";
                PreparedStatement p2 = c.prepareStatement(query);
                p2.setString(1, flightNumber);
                ResultSet rs = p2.executeQuery();
                int numberOfFreeSeats = rs.getInt("numberOfFreeSeats");
                if(numberOfFreeSeats > 0) {

                    numberOfFreeSeats--;

                    if(numberOfFreeSeats == 0){
                        String query5 = "DELETE FROM flights WHERE flightID = ?";
                        PreparedStatement p7 = c.prepareStatement(query5);
                        p7.setString(1, flightNumber);
                    }
                }else {
                    JOptionPane.showMessageDialog(this, "Brak miejscw danym locie");
                    dispose();
                }
                //////////////////////////////////////////////////////////////////////////////////
                String query4 = "UPDATE users SET portfel = ? WHERE userID = ?";
                PreparedStatement p6 = c.prepareStatement(query4);
                p6.setDouble(1, newPortfel);
                p6.setInt(2, userID);
                p6.executeUpdate();
                //////////////////////////////////////////////////////////////////////////////////
                PreparedStatement p1 = c.prepareStatement("INSERT INTO reservations (userID, flightID) VALUES (?, ?)");
                p1.setInt(1, userID);
                p1.setString(2, flightNumber);
                p1.executeUpdate();
                //////////////////////////////////////////////////////////////////////////////////

                String update = "UPDATE flights SET numberOfFreeSeats = ? WHERE flightID = ?";
                PreparedStatement p3 = c.prepareStatement(update);
                p3.setInt(1, numberOfFreeSeats);
                p3.setString(2, flightNumber);
                p3.executeUpdate();

                //////////////////////////////////////////////////////////////////////////////////
                JOptionPane.showMessageDialog(this, "Lot " + name + " został zarezerwowany!");
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Brak wystaczających środków na koncie");
                dispose();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}