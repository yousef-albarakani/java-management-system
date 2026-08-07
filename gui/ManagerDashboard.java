package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Manager;

public class ManagerDashboard extends JFrame {

    private final Manager manager;

    // ── VIP Color Palette ─────────────────────────────────────────────
    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color CARD_EDGE  = new Color(30, 30, 30);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color TEXT_MAIN  = new Color(240, 240, 235);
    private static final Color TEXT_MUTED = new Color(140, 140, 130);

    public ManagerDashboard(Manager manager) {
        this.manager = manager;

        setTitle("APU Automotive — Manager Dashboard");
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Gold grid
                g2.setColor(new Color(212, 175, 55, 8));
                g2.setStroke(new BasicStroke(0.5f));
                for (int x = 0; x < getWidth(); x += 40) {
                    g2.drawLine(x, 0, x, getHeight());
                }
                for (int y = 0; y < getHeight(); y += 40) {
                    g2.drawLine(0, y, getWidth(), y);
                }

                // Corner glow
                RadialGradientPaint glow = new RadialGradientPaint(
                        new Point(0, 0), 350,
                        new float[]{0f, 1f},
                        new Color[]{new Color(212, 175, 55, 18), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(glow);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };
        main.setOpaque(false);

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createCenterPanel(), BorderLayout.CENTER);
        main.add(createFooter(), BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────────
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(28, 28, 28),
                        0, getHeight(), new Color(18, 18, 18)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                GradientPaint line1 = new GradientPaint(
                        0, getHeight() - 1, new Color(212, 175, 55, 0),
                        getWidth() / 2f, getHeight() - 1, new Color(212, 175, 55, 140)
                );
                g2.setPaint(line1);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(0, getHeight() - 1, getWidth() / 2, getHeight() - 1);

                GradientPaint line2 = new GradientPaint(
                        getWidth() / 2f, getHeight() - 1, new Color(212, 175, 55, 140),
                        getWidth(), getHeight() - 1, new Color(212, 175, 55, 0)
                );
                g2.setPaint(line2);
                g2.drawLine(getWidth() / 2, getHeight() - 1, getWidth(), getHeight() - 1);

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 28, 18, 28));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int s = Math.min(getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, s - 3, s - 3);

                int cx = s / 2;
                int cy = s / 2;
                int r = s / 4;

