package service;

import java.util.ArrayList;

import model.CounterStaff;
import model.Customer;
import model.Manager;
import model.Technician;
import model.User;
import util.FileUtil;
import util.IdGenerator;
import util.ValidationUtil;

public class AuthService {

    private final String managersFile = "src/data/managers.txt";
    private final String counterStaffFile = "src/data/counterstaff.txt";
    private final String techniciansFile = "src/data/technicians.txt";
    private final String customersFile = "src/data/customers.txt";

    public User login(String username, String password) {
        User user = checkManagerLogin(username, password);
        if (user != null) return user;

        user = checkCounterStaffLogin(username, password);
        if (user != null) return user;

        user = checkTechnicianLogin(username, password);
        if (user != null) return user;

        user = checkCustomerLogin(username, password);
        return user;
    }

    private Manager checkManagerLogin(String username, String password) {
        ArrayList<String> lines = FileUtil.readAllLines(managersFile);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 10 && p[2].equals(username) && p[3].equals(password)) {
                return new Manager(
                        p[0], p[1], p[2], p[3], p[4],
                        Integer.parseInt(p[5]), p[6], p[7], p[8], p[9]
                );
            }
        }
        return null;
    }

    private CounterStaff checkCounterStaffLogin(String username, String password) {
        ArrayList<String> lines = FileUtil.readAllLines(counterStaffFile);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 10 && p[2].equals(username) && p[3].equals(password)) {
                return new CounterStaff(
                        p[0], p[1], p[2], p[3], p[4],
                        Integer.parseInt(p[5]), p[6], p[7], p[8], p[9]
                );
            }
        }
        return null;
    }

    private Technician checkTechnicianLogin(String username, String password) {
        ArrayList<String> lines = FileUtil.readAllLines(techniciansFile);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 12 && p[2].equals(username) && p[3].equals(password)) {
                return new Technician(
                        p[0], p[1], p[2], p[3], p[4],
                        Integer.parseInt(p[5]), p[6], p[7], p[8], p[9],
                        p[10], p[11]
                );
            }
        }
        return null;
    }

    private Customer checkCustomerLogin(String username, String password) {
        ArrayList<String> lines = FileUtil.readAllLines(customersFile);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 10 && p[2].equals(username) && p[3].equals(password)) {
                return new Customer(
                        p[0], p[1], p[2], p[3], p[4],
                        Integer.parseInt(p[5]), p[6], p[7], p[8], p[9]
                );
            }
        }
        return null;
    }

    public boolean signupCustomer(Customer customer) {
        if (customer == null) return false;

        if (ValidationUtil.isEmpty(customer.getName()) ||
            ValidationUtil.isEmpty(customer.getUsername()) ||
            ValidationUtil.isEmpty(customer.getPassword()) ||
            ValidationUtil.isEmpty(customer.getGender()) ||
            ValidationUtil.isEmpty(customer.getPhone()) ||
            ValidationUtil.isEmpty(customer.getEmail()) ||
            ValidationUtil.isEmpty(customer.getAddress()) ||
            ValidationUtil.isEmpty(customer.getNationality())) {
            return false;
        }

        if (!ValidationUtil.isValidEmail(customer.getEmail())) return false;
        if (!ValidationUtil.isValidPhone(customer.getPhone())) return false;
        if (!ValidationUtil.isValidAge(customer.getAge())) return false;
        if (isUsernameExists(customer.getUsername())) return false;

        String newId = IdGenerator.generateNextId("C", customersFile);
        customer.setId(newId);

        FileUtil.appendLine(customersFile, customer.toFileString());
        return true;
    }

    public boolean isUsernameExists(String username) {
        return existsInFile(managersFile, username) ||
               existsInFile(counterStaffFile, username) ||
               existsInFile(techniciansFile, username) ||
               existsInFile(customersFile, username);
    }

    private boolean existsInFile(String filePath, String username) {
        ArrayList<String> lines = FileUtil.readAllLines(filePath);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length > 2 && p[2].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
}