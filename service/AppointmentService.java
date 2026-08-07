package service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import model.Appointment;
import util.FileUtil;
import util.IdGenerator;
import util.ValidationUtil;

public class AppointmentService {

    private final String appointmentsFile = "src/data/appointments.txt";

    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) return false;

        if (!isAppointmentDataValid(appointment)) {
            return false;
        }

        LocalDate date = LocalDate.parse(appointment.getDate());
        LocalTime time = LocalTime.parse(appointment.getTime());

        if (!ValidationUtil.isValidAppointmentDateTime(date, time)) {
            return false;
        }

        appointment.setAppointmentId(IdGenerator.generateNextId("A", appointmentsFile));
        FileUtil.appendLine(appointmentsFile, appointment.toFileString());
        return true;
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> appointmentList = new ArrayList<>();
        ArrayList<String> lines = FileUtil.readAllLines(appointmentsFile);

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 11) {
                Appointment appointment = new Appointment(
                        p[0], p[1], p[2], p[3], p[4],
                        p[5], p[6], p[7], p[8], p[9], p[10]
                );
                appointmentList.add(appointment);
            }
        }

        return appointmentList;
    }

    public ArrayList<Appointment> getAppointmentsByTechnician(String technicianId) {
        ArrayList<Appointment> result = new ArrayList<>();

        for (Appointment appointment : getAllAppointments()) {
            if (appointment.getTechnicianId().equalsIgnoreCase(technicianId)) {
                result.add(appointment);
            }
        }

        return result;
    }

    public ArrayList<Appointment> getAppointmentsByCustomer(String customerId) {
        ArrayList<Appointment> result = new ArrayList<>();

        for (Appointment appointment : getAllAppointments()) {
            if (appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                result.add(appointment);
            }
        }

        return result;
    }

    public boolean updateAppointmentStatus(String appointmentId, String newStatus) {
        ArrayList<String> lines = FileUtil.readAllLines(appointmentsFile);
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean updated = false;

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 11 && p[0].equalsIgnoreCase(appointmentId)) {
                p[9] = newStatus;
                updatedLines.add(String.join("|", p));
                updated = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (updated) {
            FileUtil.writeAllLines(appointmentsFile, updatedLines);
        }

        return updated;
    }

    public boolean assignTechnician(String appointmentId, String technicianId, String technicianName) {
        ArrayList<String> lines = FileUtil.readAllLines(appointmentsFile);
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean updated = false;

        for (String line : lines) {
            String[] p = line.split("\\|");
            if (p.length >= 11 && p[0].equalsIgnoreCase(appointmentId)) {
                p[7] = technicianId;
                p[8] = technicianName;
                p[9] = "Assigned";
                updatedLines.add(String.join("|", p));
                updated = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (updated) {
            FileUtil.writeAllLines(appointmentsFile, updatedLines);
        }

        return updated;
    }

    private boolean isAppointmentDataValid(Appointment appointment) {
        return !ValidationUtil.isEmpty(appointment.getCustomerId()) &&
               !ValidationUtil.isEmpty(appointment.getCustomerName()) &&
               !ValidationUtil.isEmpty(appointment.getServiceId()) &&
               !ValidationUtil.isEmpty(appointment.getServiceName()) &&
               !ValidationUtil.isEmpty(appointment.getDate()) &&
               !ValidationUtil.isEmpty(appointment.getTime()) &&
               !ValidationUtil.isEmpty(appointment.getTechnicianId()) &&
               !ValidationUtil.isEmpty(appointment.getTechnicianName()) &&
               !ValidationUtil.isEmpty(appointment.getStatus()) &&
               !ValidationUtil.isEmpty(appointment.getCounterStaffId());
    }
}