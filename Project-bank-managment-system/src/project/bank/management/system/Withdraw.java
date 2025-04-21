package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Withdraw extends JFrame implements ActionListener {
    JTextField amount;
    JButton withdraw, back;
    String cardNumber;
    
    Withdraw(String cardNumber) {
        this.cardNumber = cardNumber;
        
        setLayout(null);
        
        JLabel text = new JLabel("Enter amount to withdraw");
        text.setFont(new Font("Raleway", Font.BOLD, 16));
        text.setBounds(170, 100, 400, 20);
        add(text);
        
        amount = new JTextField();
        amount.setFont(new Font("Raleway", Font.BOLD, 14));
        amount.setBounds(170, 150, 250, 25);
        add(amount);
        
        withdraw = new JButton("Withdraw");
        withdraw.setBounds(170, 200, 100, 30);
        withdraw.addActionListener(this);
        add(withdraw);
        
        back = new JButton("Back");
        back.setBounds(320, 200, 100, 30);
        back.addActionListener(this);
        add(back);
        
        setSize(600, 400);
        setLocation(300, 100);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == withdraw) {
            String withdrawAmount = amount.getText();
            try {
                Conn conn = new Conn();
                ResultSet rs = conn.s.executeQuery("SELECT * FROM transactions WHERE card_number = '"+cardNumber+"'");
                double balance = 0.0;
                while(rs.next()) {
                    if(rs.getString("type").equals("Deposit")) {
                        balance += Double.parseDouble(rs.getString("amount"));
                    } else {
                        balance -= Double.parseDouble(rs.getString("amount"));
                    }
                }
                
                if(balance < Double.parseDouble(withdrawAmount)) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance");
                    return;
                }
                
                String query = "INSERT INTO transactions VALUES('"+cardNumber+"', 'Withdraw', '"+withdrawAmount+"', NOW())";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "$"+withdrawAmount+" Withdrawn Successfully");
                setVisible(false);
                new Transaction(cardNumber).setVisible(true);
                
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            setVisible(false);
            new Transaction(cardNumber).setVisible(true);
        }
    }
}