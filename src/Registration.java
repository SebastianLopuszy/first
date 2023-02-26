import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registration extends JFrame {
    private JLabel titleLabel, usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public Registration (Connection c){
        super("Rejestracja");
        setSize(500, 300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
                dim.width/2 -getSize().width/2,
                dim.height/2-getSize().height/2
        );

        setLayout(null);

        titleLabel = new JLabel("Rejestracja");
        titleLabel.setBounds(150, 20, 200, 20);
        add(titleLabel);

        usernameLabel = new JLabel("Użytkownik:");
        usernameLabel.setBounds(50, 60, 100, 20);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150, 60, 200, 20);
        add(usernameField);

        passwordLabel = new JLabel("Hasło:");
        passwordLabel.setBounds(50, 90, 100, 20);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 200, 20);
        add(passwordField);

        registerButton = new JButton("Zarejestruj");
        registerButton.setBounds(150, 120, 100, 20);
        add(registerButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                String insertQuery = "INSERT INTO users (username, password, portfel) VALUES (?, ?, 0)";
                PreparedStatement pstmt = c.prepareStatement(insertQuery);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Rejestracja przebiegła pomyślnie!");
                this.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Błąd rejestracji: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}