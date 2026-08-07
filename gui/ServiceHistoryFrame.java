package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Appointment;
import model.Customer;
import service.AppointmentService;

public class ServiceHistoryFrame extends JFrame {

    public ServiceHistoryFrame(Customer customer) {
        Color BG = new Color(10, 10, 10);
        Color GOLD = new Color(212, 175, 55);
        Color TEXT = new Color(240, 240, 235);
        Color FIELD = new Color(30, 30, 30);

        setTitle("Service History");
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Service History - " + customer.getName(), SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Appointment ID", "Service", "Date", "Time", "Technician", "Status"}, 0
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

        AppointmentService service = new AppointmentService();
        ArrayList<Appointment> list = service.getAppointmentsByCustomer(customer.getId());

        for (Appointment a : list) {
            model.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getServiceName(),
                    a.getDate(),
                    a.getTime(),
                    a.getTechnicianName(),
                    a.getStatus()
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