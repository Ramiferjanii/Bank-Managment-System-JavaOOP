package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BalanceEnquiry extends JFrame {
    
    BalanceEnquiry(String cardNumber) {
        setTitle("Balance Enquiry");
        setLayout(null);
        
        JLabel text = new JLabel();
        text.setBounds(20, 100, 400, 20);
        add(text);
        
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("SELECT * FROM transactions WHERE card_number = '"+cardNumber+"'");
            double balance = 0;
            while(rs.next()) {
                if(rs.getString("type").equals("Deposit")) {
                    balance += Double.parseDouble(rs.getString("amount"));
                } else {
                    balance -= Double.parseDouble(rs.getString("amount"));
                }
            }
            text.setText("Your Current Balance is: $" + balance);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        setSize(400, 200);
        setLocation(300, 100);
        setVisible(true);
    }
}