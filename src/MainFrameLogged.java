import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFrameLogged extends JFrame{

    private JMenuBar menuBar;
    private JMenu flightsMenu;
    private JMenu userMenu;
    private JMenu logoutMenu;

    private JMenuItem searchFlightsMenuItem, editProfileMenuItem, logoutMenuItem, flightsDisplayMenuItem, depositMoneyMenuItem, myReservationsMenuItem;

    MainFrameLogged(Connection c, String userSes, MainFrame frame){
        super("Menu główne");
        //System.out.println(userSes);
        menuBar = new JMenuBar();

        searchFlightsMenuItem = new JMenuItem("Wyszukaj loty");
        flightsDisplayMenuItem = new JMenuItem("Wyswietl loty");
        editProfileMenuItem = new JMenuItem("Edytuj profil");
        depositMoneyMenuItem = new JMenuItem("Dodaj środki");
        myReservationsMenuItem = new JMenuItem("Moje rezerwacje");
        //logoutMenuItem = new JMenuItem("Wyloguj");

        flightsMenu = new JMenu("Loty");
        flightsMenu.add(searchFlightsMenuItem);
        flightsMenu.add(flightsDisplayMenuItem);
        menuBar.add(flightsMenu);

        userMenu = new JMenu("Profil");
        userMenu.add(myReservationsMenuItem);
        userMenu.add(editProfileMenuItem);
        userMenu.add(depositMoneyMenuItem);
        //userMenu.add(logoutMenuItem);
        menuBar.add(userMenu);

        logoutMenu = new JMenu("Wyloguj");
        menuBar.add(logoutMenu);

        setJMenuBar(menuBar);

        FlightsDisplayLogged mainFlightDisplayLogged = new FlightsDisplayLogged(c, userSes, this);
        getContentPane().add(mainFlightDisplayLogged, BorderLayout.CENTER);

        logoutMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                dispose();
                frame.setVisible(true);
                logoutMenu.setArmed(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                logoutMenu.setArmed(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                logoutMenu.setArmed(false);
            }
        });

        flightsMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                flightsMenu.setArmed(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                flightsMenu.setArmed(false);
            }
        });

        userMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                userMenu.setArmed(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                userMenu.setArmed(false);
            }
        });

        searchFlightsMenuItem.addActionListener(e -> {
            getContentPane().removeAll();
            FlightSearchLogged newFlightSearchLoggedPanel = new FlightSearchLogged(c, userSes, this);
            getContentPane().add(newFlightSearchLoggedPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        flightsDisplayMenuItem.addActionListener(e -> {
            getContentPane().removeAll();
            FlightsDisplayLogged newFlightDisplayPanel = new FlightsDisplayLogged(c, userSes, this);
            getContentPane().add(newFlightDisplayPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        /*logoutMenuItem.addActionListener(e -> {
            dispose();
            frame.setVisible(true);
        });*/

        editProfileMenuItem.addActionListener(e -> {
            getContentPane().removeAll();
            UserProfile userProfilePanel = new UserProfile(c, userSes);
            getContentPane().add(userProfilePanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        depositMoneyMenuItem.addActionListener(e -> {
            DepositMoney depositMoney = new DepositMoney(c, userSes);
        });

        myReservationsMenuItem.addActionListener(e -> {
            ReservedFlights reservedFlights = new ReservedFlights(c, userSes);
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }

    public int getUserID(Connection c, String username) {
        try {
             PreparedStatement pstmt = c.prepareStatement("SELECT userID FROM users WHERE username = ?");
             pstmt.setString(1, username);
             ResultSet rs = pstmt.executeQuery();
             if (rs.next()) {
                return rs.getInt("userID");
             }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
