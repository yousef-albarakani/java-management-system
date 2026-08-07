package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.CounterStaff;
import model.Customer;
import model.Manager;
import model.Technician;
import model.User;
import service.AuthService;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private final AuthService authService = new AuthService();

    // ── VIP Color Palette ──────────────────────────────────────────────
    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color CARD_EDGE  = new Color(26, 26, 26);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color GOLD_DIM   = new Color(212, 175, 55, 80);
    private static final Color GOLD_DARK  = new Color(160, 130, 30);
    private static final Color TEXT_MAIN  = new Color(240, 240, 235);
    private static final Color TEXT_MUTED = new Color(140, 140, 130);
    private static final Color FIELD_BG   = new Color(32, 32, 32);
    private static final Color FIELD_BORDER = new Color(212, 175, 55, 60);
    private static final Color FIELD_FOCUS  = new Color(212, 175, 55, 160);

    public LoginFrame() {
        setTitle("APU Automotive Service Centre");
        setSize(720, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(false);

        // Main background panel with custom painting
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(212, 175, 55, 10));
                g2.setStroke(new BasicStroke(0.5f));
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);

                RadialGradientPaint glow1 = new RadialGradientPaint(
                    new Point(0, 0), 300,
                    new float[]{0f, 1f},
                    new Color[]{new Color(212, 175, 55, 20), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(glow1);
                g2.fillRect(0, 0, getWidth(), getHeight());

                RadialGradientPaint glow2 = new RadialGradientPaint(
                    new Point(getWidth(), getHeight()), 250,
                    new float[]{0f, 1f},
                    new Color[]{new Color(212, 175, 55, 12), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(glow2);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);

        // Card panel
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight(), arc = 16;

                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRoundRect(4, 6, w - 8, h - 6, arc, arc);

                GradientPaint cardGrad = new GradientPaint(0, 0, CARD_EDGE, 0, h, CARD_BG);
                g2.setPaint(cardGrad);
                g2.fillRoundRect(0, 0, w - 2, h - 2, arc, arc);

                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, arc, arc);

                GradientPaint shimmer = new GradientPaint(
                    w * 0.2f, 0, new Color(212, 175, 55, 0),
                    w * 0.5f, 0, new Color(212, 175, 55, 180)
                );
                g2.setPaint(shimmer);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine((int)(w * 0.2f), 0, (int)(w * 0.5f), 0);

                GradientPaint shimmer2 = new GradientPaint(
                    w * 0.5f, 0, new Color(212, 175, 55, 180),
                    w * 0.8f, 0, new Color(212, 175, 55, 0)
                );
                g2.setPaint(shimmer2);
                g2.drawLine((int)(w * 0.5f), 0, (int)(w * 0.8f), 0);

                g2.setColor(new Color(212, 175, 55, 100));
                g2.setStroke(new BasicStroke(1.2f));
                int margin = 12, len = 18;
                g2.drawLine(margin, margin, margin + len, margin);
                g2.drawLine(margin, margin, margin, margin + len);
                g2.drawLine(w - margin - 2, margin, w - margin - len - 2, margin);
                g2.drawLine(w - margin - 2, margin, w - margin - 2, margin + len);
                g2.drawLine(margin, h - margin - 2, margin + len, h - margin - 2);
                g2.drawLine(margin, h - margin - 2, margin, h - margin - len - 2);
                g2.drawLine(w - margin - 2, h - margin - 2, w - margin - len - 2, h - margin - 2);
                g2.drawLine(w - margin - 2, h - margin - 2, w - margin - 2, h - margin - len - 2);

                g2.dispose();
            }
        };
        cardPanel.setPreferredSize(new Dimension(430, 460));
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(28, 32, 24, 32));

        // ── Logo / Brand Row ──────────────────────────────────────────
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        logoRow.setOpaque(false);
        logoRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = Math.min(getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, s - 3, s - 3);
                int cx = s / 2, cy = s / 2, r = s / 4;
                int[] xp = {cx, cx + r, cx, cx - r};
                int[] yp = {cy - r, cy, cy + r, cy};
                g2.setColor(GOLD);
                g2.fillPolygon(xp, yp, 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(40, 40); }
            @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
        };
        logoIcon.setOpaque(false);

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel lblBrand = new JLabel("APU Automotive");
        lblBrand.setForeground(GOLD);
        lblBrand.setFont(new Font("Georgia", Font.BOLD, 20));
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBrandSub = new JLabel("SERVICE  CENTRE");
        lblBrandSub.setForeground(new Color(212, 175, 55, 130));
        lblBrandSub.setFont(new Font("Arial Narrow", Font.PLAIN, 9));
        lblBrandSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandText.add(lblBrand);
        brandText.add(Box.createVerticalStrut(1));
        brandText.add(lblBrandSub);

        logoRow.add(logoIcon);
        logoRow.add(brandText);
        cardPanel.add(logoRow);
        cardPanel.add(Box.createVerticalStrut(16));

        // ── Divider ───────────────────────────────────────────────────
        cardPanel.add(createDivider());
        cardPanel.add(Box.createVerticalStrut(12));

        JLabel lblSub = new JLabel("SECURE PORTAL ACCESS");
        lblSub.setForeground(new Color(160, 160, 150));
        lblSub.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setBorder(new EmptyBorder(0, 0, 4, 0));
        cardPanel.add(lblSub);
        cardPanel.add(Box.createVerticalStrut(18));

        // ── Fields ────────────────────────────────────────────────────
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        int fieldWidth = 320;

        JLabel lblUser = createFieldLabel("USERNAME");
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.add(lblUser);
        fieldsPanel.add(Box.createVerticalStrut(5));
        txtUsername = createStyledTextField();
        txtUsername.setMaximumSize(new Dimension(fieldWidth, 42));
        txtUsername.setPreferredSize(new Dimension(fieldWidth, 42));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(14));

        JLabel lblPass = createFieldLabel("PASSWORD");
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.add(lblPass);
        fieldsPanel.add(Box.createVerticalStrut(5));
        txtPassword = createStyledPasswordField();
        txtPassword.setMaximumSize(new Dimension(fieldWidth, 42));
        txtPassword.setPreferredSize(new Dimension(fieldWidth, 42));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(22));

        // LOGIN button
        JButton btnLogin = createGoldButton("LOGIN");
        btnLogin.setMaximumSize(new Dimension(fieldWidth, 42));
        btnLogin.setPreferredSize(new Dimension(fieldWidth, 42));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(this::loginAction);
        fieldsPanel.add(btnLogin);
        fieldsPanel.add(Box.createVerticalStrut(10));

        // CREATE ACCOUNT ghost button
        JButton btnSignup = createGhostButton("CREATE CUSTOMER ACCOUNT");
        btnSignup.setMaximumSize(new Dimension(fieldWidth, 38));
        btnSignup.setPreferredSize(new Dimension(fieldWidth, 38));
        btnSignup.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignup.addActionListener(e -> {
            new SignupFrame();
            dispose();
        });
        fieldsPanel.add(btnSignup);
        fieldsPanel.add(Box.createVerticalStrut(8));

        // ── EXIT ghost button ─────────────────────────────────────────
        JButton btnExit = createExitButton("EXIT APPLICATION");
        btnExit.setMaximumSize(new Dimension(fieldWidth, 38));
        btnExit.setPreferredSize(new Dimension(fieldWidth, 38));
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.addActionListener(e -> System.exit(0));
        fieldsPanel.add(btnExit);

        cardPanel.add(fieldsPanel);
        cardPanel.add(Box.createVerticalStrut(14));

        // Footer
        JLabel footer = new JLabel("© 2025  APU AUTOMOTIVE · ALL RIGHTS RESERVED");
        footer.setForeground(new Color(80, 80, 75));
        footer.setFont(new Font("Arial Narrow", Font.PLAIN, 9));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(footer);

        mainPanel.add(cardPanel);
        add(mainPanel);
        setVisible(true);
    }

    // ── Login Action ──────────────────────────────────────────────────
    private void loginAction(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();

        User user = authService.login(username, password);

        if (user == null) {
            showStyledDialog("Access Denied", "Invalid username or password.", false);
            return;
        }

        showStyledDialog("Welcome Back", "Login Successful!  Welcome, " + user.getName(), true);

        if (user instanceof Manager)           new ManagerDashboard((Manager) user);
        else if (user instanceof CounterStaff) new CounterStaffDashboard((CounterStaff) user);
        else if (user instanceof Technician)   new TechnicianDashboard((Technician) user);
        else if (user instanceof Customer)     new CustomerDashboard((Customer) user);

        dispose();
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(212, 175, 55, 180));
        lbl.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            boolean focused = false;
            {
                addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { focused = true;  repaint(); }
                    public void focusLost(FocusEvent e)   { focused = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            boolean focused = false;
            {
                addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { focused = true;  repaint(); }
                    public void focusLost(FocusEvent e)   { focused = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
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
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(new Color(212, 175, 55, 18));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }
                g2.setColor(hovered ? new Color(212, 175, 55, 140) : new Color(212, 175, 55, 65));
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
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    // ── Exit button — red-tinted ghost style ──────────────────────────
    private JButton createExitButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(new Color(180, 60, 50, 22));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }
                g2.setColor(hovered ? new Color(200, 80, 65, 180) : new Color(180, 60, 50, 90));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(new Color(200, 80, 65, 200));
        btn.setFont(new Font("Arial Narrow", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private JPanel createDivider() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2, d = 5;
                g2.setPaint(new GradientPaint(0, cy, new Color(212,175,55,0), cx-10, cy, new Color(212,175,55,80)));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawLine(0, cy, cx-9, cy);
                g2.setPaint(new GradientPaint(cx+10, cy, new Color(212,175,55,80), getWidth(), cy, new Color(212,175,55,0)));
                g2.drawLine(cx+9, cy, getWidth(), cy);
                g2.setColor(GOLD);
                g2.fillPolygon(new int[]{cx, cx+d, cx, cx-d}, new int[]{cy-d, cy, cy+d, cy}, 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(300, 16); }
            @Override public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE, 16); }
        };
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
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