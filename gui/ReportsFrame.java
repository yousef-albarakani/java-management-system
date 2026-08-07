package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import util.FileUtil;

public class ReportsFrame extends JFrame {

    private static final Color BG_DEEP    = new Color(10, 10, 10);
    private static final Color CARD_BG    = new Color(22, 22, 22);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color TEXT_MAIN  = new Color(240, 240, 235);
    private static final Color TEXT_MUTED = new Color(150, 150, 140);

    private JLabel lblAppointments;
    private JLabel lblCompleted;
    private JLabel lblPending;
    private JLabel lblPayments;
    private JLabel lblFeedbacks;

    public ReportsFrame() {
        setTitle("Reports");
        setSize(950, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_DEEP);

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createContent(), BorderLayout.CENTER);

        add(main);
        setVisible(true);

        loadReportData();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DEEP);
        panel.setBorder(new EmptyBorder(18, 25, 18, 25));

        JPanel left = new JPanel();
        left.setBackground(BG_DEEP);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Reports Dashboard");
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JLabel sub = new JLabel("Quick statistics for the manager");
        sub.setForeground(TEXT_MUTED);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(sub);

        JButton close = new JButton("Close");
        close.setBackground(GOLD);
        close.setForeground(Color.BLACK);
        close.setFocusPainted(false);
        close.addActionListener(e -> dispose());

        panel.add(left, BorderLayout.WEST);
        panel.add(close, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContent() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setBackground(BG_DEEP);
        grid.setBorder(new EmptyBorder(10, 25, 25, 25));

        lblAppointments = new JLabel();
        lblCompleted = new JLabel();
        lblPending = new JLabel();
        lblPayments = new JLabel();
        lblFeedbacks = new JLabel();

        grid.add(createReportCard("Total Appointments", lblAppointments));
        grid.add(createReportCard("Completed Appointments", lblCompleted));
        grid.add(createReportCard("Pending Appointments", lblPending));
        grid.add(createReportCard("Total Payments", lblPayments));
        grid.add(createReportCard("Total Feedbacks", lblFeedbacks));
        grid.add(createReportCard("System Status", new JLabel("Active", SwingConstants.CENTER)));

        return grid;
    }

    private JPanel createReportCard(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55, 55), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Georgia", Font.BOLD, 18));

        valueLabel.setForeground(TEXT_MAIN);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private void loadReportData() {
        ArrayList<String> appointments = FileUtil.readAllLines("src/data/appointments.txt");
        ArrayList<String> payments = FileUtil.readAllLines("src/data/payments.txt");
        ArrayList<String> feedbacks = FileUtil.readAllLines("src/data/feedbacks.txt");

        int completed = 0;
        int pending = 0;

        for (String line : appointments) {
            String[] p = line.split("\\|");
            if (p.length >= 10) {
                if (p[9].equalsIgnoreCase("Completed")) {
                    completed++;
                } else if (p[9].equalsIgnoreCase("Pending")) {
                    pending++;
                }
            }
        }

        lblAppointments.setText(String.valueOf(appointments.size()));
        lblCompleted.setText(String.valueOf(completed));
        lblPending.setText(String.valueOf(pending));
        lblPayments.setText(String.valueOf(payments.size()));
        lblFeedbacks.setText(String.valueOf(feedbacks.size()));
    }
}