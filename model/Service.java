package model;

public class Service {
    private String serviceId;
    private String serviceName;
    private int durationHours;
    private double price;
    private String description;

    public Service() {
    }

    public Service(String serviceId, String serviceName, int durationHours, double price, String description) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.durationHours = durationHours;
        this.price = price;
        this.description = description;
    }

    public String toFileString() {
        return serviceId + "|" + serviceName + "|" + durationHours + "|" + price + "|" + description;
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

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}