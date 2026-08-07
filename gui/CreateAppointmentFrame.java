package gui;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import model.CounterStaff;
import service.AppointmentService;
import util.FileUtil;

public class CreateAppointmentFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);

    private JComboBox<String> cmbCustomer;
    private JComboBox<String> cmbService;
    private JTextField txtDate, txtTime;
    private final CounterStaff staff;

    public CreateAppointmentFrame(CounterStaff staff) {
        this.staff = staff;

        setTitle("Create Appointment");
        setSize(760, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Create Appointment", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        cmbCustomer = new JComboBox<>();
        cmbService = new JComboBox<>();
        txtDate = new JTextField(LocalDate.now().toString());
        txtTime = new JTextField("09:00");

        loadCustomers();
        loadServices();

        form.add(label("Customer"));
        form.add(cmbCustomer);
        form.add(label("Service"));
        form.add(cmbService);
        form.add(label("Date (YYYY-MM-DD)"));
        form.add(txtDate);
        form.add(label("Time (HH:MM)"));
        form.add(txtTime);

        JButton btnCreate = new JButton("Create Appointment");
        btnCreate.setBackground(GOLD);
        btnCreate.setForeground(Color.BLACK);
        btnCreate.addActionListener(e -> createAppointment());

        JPanel south = new JPanel();
        south.setBackground(BG);
        south.add(btnCreate);

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        main.add(south, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void loadCustomers() {
        ArrayList<String> lines = FileUtil.readAllLines("src/data/customers.txt");
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 2) {
                cmbCustomer.addItem(p[0] + " - " + p[1]);
            }
        }
    }

    private void loadServices() {
        ArrayList<String> lines = FileUtil.readAllLines("src/data/services.txt");
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 2) {
                cmbService.addItem(p[0] + " - " + p[1]);
            }
        }
    }

    private void createAppointment() {
        String customer = (String) cmbCustomer.getSelectedItem();
        String service = (String) cmbService.getSelectedItem();

        if (customer == null || service == null) {
            JOptionPane.showMessageDialog(this, "Please select customer and service.");
            return;
        }

        String[] c = customer.split(" - ");
        String[] s = service.split(" - ");

        Appointment appointment = new Appointment();
        appointment.setCustomerId(c[0]);
        appointment.setCustomerName(c[1]);
        appointment.setServiceId(s[0]);
        appointment.setServiceName(s[1]);
        appointment.setDate(txtDate.getText().trim());
        appointment.setTime(txtTime.getText().trim());
        appointment.setTechnicianId("T001");
        appointment.setTechnicianName("Faizal Ahmad");
        appointment.setStatus("Pending");
        appointment.setCounterStaffId(staff.getId());

        AppointmentService appointmentService = new AppointmentService();
        boolean success = appointmentService.addAppointment(appointment);

        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment created successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create appointment. Check date/time rules.");
        }
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        return l;
    }
}