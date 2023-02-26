import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositMoney extends JFrame {

    private JLabel titleLabel, amountLabel, yourAmountLabel, yourAmountValLabel;
    private JTextField amountField;
    private JButton addButton;
    private JButton cancelButton;

    private Connection connection;
    private String userSes;

    public DepositMoney(Connection connection, String userSes) {
        setTitle("Dodaj pieniądze do portfela");
        setLayout(null);

        this.connection = connection;
        this.userSes = userSes;

        titleLabel = new JLabel("Dodaj środki");
        titleLabel.setBounds(150, 20, 400, 20);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        add(titleLabel);

        yourAmountLabel = new JLabel("Twoje środki:");
        yourAmountLabel.setBounds(100, 60, 100, 20);
        add(yourAmountLabel);

        yourAmountValLabel = new JLabel(getAmountValue(connection, userSes));
        yourAmountValLabel.setBounds(200, 60, 100, 20);
        add(yourAmountValLabel);

        amountLabel = new JLabel("Ilość:");
        amountLabel.setBounds(100, 90, 100, 20);
        add(amountLabel);

        amountField = new JTextField(20);
        amountField.setBounds(210, 90, 100, 20);
        add(amountField);

        addButton = new JButton("Dodaj");
        addButton.setBounds(210, 120, 100, 20);
        add(addButton);

        cancelButton = new JButton("Anuluj");
        cancelButton.setBounds(100, 120, 100, 20);
        add(cancelButton);

        addButton.addActionListener(e -> addMoneyToPortfel());
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

    private void addMoneyToPortfel() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount < 0) {
                JOptionPane.showMessageDialog(this, "Niepoprawna kwota.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET portfel = portfel + ? WHERE username = ?");
            pstmt.setDouble(1, amount);
            pstmt.setString(2, userSes);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Pomyślnie dodano " + amount + " zł do portfela.");

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Niepoprawna kwota.", "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd dodawania pieniędzy do portfela.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String getAmountValue(Connection c, String userSes) {
        try {
            String sql = "SELECT portfel FROM users WHERE username = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, userSes);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("portfel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}