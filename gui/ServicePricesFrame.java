package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import util.FileUtil;

public class ServicePricesFrame extends JFrame {

    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color GOLD_DARK  = new Color(160, 130, 30);
    private static final Color TEXT_MAIN  = new Color(240, 240, 235);
    private static final Color TEXT_MUTED = new Color(150, 150, 140);
    private static final Color FIELD_BG   = new Color(30, 30, 30);
    private static final Color FIELD_BORDER = new Color(212, 175, 55, 80);

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;

    private final String servicesFile = "src/data/services.txt";

    public ServicePricesFrame() {
        setTitle("Set Service Prices");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = createBackgroundPanel();
        main.setLayout(new BorderLayout());

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createContent(), BorderLayout.CENTER);

        add(main);
        setVisible(true);

        loadServices();
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
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

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 25, 18, 25));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Set Service Prices");
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JLabel sub = new JLabel("Manage prices for all automotive services");
        sub.setForeground(TEXT_MUTED);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(sub);

        JButton close = createButton("Close");
        close.addActionListener(e -> dispose());

        panel.add(left, BorderLayout.WEST);
        panel.add(close, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContent() {
        JPanel outer = new JPanel(new BorderLayout(20, 20));
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(10, 25, 25, 25));

        JPanel left = createCardPanel();
        left.setPreferredSize(new Dimension(280, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel("Price Controls");
        lbl.setForeground(GOLD);
        lbl.setFont(new Font("Georgia", Font.BOLD, 20));

        JLabel info = new JLabel("<html>Manager can view, search and update service pricing from here.</html>");
        info.setForeground(TEXT_MUTED);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtSearch = createTextField();

        JButton btnSearch = createButton("Search Service");
        btnSearch.addActionListener(e -> searchService());

        JButton btnRefresh = createButton("Refresh Services");
        btnRefresh.addActionListener(e -> loadServices());

        JButton btnUpdate = createButton("Update Selected Price");
        btnUpdate.addActionListener(e -> updateSelectedPrice());

        left.add(lbl);
        left.add(Box.createVerticalStrut(8));
        left.add(info);
        left.add(Box.createVerticalStrut(20));

        JLabel lblSearch = new JLabel("Search by Name / ID");
        lblSearch.setForeground(TEXT_MAIN);
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(lblSearch);
        left.add(Box.createVerticalStrut(8));
        left.add(txtSearch);
        left.add(Box.createVerticalStrut(12));
        left.add(btnSearch);
        left.add(Box.createVerticalStrut(10));
        left.add(btnRefresh);
        left.add(Box.createVerticalStrut(10));
        left.add(btnUpdate);

        JPanel right = createCardPanel();
        right.setLayout(new BorderLayout(10, 10));

        JLabel tblTitle = new JLabel("Service Price List");
        tblTitle.setForeground(GOLD);
        tblTitle.setFont(new Font("Georgia", Font.BOLD, 20));

        model = new DefaultTableModel(
                new String[]{"Service ID", "Service Name", "Duration (Hours)", "Price (RM)", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(FIELD_BG);
        sp.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55, 60), 1));

        right.add(tblTitle, BorderLayout.NORTH);
        right.add(sp, BorderLayout.CENTER);

        outer.add(left, BorderLayout.WEST);
        outer.add(right, BorderLayout.CENTER);

        return outer;
    }

    private void loadServices() {
        model.setRowCount(0);
        ArrayList<String> lines = FileUtil.readAllLines(servicesFile);
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 5) model.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4]});
        }
    }

    private void searchService() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) { loadServices(); return; }

        model.setRowCount(0);
        ArrayList<String> lines = FileUtil.readAllLines(servicesFile);
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 5 && (p[0].toLowerCase().contains(keyword) || p[1].toLowerCase().contains(keyword))) {
                model.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4]});
            }
        }
        if (model.getRowCount() == 0)
            showStyledMessage("No matching service found.");
    }

    // ── Update Price — fully dark-themed custom dialog ────────────────
    private void updateSelectedPrice() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) { showStyledMessage("Please select a service row first."); return; }

        String serviceId          = model.getValueAt(selectedRow, 0).toString();
        String serviceName        = model.getValueAt(selectedRow, 1).toString();
        String currentDuration    = model.getValueAt(selectedRow, 2).toString();
        String currentPrice       = model.getValueAt(selectedRow, 3).toString();
        String currentDescription = model.getValueAt(selectedRow, 4).toString();

        JTextField txtDuration    = createTextField();  txtDuration.setText(currentDuration);
        JTextField txtPrice       = createTextField();  txtPrice.setText(currentPrice);
        JTextField txtDescription = createTextField();  txtDescription.setText(currentDescription);

        // ── Dark dialog panel ─────────────────────────────────────────
        JPanel dialogPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(18, 18, 18));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(212, 175, 55, 8));
                for (int x = 0; x < getWidth(); x += 30) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 30) g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };
        dialogPanel.setOpaque(false);
        dialogPanel.setLayout(new GridBagLayout());
        dialogPanel.setBorder(new EmptyBorder(10, 6, 6, 6));
        dialogPanel.setPreferredSize(new Dimension(360, 260));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new Insets(4, 0, 2, 0);

        // Service name label (gold header)
        gc.gridx = 0; gc.gridy = 0;
        JLabel lblService = makeDialogLabel("Service:  " + serviceName, true);
        dialogPanel.add(lblService, gc);

        // Duration
        gc.gridy = 1; gc.insets = new Insets(12, 0, 2, 0);
        dialogPanel.add(makeDialogLabel("Duration (hours):", false), gc);
        gc.gridy = 2; gc.insets = new Insets(0, 0, 0, 0);
        dialogPanel.add(txtDuration, gc);

        // Price
        gc.gridy = 3; gc.insets = new Insets(10, 0, 2, 0);
        dialogPanel.add(makeDialogLabel("Price (RM):", false), gc);
        gc.gridy = 4; gc.insets = new Insets(0, 0, 0, 0);
        dialogPanel.add(txtPrice, gc);

        // Description
        gc.gridy = 5; gc.insets = new Insets(10, 0, 2, 0);
        dialogPanel.add(makeDialogLabel("Description:", false), gc);
        gc.gridy = 6; gc.insets = new Insets(0, 0, 0, 0);
        dialogPanel.add(txtDescription, gc);

        // ── Custom JDialog ────────────────────────────────────────────
        JDialog dialog = new JDialog(this, "Update Service Price", true);
        dialog.setUndecorated(false);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        // Outer wrapper painted dark
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 24, 16, 24));

        // Title bar inside wrapper
        JLabel dlgTitle = new JLabel("Update Service Price");
        dlgTitle.setForeground(GOLD);
        dlgTitle.setFont(new Font("Georgia", Font.BOLD, 16));
        dlgTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        // Button row
        JButton btnOK     = createGoldButton("OK");
        JButton btnCancel = createGhostButton("Cancel");
        btnOK.setPreferredSize(new Dimension(100, 36));
        btnCancel.setPreferredSize(new Dimension(100, 36));

        final boolean[] confirmed = {false};
        btnOK.addActionListener(e -> { confirmed[0] = true;  dialog.dispose(); });
        btnCancel.addActionListener(e -> dialog.dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(14, 0, 0, 0));
        btnRow.add(btnOK);
        btnRow.add(btnCancel);

        wrapper.add(dlgTitle,     BorderLayout.NORTH);
        wrapper.add(dialogPanel,  BorderLayout.CENTER);
        wrapper.add(btnRow,       BorderLayout.SOUTH);

        dialog.getContentPane().setBackground(CARD_BG);
        dialog.add(wrapper);
        dialog.setVisible(true);

        // ── After dialog closes ───────────────────────────────────────
        if (!confirmed[0]) return;

        String newDuration    = txtDuration.getText().trim();
        String newPrice       = txtPrice.getText().trim();
        String newDescription = txtDescription.getText().trim();

        if (newDuration.isEmpty() || newPrice.isEmpty() || newDescription.isEmpty()) {
            showStyledMessage("All fields are required."); return;
        }

        try {
            Integer.parseInt(newDuration);
            Double.parseDouble(newPrice);
        } catch (NumberFormatException e) {
            showStyledMessage("Duration and Price must be valid numbers."); return;
        }

        ArrayList<String> lines = FileUtil.readAllLines(servicesFile);
        ArrayList<String> updatedLines = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 5 && p[0].equals(serviceId)) {
                updatedLines.add(p[0]+"|"+p[1]+"|"+newDuration+"|"+newPrice+"|"+newDescription);
            } else {
                updatedLines.add(line);
            }
        }

        FileUtil.writeAllLines(servicesFile, updatedLines);
        showStyledMessage("Service updated successfully.");
        loadServices();
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private JLabel makeDialogLabel(String text, boolean gold) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(gold ? GOLD : TEXT_MAIN);
        lbl.setFont(new Font("Segoe UI", gold ? Font.BOLD : Font.PLAIN, 13));
        return lbl;
    }

    private void showStyledMessage(String msg) {
        UIManager.put("OptionPane.background",        CARD_BG);
        UIManager.put("Panel.background",             CARD_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
        UIManager.put("OptionPane.messageFont",       new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.background",            GOLD);
        UIManager.put("Button.foreground",            Color.BLACK);
        JOptionPane.showMessageDialog(this, msg, "APU Automotive", JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(new Color(212, 175, 55, 55));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    // ── Gold solid button (sidebar) ───────────────────────────────────
    private JButton createButton(String text) {
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

    // ── Gold gradient button (dialog OK) ─────────────────────────────
    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { hovered=true;  repaint(); }
                public void mouseExited (java.awt.event.MouseEvent e) { hovered=false; repaint(); }
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

    // ── Ghost button (dialog Cancel) ──────────────────────────────────
    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { hovered=true;  repaint(); }
                public void mouseExited (java.awt.event.MouseEvent e) { hovered=false; repaint(); }
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

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_MAIN);
        field.setCaretColor(GOLD);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return field;
    }

    private void styleTable(JTable table) {
        table.setBackground(FIELD_BG);
        table.setForeground(TEXT_MAIN);
        table.setGridColor(new Color(70, 70, 70));
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(35, 35, 35));
        table.getTableHeader().setForeground(GOLD);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(212, 175, 55, 80));
        table.setSelectionForeground(TEXT_MAIN);
    }
}