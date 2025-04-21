package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignupTwo extends JFrame implements ActionListener {
    private String formNumber;
    private JComboBox<String> religionCombo, categoryCombo, incomeCombo, educationCombo;
    private JTextField panField, aadharField;
    private JRadioButton seniorYes, seniorNo, existingYes, existingNo;

    public SignupTwo(String formNumber) {
        this.formNumber = formNumber;
        
        setTitle("New Account Application - Page 2");
        setSize(850, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        addFormHeader();
        addComponents();
        setVisible(true);
    }

    private void addFormHeader() {
        JLabel header = new JLabel("Page 2: Additional Details");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setBounds(280, 20, 400, 40);
        add(header);

        JLabel formNumLabel = new JLabel("Form Number: " + formNumber);
        formNumLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formNumLabel.setBounds(20, 20, 200, 30);
        add(formNumLabel);
    }

    private void addComponents() {
        int yPos = 80;
        final int yStep = 60;

        addLabel("Religion:", 140, yPos);
        religionCombo = createComboBox(new String[]{"Hindu", "Muslim", "Sikh", "Christian", "Other"}, 300, yPos);
        yPos += yStep;

        addLabel("Category:", 140, yPos);
        categoryCombo = createComboBox(new String[]{"General", "OBC", "SC", "ST", "Other"}, 300, yPos);
        yPos += yStep;

        addLabel("Income:", 140, yPos);
        incomeCombo = createComboBox(new String[]{"Select", "< ₹1.5L", "< ₹2.5L", "< ₹5L", "> ₹5L"}, 300, yPos);
        yPos += yStep;

        addLabel("Education:", 140, yPos);
        educationCombo = createComboBox(new String[]{"Non-Graduate", "Graduate", "Post-Graduate", "Doctorate"}, 300, yPos);
        yPos += yStep;

        addLabel("PAN Number:", 140, yPos);
        panField = createTextField(300, yPos);
        yPos += yStep;

        addLabel("Aadhar Number:", 140, yPos);
        aadharField = createTextField(300, yPos);
        yPos += yStep;

        addLabel("Senior Citizen:", 140, yPos);
        seniorYes = createRadio("Yes", 300, yPos);
        seniorNo = createRadio("No", 400, yPos);
        new ButtonGroup() {{ add(seniorYes); add(seniorNo); }};
        yPos += yStep;

        addLabel("Existing Account:", 140, yPos);
        existingYes = createRadio("Yes", 300, yPos);
        existingNo = createRadio("No", 400, yPos);
        new ButtonGroup() {{ add(existingYes); add(existingNo); }};

        JButton next = new JButton("Next");
        next.setBounds(650, 680, 100, 30);
        next.addActionListener(this);
        add(next);
    }

    // Helper method to add labels
    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBounds(x, y, 200, 30);
        add(label);
    }

    // Helper method to create combo boxes
    private JComboBox<String> createComboBox(String[] items, int x, int y) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBounds(x, y, 300, 30);
        combo.setBackground(Color.WHITE);
        add(combo);
        return combo;
    }

    // Helper method to create text fields
    private JTextField createTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 300, 30);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(textField);
        return textField;
    }

    // Helper method to create radio buttons
    private JRadioButton createRadio(String text, int x, int y) {
        JRadioButton radio = new JRadioButton(text);
        radio.setBounds(x, y, 100, 30);
        radio.setBackground(Color.WHITE);
        add(radio);
        return radio;
    }

    public void actionPerformed(ActionEvent ae) {
        if (!aadharField.getText().matches("\\d{12}")) {
    JOptionPane.showMessageDialog(this, "Aadhar must be 12 digits!");
    return;
}
        
    try {
        Conn conn = new Conn();
        // Explicitly list columns to avoid ambiguity
        String query = "INSERT INTO signup_two (form_number, religion, category, income, education, pan, aadhar, senior_citizen, existing_account) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(query);
        
        ps.setString(1, formNumber);
        ps.setString(2, (String) religionCombo.getSelectedItem());
        ps.setString(3, (String) categoryCombo.getSelectedItem());
        ps.setString(4, (String) incomeCombo.getSelectedItem());
        ps.setString(5, (String) educationCombo.getSelectedItem());
        ps.setString(6, panField.getText());
        ps.setString(7, aadharField.getText());
        ps.setString(8, seniorYes.isSelected() ? "Yes" : "No");
        ps.setString(9, existingYes.isSelected() ? "Yes" : "No");
        
        ps.executeUpdate();
        
        new SignupThree(formNumber).setVisible(true);
        this.dispose();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
}

    public static void main(String[] args) {
        new SignupTwo("");
    }
}