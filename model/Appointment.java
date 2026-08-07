package model;

public class Appointment {
    private String appointmentId;
    private String customerId;
    private String customerName;
    private String serviceId;
    private String serviceName;
    private String date;
    private String time;
    private String technicianId;
    private String technicianName;
    private String status;
    private String counterStaffId;

    public Appointment() {
    }

    public Appointment(String appointmentId, String customerId, String customerName, String serviceId,
                       String serviceName, String date, String time, String technicianId,
                       String technicianName, String status, String counterStaffId) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.date = date;
        this.time = time;
        this.technicianId = technicianId;
        this.technicianName = technicianName;
        this.status = status;
        this.counterStaffId = counterStaffId;
    }

    public String toFileString() {
        return appointmentId + "|" + customerId + "|" + customerName + "|" + serviceId + "|" + serviceName + "|"
                + date + "|" + time + "|" + technicianId + "|" + technicianName + "|" + status + "|" + counterStaffId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCounterStaffId() {
        return counterStaffId;
    }

    public void setCounterStaffId(String counterStaffId) {
        this.counterStaffId = counterStaffId;
    }
}