                int[] xp = {cx, cx + r, cx, cx - r};
                int[] yp = {cy - r, cy, cy + r, cy};
                g2.fillPolygon(xp, yp, 4);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(38, 38);
            }
        };
        logoIcon.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Manager Dashboard");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 20));

        JLabel lblSub = new JLabel("Welcome back, " + manager.getName().toUpperCase());
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("Arial Narrow", Font.PLAIN, 11));

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(lblSub);

        left.add(logoIcon);
        left.add(titleBlock);

        JButton btnLogout = new JButton("LOGOUT") {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (hovered) {
                    g2.setColor(new Color(212, 175, 55, 20));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }

                g2.setColor(hovered ? new Color(212, 175, 55, 160) : new Color(212, 175, 55, 80));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        btnLogout.setForeground(new Color(212, 175, 55, 200));
        btnLogout.setFont(new Font("Arial Narrow", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setOpaque(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(100, 36));
        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        panel.add(left, BorderLayout.WEST);
        panel.add(btnLogout, BorderLayout.EAST);

        return panel;
    }

    // ── Center Grid ───────────────────────────────────────────────────
    private JPanel createCenterPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 35, 20, 35));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);

        String[][] cards = {
                {"Manage Staff", "Add, update or remove staff members"},
                {"Set Service Prices", "Configure and manage service pricing"},
                {"View Feedback", "Customer and technician feedback reports"},
                {"Reports", "Appointments, payments, services overview"}
        };

        for (String[] c : cards) {
            grid.add(createVipCard(c[0], c[1]));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        wrapper.add(grid, gbc);
        return wrapper;
    }

    private JPanel createVipCard(String title, String desc) {
        JPanel card = new JPanel() {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }

                    public void mouseClicked(MouseEvent e) {
                        openManagerScreen(title);
                    }
                });

                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(3, 5, w - 6, h - 5, 14, 14);

                GradientPaint gp = new GradientPaint(
                        0, 0,
                        hovered ? new Color(35, 33, 20) : CARD_EDGE,
                        0, h,
                        hovered ? new Color(28, 26, 15) : CARD_BG
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w - 2, h - 2, 14, 14);

                g2.setColor(hovered ? new Color(212, 175, 55, 160) : new Color(212, 175, 55, 55));
                g2.setStroke(new BasicStroke(hovered ? 1.2f : 0.8f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, 14, 14);

                if (hovered) {
                    GradientPaint shimmer1 = new GradientPaint(
                            w * 0.2f, 0, new Color(212, 175, 55, 0),
                            w * 0.5f, 0, new Color(212, 175, 55, 120)
                    );
                    g2.setPaint(shimmer1);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawLine((int) (w * 0.2f), 0, (int) (w * 0.5f), 0);

                    GradientPaint shimmer2 = new GradientPaint(
                            w * 0.5f, 0, new Color(212, 175, 55, 120),
                            w * 0.8f, 0, new Color(212, 175, 55, 0)
                    );
                    g2.setPaint(shimmer2);
                    g2.drawLine((int) (w * 0.5f), 0, (int) (w * 0.8f), 0);
                }

                int m = 10;
                int len = 14;
                g2.setColor(new Color(212, 175, 55, hovered ? 160 : 70));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(m, m, m + len, m);
                g2.drawLine(m, m, m, m + len);

                g2.drawLine(w - m - 2, m, w - m - len - 2, m);
                g2.drawLine(w - m - 2, m, w - m - 2, m + len);

                g2.drawLine(m, h - m - 2, m + len, h - m - 2);
                g2.drawLine(m, h - m - 2, m, h - m - len - 2);

                g2.drawLine(w - m - 2, h - m - 2, w - m - len - 2, h - m - 2);
                g2.drawLine(w - m - 2, h - m - 2, w - m - 2, h - m - len - 2);

                int cx = w / 2;
                int dy = 22;
                int dr = 6;
                g2.setColor(hovered ? GOLD : new Color(212, 175, 55, 120));
                int[] xp = {cx, cx + dr, cx, cx - dr};
                int[] yp = {dy - dr, dy, dy + dr, dy};
                g2.fillPolygon(xp, yp, 4);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(42, 25, 20, 25));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel divLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int cx = getWidth() / 2;

                GradientPaint gp1 = new GradientPaint(
                        0, 0, new Color(212, 175, 55, 0),
                        cx, 0, new Color(212, 175, 55, 100)
                );
                g2.setPaint(gp1);
                g2.setStroke(new BasicStroke(0.7f));
                g2.drawLine(0, 0, cx, 0);

                GradientPaint gp2 = new GradientPaint(
                        cx, 0, new Color(212, 175, 55, 100),
                        getWidth(), 0, new Color(212, 175, 55, 0)
                );
                g2.setPaint(gp2);
                g2.drawLine(cx, 0, getWidth(), 0);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 8);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 8);
            }
        };
        divLine.setOpaque(false);
        divLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + desc + "</center></html>");
        lblDesc.setForeground(TEXT_MUTED);
        lblDesc.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(8));
        card.add(divLine);
        card.add(Box.createVerticalStrut(10));
        card.add(lblDesc);

        return card;
    }

    // ── Open Screens ──────────────────────────────────────────────────
    private void openManagerScreen(String title) {
        if (title.equals("Manage Staff")) {
            new ManageStaffFrame();
        } else if (title.equals("Set Service Prices")) {
            new ServicePricesFrame();
        } else if (title.equals("View Feedback")) {
            new ManagerFeedbackFrame();
        } else if (title.equals("Reports")) {
            new ReportsFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Screen not connected yet.");
        }
    }

    // ── Footer ────────────────────────────────────────────────────────
    private JPanel createFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                GradientPaint gp1 = new GradientPaint(
                        0, 0, new Color(212, 175, 55, 0),
                        getWidth() / 2f, 0, new Color(212, 175, 55, 50)
                );
                g2.setPaint(gp1);
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawLine(0, 0, getWidth() / 2, 0);

                GradientPaint gp2 = new GradientPaint(
                        getWidth() / 2f, 0, new Color(212, 175, 55, 50),
                        getWidth(), 0, new Color(212, 175, 55, 0)
                );
                g2.setPaint(gp2);
                g2.drawLine(getWidth() / 2, 0, getWidth(), 0);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(6, 0, 10, 0));

        JLabel footer = new JLabel("© 2025  APU AUTOMOTIVE · ALL RIGHTS RESERVED");
        footer.setForeground(new Color(80, 80, 75));
        footer.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        panel.add(footer);

        return panel;
    }
}