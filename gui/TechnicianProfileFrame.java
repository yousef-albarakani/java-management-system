package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Technician;
import util.FileUtil;

public class TechnicianProfileFrame extends JFrame {

    private static final Color BG_DEEP     = new Color(10, 10, 10);
    private static final Color CARD_BG     = new Color(22, 22, 22);
    private static final Color CARD_EDGE   = new Color(30, 30, 30);
    private static final Color GOLD        = new Color(212, 175, 55);
    private static final Color GOLD_DARK   = new Color(160, 130, 30);
    private static final Color TEXT_MAIN   = new Color(240, 240, 235);
    private static final Color TEXT_LOCKED = new Color(85, 85, 80);
    private static final Color FIELD_BG    = new Color(32, 32, 32);
    private static final Color FIELD_LOCKED= new Color(20, 20, 20);
    private static final Color FIELD_BORDER= new Color(212, 175, 55, 60);
    private static final Color FIELD_FOCUS = new Color(212, 175, 55, 160);

    private final Technician technician;
    private JTextField txtName, txtPassword, txtPhone, txtEmail, txtAddress, txtStatus;

    public TechnicianProfileFrame(Technician technician) {
        this.technician = technician;

        setTitle("APU Automotive — Technician Profile");
        setSize(820, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // ── Main background ──────────────────────────────────────────
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
                RadialGradientPaint glow = new RadialGradientPaint(new Point(0, 0), 320,
                    new float[]{0f, 1f}, new Color[]{new Color(212,175,55,18), new Color(0,0,0,0)});
                g2.setPaint(glow); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(22, 28, 18, 28));

        // ── Header ───────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Edit Technician Profile");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("MANAGE YOUR ACCOUNT DETAILS");
        lblSub.setForeground(new Color(212, 175, 55, 100));
        lblSub.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblTitle);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSub);
        header.add(Box.createVerticalStrut(14));
        header.add(createDivider());
        header.add(Box.createVerticalStrut(16));

        // ── Card ─────────────────────────────────────────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w=getWidth(), h=getHeight(), arc=14;
                g2.setColor(new Color(0,0,0,80)); g2.fillRoundRect(3,5,w-6,h-5,arc,arc);
                g2.setPaint(new GradientPaint(0,0,CARD_EDGE,0,h,CARD_BG));
                g2.fillRoundRect(0,0,w-2,h-2,arc,arc);
                g2.setColor(FIELD_BORDER); g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,w-2,h-2,arc,arc);
                g2.setPaint(new GradientPaint(w*0.15f,0,new Color(212,175,55,0),w*0.5f,0,new Color(212,175,55,140)));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine((int)(w*0.15f),0,(int)(w*0.5f),0);
                g2.setPaint(new GradientPaint(w*0.5f,0,new Color(212,175,55,140),w*0.85f,0,new Color(212,175,55,0)));
                g2.drawLine((int)(w*0.5f),0,(int)(w*0.85f),0);
                int m=10, len=14;
                g2.setColor(new Color(212,175,55,80)); g2.setStroke(new BasicStroke(1f));
                g2.drawLine(m,m,m+len,m);           g2.drawLine(m,m,m,m+len);
                g2.drawLine(w-m-2,m,w-m-len-2,m);   g2.drawLine(w-m-2,m,w-m-2,m+len);
                g2.drawLine(m,h-m-2,m+len,h-m-2);   g2.drawLine(m,h-m-2,m,h-m-len-2);
                g2.drawLine(w-m-2,h-m-2,w-m-len-2,h-m-2); g2.drawLine(w-m-2,h-m-2,w-m-2,h-m-len-2);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(22, 26, 22, 26));
        // Use GridBagLayout for full control over column widths
        card.setLayout(new GridBagLayout());

        // ── Build fields ─────────────────────────────────────────────
        txtName     = createEditableField(technician.getName());
        txtPassword = createEditableField(technician.getPassword());
        txtPhone    = createEditableField(technician.getPhone());
        txtEmail    = createEditableField(technician.getEmail());
        txtAddress  = createEditableField(technician.getAddress());
        txtStatus   = createEditableField(technician.getStatus());

        JTextField txtUsername       = createLockedField(technician.getUsername());
        JTextField txtGender         = createLockedField(technician.getGender());
        JTextField txtAge            = createLockedField(String.valueOf(technician.getAge()));
        JTextField txtNationality    = createLockedField(technician.getNationality());
        JTextField txtSpecialization = createLockedField(technician.getSpecialization());

        // GridBagConstraints templates
        // Label col left
        GridBagConstraints lblLeft = new GridBagConstraints();
        lblLeft.gridx=0; lblLeft.gridy=0;
        lblLeft.anchor=GridBagConstraints.SOUTHWEST;
        lblLeft.insets=new Insets(0,0,3,0);

        // Field col left
        GridBagConstraints fldLeft = new GridBagConstraints();
        fldLeft.gridx=0; fldLeft.gridy=0;
        fldLeft.fill=GridBagConstraints.HORIZONTAL;
        fldLeft.weightx=1.0;
        fldLeft.insets=new Insets(0,0,10,14);

        // Label col right
        GridBagConstraints lblRight = new GridBagConstraints();
        lblRight.gridx=1; lblRight.gridy=0;
        lblRight.anchor=GridBagConstraints.SOUTHWEST;
        lblRight.insets=new Insets(0,0,3,0);

        // Field col right
        GridBagConstraints fldRight = new GridBagConstraints();
        fldRight.gridx=1; fldRight.gridy=0;
        fldRight.fill=GridBagConstraints.HORIZONTAL;
        fldRight.weightx=1.0;
        fldRight.insets=new Insets(0,0,10,0);

        // Helper to add a full row: label+field on left, label+field on right
        // Each "row" takes 2 GridBag rows: one for labels, one for fields
        int row = 0;

        row = addGridRow(card, row, "NAME", txtName, true, "USERNAME", txtUsername, false);
        row = addGridRow(card, row, "PASSWORD", txtPassword, true, "GENDER", txtGender, false);
        row = addGridRow(card, row, "AGE", txtAge, false, "PHONE", txtPhone, true);
        row = addGridRow(card, row, "EMAIL", txtEmail, true, "ADDRESS", txtAddress, true);
        row = addGridRow(card, row, "NATIONALITY", txtNationality, false, "SPECIALIZATION", txtSpecialization, false);
        row = addGridRow(card, row, "STATUS", txtStatus, true, null, null, false);

        // ── South ────────────────────────────────────────────────────
        JButton btnSave  = createGoldButton("SAVE CHANGES");
        JButton btnClose = createGhostButton("CLOSE");
        btnSave.setPreferredSize(new Dimension(200, 42));
        btnClose.setPreferredSize(new Dimension(140, 42));
        btnSave.addActionListener(e -> updateProfile());
        btnClose.addActionListener(e -> dispose());

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnSave); btnRow.add(btnClose);
        south.add(btnRow);

        south.add(Box.createVerticalStrut(8));
        JLabel footer = new JLabel("© 2025  APU AUTOMOTIVE · ALL RIGHTS RESERVED");
        footer.setForeground(new Color(70,70,65));
        footer.setFont(new Font("Arial Narrow", Font.PLAIN, 9));
        JPanel fp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fp.setOpaque(false); fp.add(footer);
        south.add(fp);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(header, BorderLayout.NORTH);
        center.add(card,   BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);
        main.add(south,  BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    /**
     * Adds one logical row (label row + field row) for TWO label-field pairs side by side.
     * Returns the next available GridBag row index.
     */
    private int addGridRow(JPanel card, int startRow,
                           String lbl1, JTextField fld1, boolean editable1,
                           String lbl2, JTextField fld2, boolean editable2) {

        GridBagConstraints gc = new GridBagConstraints();

        // ── Label row ────────────────────────────────────────────────
        gc.gridy = startRow;
        gc.fill  = GridBagConstraints.HORIZONTAL;
        gc.anchor= GridBagConstraints.SOUTHWEST;
        gc.weightx = 1.0;

        gc.gridx  = 0;
        gc.insets = new Insets(6, 0, 2, 14);
        card.add(makeLabel(lbl1, editable1), gc);

        gc.gridx  = 1;
        gc.insets = new Insets(6, 0, 2, 0);
        if (lbl2 != null) {
            card.add(makeLabel(lbl2, editable2), gc);
        } else {
            card.add(new JPanel(){{ setOpaque(false); }}, gc);
        }

        // ── Field row ────────────────────────────────────────────────
        gc.gridy  = startRow + 1;
        gc.anchor = GridBagConstraints.CENTER;

        gc.gridx  = 0;
        gc.insets = new Insets(0, 0, 0, 14);
        card.add(fld1, gc);

        gc.gridx  = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        if (fld2 != null) {
            card.add(fld2, gc);
        } else {
            card.add(new JPanel(){{ setOpaque(false); }}, gc);
        }

        return startRow + 2;
    }

    private JLabel makeLabel(String text, boolean editable) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(editable
            ? new Color(212, 175, 55, 180)
            : new Color(100, 100, 92));
        lbl.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
        return lbl;
    }

    // ── Update Logic ──────────────────────────────────────────────────
    private void updateProfile() {
        String filePath = "src/data/technicians.txt";
        ArrayList<String> lines = FileUtil.readAllLines(filePath);
        ArrayList<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 12 && p[0].equals(technician.getId())) {
                updated.add(p[0]+"|"+txtName.getText().trim()+"|"+p[2]+"|"+
                    txtPassword.getText().trim()+"|"+p[4]+"|"+p[5]+"|"+
                    txtPhone.getText().trim()+"|"+txtEmail.getText().trim()+"|"+
                    txtAddress.getText().trim()+"|"+p[9]+"|"+p[10]+"|"+
                    txtStatus.getText().trim());
            } else { updated.add(line); }
        }
        FileUtil.writeAllLines(filePath, updated);
        showStyledDialog("Success", "Profile updated successfully.", true);
        dispose();
    }

    // ── Field Factories ───────────────────────────────────────────────
    private JTextField createEditableField(String value) {
        JTextField f = new JTextField(value) {
            boolean focused = false;
            { addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { focused=true; repaint(); }
                public void focusLost(FocusEvent e)   { focused=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(focused ? FIELD_FOCUS : FIELD_BORDER);
                g2.setStroke(new BasicStroke(focused ? 1.2f : 0.8f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        f.setOpaque(false); f.setForeground(TEXT_MAIN);
        f.setCaretColor(GOLD); f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(new EmptyBorder(8,12,8,12));
        f.setPreferredSize(new Dimension(0, 38));
        return f;
    }

    private JTextField createLockedField(String value) {
        JTextField f = new JTextField(value) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_LOCKED);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(new Color(212,175,55,22));
                g2.setStroke(new BasicStroke(0.6f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        f.setOpaque(false); f.setEditable(false);
        f.setForeground(TEXT_LOCKED); f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(new EmptyBorder(8,12,8,12));
        f.setPreferredSize(new Dimension(0, 38));
        return f;
    }

    // ── Button Factories ──────────────────────────────────────────────
    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered=true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1=hovered?new Color(230,195,70):GOLD, c2=hovered?new Color(180,148,35):GOLD_DARK;
                g2.setPaint(new GradientPaint(0,0,c1,0,getHeight(),c2));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(new Color(255,255,255,40));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()/2,8,8);
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
                public void mouseEntered(MouseEvent e) { hovered=true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) { g2.setColor(new Color(212,175,55,18)); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8); }
                g2.setColor(hovered?new Color(212,175,55,140):new Color(212,175,55,65));
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

    private JPanel createDivider() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int cx=getWidth()/2, cy=getHeight()/2, d=5;
                g2.setPaint(new GradientPaint(0,cy,new Color(212,175,55,0),cx-10,cy,new Color(212,175,55,80)));
                g2.setStroke(new BasicStroke(0.8f)); g2.drawLine(0,cy,cx-9,cy);
                g2.setPaint(new GradientPaint(cx+10,cy,new Color(212,175,55,80),getWidth(),cy,new Color(212,175,55,0)));
                g2.drawLine(cx+9,cy,getWidth(),cy);
                g2.setColor(GOLD);
                g2.fillPolygon(new int[]{cx,cx+d,cx,cx-d},new int[]{cy-d,cy,cy+d,cy},4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(300,16); }
            @Override public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE,16); }
        };
        p.setOpaque(false); p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }

    private void showStyledDialog(String title, String msg, boolean success) {
        UIManager.put("OptionPane.background",        CARD_BG);
        UIManager.put("Panel.background",             CARD_BG);
        UIManager.put("OptionPane.messageForeground", success ? GOLD : new Color(220,100,80));
        UIManager.put("OptionPane.messageFont",       new Font("Georgia", Font.PLAIN, 14));
        UIManager.put("Button.background",            success ? GOLD : new Color(180,60,60));
        UIManager.put("Button.foreground",            Color.BLACK);
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.PLAIN_MESSAGE);
    }
}