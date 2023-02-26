import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfile extends JPanel {

    private JTextField firstNameField, lastNameField, addressField, aprtmntNumberField, cityField, postalCodeField, phoneNumberField;

    private JLabel titleLabel, firstNameLabel, lastNameLabel, addressLabel, aprtmntNumberLabel, cityLabel, postalCodeLabel, phoneNumberLabel;
    private JButton saveButton;

    public UserProfile(Connection c, String userSes){
        System.out.println(userSes);
        this.firstNameField = firstNameField;
        this.lastNameField = lastNameField;
        this.addressField = addressField;
        this.aprtmntNumberField = aprtmntNumberField;
        this.cityField = cityField;
        this.postalCodeField = postalCodeField;
        this.phoneNumberField = phoneNumberField;
        setSize(750, 750);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setLayout(null);

        titleLabel = new JLabel("Profil Uzytkownika");
        titleLabel.setBounds(150, 20, 200, 20);
        add(titleLabel);

        firstNameLabel = new JLabel("Imie:");
        firstNameLabel.setBounds(50, 60, 100, 20);
        add(firstNameLabel);
        firstNameField = new JTextField();
        firstNameField.setBounds(150, 60, 200, 20);
        add(firstNameField);

        lastNameLabel = new JLabel("Nazwisko:");
        lastNameLabel.setBounds(50, 90, 100, 20);
        add(lastNameLabel);
        lastNameField = new JTextField();
        lastNameField.setBounds(150, 90, 200, 20);
        add(lastNameField);

        addressLabel = new JLabel("Ulica:");
        addressLabel.setBounds(50, 120, 100, 20);
        add(addressLabel);
        addressField = new JTextField();
        addressField.setBounds(150, 120, 200, 20);
        add(addressField);

        aprtmntNumberLabel = new JLabel("Nr domu/mieszk.:");
        aprtmntNumberLabel.setBounds(50, 150, 100, 20);
        add(aprtmntNumberLabel);
        aprtmntNumberField = new JTextField();
        aprtmntNumberField.setBounds(150, 150, 50, 20);
        add(aprtmntNumberField);

        cityLabel = new JLabel("Miasto:");
        cityLabel.setBounds(50, 180, 100, 20);
        add(cityLabel);
        cityField = new JTextField();
        cityField.setBounds(150, 180, 200, 20);
        add(cityField);

        postalCodeLabel = new JLabel("Kod pocztowy:");
        postalCodeLabel.setBounds(50, 210, 100, 20);
        add(postalCodeLabel);
        postalCodeField = new JTextField();
        postalCodeField.setBounds(150, 210, 200, 20);
        add(postalCodeField);

        phoneNumberLabel = new JLabel("Telefon:");
        phoneNumberLabel.setBounds(50, 240, 100, 20);
        add(phoneNumberLabel);
        phoneNumberField = new JTextField();
        phoneNumberField.setBounds(150, 240, 200, 20);
        add(phoneNumberField);

        saveButton = new JButton("Zapisz");
        saveButton.setBounds(150, 270, 100, 20);
        add(saveButton);

        saveButton.addActionListener(e -> {save(c, userSes);});

        getTextField(c, userSes, firstNameField, "imie");
        getTextField(c, userSes, lastNameField, "nazwisko");
        getTextField(c, userSes, addressField, "ulica");
        getTextField(c, userSes, aprtmntNumberField, "nr_domu");
        getTextField(c, userSes, cityField, "miejscowosc");
        getTextField(c, userSes, postalCodeField, "kod_pocztowy");
        getTextField(c, userSes, phoneNumberField, "nr_tel");
    }
    public void getTextField(Connection c, String userSes, JTextField textField, String columnName){
        String insertQuery = "SELECT " + columnName + " FROM users WHERE username = ?";
        try{
            PreparedStatement pstmt = c.prepareStatement(insertQuery);
            pstmt.setString(1, userSes);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String field = rs.getString(columnName);
                if(columnName != null && !columnName.isEmpty()) {
                    textField.setText(field);
                    System.out.println(field);
                }
            }
            rs.close();
            pstmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        textField.setEditable(true);
    }

    public void save(Connection c, String userSes){
        String insertQuery = "UPDATE users SET imie = ? , " +
                " nazwisko = ? , " +
                " ulica = ? , " +
                " nr_domu = ? , " +
                " miejscowosc = ? , " +
                " kod_pocztowy = ? , " +
                " nr_tel = ? "  +
                "WHERE username = ?";
        try{
            PreparedStatement pstmt = c.prepareStatement(insertQuery);
            pstmt.setString(1, firstNameField.getText());
            pstmt.setString(2, lastNameField.getText());
            pstmt.setString(3, addressField.getText());
            pstmt.setString(4, aprtmntNumberField.getText());
            pstmt.setString(5, cityField.getText());
            pstmt.setString(6, postalCodeField.getText());
            pstmt.setString(7, phoneNumberField.getText());
            pstmt.setString(8, userSes);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(saveButton, "Pomyślna zmiana danych");
        } catch(SQLException e){
            System.out.println("Blad podczas wprowadzania danych: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Blad podczas wprowadzania danych.");
        }
    }
}