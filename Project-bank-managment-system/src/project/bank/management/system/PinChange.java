package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PinChange extends JFrame implements ActionListener {
    JPasswordField newPin, rePin;
    JButton change, back;
    String cardNumber;
    
    PinChange(String cardNumber) {
        this.cardNumber = cardNumber;
        
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        JLabel text = new JLabel("Change Your PIN");
        text.setFont(new Font("Raleway", Font.BOLD, 16));
        text.setBounds(170, 50, 400, 20);
        add(text);
        
        JLabel pinText = new JLabel("New PIN (4 digits):");
        pinText.setFont(new Font("Raleway", Font.BOLD, 14));
        pinText.setBounds(100, 100, 400, 20);
        add(pinText);
        
        newPin = new JPasswordField();
        newPin.setFont(new Font("Raleway", Font.BOLD, 14));
        newPin.setBounds(250, 100, 150, 25);
        add(newPin);
        
        JLabel rePinText = new JLabel("Re-Enter PIN:");
        rePinText.setFont(new Font("Raleway", Font.BOLD, 14));
        rePinText.setBounds(100, 150, 400, 20);
        add(rePinText);
        
        rePin = new JPasswordField();
        rePin.setFont(new Font("Raleway", Font.BOLD, 14));
        rePin.setBounds(250, 150, 150, 25);
        add(rePin);
        
        change = new JButton("Change");
        change.setBounds(150, 200, 100, 30);
        change.addActionListener(this);
        add(change);
        
        back = new JButton("Back");
        back.setBounds(250, 200, 100, 30);
        back.addActionListener(this);
        add(back);
        
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            if(ae.getSource() == change) {
                String npin = new String(newPin.getPassword());
                String rpin = new String(rePin.getPassword());
                
                if(!validatePin(npin, rpin)) return;
                
                updatePinInDatabase(npin);
                
                JOptionPane.showMessageDialog(this, "PIN Changed Successfully");
                navigateBackToTransaction();
            } 
            else {
                navigateBackToTransaction();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private boolean validatePin(String npin, String rpin) {
        if(npin.isEmpty() || rpin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both PIN fields");
            return false;
        }
        
        
        
        if(!npin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "PIN must be 4 digits");
            return false;
        }
        
        return true;
    }

   private void updatePinInDatabase(String newPin) throws SQLException {
    try (Conn conn = new Conn()) {
        String query = "UPDATE login SET pin = ? WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newPin);
            ps.setString(2, this.cardNumber); // Use String for VARCHAR
            int updatedRows = ps.executeUpdate();
            
            if (updatedRows == 0) {
                throw new SQLException("Card number not found: " + this.cardNumber);
            }
        }
    }
}

    private void navigateBackToTransaction() {
        this.dispose();
        new Transaction(cardNumber).setVisible(true);
    }

    public static void main(String[] args) {
        new PinChange("");
    }
}