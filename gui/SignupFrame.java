package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Customer;
import service.AuthService;

public class SignupFrame extends JFrame {

    private JTextField txtName, txtUsername, txtGender, txtAge, txtPhone, txtEmail, txtAddress, txtNationality;
    private JPasswordField txtPassword;
    private final AuthService authService = new AuthService();

    // ── VIP Color Palette (same as LoginFrame & ManagerDashboard) ─────
    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color CARD_EDGE  = new Color(30, 30, 30);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color GOLD_DARK  = new Color(160, 130, 30);
    private static final Color GOLD_DIM   = new Color(212, 175, 55, 60);
    private static final Color TEXT_MAIN  = new Color(240, 240, 235);
    private static final Color TEXT_MUTED = new Color(140, 140, 130);
    private static final Color FIELD_BG   = new Color(32, 32, 32);
    private static final Color FIELD_BORDER = new Color(212, 175, 55, 60);
    private static final Color FIELD_FOCUS  = new Color(212, 175, 55, 160);

    public SignupFrame() {
        setTitle("APU Automotive — Customer Registration");
        setSize(860, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ── Main background ──────────────────────────────────────────
        JPanel main = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Gold grid
                g2.setColor(new Color(212, 175, 55, 8));
                g2.setStroke(new BasicStroke(0.5f));
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);

                // Corner glows
                RadialGradientPaint glow1 = new RadialGradientPaint(new Point(0, 0), 320,
                    new float[]{0f, 1f}, new Color[]{new Color(212, 175, 55, 18), new Color(0,0,0,0)});
                g2.setPaint(glow1); g2.fillRect(0, 0, getWidth(), getHeight());

                RadialGradientPaint glow2 = new RadialGradientPaint(new Point(getWidth(), getHeight()), 250,
                    new float[]{0f, 1f}, new Color[]{new Color(212, 175, 55, 10), new Color(0,0,0,0)});
                g2.setPaint(glow2); g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };
        main.setOpaque(false);

        // ── Outer wrapper (vertical stack) ───────────────────────────
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        // ── Card panel ───────────────────────────────────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), arc = 16;

                // Shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRoundRect(4, 6, w - 8, h - 6, arc, arc);

