package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import model.Feedback;
import model.Technician;
import service.AppointmentService;
import service.FeedbackService;

public class TechnicianFeedbackFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);

    private final Technician technician;
    private JComboBox<String> cmbAppointments;
    private JTextArea txtFeedback;
    private JComboBox<Integer> cmbRating;

    public TechnicianFeedbackFrame(Technician technician) {
        this.technician = technician;

        setTitle("Add Feedback");
        setSize(760, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Add Technician Feedback", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblAppointment = new JLabel("Select Appointment");
        lblAppointment.setForeground(TEXT);

        cmbAppointments = new JComboBox<>();
        loadAppointments();

        JLabel lblRating = new JLabel("Rating");
        lblRating.setForeground(TEXT);
        cmbRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        JLabel lblFeedback = new JLabel("Feedback Text");
        lblFeedback.setForeground(TEXT);

        txtFeedback = new JTextArea(6, 30);
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);

        JButton btnSubmit = new JButton("Submit Feedback");
        btnSubmit.setBackground(GOLD);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.addActionListener(e -> submitFeedback());

        form.add(lblAppointment);
        form.add(cmbAppointments);
        form.add(lblRating);
        form.add(cmbRating);
        form.add(lblFeedback);
        form.add(new JScrollPane(txtFeedback));
        form.add(btnSubmit);

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private void loadAppointments() {
        AppointmentService appointmentService = new AppointmentService();
        ArrayList<Appointment> list = appointmentService.getAppointmentsByTechnician(technician.getId());

        for (Appointment a : list) {
            cmbAppointments.addItem(a.getAppointmentId() + " - " + a.getCustomerId() + " - " + a.getCustomerName());
        }
    }

    private void submitFeedback() {
        String selected = (String) cmbAppointments.getSelectedItem();
        String feedbackText = txtFeedback.getText().trim();
        int rating = (Integer) cmbRating.getSelectedItem();

        if (selected == null || feedbackText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        String[] parts = selected.split(" - ");
        String appointmentId = parts[0];
        String customerId = parts[1];

        Feedback feedback = new Feedback();
        feedback.setAppointmentId(appointmentId);
        feedback.setTechnicianId(technician.getId());
        feedback.setTechnicianName(technician.getName());
        feedback.setCustomerId(customerId);
        feedback.setFeedbackText(feedbackText);
        feedback.setRating(rating);

        FeedbackService feedbackService = new FeedbackService();
        boolean success = feedbackService.addFeedback(feedback);

        if (success) {
            JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
            txtFeedback.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit feedback.");
        }
    }
}