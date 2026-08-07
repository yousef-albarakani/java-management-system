package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Customer;
import model.Feedback;
import service.FeedbackService;

public class CustomerFeedbackFrame extends JFrame {

    public CustomerFeedbackFrame(Customer customer) {
        Color BG = new Color(10, 10, 10);
        Color GOLD = new Color(212, 175, 55);
        Color TEXT = new Color(240, 240, 235);
        Color FIELD = new Color(30, 30, 30);

        setTitle("View Feedback");
        setSize(980, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Feedback - " + customer.getName(), SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Feedback ID", "Appointment ID", "Technician", "Rating", "Feedback Text"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setBackground(FIELD);
        table.setForeground(TEXT);
        table.setGridColor(new Color(70, 70, 70));
        table.setRowHeight(28);
        table.getTableHeader().setBackground(new Color(35, 35, 35));
        table.getTableHeader().setForeground(GOLD);

        FeedbackService feedbackService = new FeedbackService();
        ArrayList<Feedback> list = feedbackService.getFeedbackByCustomer(customer.getId());

        for (Feedback f : list) {
            model.addRow(new Object[]{
                    f.getFeedbackId(),
                    f.getAppointmentId(),
                    f.getTechnicianName(),
                    f.getRating(),
                    f.getFeedbackText()
            });
        }

        JScrollPane sp = new JScrollPane(table);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(GOLD);
        btnClose.setForeground(Color.BLACK);
        btnClose.addActionListener(e -> dispose());

        JPanel south = new JPanel();
        south.setBackground(BG);
        south.add(btnClose);

        main.add(title, BorderLayout.NORTH);
        main.add(sp, BorderLayout.CENTER);
        main.add(south, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }
}