package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Technician;

public class TechnicianDashboard extends JFrame {

    private final Technician technician;

    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color CARD_EDGE  = new Color(30, 30, 30);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color TEXT_MUTED = new Color(140, 140, 130);

    public TechnicianDashboard(Technician technician) {
        this.technician = technician;

        setTitle("APU Automotive — Technician Dashboard");
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

                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(212, 175, 55, 8));
                g2.setStroke(new BasicStroke(0.5f));
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);

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

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, new Color(28, 28, 28), 0, getHeight(), new Color(18, 18, 18));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                GradientPaint l1 = new GradientPaint(0, getHeight() - 1, new Color(212, 175, 55, 0),
                        getWidth() / 2f, getHeight() - 1, new Color(212, 175, 55, 140));
                g2.setPaint(l1);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(0, getHeight() - 1, getWidth() / 2, getHeight() - 1);

                GradientPaint l2 = new GradientPaint(getWidth() / 2f, getHeight() - 1, new Color(212, 175, 55, 140),
                        getWidth(), getHeight() - 1, new Color(212, 175, 55, 0));
                g2.setPaint(l2);
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
                int s = Math.min(getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, s - 3, s - 3);
                int cx = s / 2, cy = s / 2, r = s / 4;
                g2.fillPolygon(new int[]{cx, cx + r, cx, cx - r}, new int[]{cy - r, cy, cy + r, cy}, 4);
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

        JLabel lblTitle = new JLabel("Technician Dashboard");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 20));

        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badgeRow.setOpaque(false);

        JLabel lblWelcome = new JLabel("Welcome, " + technician.getName().toUpperCase());
        lblWelcome.setForeground(TEXT_MUTED);
        lblWelcome.setFont(new Font("Arial Narrow", Font.PLAIN, 11));

        JLabel lblSpec = new JLabel(technician.getSpecialization().toUpperCase()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(212, 175, 55, 25));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(new Color(212, 175, 55, 100));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblSpec.setForeground(new Color(212, 175, 55, 200));
        lblSpec.setFont(new Font("Arial Narrow", Font.BOLD, 9));
        lblSpec.setBorder(new EmptyBorder(2, 8, 2, 8));
        lblSpec.setOpaque(false);

        badgeRow.add(lblWelcome);
        badgeRow.add(lblSpec);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(badgeRow);

        left.add(logoIcon);
        left.add(titleBlock);

        JButton btnLogout = new JButton("LOGOUT") {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (hovered) {
                    g2.setColor(new Color(212, 175, 55, 20));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                g2.setColor(hovered ? new Color(212, 175, 55, 160) : new Color(212, 175, 55, 80));
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

    private JPanel createCenterPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 35, 20, 35));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);

        String[][] cards = {
                {"Edit Profile", "Update your technician details"},
                {"Assigned Appointments", "Check your assigned jobs"},
                {"Mark as Completed", "Update appointment status"},
                {"Add Feedback", "Provide service feedback"}
        };

        for (String[] c : cards) grid.add(createVipCard(c[0], c[1]));

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
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                    public void mouseClicked(MouseEvent e) { openTechnicianScreen(title); }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth(), h = getHeight();

                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(3, 5, w - 6, h - 5, 14, 14);

                g2.setPaint(new GradientPaint(0, 0,
                        hovered ? new Color(35, 33, 20) : CARD_EDGE,
                        0, h,
                        hovered ? new Color(28, 26, 15) : CARD_BG));
                g2.fillRoundRect(0, 0, w - 2, h - 2, 14, 14);

                g2.setColor(hovered ? new Color(212, 175, 55, 160) : new Color(212, 175, 55, 55));
                g2.setStroke(new BasicStroke(hovered ? 1.2f : 0.8f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, 14, 14);

                int m = 10, len = 14;
                g2.setColor(new Color(212, 175, 55, hovered ? 160 : 70));
                g2.drawLine(m, m, m + len, m); g2.drawLine(m, m, m, m + len);
                g2.drawLine(w - m - 2, m, w - m - len - 2, m); g2.drawLine(w - m - 2, m, w - m - 2, m + len);
                g2.drawLine(m, h - m - 2, m + len, h - m - 2); g2.drawLine(m, h - m - 2, m, h - m - len - 2);
                g2.drawLine(w - m - 2, h - m - 2, w - m - len - 2, h - m - 2);
                g2.drawLine(w - m - 2, h - m - 2, w - m - 2, h - m - len - 2);

                int cx = w / 2, dy = 22, dr = 6;
                g2.setColor(hovered ? GOLD : new Color(212, 175, 55, 120));
                g2.fillPolygon(new int[]{cx, cx + dr, cx, cx - dr}, new int[]{dy - dr, dy, dy + dr, dy}, 4);

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
                g2.setPaint(new GradientPaint(0, 0, new Color(212, 175, 55, 0), cx, 0, new Color(212, 175, 55, 100)));
                g2.drawLine(0, 0, cx, 0);
                g2.setPaint(new GradientPaint(cx, 0, new Color(212, 175, 55, 100), getWidth(), 0, new Color(212, 175, 55, 0)));
                g2.drawLine(cx, 0, getWidth(), 0);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 8);
            }
        };
        divLine.setOpaque(false);
        divLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + desc + "</center></html>");
        lblDesc.setForeground(TEXT_MUTED);
        lblDesc.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(8));
        card.add(divLine);
        card.add(Box.createVerticalStrut(10));
        card.add(lblDesc);

        return card;
    }

    private void openTechnicianScreen(String title) {
        if (title.equals("Edit Profile")) {
            new TechnicianProfileFrame(technician);
        } else if (title.equals("Assigned Appointments")) {
            new TechnicianAppointmentsFrame(technician);
        } else if (title.equals("Mark as Completed")) {
            new CompleteAppointmentFrame(technician);
        } else if (title.equals("Add Feedback")) {
            new TechnicianFeedbackFrame(technician);
        }
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(212, 175, 55, 0),
                        getWidth() / 2f, 0, new Color(212, 175, 55, 50)));
                g2.drawLine(0, 0, getWidth() / 2, 0);
                g2.setPaint(new GradientPaint(getWidth() / 2f, 0, new Color(212, 175, 55, 50),
                        getWidth(), 0, new Color(212, 175, 55, 0)));
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