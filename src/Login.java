import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Login extends JPanel{
    private JLabel titleLabel, usernameLabel, passwordLabel, notRegisteredLabel, signUpLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login(Connection c, MainFrame frame) {
        Boolean a = true;
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setOpaque(false);

        titleLabel = new JLabel("Logowanie");
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


        loginButton = new JButton("Zaloguj");
        loginButton.setBounds(150, 120, 100, 20);
        add(loginButton);

        notRegisteredLabel = new JLabel("Nie masz konta?");
        notRegisteredLabel.setBounds(50, 150, 100, 20);
        add(notRegisteredLabel);

        signUpLabel = new JLabel("Zarejestruj się!");
        signUpLabel.setBounds(150, 150, 100, 20);
        signUpLabel.setForeground(Color.BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                signUpLabel.setForeground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e){
                signUpLabel.setForeground(Color.BLUE);
            }
            public void mouseClicked(MouseEvent e){
                new Registration(c);
            }
        });
        add(signUpLabel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                String selectQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pstmt = c.prepareStatement(selectQuery);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Logowanie przebiegło pomyślnie!");
                    MainFrameLogged mfl = new MainFrameLogged(c, username, frame);
                    frame.setVisible(false);
                    mfl.setTitle("Zalogowano");
                    mfl.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Nieprawidłowy użytkownik lub hasło");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Błąd logowania: " + ex.getMessage());
                ex.printStackTrace();
            }

        });
    }
}
