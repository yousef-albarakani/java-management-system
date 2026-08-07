package service;

import java.time.LocalDate;
import java.util.ArrayList;

import model.Payment;
import util.FileUtil;
import util.IdGenerator;
import util.ValidationUtil;

public class PaymentService {

    private final String paymentsFile = "src/data/payments.txt";
    private final String receiptsFile = "src/data/receipts.txt";

    public boolean addPayment(Payment payment, String customerName) {
        if (payment == null || ValidationUtil.isEmpty(customerName)) {
            return false;
        }

        if (!isPaymentDataValid(payment)) {
            return false;
        }

        payment.setPaymentId(IdGenerator.generateNextId("P", paymentsFile));
        FileUtil.appendLine(paymentsFile, payment.toFileString());

        generateReceipt(payment, customerName);
        return true;
    }

    public ArrayList<Payment> getAllPayments() {
        ArrayList<Payment> paymentList = new ArrayList<>();
        ArrayList<String> lines = FileUtil.readAllLines(paymentsFile);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 7) {
                Payment payment = new Payment(
                        p[0], p[1], p[2], Double.parseDouble(p[3]),
                        p[4], p[5], p[6]
                );
                paymentList.add(payment);
            }
        }

        return paymentList;
    }

    public ArrayList<Payment> getPaymentsByCustomer(String customerId) {
        ArrayList<Payment> result = new ArrayList<>();

        for (Payment payment : getAllPayments()) {
            if (payment.getCustomerId().equalsIgnoreCase(customerId)) {
                result.add(payment);
            }
        }

        return result;
    }

    public boolean updatePaymentStatus(String paymentId, String newStatus) {
        ArrayList<String> lines = FileUtil.readAllLines(paymentsFile);
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean updated = false;

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 7 && p[0].equalsIgnoreCase(paymentId)) {
                p[6] = newStatus;
                updatedLines.add(String.join("|", p));
                updated = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (updated) {
            FileUtil.writeAllLines(paymentsFile, updatedLines);
        }

        return updated;
    }

    private void generateReceipt(Payment payment, String customerName) {
        String receiptId = IdGenerator.generateNextId("R", receiptsFile);
        String receiptLine = receiptId + "|" +
                             payment.getPaymentId() + "|" +
                             payment.getAppointmentId() + "|" +
                             customerName + "|" +
                             "RM" + payment.getAmount() + "|" +
                             LocalDate.now();

        FileUtil.appendLine(receiptsFile, receiptLine);
    }

    private boolean isPaymentDataValid(Payment payment) {
        return !ValidationUtil.isEmpty(payment.getAppointmentId()) &&
               !ValidationUtil.isEmpty(payment.getCustomerId()) &&
               payment.getAmount() > 0 &&
               !ValidationUtil.isEmpty(payment.getPaymentMethod()) &&
               !ValidationUtil.isEmpty(payment.getPaymentDate()) &&
               !ValidationUtil.isEmpty(payment.getPaymentStatus());
    }
}