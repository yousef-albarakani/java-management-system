package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import util.FileUtil;
import util.IdGenerator;

public class ManageStaffFrame extends JFrame {

    private static final Color BG_DEEP     = new Color(10, 10, 10);
    private static final Color CARD_BG     = new Color(22, 22, 22);
    private static final Color CARD_EDGE   = new Color(30, 30, 30);
    private static final Color GOLD        = new Color(212, 175, 55);
    private static final Color GOLD_DARK   = new Color(160, 130, 30);
    private static final Color TEXT_MAIN   = new Color(240, 240, 235);
    private static final Color TEXT_MUTED  = new Color(150, 150, 140);
    private static final Color FIELD_BG    = new Color(30, 30, 30);
    private static final Color FIELD_BORDER= new Color(212, 175, 55, 60);
    private static final Color FIELD_FOCUS = new Color(212, 175, 55, 160);

    private JTable table;
    private DefaultTableModel tableModel;

    public ManageStaffFrame() {
        setTitle("Manage Staff");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = createBackgroundPanel();
        main.setLayout(new BorderLayout());
        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createContent(), BorderLayout.CENTER);

        add(main);
        setVisible(true);
        loadStaffData();
    }

    // ── Background ────────────────────────────────────────────────────
    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(212, 175, 55, 10));
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };
    }

    // ── Header ────────────────────────────────────────────────────────
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 25, 18, 25));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Manage Staff");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 24));

        JLabel lblSub = new JLabel("Add, view and manage managers, counter staff and technicians");
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);

        JButton closeBtn = createSidebarButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.addActionListener(e -> dispose());

        panel.add(left, BorderLayout.WEST);
        panel.add(closeBtn, BorderLayout.EAST);
        return panel;
    }

    // ── Content ───────────────────────────────────────────────────────
    private JPanel createContent() {
        JPanel wrapper = new JPanel(new BorderLayout(20, 20));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(10, 25, 25, 25));
        wrapper.add(createLeftPanel(),  BorderLayout.WEST);
        wrapper.add(createTablePanel(), BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createLeftPanel() {
        JPanel panel = createCardPanel();
        panel.setPreferredSize(new Dimension(290, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Staff Controls");
        heading.setForeground(GOLD);
        heading.setFont(new Font("Georgia", Font.BOLD, 20));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html>Manage staff records for:<br>Manager, Counter Staff, Technician</html>");
        desc.setForeground(TEXT_MUTED);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnViewManagers = createSidebarButton("View Managers");
        btnViewManagers.addActionListener(e -> loadSpecificFile("src/data/managers.txt", "Manager"));

        JButton btnViewCounter = createSidebarButton("View Counter Staff");
        btnViewCounter.addActionListener(e -> loadSpecificFile("src/data/counterstaff.txt", "Counter Staff"));

        JButton btnViewTech = createSidebarButton("View Technicians");
        btnViewTech.addActionListener(e -> loadSpecificFile("src/data/technicians.txt", "Technician"));

        JButton btnLoadAll = createSidebarButton("Load All Staff");
        btnLoadAll.addActionListener(e -> loadStaffData());

        JButton btnAdd = createSidebarButton("Add Staff");
        btnAdd.addActionListener(e -> openAddStaffDialog());

        JButton btnDelete = createSidebarButton("Delete Selected Staff");
        btnDelete.addActionListener(e -> deleteSelectedStaff());

        JButton btnRefresh = createSidebarButton("Refresh Table");
        btnRefresh.addActionListener(e -> loadStaffData());

        panel.add(heading);
        panel.add(Box.createVerticalStrut(8));
        panel.add(desc);
        panel.add(Box.createVerticalStrut(25));
        panel.add(btnViewManagers);  panel.add(Box.createVerticalStrut(10));
        panel.add(btnViewCounter);   panel.add(Box.createVerticalStrut(10));
        panel.add(btnViewTech);      panel.add(Box.createVerticalStrut(10));
        panel.add(btnLoadAll);       panel.add(Box.createVerticalStrut(20));
        panel.add(btnAdd);           panel.add(Box.createVerticalStrut(10));
        panel.add(btnDelete);        panel.add(Box.createVerticalStrut(10));
        panel.add(btnRefresh);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = createCardPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("Staff Records");
        heading.setForeground(GOLD);
        heading.setFont(new Font("Georgia", Font.BOLD, 20));

        String[] columns = {"Type", "ID", "Name", "Username", "Gender", "Age", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(FIELD_BG);
        sp.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55, 60), 1));

        panel.add(heading, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ── Data Loading ──────────────────────────────────────────────────
    private void loadStaffData() {
        tableModel.setRowCount(0);
        loadSpecificFileToTable("src/data/managers.txt",    "Manager");
        loadSpecificFileToTable("src/data/counterstaff.txt","Counter Staff");
        loadSpecificFileToTable("src/data/technicians.txt", "Technician");
    }

    private void loadSpecificFile(String filePath, String type) {
        tableModel.setRowCount(0);
        loadSpecificFileToTable(filePath, type);
    }

    private void loadSpecificFileToTable(String filePath, String type) {
        ArrayList<String> lines = FileUtil.readAllLines(filePath);
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] p = line.split("\\|");
            if (p.length >= 8) {
                tableModel.addRow(new Object[]{type, p[0], p[1], p[2], p[4], p[5], p[6], p[7]});
            }
        }
    }

    // ── Add Staff Dialog ──────────────────────────────────────────────
    private void openAddStaffDialog() {
        JDialog dialog = new JDialog(this, "Add Staff", true);
        dialog.setSize(520, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        // ── Dark wrapper ──────────────────────────────────────────────
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(212, 175, 55, 8));
                for (int x = 0; x < getWidth(); x += 30) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 30) g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 24, 16, 24));

        // Title
        JLabel lblTitle = new JLabel("Add New Staff");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(0, 0, 14, 0));

        // ── Form using GridBagLayout for proper row heights ────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints lc = new GridBagConstraints(); // label constraints
        lc.gridx = 0; lc.fill = GridBagConstraints.HORIZONTAL;
        lc.weightx = 0.35; lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(6, 0, 6, 10);

        GridBagConstraints fc = new GridBagConstraints(); // field constraints
        fc.gridx = 1; fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 0.65; fc.anchor = GridBagConstraints.WEST;
        fc.insets = new Insets(6, 0, 6, 0);

        // Role dropdown
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Manager", "Counter Staff", "Technician"});
        cmbRole.setBackground(FIELD_BG);
        cmbRole.setForeground(TEXT_MAIN);
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbRole.setBorder(BorderFactory.createLineBorder(FIELD_BORDER, 1));
        ((JLabel) cmbRole.getRenderer()).setOpaque(true);
        ((JLabel) cmbRole.getRenderer()).setBackground(FIELD_BG);

        JTextField txtName           = createDialogField();
        JTextField txtUsername       = createDialogField();
        JTextField txtPassword       = createDialogField();
        JTextField txtGender         = createDialogField();
        JTextField txtAge            = createDialogField();
        JTextField txtPhone          = createDialogField();
        JTextField txtEmail          = createDialogField();
        JTextField txtAddress        = createDialogField();
        JTextField txtNationality    = createDialogField();
        JTextField txtSpecialization = createDialogField();
        JTextField txtStatus         = createDialogField();
        txtSpecialization.setText("General Service");
        txtStatus.setText("Available");

        // Show/hide Specialization & Status based on role
        JLabel lblSpec   = makeDialogLabel("Specialization");
        JLabel lblStatus = makeDialogLabel("Status");

        cmbRole.addActionListener(e -> {
            boolean isTech = "Technician".equals(cmbRole.getSelectedItem());
            lblSpec.setVisible(isTech);
            txtSpecialization.setVisible(isTech);
            lblStatus.setVisible(isTech);
            txtStatus.setVisible(isTech);
        });

        int row = 0;
        addFormRow(form, lc, fc, row++, makeDialogLabel("Role"),          cmbRole);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Name"),          txtName);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Username"),      txtUsername);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Password"),      txtPassword);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Gender"),        txtGender);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Age"),           txtAge);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Phone"),         txtPhone);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Email"),         txtEmail);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Address"),       txtAddress);
        addFormRow(form, lc, fc, row++, makeDialogLabel("Nationality"),   txtNationality);
        addFormRow(form, lc, fc, row++, lblSpec,                          txtSpecialization);
        addFormRow(form, lc, fc, row++, lblStatus,                        txtStatus);

        // Initially hide spec/status (Manager selected by default)
        lblSpec.setVisible(false);   txtSpecialization.setVisible(false);
        lblStatus.setVisible(false); txtStatus.setVisible(false);

        // Scrollable form
        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formScroll.setBorder(BorderFactory.createLineBorder(FIELD_BORDER, 1));
        formScroll.getVerticalScrollBar().setUnitIncrement(14);

        // ── Buttons ───────────────────────────────────────────────────
        JButton btnSave   = createGoldButton("SAVE");
        JButton btnCancel = createGhostButton("CANCEL");
        btnSave.setPreferredSize(new Dimension(110, 38));
        btnCancel.setPreferredSize(new Dimension(110, 38));

        btnSave.addActionListener(e -> {
            String role          = cmbRole.getSelectedItem().toString();
            String name          = txtName.getText().trim();
            String username      = txtUsername.getText().trim();
            String password      = txtPassword.getText().trim();
            String gender        = txtGender.getText().trim();
            String age           = txtAge.getText().trim();
            String phone         = txtPhone.getText().trim();
            String email         = txtEmail.getText().trim();
            String address       = txtAddress.getText().trim();
            String nationality   = txtNationality.getText().trim();
            String specialization= txtSpecialization.getText().trim();
            String status        = txtStatus.getText().trim();

            // Validation
            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || gender.isEmpty()
                    || age.isEmpty() || phone.isEmpty() || email.isEmpty()
                    || address.isEmpty() || nationality.isEmpty()) {
                showDialogMessage(dialog, "Please fill all required fields.");
                return;
            }

            try {
                Integer.parseInt(age);
            } catch (NumberFormatException ex) {
                showDialogMessage(dialog, "Age must be a valid number.");
                return;
            }

            String filePath;
            String id;
            String line;

            if (role.equals("Manager")) {
                filePath = "src/data/managers.txt";
                id = IdGenerator.generateNextId("M", filePath);
                line = id + "|" + name + "|" + username + "|" + password + "|"
                     + gender + "|" + age + "|" + phone + "|" + email + "|"
                     + address + "|" + nationality;

            } else if (role.equals("Counter Staff")) {
                filePath = "src/data/counterstaff.txt";
                id = IdGenerator.generateNextId("CS", filePath);
                line = id + "|" + name + "|" + username + "|" + password + "|"
                     + gender + "|" + age + "|" + phone + "|" + email + "|"
                     + address + "|" + nationality;

            } else { // Technician
                filePath = "src/data/technicians.txt";
                id = IdGenerator.generateNextId("T", filePath);
                if (specialization.isEmpty()) specialization = "General Service";
                if (status.isEmpty())         status         = "Available";
                line = id + "|" + name + "|" + username + "|" + password + "|"
                     + gender + "|" + age + "|" + phone + "|" + email + "|"
                     + address + "|" + nationality + "|" + specialization + "|" + status;
            }

            System.out.println("[ManageStaffFrame] Writing to: " + filePath);
            System.out.println("[ManageStaffFrame] Line: " + line);

            FileUtil.appendLine(filePath, line);
            showDialogMessage(dialog, "Staff added successfully! ID: " + id);
            dialog.dispose();
            loadStaffData();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(14, 0, 0, 0));
        btnRow.add(btnSave);
        btnRow.add(btnCancel);

        wrapper.add(lblTitle,    BorderLayout.NORTH);
        wrapper.add(formScroll,  BorderLayout.CENTER);
        wrapper.add(btnRow,      BorderLayout.SOUTH);

        dialog.getContentPane().setBackground(CARD_BG);
        dialog.add(wrapper);
        dialog.setVisible(true);
    }

    private void addFormRow(JPanel form, GridBagConstraints lc, GridBagConstraints fc,
                            int row, JLabel label, JComponent field) {
        lc.gridy = row;
        fc.gridy = row;
        form.add(label, lc);
        form.add(field, fc);
    }

    private JLabel makeDialogLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(212, 175, 55, 180));
        lbl.setFont(new Font("Arial Narrow", Font.PLAIN, 12));
        return lbl;
    }

    // ── Delete ────────────────────────────────────────────────────────
    private void deleteSelectedStaff() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) { showMainMessage("Please select a staff row first."); return; }

        String type = tableModel.getValueAt(selectedRow, 0).toString();
        String id   = tableModel.getValueAt(selectedRow, 1).toString();

        String filePath;
        if      (type.equals("Manager"))      filePath = "src/data/managers.txt";
        else if (type.equals("Counter Staff")) filePath = "src/data/counterstaff.txt";
        else                                   filePath = "src/data/technicians.txt";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete staff ID: " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        ArrayList<String> lines = FileUtil.readAllLines(filePath);
        ArrayList<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length > 0 && !p[0].equals(id)) updated.add(line);
        }

        FileUtil.writeAllLines(filePath, updated);
        showMainMessage("Staff deleted successfully.");
        loadStaffData();
    }

    // ── Field / Button Factories ──────────────────────────────────────
    private JTextField createDialogField() {
        JTextField f = new JTextField() {
            boolean focused = false;
            { addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { focused = true;  repaint(); }
                public void focusLost (FocusEvent e)  { focused = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(focused ? FIELD_FOCUS : FIELD_BORDER);
                g2.setStroke(new BasicStroke(focused ? 1.2f : 0.8f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setOpaque(false);
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(GOLD);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(new EmptyBorder(8, 12, 8, 12));
        f.setPreferredSize(new Dimension(0, 38));
        return f;
    }

    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = hovered ? new Color(230,195,70) : GOLD;
                Color c2 = hovered ? new Color(180,148,35) : GOLD_DARK;
                g2.setPaint(new GradientPaint(0,0,c1,0,getHeight(),c2));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(new Color(15,15,15));
        btn.setFont(new Font("Arial Narrow", Font.BOLD, 13));
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) { g2.setColor(new Color(212,175,55,18)); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8); }
                g2.setColor(hovered ? new Color(212,175,55,140) : new Color(212,175,55,65));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(new Color(212,175,55,190));
        btn.setFont(new Font("Arial Narrow", Font.BOLD, 12));
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(GOLD);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return btn;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(new Color(212, 175, 55, 55));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    // ── Message Helpers ───────────────────────────────────────────────
    private void showMainMessage(String msg) {
        UIManager.put("OptionPane.background",        CARD_BG);
        UIManager.put("Panel.background",             CARD_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
        UIManager.put("Button.background",            GOLD);
        UIManager.put("Button.foreground",            Color.BLACK);
        JOptionPane.showMessageDialog(this, msg, "APU Automotive", JOptionPane.PLAIN_MESSAGE);
    }

    private void showDialogMessage(JDialog parent, String msg) {
        UIManager.put("OptionPane.background",        CARD_BG);
        UIManager.put("Panel.background",             CARD_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
        UIManager.put("Button.background",            GOLD);
        UIManager.put("Button.foreground",            Color.BLACK);
        JOptionPane.showMessageDialog(parent, msg, "APU Automotive", JOptionPane.PLAIN_MESSAGE);
    }

    private void styleTable(JTable tbl) {
        tbl.setBackground(FIELD_BG);
        tbl.setForeground(TEXT_MAIN);
        tbl.setGridColor(new Color(70, 70, 70));
        tbl.setRowHeight(28);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.getTableHeader().setBackground(new Color(35, 35, 35));
        tbl.getTableHeader().setForeground(GOLD);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tbl.setSelectionBackground(new Color(212, 175, 55, 80));
        tbl.setSelectionForeground(TEXT_MAIN);
    }
}