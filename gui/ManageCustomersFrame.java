package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import util.FileUtil;
import util.IdGenerator;

public class ManageCustomersFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);
    private static final Color FIELD = new Color(30, 30, 30);

    private JTable table;
    private DefaultTableModel model;
    private final String filePath = "src/data/customers.txt";

    public ManageCustomersFrame() {
        setTitle("Manage Customers");
        setSize(1020, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Manage Customers", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        model = new DefaultTableModel(new String[]{
                "Customer ID", "Name", "Username", "Gender", "Age", "Phone", "Email", "Address", "Nationality"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        styleTable(table);
        loadCustomers();

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(BG);

        JButton btnAdd = createButton("Add Customer");
        JButton btnDelete = createButton("Delete Selected");
        JButton btnRefresh = createButton("Refresh");
        JButton btnClose = createButton("Close");

        btnAdd.addActionListener(e -> addCustomerDialog());
        btnDelete.addActionListener(e -> deleteSelectedCustomer());
        btnRefresh.addActionListener(e -> loadCustomers());
        btnClose.addActionListener(e -> dispose());

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClose);

        main.add(title, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void loadCustomers() {
        model.setRowCount(0);
        ArrayList<String> lines = FileUtil.readAllLines(filePath);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 10) {
                model.addRow(new Object[]{p[0], p[1], p[2], p[4], p[5], p[6], p[7], p[8], p[9]});
            }
        }
    }

    private void addCustomerDialog() {
        JTextField txtName = createField("");
        JTextField txtUsername = createField("");
        JTextField txtPassword = createField("");
        JTextField txtGender = createField("");
        JTextField txtAge = createField("");
        JTextField txtPhone = createField("");
        JTextField txtEmail = createField("");
        JTextField txtAddress = createField("");
        JTextField txtNationality = createField("");

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Name")); panel.add(txtName);
        panel.add(new JLabel("Username")); panel.add(txtUsername);
        panel.add(new JLabel("Password")); panel.add(txtPassword);
        panel.add(new JLabel("Gender")); panel.add(txtGender);
        panel.add(new JLabel("Age")); panel.add(txtAge);
        panel.add(new JLabel("Phone")); panel.add(txtPhone);
        panel.add(new JLabel("Email")); panel.add(txtEmail);
        panel.add(new JLabel("Address")); panel.add(txtAddress);
        panel.add(new JLabel("Nationality")); panel.add(txtNationality);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Customer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String id = IdGenerator.generateNextId("C", filePath);
            String line = id + "|" +
                    txtName.getText().trim() + "|" +
                    txtUsername.getText().trim() + "|" +
                    txtPassword.getText().trim() + "|" +
                    txtGender.getText().trim() + "|" +
                    txtAge.getText().trim() + "|" +
                    txtPhone.getText().trim() + "|" +
                    txtEmail.getText().trim() + "|" +
                    txtAddress.getText().trim() + "|" +
                    txtNationality.getText().trim();

            FileUtil.appendLine(filePath, line);
            JOptionPane.showMessageDialog(this, "Customer added successfully.");
            loadCustomers();
        }
    }

    private void deleteSelectedCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer first.");
            return;
        }

        String customerId = model.getValueAt(row, 0).toString();
        ArrayList<String> lines = FileUtil.readAllLines(filePath);
        ArrayList<String> updated = new ArrayList<>();

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (!p[0].equals(customerId)) {
                updated.add(line);
            }
        }

        FileUtil.writeAllLines(filePath, updated);
        JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
        loadCustomers();
    }

    private JTextField createField(String value) {
        JTextField field = new JTextField(value);
        field.setBackground(FIELD);
        field.setForeground(TEXT);
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(GOLD);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(FIELD);
        table.setForeground(TEXT);
        table.setGridColor(new Color(70, 70, 70));
        table.setRowHeight(28);
        table.getTableHeader().setBackground(new Color(35, 35, 35));
        table.getTableHeader().setForeground(GOLD);
    }
}