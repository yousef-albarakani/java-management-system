package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import util.FileUtil;

public class ManagerFeedbackFrame extends JFrame {

    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color TEXT_MAIN  = new Color(240, 240, 235);
    private static final Color TEXT_MUTED = new Color(150, 150, 140);
    private static final Color FIELD_BG   = new Color(30, 30, 30);

    private JTable table;
    private DefaultTableModel model;

    public ManagerFeedbackFrame() {
        setTitle("View Feedback");
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_DEEP);

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createContent(), BorderLayout.CENTER);

        add(main);
        setVisible(true);

        loadFeedbackData();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DEEP);
        panel.setBorder(new EmptyBorder(18, 25, 18, 25));

        JPanel left = new JPanel();
        left.setBackground(BG_DEEP);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Feedback Overview");
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JLabel sub = new JLabel("View all technician feedback records");
        sub.setForeground(TEXT_MUTED);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(sub);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(GOLD);
        btnClose.setForeground(Color.BLACK);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());

        panel.add(left, BorderLayout.WEST);
        panel.add(btnClose, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContent() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Feedback List");
        heading.setForeground(GOLD);
        heading.setFont(new Font("Georgia", Font.BOLD, 20));

        model = new DefaultTableModel(
                new String[]{"Feedback ID", "Appointment ID", "Technician", "Customer ID", "Rating", "Feedback Text"}, 0);
        table = new JTable(model);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(FIELD_BG);
        sp.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55, 60), 1));

        card.add(heading, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_DEEP);
        outer.setBorder(new EmptyBorder(10, 25, 25, 25));
        outer.add(card, BorderLayout.CENTER);

        return outer;
    }

    private void loadFeedbackData() {
        model.setRowCount(0);
        ArrayList<String> lines = FileUtil.readAllLines("src/data/feedbacks.txt");

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 7) {
                model.addRow(new Object[]{p[0], p[1], p[3], p[4], p[6], p[5]});
            }
        }
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
    }
}