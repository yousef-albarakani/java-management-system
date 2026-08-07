package gui;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Payment;
import service.PaymentService;
import util.FileUtil;

public class CollectPaymentFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color CARD = new Color(22, 22, 22);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);

    private JComboBox<String> cmbAppointments;
    private JTextField txtAmount;
    private JComboBox<String> cmbMethod;

    public CollectPaymentFrame() {
        setTitle("Collect Payment");
        setSize(760, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Collect Payment", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        cmbAppointments = new JComboBox<>();
        txtAmount = new JTextField();
        cmbMethod = new JComboBox<>(new String[]{"Cash", "Card", "Online Transfer"});

        loadAppointments();

        form.add(label("Appointment"));
        form.add(cmbAppointments);
        form.add(label("Amount"));
        form.add(txtAmount);
        form.add(label("Payment Method"));
        form.add(cmbMethod);

        JButton btnPay = new JButton("Collect Payment");
        btnPay.setBackground(GOLD);
        btnPay.setForeground(Color.BLACK);
        btnPay.addActionListener(e -> collectPayment());

        JPanel south = new JPanel();
        south.setBackground(BG);
        south.add(btnPay);

        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        main.add(south, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void loadAppointments() {
        ArrayList<String> lines = FileUtil.readAllLines("src/data/appointments.txt");
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 11) {
                cmbAppointments.addItem(p[0] + " - " + p[1] + " - " + p[2]);
            }
        }
    }

    private void collectPayment() {
        String selected = (String) cmbAppointments.getSelectedItem();
        if (selected == null || txtAmount.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete all fields.");
            return;
        }

        try {
            String[] parts = selected.split(" - ");
            String appointmentId = parts[0];
            String customerId = parts[1];
            String customerName = parts[2];

            Payment payment = new Payment();
            payment.setAppointmentId(appointmentId);
            payment.setCustomerId(customerId);
            payment.setAmount(Double.parseDouble(txtAmount.getText().trim()));
            payment.setPaymentMethod(cmbMethod.getSelectedItem().toString());
            payment.setPaymentDate(LocalDate.now().toString());
            payment.setPaymentStatus("Paid");

            PaymentService paymentService = new PaymentService();
            boolean success = paymentService.addPayment(payment, customerName);

            if (success) {
                JOptionPane.showMessageDialog(this, "Payment collected and receipt generated.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        return l;
    }
}