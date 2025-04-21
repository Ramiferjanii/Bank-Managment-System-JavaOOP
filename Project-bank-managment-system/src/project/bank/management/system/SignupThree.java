package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random; 

public class SignupThree extends JFrame implements ActionListener {
    private String formNumber;
    private JRadioButton savingAccount, fixedDeposit, currentAccount, recurringDeposit;
    private JCheckBox atmCard, internetBanking, mobileBanking, emailAlert, chequeBook, eStatement;
    private JButton submit;
    private JTextField accountNumberField;

    public SignupThree(String formNumber) {
        this.formNumber = formNumber;
        
        setTitle("Account Creation - Final Page");
        setSize(850, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Header
        JLabel header = new JLabel("Page 3: Account Details");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setBounds(280, 20, 400, 40);
        add(header);

        // Account Type
        addLabel("Account Type:", 140, 80);
        savingAccount = createRadio("Savings Account", 300, 80);
        fixedDeposit = createRadio("Fixed Deposit Account", 450, 80);
        currentAccount = createRadio("Current Account", 300, 120);
        recurringDeposit = createRadio("Recurring Deposit Account", 450, 120);
        new ButtonGroup() {{ add(savingAccount); add(fixedDeposit); add(currentAccount); add(recurringDeposit); }};

        // Services
        addLabel("Services Required:", 140, 180);
        atmCard = createCheckbox("ATM Card", 300, 180);
        internetBanking = createCheckbox("Internet Banking", 450, 180);
        mobileBanking = createCheckbox("Mobile Banking", 300, 220);
        emailAlert = createCheckbox("Email Alerts", 450, 220);
        chequeBook = createCheckbox("Cheque Book", 300, 260);
        eStatement = createCheckbox("E-Statement", 450, 260);

        // Account Number
        addLabel("Account Number:", 140, 320);
        accountNumberField = createTextField(300, 320);
        accountNumberField.setEditable(false);
        generateAccountNumber();

        // Submit Button
        submit = new JButton("Submit Application");
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Raleway", Font.BOLD, 14));
        submit.setBounds(300, 400, 200, 30);
        submit.addActionListener(this);
        add(submit);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBounds(x, y, 150, 30);
        add(label);
    }

    private JRadioButton createRadio(String text, int x, int y) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(x, y, 200, 30);
        rb.setBackground(Color.WHITE);
        add(rb);
        return rb;
    }

    private JCheckBox createCheckbox(String text, int x, int y) {
        JCheckBox cb = new JCheckBox(text);
        cb.setBounds(x, y, 150, 30);
        cb.setBackground(Color.WHITE);
        add(cb);
        return cb;
    }

    private JTextField createTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 30);
        add(tf);
        return tf;
    }

     private void generateAccountNumber() {
        Random rand = new Random();
        StringBuilder accountNumber = new StringBuilder();
        
        // First digit (1-9 to avoid leading zero)
        accountNumber.append(rand.nextInt(9) + 1);
        
        // Remaining 15 digits
        for(int i = 0; i < 15; i++) {
            accountNumber.append(rand.nextInt(10));
        }
        
        accountNumberField.setText(accountNumber.toString());
    }

    

    public void actionPerformed(ActionEvent ae) {
    if (!validateSelections()) {
        return;
    }

    try (Conn conn = new Conn()) {
        String accountNumber = accountNumberField.getText();

        // 1. Insert into signup_three
        String query = "INSERT INTO signup_three VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, formNumber);
            ps.setString(2, accountNumber);
            ps.setString(3, getAccountType());
            ps.setString(4, getServices());
            ps.setString(5, atmCard.isSelected() ? "Yes" : "No");
            ps.setString(6, internetBanking.isSelected() ? "Yes" : "No");
            ps.setString(7, mobileBanking.isSelected() ? "Yes" : "No");
            ps.setString(8, emailAlert.isSelected() ? "Yes" : "No");
            ps.setString(9, chequeBook.isSelected() ? "Yes" : "No");
            ps.setString(10, eStatement.isSelected() ? "Yes" : "No");
            ps.executeUpdate();
        }

        // 2. Update login table with account number
        String updateQuery = "UPDATE login SET account_number = ? WHERE card_number = ?";
        try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
            psUpdate.setString(1, accountNumber);
            psUpdate.setString(2, formNumber);
            psUpdate.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Account Created Successfully!\nAccount Number: " + accountNumber);
        this.dispose();
        new Login().setVisible(true);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
}

    private boolean validateSelections() {
        if (!savingAccount.isSelected() && !fixedDeposit.isSelected() && 
            !currentAccount.isSelected() && !recurringDeposit.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an account type!");
            return false;
        }
        
        if (!atmCard.isSelected() && !internetBanking.isSelected() && 
            !mobileBanking.isSelected() && !emailAlert.isSelected() && 
            !chequeBook.isSelected() && !eStatement.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select at least one service!");
            return false;
        }
        return true;
    }

    private String getAccountType() {
        if (savingAccount.isSelected()) return "Savings";
        if (fixedDeposit.isSelected()) return "Fixed Deposit";
        if (currentAccount.isSelected()) return "Current";
        if (recurringDeposit.isSelected()) return "Recurring Deposit";
        return "";
    }

    private String getServices() {
        StringBuilder services = new StringBuilder();
        if (atmCard.isSelected()) services.append("ATM Card, ");
        if (internetBanking.isSelected()) services.append("Internet Banking, ");
        if (mobileBanking.isSelected()) services.append("Mobile Banking, ");
        if (emailAlert.isSelected()) services.append("Email Alerts, ");
        if (chequeBook.isSelected()) services.append("Cheque Book, ");
        if (eStatement.isSelected()) services.append("E-Statement");
        return services.toString().replaceAll(", $", "");
    }

    public static void main(String[] args) {
        new SignupThree("");
    }
}