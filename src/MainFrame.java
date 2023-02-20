//import org.w3c.dom.events.MouseEvent;
import java.awt.event.*;
import java.awt.event.MouseEvent.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;

public class MainFrame extends JFrame{

    private JMenuBar menuBar;
    private JMenu flightsMenu;
    private JMenu userMenu;

    private JMenuItem searchFlightsMenuItem, loginMenuItem, flightsDisplayMenuItem;

    MainFrame(Connection c){
        super("Menu Główne");
        menuBar = new JMenuBar();

        searchFlightsMenuItem = new JMenuItem("Wyszukaj loty");
        flightsDisplayMenuItem = new JMenuItem("Wyswietl loty");
        loginMenuItem = new JMenuItem("Logowanie");

        flightsMenu = new JMenu("Loty");
        flightsMenu.add(searchFlightsMenuItem);
        flightsMenu.add(flightsDisplayMenuItem);
        menuBar.add(flightsMenu);

        userMenu = new JMenu("Zaloguj");
        menuBar.add(userMenu);

        setJMenuBar(menuBar);
        FlightsDisplay mainFlightDisplay = new FlightsDisplay(c);
        getContentPane().add(mainFlightDisplay, BorderLayout.CENTER);

        userMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                getContentPane().removeAll();
                Login loginPanel = new Login(c, MainFrame.this);
                getContentPane().add(loginPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }

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

        searchFlightsMenuItem.addActionListener(e -> {
            userMenu.setArmed(false);
            getContentPane().removeAll();
            FlightSearch newFlightSearchPanel = new FlightSearch(c);
            getContentPane().add(newFlightSearchPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        flightsDisplayMenuItem.addActionListener(e -> {
            userMenu.setArmed(false);
            getContentPane().removeAll();
            FlightsDisplay newFlightDisplayPanel = new FlightsDisplay(c);
            getContentPane().add(newFlightDisplayPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        /*loginMenuItem.addActionListener(e -> {
            getContentPane().removeAll();
            Login loginPanel = new Login(c, this);
            getContentPane().add(loginPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });*/

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public final JFrame getMainFrame(){
        setVisible(false);
        return this;
    }

}
