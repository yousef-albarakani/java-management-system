package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import service.AppointmentService;
import util.FileUtil;

public class AssignTechnicianFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);

    private JComboBox<String> cmbAppointments;
    private JComboBox<String> cmbTechnicians;

    public AssignTechnicianFrame() {
        setTitle("Assign Technician");
        setSize(760, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Assign Technician", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        cmbAppointments = new JComboBox<>();
        cmbTechnicians = new JComboBox<>();

        loadAppointments();
        loadTechnicians();

        form.add(label("Appointment"));
        form.add(cmbAppointments);
        form.add(label("Technician"));
        form.add(cmbTechnicians);

        JButton btnAssign = new JButton("Assign Technician");
        btnAssign.setBackground(GOLD);
        btnAssign.setForeground(Color.BLACK);
        btnAssign.addActionListener(e -> assignTechnician());

        JPanel south = new JPanel();
        south.setBackground(BG);
        south.add(btnAssign);

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        main.add(south, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void loadAppointments() {
        AppointmentService service = new AppointmentService();
        ArrayList<Appointment> list = service.getAllAppointments();

        for (Appointment a : list) {
            if (!a.getStatus().equalsIgnoreCase("Completed")) {
                cmbAppointments.addItem(a.getAppointmentId() + " - " + a.getCustomerName());
            }
        }
    }

    private void loadTechnicians() {
        ArrayList<String> lines = FileUtil.readAllLines("src/data/technicians.txt");
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 12) {
                cmbTechnicians.addItem(p[0] + " - " + p[1]);
            }
        }
    }

    private void assignTechnician() {
        String appointment = (String) cmbAppointments.getSelectedItem();
        String technician = (String) cmbTechnicians.getSelectedItem();

        if (appointment == null || technician == null) {
            JOptionPane.showMessageDialog(this, "Select appointment and technician.");
            return;
        }

        String appointmentId = appointment.split(" - ")[0];
        String technicianId = technician.split(" - ")[0];
        String technicianName = technician.split(" - ")[1];

        AppointmentService service = new AppointmentService();
        boolean success = service.assignTechnician(appointmentId, technicianId, technicianName);

        if (success) {
            JOptionPane.showMessageDialog(this, "Technician assigned successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to assign technician.");
        }
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        return l;
    }
}