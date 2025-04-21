package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Deposit extends JFrame implements ActionListener {
    JTextField amount;
    JButton deposit, back;
    String cardNumber;
    
    Deposit(String cardNumber) {
        this.cardNumber = cardNumber;
        
        setLayout(null);
        
        JLabel text = new JLabel("Enter amount to deposit");
        text.setFont(new Font("Raleway", Font.BOLD, 16));
        text.setBounds(170, 100, 400, 20);
        add(text);
        
        amount = new JTextField();
        amount.setFont(new Font("Raleway", Font.BOLD, 14));
        amount.setBounds(170, 150, 250, 25);
        add(amount);
        
        deposit = new JButton("Deposit");
        deposit.setBounds(170, 200, 100, 30);
        deposit.addActionListener(this);
        add(deposit);
        
        back = new JButton("Back");
        back.setBounds(320, 200, 100, 30);
        back.addActionListener(this);
        add(back);
        
        setSize(600, 400);
        setLocation(300, 100);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == deposit) {
            String depositAmount = amount.getText();
            try {
                if(depositAmount.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter amount");
                } else {
                    Conn conn = new Conn();
                    String query = "INSERT INTO transactions VALUES('"+cardNumber+"', 'Deposit', '"+depositAmount+"', NOW())";
                    conn.s.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "$"+depositAmount+" Deposited Successfully");
                    setVisible(false);
                    new Transaction(cardNumber).setVisible(true);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            setVisible(false);
            new Transaction(cardNumber).setVisible(true);
        }
    }
}