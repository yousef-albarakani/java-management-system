package model;

public class Comment {
    private String commentId;
    private String appointmentId;
    private String customerId;
    private String customerName;
    private String staffOrTechnicianId;
    private String commentText;
    private String date;

    public Comment() {
    }

    public Comment(String commentId, String appointmentId, String customerId, String customerName,
                   String staffOrTechnicianId, String commentText, String date) {
        this.commentId = commentId;
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.staffOrTechnicianId = staffOrTechnicianId;
        this.commentText = commentText;
        this.date = date;
    }

    public String toFileString() {
        return commentId + "|" + appointmentId + "|" + customerId + "|" + customerName + "|"
                + staffOrTechnicianId + "|" + commentText + "|" + date;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getStaffOrTechnicianId() {
        return staffOrTechnicianId;
    }

    public void setStaffOrTechnicianId(String staffOrTechnicianId) {
        this.staffOrTechnicianId = staffOrTechnicianId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}