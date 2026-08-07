package model;

public class Feedback {
    private String feedbackId;
    private String appointmentId;
    private String technicianId;
    private String technicianName;
    private String customerId;
    private String feedbackText;
    private int rating;

    public Feedback() {
    }

    public Feedback(String feedbackId, String appointmentId, String technicianId,
                    String technicianName, String customerId, String feedbackText, int rating) {
        this.feedbackId = feedbackId;
        this.appointmentId = appointmentId;
        this.technicianId = technicianId;
        this.technicianName = technicianName;
        this.customerId = customerId;
        this.feedbackText = feedbackText;
        this.rating = rating;
    }

    public String toFileString() {
        return feedbackId + "|" + appointmentId + "|" + technicianId + "|" + technicianName + "|"
                + customerId + "|" + feedbackText + "|" + rating;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}