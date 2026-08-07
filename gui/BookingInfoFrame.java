package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import model.Customer;
import service.AppointmentService;

public class BookingInfoFrame extends JFrame {

    public BookingInfoFrame(Customer customer) {
        Color BG = new Color(10, 10, 10);
        Color CARD = new Color(22, 22, 22);
        Color GOLD = new Color(212, 175, 55);
        Color TEXT = new Color(240, 240, 235);

        setTitle("Booking Info");
        setSize(760, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Booking Info - " + customer.getName(), SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(CARD);
        area.setForeground(TEXT);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setMargin(new Insets(15, 15, 15, 15));

        AppointmentService appointmentService = new AppointmentService();
        ArrayList<Appointment> list = appointmentService.getAppointmentsByCustomer(customer.getId());

        StringBuilder sb = new StringBuilder();
        if (list.isEmpty()) {
            sb.append("No booking information found.");
        } else {
            for (Appointment a : list) {
                sb.append("Appointment ID: ").append(a.getAppointmentId()).append("\n");
                sb.append("Service: ").append(a.getServiceName()).append("\n");
                sb.append("Date: ").append(a.getDate()).append("\n");
                sb.append("Time: ").append(a.getTime()).append("\n");
                sb.append("Technician: ").append(a.getTechnicianName()).append("\n");
                sb.append("Status: ").append(a.getStatus()).append("\n");
                sb.append("--------------------------------------------------\n");
            }
        }

        area.setText(sb.toString());

        JScrollPane sp = new JScrollPane(area);

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