                // Card gradient
                GradientPaint gp = new GradientPaint(0, 0, CARD_EDGE, 0, h, CARD_BG);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w - 2, h - 2, arc, arc);

                // Border
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, arc, arc);

                // Top shimmer
                GradientPaint s1 = new GradientPaint(w*0.15f, 0, new Color(212,175,55,0), w*0.5f, 0, new Color(212,175,55,160));
                g2.setPaint(s1); g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine((int)(w*0.15f), 0, (int)(w*0.5f), 0);
                GradientPaint s2 = new GradientPaint(w*0.5f, 0, new Color(212,175,55,160), w*0.85f, 0, new Color(212,175,55,0));
                g2.setPaint(s2);
                g2.drawLine((int)(w*0.5f), 0, (int)(w*0.85f), 0);

                // Corner brackets
                int m = 12, len = 16;
                g2.setColor(new Color(212, 175, 55, 90));
                g2.setStroke(new BasicStroke(1.1f));
                g2.drawLine(m, m, m+len, m);       g2.drawLine(m, m, m, m+len);
                g2.drawLine(w-m-2, m, w-m-len-2, m); g2.drawLine(w-m-2, m, w-m-2, m+len);
                g2.drawLine(m, h-m-2, m+len, h-m-2); g2.drawLine(m, h-m-2, m, h-m-len-2);
                g2.drawLine(w-m-2, h-m-2, w-m-len-2, h-m-2); g2.drawLine(w-m-2, h-m-2, w-m-2, h-m-len-2);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(740, 540));
        card.setMaximumSize(new Dimension(740, 540));
        card.setBorder(new EmptyBorder(28, 36, 28, 36));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Card Header ──────────────────────────────────────────────
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        logoRow.setOpaque(false);
        logoRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = Math.min(getWidth(), getHeight());
                g2.setColor(GOLD); g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, s-3, s-3);
                int cx = s/2, cy = s/2, r = s/4;
                g2.fillPolygon(new int[]{cx, cx+r, cx, cx-r}, new int[]{cy-r, cy, cy+r, cy}, 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(38, 38); }
        };
        logoIcon.setOpaque(false);

        JPanel brandBlock = new JPanel();
        brandBlock.setOpaque(false);
        brandBlock.setLayout(new BoxLayout(brandBlock, BoxLayout.Y_AXIS));
        JLabel lblBrand = new JLabel("APU Automotive");
        lblBrand.setForeground(GOLD);
        lblBrand.setFont(new Font("Georgia", Font.BOLD, 20));
        JLabel lblBrandSub = new JLabel("CUSTOMER REGISTRATION");
        lblBrandSub.setForeground(new Color(212, 175, 55, 120));
        lblBrandSub.setFont(new Font("Arial Narrow", Font.PLAIN, 9));
        brandBlock.add(lblBrand);
        brandBlock.add(Box.createVerticalStrut(1));
        brandBlock.add(lblBrandSub);

        logoRow.add(logoIcon);
        logoRow.add(brandBlock);
        card.add(logoRow);
        card.add(Box.createVerticalStrut(14));

        // Divider
        card.add(createDivider());
        card.add(Box.createVerticalStrut(18));

        // ── Fields Grid (2 columns) ──────────────────────────────────
        txtName        = createStyledTextField();
        txtUsername    = createStyledTextField();
        txtPassword    = createStyledPasswordField();
        txtGender      = createStyledTextField();
        txtAge         = createStyledTextField();
        txtPhone       = createStyledTextField();
        txtEmail       = createStyledTextField();
        txtAddress     = createStyledTextField();
        txtNationality = createStyledTextField();

        JPanel fieldsGrid = new JPanel(new GridLayout(0, 2, 18, 10));
        fieldsGrid.setOpaque(false);
        fieldsGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        addField(fieldsGrid, "FULL NAME",    txtName);
        addField(fieldsGrid, "USERNAME",     txtUsername);
        addField(fieldsGrid, "PASSWORD",     txtPassword);
        addField(fieldsGrid, "GENDER",       txtGender);
        addField(fieldsGrid, "AGE",          txtAge);
        addField(fieldsGrid, "PHONE",        txtPhone);
        addField(fieldsGrid, "EMAIL",        txtEmail);
        addField(fieldsGrid, "ADDRESS",      txtAddress);
        addField(fieldsGrid, "NATIONALITY",  txtNationality);

        card.add(fieldsGrid);
        card.add(Box.createVerticalStrut(22));

        // ── Buttons ──────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSignup = createGoldButton("CREATE ACCOUNT");
        btnSignup.setPreferredSize(new Dimension(220, 42));
        btnSignup.addActionListener(e -> signupAction());

        JButton btnBack = createGhostButton("BACK TO LOGIN");
        btnBack.setPreferredSize(new Dimension(180, 42));
        btnBack.addActionListener(e -> { new LoginFrame(); dispose(); });

        btnPanel.add(btnSignup);
        btnPanel.add(btnBack);
        card.add(btnPanel);

        // ── Footer ───────────────────────────────────────────────────
        card.add(Box.createVerticalStrut(16));
        JLabel footer = new JLabel("© 2025  APU AUTOMOTIVE · ALL RIGHTS RESERVED");
        footer.setForeground(new Color(80, 80, 75));
        footer.setFont(new Font("Arial Narrow", Font.PLAIN, 9));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footer);

        wrapper.add(card);
        main.add(wrapper);
        add(main);
        setVisible(true);
    }

    // ── Signup Logic ──────────────────────────────────────────────────
    private void signupAction() {
        try {
            Customer customer = new Customer(
                "",
                txtName.getText().trim(),
                txtUsername.getText().trim(),
                String.valueOf(txtPassword.getPassword()).trim(),
                txtGender.getText().trim(),
                Integer.parseInt(txtAge.getText().trim()),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim(),
                txtNationality.getText().trim()
            );

            boolean success = authService.signupCustomer(customer);
            if (success) {
                showStyledDialog("Success", "Account created! Please login.", true);
                new LoginFrame();
                dispose();
            } else {
                showStyledDialog("Failed", "Signup failed. Username may already exist.", false);
            }
        } catch (Exception ex) {
            showStyledDialog("Invalid Input", "Please check all fields and enter a valid age.", false);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private void addField(JPanel panel, String labelText, JComponent field) {
        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(212, 175, 55, 180));
        lbl.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        group.add(lbl);
        group.add(Box.createVerticalStrut(4));
        group.add(field);

        panel.add(group);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            boolean focused = false;
            { addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { focused = true; repaint(); }
                public void focusLost(FocusEvent e)   { focused = false; repaint(); }
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
        field.setOpaque(false);
        field.setForeground(TEXT_MAIN);
        field.setCaretColor(GOLD);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(new EmptyBorder(8, 12, 8, 12));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            boolean focused = false;
            { addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { focused = true; repaint(); }
                public void focusLost(FocusEvent e)   { focused = false; repaint(); }
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
        field.setOpaque(false);
        field.setForeground(TEXT_MAIN);
        field.setCaretColor(GOLD);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(new EmptyBorder(8, 12, 8, 12));
        return field;
    }

    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = hovered ? new Color(230, 195, 70) : GOLD;
                Color c2 = hovered ? new Color(180, 148, 35) : GOLD_DARK;
                g2.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()/2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(new Color(15, 15, 15));
        btn.setFont(new Font("Arial Narrow", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(new Color(212, 175, 55, 18));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }
                g2.setColor(hovered ? new Color(212,175,55,140) : new Color(212,175,55,65));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(new Color(212, 175, 55, 190));
        btn.setFont(new Font("Arial Narrow", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createDivider() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2;
                g2.setPaint(new GradientPaint(0, cy, new Color(212,175,55,0), cx-10, cy, new Color(212,175,55,80)));
                g2.setStroke(new BasicStroke(0.8f)); g2.drawLine(0, cy, cx-9, cy);
                g2.setPaint(new GradientPaint(cx+10, cy, new Color(212,175,55,80), getWidth(), cy, new Color(212,175,55,0)));
                g2.drawLine(cx+9, cy, getWidth(), cy);
                int d = 5;
                g2.setColor(GOLD);
                g2.fillPolygon(new int[]{cx, cx+d, cx, cx-d}, new int[]{cy-d, cy, cy+d, cy}, 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(300, 16); }
            @Override public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE, 16); }
        };
        p.setOpaque(false);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }

    private void showStyledDialog(String title, String msg, boolean success) {
        UIManager.put("OptionPane.background",        CARD_BG);
        UIManager.put("Panel.background",             CARD_BG);
        UIManager.put("OptionPane.messageForeground", success ? GOLD : new Color(220, 100, 80));
        UIManager.put("OptionPane.messageFont",       new Font("Georgia", Font.PLAIN, 14));
        UIManager.put("Button.background",            success ? GOLD : new Color(180, 60, 60));
        UIManager.put("Button.foreground",            Color.BLACK);
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.PLAIN_MESSAGE);
    }
}