package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.CounterStaff;
import util.FileUtil;

public class CounterStaffProfileFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);
    private static final Color FIELD = new Color(30, 30, 30);

    private final CounterStaff staff;
    private JTextField txtName, txtPassword, txtPhone, txtEmail, txtAddress;

    public CounterStaffProfileFrame(CounterStaff staff) {
        this.staff = staff;

        setTitle("Counter Staff Profile");
        setSize(720, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Edit Counter Staff Profile", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtName = createField(staff.getName());
        JTextField txtUsername = createField(staff.getUsername());
        txtUsername.setEditable(false);
        txtPassword = createField(staff.getPassword());
        JTextField txtGender = createField(staff.getGender());
        txtGender.setEditable(false);
        JTextField txtAge = createField(String.valueOf(staff.getAge()));
        txtAge.setEditable(false);
        txtPhone = createField(staff.getPhone());
        txtEmail = createField(staff.getEmail());
        txtAddress = createField(staff.getAddress());
        JTextField txtNationality = createField(staff.getNationality());
        txtNationality.setEditable(false);

        addRow(form, "Name", txtName);
        addRow(form, "Username", txtUsername);
        addRow(form, "Password", txtPassword);
        addRow(form, "Gender", txtGender);
        addRow(form, "Age", txtAge);
        addRow(form, "Phone", txtPhone);
        addRow(form, "Email", txtEmail);
        addRow(form, "Address", txtAddress);
        addRow(form, "Nationality", txtNationality);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(BG);

        JButton btnSave = createButton("Save Changes");
        JButton btnClose = createButton("Close");

        btnSave.addActionListener(e -> updateProfile());
        btnClose.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnClose);

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void updateProfile() {
        String filePath = "src/data/counterstaff.txt";
        ArrayList<String> lines = FileUtil.readAllLines(filePath);
        ArrayList<String> updated = new ArrayList<>();

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 10 && p[0].equals(staff.getId())) {
                String newLine = p[0] + "|" +
                        txtName.getText().trim() + "|" +
                        p[2] + "|" +
                        txtPassword.getText().trim() + "|" +
                        p[4] + "|" +
                        p[5] + "|" +
                        txtPhone.getText().trim() + "|" +
                        txtEmail.getText().trim() + "|" +
                        txtAddress.getText().trim() + "|" +
                        p[9];
                updated.add(newLine);
            } else {
                updated.add(line);
            }
        }

        FileUtil.writeAllLines(filePath, updated);
        JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        dispose();
    }

    private void addRow(JPanel panel, String label, JComponent field) {
        JLabel l = new JLabel(label);
        l.setForeground(TEXT);
        panel.add(l);
        panel.add(field);
    }

    private JTextField createField(String value) {
        JTextField field = new JTextField(value);
        field.setBackground(FIELD);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55, 80), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(GOLD);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        return btn;
    }
}