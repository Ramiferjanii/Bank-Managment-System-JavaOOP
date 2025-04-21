package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FastCash extends JFrame implements ActionListener {
    JButton rs100, rs500, rs1000, rs2000, rs5000, rs10000, back;
    String cardNumber;
    
    FastCash(String cardNumber) {
        this.cardNumber = cardNumber;
        
        setLayout(null);
        
        JLabel text = new JLabel("Select withdrawal amount");
        text.setFont(new Font("Raleway", Font.BOLD, 16));
        text.setBounds(170, 50, 400, 20);
        add(text);
        
        rs100 = new JButton("Rs 100");
        rs100.setBounds(170, 100, 100, 30);
        rs100.addActionListener(this);
        add(rs100);
        
        rs500 = new JButton("Rs 500");
        rs500.setBounds(320, 100, 100, 30);
        rs500.addActionListener(this);
        add(rs500);
        
        rs1000 = new JButton("Rs 1000");
        rs1000.setBounds(170, 150, 100, 30);
        rs1000.addActionListener(this);
        add(rs1000);
        
        rs2000 = new JButton("Rs 2000");
        rs2000.setBounds(320, 150, 100, 30);
        rs2000.addActionListener(this);
        add(rs2000);
        
        rs5000 = new JButton("Rs 5000");
        rs5000.setBounds(170, 200, 100, 30);
        rs5000.addActionListener(this);
        add(rs5000);
        
        rs10000 = new JButton("Rs 10000");
        rs10000.setBounds(320, 200, 100, 30);
        rs10000.addActionListener(this);
        add(rs10000);
        
        back = new JButton("Back");
        back.setBounds(250, 250, 100, 30);
        back.addActionListener(this);
        add(back);
        
        setSize(600, 400);
        setLocation(300, 100);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == back) {
            setVisible(false);
            new Transaction(cardNumber).setVisible(true);
        } else {
            String amount = ((JButton)ae.getSource()).getText().substring(3);
            new Withdraw(cardNumber).amount.setText(amount);
            setVisible(false);
        }
    }
}