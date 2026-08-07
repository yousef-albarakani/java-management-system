package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import util.FileUtil;

public class CustomerListFrame extends JFrame {

    private static final Color BG = new Color(10, 10, 10);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color TEXT = new Color(240, 240, 235);
    private static final Color FIELD = new Color(30, 30, 30);

    public CustomerListFrame() {
        setTitle("Customer List");
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Customer List", SwingConstants.CENTER);
        title.setForeground(GOLD);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Customer ID", "Name", "Username", "Phone", "Email", "Address", "Nationality"}, 0
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

        ArrayList<String> lines = FileUtil.readAllLines("src/data/customers.txt");
        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 10) {
                model.addRow(new Object[]{p[0], p[1], p[2], p[6], p[7], p[8], p[9]});
            }
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