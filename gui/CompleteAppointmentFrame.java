package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import model.Technician;
import service.AppointmentService;

public class CompleteAppointmentFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);

    private final Technician technician;
    private JComboBox<String> cmbAppointments;
    private AppointmentService appointmentService;

    public CompleteAppointmentFrame(Technician technician) {
        this.technician = technician;
        this.appointmentService = new AppointmentService();

        setTitle("Mark Appointment as Completed");
        setSize(650, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Mark Appointment as Completed", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel center = new JPanel(new GridLayout(0, 1, 10, 10));
        center.setBackground(CARD);
        center.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel("Select Appointment");
        lbl.setForeground(TEXT);

        cmbAppointments = new JComboBox<>();
        loadAppointments();

        JButton btnComplete = new JButton("Complete Selected Appointment");
        btnComplete.setBackground(GOLD);
        btnComplete.setForeground(Color.BLACK);
        btnComplete.addActionListener(e -> completeAppointment());

        center.add(lbl);
        center.add(cmbAppointments);
        center.add(btnComplete);

        main.add(title, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private void loadAppointments() {
        cmbAppointments.removeAllItems();
        ArrayList<Appointment> list = appointmentService.getAppointmentsByTechnician(technician.getId());

        for (Appointment a : list) {
            if (!a.getStatus().equalsIgnoreCase("Completed")) {
                cmbAppointments.addItem(a.getAppointmentId() + " - " + a.getCustomerName() + " - " + a.getServiceName());
            }
        }
    }

    private void completeAppointment() {
        String selected = (String) cmbAppointments.getSelectedItem();

        if (selected == null || selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointment selected.");
            return;
        }

        String appointmentId = selected.split(" - ")[0];
        boolean updated = appointmentService.updateAppointmentStatus(appointmentId, "Completed");

        if (updated) {
            JOptionPane.showMessageDialog(this, "Appointment marked as completed.");
            loadAppointments();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update appointment.");
        }
    }
}