package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Transaction extends JFrame implements ActionListener {
    JButton deposit, withdraw, fastcash, ministatement, pinchange, balance, exit;
    String cardNumber;
    
    Transaction(String cardNumber) {
        this.cardNumber = cardNumber;
        
        // Window setup
        setLayout(null);
        setTitle("ATM Interface");
        
        // ATM Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 900, 900);
        add(image);

        // Instruction Text
        JLabel text = new JLabel("Please select your Transaction");
        text.setBounds(210, 300, 700, 35);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("System", Font.BOLD, 16));
        image.add(text);

        // Transaction Buttons
        deposit = createButton("Deposit", 170, 415);
        withdraw = createButton("Cash Withdraw", 355, 415);
        fastcash = createButton("Fast Cash", 170, 450);
        ministatement = createButton("Mini Statement", 355, 450);
        pinchange = createButton("PIN Change", 170, 485);
        balance = createButton("Balance Enquiry", 355, 485);
        exit = createButton("Exit", 170, 520);

        // Window settings
        setSize(900, 900);
        setLocation(300, 0);
        setUndecorated(true);
        setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 30);
        button.addActionListener(this);
        button.setFont(new Font("Raleway", Font.BOLD, 12));
        add(button);
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == exit) {
            System.exit(0);
        } else {
            String buttonText = ((JButton)ae.getSource()).getText();
            handleTransaction(buttonText);
        }
    }

    private void handleTransaction(String transactionType) {
        setVisible(false);
        switch(transactionType) {
            case "Deposit":
                new Deposit(cardNumber).setVisible(true);
                break;
            case "Cash Withdraw":
                new Withdraw(cardNumber).setVisible(true);
                break;
            case "Fast Cash":
                new FastCash(cardNumber).setVisible(true);
                break;
            case "Mini Statement":
                new MiniStatement(cardNumber).setVisible(true);
                break;
            case "PIN Change":
                new PinChange(cardNumber).setVisible(true);
                break;
            case "Balance Enquiry":
                new BalanceEnquiry(cardNumber).setVisible(true);
                break;
        }
    }

    public static void main(String[] args) {
        new Transaction(""); // For testing purposes only
    }
}