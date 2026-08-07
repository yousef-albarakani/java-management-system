package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.CounterStaff;

public class CounterStaffDashboard extends JFrame {

    private final CounterStaff staff;

    private static final Color BG_DEEP   = new Color(10, 10, 10);
    private static final Color CARD_BG   = new Color(22, 22, 22);
    private static final Color CARD_EDGE = new Color(30, 30, 30);
    private static final Color GOLD      = new Color(212, 175, 55);
    private static final Color FIELD_BORDER = new Color(212, 175, 55, 60);

    public CounterStaffDashboard(CounterStaff staff) {
        this.staff = staff;

        setTitle("APU Automotive — Counter Staff Dashboard");
        setSize(1000, 660);
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

                g2.dispose();
            }
        };
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(22, 28, 18, 28));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Counter Staff Dashboard");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("WELCOME, " + staff.getName().toUpperCase());
        lblSub.setForeground(new Color(212, 175, 55, 100));
        lblSub.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblTitle);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSub);
        header.add(Box.createVerticalStrut(14));
        header.add(createDivider());
        header.add(Box.createVerticalStrut(20));

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);

        String[][] cards = {
                {"Edit Profile", "Update your own account details"},
                {"Manage Customers", "Add, update or delete customers"},
                {"Create Appointment", "Book a new service appointment"},
                {"Assign Technician", "Assign an available technician"},
                {"Collect Payment", "Receive payment & generate receipt"},
                {"Customer List", "View all customer records"}
        };

        for (String[] c : cards) {
            grid.add(createCardButton(c[0], c[1]));
        }

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(header, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(Box.createVerticalStrut(18));

        JButton btnLogout = createGhostButton("LOGOUT");
        btnLogout.setPreferredSize(new Dimension(160, 38));
        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnLogout);
        south.add(btnRow);

        south.add(Box.createVerticalStrut(8));
        JLabel footer = new JLabel("© 2025  APU AUTOMOTIVE · ALL RIGHTS RESERVED");
        footer.setForeground(new Color(70, 70, 65));
        footer.setFont(new Font("Arial Narrow", Font.PLAIN, 9));
        JPanel fp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fp.setOpaque(false);
        fp.add(footer);
        south.add(fp);

        main.add(center, BorderLayout.CENTER);
        main.add(south, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void openCounterStaffScreen(String title) {
        if (title.equals("Edit Profile")) {
            new CounterStaffProfileFrame(staff);
        } else if (title.equals("Manage Customers")) {
            new ManageCustomersFrame();
        } else if (title.equals("Create Appointment")) {
            new CreateAppointmentFrame(staff);
        } else if (title.equals("Assign Technician")) {
            new AssignTechnicianFrame();
        } else if (title.equals("Collect Payment")) {
            new CollectPaymentFrame();
        } else if (title.equals("Customer List")) {
            new CustomerListFrame();
        }
    }

    private JButton createCardButton(String title, String desc) {
        JButton btn = new JButton() {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
                addActionListener(e -> openCounterStaffScreen(title));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight(), arc = 14;

                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(3, 5, w - 6, h - 5, arc, arc);

                Color top = hovered ? new Color(35, 35, 35) : CARD_EDGE;
                Color bot = hovered ? new Color(28, 28, 28) : CARD_BG;
                g2.setPaint(new GradientPaint(0, 0, top, 0, h, bot));
                g2.fillRoundRect(0, 0, w - 2, h - 2, arc, arc);

                g2.setColor(hovered ? new Color(212, 175, 55, 140) : FIELD_BORDER);
                g2.setStroke(new BasicStroke(hovered ? 1.4f : 1f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, arc, arc);

                g2.dispose();

                FontMetrics fmTitle = g.getFontMetrics(new Font("Georgia", Font.BOLD, 16));
                g.setFont(new Font("Georgia", Font.BOLD, 16));
                g.setColor(hovered ? new Color(230, 195, 70) : GOLD);
                int titleY = h / 2 - 8;
                g.drawString(title, (w - fmTitle.stringWidth(title)) / 2, titleY);

                g.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
                g.setColor(new Color(160, 155, 145));
                FontMetrics fmDesc = g.getFontMetrics();
                g.drawString(desc, (w - fmDesc.stringWidth(desc)) / 2, titleY + 20);
            }
        };

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
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(new Color(212, 175, 55, 18));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                g2.setColor(hovered ? new Color(212, 175, 55, 140) : new Color(212, 175, 55, 65));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
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
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int cx = getWidth() / 2, cy = getHeight() / 2, d = 5;
                g2.setPaint(new GradientPaint(0, cy, new Color(212, 175, 55, 0), cx - 10, cy, new Color(212, 175, 55, 80)));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawLine(0, cy, cx - 9, cy);
                g2.setPaint(new GradientPaint(cx + 10, cy, new Color(212, 175, 55, 80), getWidth(), cy, new Color(212, 175, 55, 0)));
                g2.drawLine(cx + 9, cy, getWidth(), cy);
                g2.setColor(GOLD);
                g2.fillPolygon(new int[]{cx, cx + d, cx, cx - d}, new int[]{cy - d, cy, cy + d, cy}, 4);
                g2.dispose();
            }

            @Override public Dimension getPreferredSize() { return new Dimension(300, 16); }
            @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, 16); }
        };
        p.setOpaque(false);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }
}