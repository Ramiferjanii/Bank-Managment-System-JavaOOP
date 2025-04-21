package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton login, signup, clear;
    JTextField cardTextField;
    JPasswordField pinTextField;

    public Login() {
        setTitle("AUTOMATED TELLER MACHINE");
        setLayout(null);
        
        // Load and display logo
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.jpg"));
            Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel label = new JLabel(i3);
            label.setBounds(70, 10, 100, 100);
            add(label);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading application icon");
        }

        // Application title
        JLabel text = new JLabel("Welcome to ATM");
        text.setFont(new Font("Raleway", Font.BOLD, 38));
        text.setBounds(200, 40, 400, 40);
        add(text);

        // Card Number Section
        JLabel cardno = new JLabel("Card No:");
        cardno.setFont(new Font("Raleway", Font.BOLD, 28));
        cardno.setBounds(120, 150, 150, 40);
        add(cardno);

        cardTextField = new JTextField();
        cardTextField.setBounds(300, 150, 230, 30);
        cardTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardTextField);

        // PIN Section
        JLabel pin = new JLabel("PIN:");
        pin.setFont(new Font("Raleway", Font.BOLD, 28));
        pin.setBounds(120, 220, 150, 40);
        add(pin);

        pinTextField = new JPasswordField();
        pinTextField.setBounds(300, 220, 230, 30);
        pinTextField.setEchoChar('*');
        add(pinTextField);

        // Buttons
        login = createButton("SIGN IN", 300, 300, 100, 30);
        clear = createButton("CLEAR", 430, 300, 100, 30);
        signup = createButton("SIGN UP", 300, 350, 230, 30);

        getContentPane().setBackground(Color.WHITE);
        setSize(800, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setBounds(x, y, w, h);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        add(button);
        return button;
    }

    private boolean validateInput(String cardnumber, String pinnumber) {
        if (cardnumber.isEmpty() || pinnumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter both card number and PIN");
            return false;
        }

        if (!cardnumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(null, "Card number must be 16 digits");
            return false;
        }

        if (!pinnumber.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(null, "PIN must be 4 digits");
            return false;
        }

        return true;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == clear) {
                cardTextField.setText("");
                pinTextField.setText("");
            } 
            else if (ae.getSource() == login) {
                String cardnumber = cardTextField.getText().trim();
                String pinnumber = new String(pinTextField.getPassword()).trim();

                if (!validateInput(cardnumber, pinnumber)) return;

                try (Conn conn = new Conn()) { // Now works with AutoCloseable
                    String query = "SELECT * FROM login WHERE account_number = ? AND pin = ?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, cardnumber);
                    ps.setString(2, pinnumber);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            dispose();
                            new Transaction(rs.getString("account_number")).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid credentials");
                        }
                    }
                }
            } 
            else if (ae.getSource() == signup) {
                dispose();
                new SignUpOne().setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}