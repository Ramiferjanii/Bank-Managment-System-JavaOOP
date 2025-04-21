package project.bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;

public class SignUpOne extends JFrame implements ActionListener {
    private long formNumber;
    private JRadioButton male, female, married, unmarried, other;
    private JTextField nameField, fatherField, dobField, emailField, 
                      addressField, cityField, stateField, pinField;
    private JButton next, back;

    public SignUpOne() {
        setTitle("New Account Application");
        setSize(850, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Generate form number
        Random rand = new Random();
        formNumber = Math.abs(rand.nextLong() % 9000L) + 1000L;

        // Form number label
        JLabel formNoLabel = new JLabel("APPLICATION FORM NO. " + formNumber);
        formNoLabel.setFont(new Font("SansSerif", Font.BOLD, 38));
        formNoLabel.setBounds(100, 20, 650, 50);
        add(formNoLabel);

        // Page title
        JLabel pageLabel = new JLabel("Page 1: Personal Details");
        pageLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageLabel.setBounds(280, 80, 300, 30);
        add(pageLabel);

        // Form fields
        nameField = createTextField("Name:", 140, 140);
        fatherField = createTextField("Father's Name:", 140, 190);
        dobField = createTextField("Date of Birth (DD-MM-YYYY):", 140, 240);
        setupGenderFields();
        emailField = createTextField("Email:", 140, 340);
        setupMaritalStatusFields();
        addressField = createTextField("Address:", 140, 440);
        cityField = createTextField("City:", 140, 490);
        stateField = createTextField("State:", 140, 540);
        pinField = createTextField("PIN Code:", 140, 590);

        // Back button
        back = new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Raleway", Font.BOLD, 14));
        back.setBounds(500, 660, 80, 30);
        back.addActionListener(this);
        add(back);

        // Next button
        next = new JButton("Next");
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Raleway", Font.BOLD, 14));
        next.setBounds(620, 660, 80, 30);
        next.addActionListener(this);
        add(next);

        setVisible(true);
    }

    private JTextField createTextField(String label, int x, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        jLabel.setBounds(x, y, 200, 30);
        add(jLabel);
        
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBounds(x + 200, y, 300, 30);
        add(field);
        return field;
    }

    private void setupGenderFields() {
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        genderLabel.setBounds(140, 290, 200, 30);
        add(genderLabel);
        
        male = createRadioButton("Male", 300, 290);
        female = createRadioButton("Female", 450, 290);
        new ButtonGroup() {{ add(male); add(female); }};
    }

    private void setupMaritalStatusFields() {
        JLabel statusLabel = new JLabel("Marital Status:");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        statusLabel.setBounds(140, 390, 200, 30);
        add(statusLabel);
        
        married = createRadioButton("Married", 300, 390);
        unmarried = createRadioButton("Unmarried", 450, 390);
        other = createRadioButton("Other", 600, 390);
        new ButtonGroup() {{ add(married); add(unmarried); add(other); }};
    }

    private JRadioButton createRadioButton(String text, int x, int y) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(x, y, 120, 30);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rb.setBackground(Color.WHITE);
        add(rb);
        return rb;
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == back) {
            this.dispose();
            new Login().setVisible(true);
            return;
        }
        
        String name = nameField.getText().trim();
        String father = fatherField.getText().trim();
        String dob = dobField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String pin = pinField.getText().trim();
        String gender = getGender();
        String maritalStatus = getMaritalStatus();

        if (validateInputs(name, father, dob, email, address, city, state, pin, gender, maritalStatus)) {
            try {
                saveToDatabase(name, father, dob, email, address, city, state, pin, gender, maritalStatus);
                JOptionPane.showMessageDialog(this, "Data saved successfully!");
                this.dispose();
                new SignupTwo(String.valueOf(formNumber)).setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            }
        }
    }

    private boolean validateInputs(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return false;
            }
        }
        if (!fields[7].matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "PIN must be 4 digits!");
            return false;
        }
        return true;
    }

    private String getGender() {
        return male.isSelected() ? "M" : female.isSelected() ? "F" : "";
    }

    private String getMaritalStatus() {
        if (married.isSelected()) return "Married";
        if (unmarried.isSelected()) return "Unmarried";
        return other.isSelected() ? "Other" : "";
    }

    private void saveToDatabase(String name, String father, String dob, String email,
                               String address, String city, String state, String pin,
                               String gender, String maritalStatus) throws SQLException {
        Conn conn = new Conn();
        String signupQuery = "INSERT INTO signup VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        String loginQuery = "INSERT INTO login (card_number, pin) VALUES (?, ?)";
        
        try {
            // Insert into signup table
            try (PreparedStatement ps = conn.prepareStatement(signupQuery)) {
                ps.setLong(1, formNumber);
                ps.setString(2, name);
                ps.setString(3, father);
                ps.setString(4, dob);
                ps.setString(5, email);
                ps.setString(6, address);
                ps.setString(7, city);
                ps.setString(8, state);
                ps.setString(9, pin);
                ps.setString(10, gender);
                ps.setString(11, maritalStatus);
                ps.executeUpdate();
            }
            
            // Insert into login table
            try (PreparedStatement psLogin = conn.prepareStatement(loginQuery)) {
                psLogin.setString(1, String.valueOf(formNumber));
                psLogin.setString(2, pin);
                psLogin.executeUpdate();
            }
            
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        new SignUpOne();
    }
}