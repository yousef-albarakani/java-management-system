package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import model.Comment;
import model.Customer;
import service.AppointmentService;
import service.FeedbackService;

public class AddCommentFrame extends JFrame {

    private JComboBox<String> cmbAppointments;
    private JTextField txtStaffOrTechId;
    private JTextArea txtComment;
    private final Customer customer;

    public AddCommentFrame(Customer customer) {
        this.customer = customer;

        Color BG = new Color(10, 10, 10);
        Color CARD = new Color(22, 22, 22);
        Color GOLD = new Color(212, 175, 55);
        Color TEXT = new Color(240, 240, 235);

        setTitle("Add Comment");
        setSize(760, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Add Customer Comment", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblAppointment = new JLabel("Select Appointment");
        lblAppointment.setForeground(TEXT);

        cmbAppointments = new JComboBox<>();
        loadAppointments();

        JLabel lblId = new JLabel("Staff / Technician ID");
        lblId.setForeground(TEXT);
        txtStaffOrTechId = new JTextField();

        JLabel lblComment = new JLabel("Comment Text");
        lblComment.setForeground(TEXT);
        txtComment = new JTextArea(6, 30);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);

        JButton btnSubmit = new JButton("Submit Comment");
        btnSubmit.setBackground(GOLD);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.addActionListener(e -> submitComment());

        form.add(lblAppointment);
        form.add(cmbAppointments);
        form.add(lblId);
        form.add(txtStaffOrTechId);
        form.add(lblComment);
        form.add(new JScrollPane(txtComment));
        form.add(btnSubmit);

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private void loadAppointments() {
        AppointmentService appointmentService = new AppointmentService();
        ArrayList<Appointment> list = appointmentService.getAppointmentsByCustomer(customer.getId());

        for (Appointment a : list) {
            cmbAppointments.addItem(a.getAppointmentId() + " - " + a.getServiceName());
        }
    }

    private void submitComment() {
        String selected = (String) cmbAppointments.getSelectedItem();
        String staffOrTechId = txtStaffOrTechId.getText().trim();
        String commentText = txtComment.getText().trim();

        if (selected == null || staffOrTechId.isEmpty() || commentText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        String appointmentId = selected.split(" - ")[0];

        Comment comment = new Comment();
        comment.setAppointmentId(appointmentId);
        comment.setCustomerId(customer.getId());
        comment.setCustomerName(customer.getName());
        comment.setStaffOrTechnicianId(staffOrTechId);
        comment.setCommentText(commentText);
        comment.setDate("");

        FeedbackService feedbackService = new FeedbackService();
        boolean success = feedbackService.addComment(comment);

        if (success) {
            JOptionPane.showMessageDialog(this, "Comment submitted successfully.");
            txtComment.setText("");
            txtStaffOrTechId.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit comment.");
        }
    }
}