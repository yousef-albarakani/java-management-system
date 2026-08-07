package model;

public class Technician extends User {
    private String specialization;
    private String status;

    public Technician() {
        super();
    }

    public Technician(String id, String name, String username, String password, String gender, int age,
                      String phone, String email, String address, String nationality,
                      String specialization, String status) {
        super(id, name, username, password, gender, age, phone, email, address, nationality);
        this.specialization = specialization;
        this.status = status;
    }

    @Override
    public String getRole() {
        return "Technician";
    }

    @Override
    public String toFileString() {
        return getId() + "|" + getName() + "|" + getUsername() + "|" + getPassword() + "|" + getGender() + "|"
                + getAge() + "|" + getPhone() + "|" + getEmail() + "|" + getAddress() + "|"
                + getNationality() + "|" + specialization + "|" + status;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}