import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DisplayUserProfile extends JFrame {
    private JLabel nameLabel, addressLabel, phoneLabel;
    private JTextField nameTextField, addressTextField, phoneTextField;
    private JButton saveButton;
    private String username;
    private Connection connection;

    public DisplayUserProfile(String username, Connection c) {
        super("Twoje dane");

        this.username = username;
        this.connection = c;

        setLayout(new GridLayout(4, 2, 5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        nameLabel = new JLabel("Name: ");
        nameTextField = new JTextField(20);
        add(nameLabel);
        add(nameTextField);

        addressLabel = new JLabel("Address: ");
        addressTextField = new JTextField(20);
        add(addressLabel);
        add(addressTextField);

        phoneLabel = new JLabel("Phone: ");
        phoneTextField = new JTextField(20);
        add(phoneLabel);
        add(phoneTextField);

        saveButton = new JButton("Save");
        add(saveButton);

        fetchUserProfile();
        setResizable(false);
        setVisible(true);
    }

    private void fetchUserProfile() {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nameTextField.setText(resultSet.getString("name"));
                addressTextField.setText(resultSet.getString("address"));
                phoneTextField.setText(resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
