package model;

public abstract class User {
    private String id;
    private String name;
    private String username;
    private String password;
    private String gender;
    private int age;
    private String phone;
    private String email;
    private String address;
    private String nationality;

    public User() {
    }

    public User(String id, String name, String username, String password, String gender, int age,
                String phone, String email, String address, String nationality) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.nationality = nationality;
    }

    public abstract String getRole(); // Abstraction + Polymorphism

    public String toFileString() {
        return id + "|" + name + "|" + username + "|" + password + "|" + gender + "|" + age + "|"
                + phone + "|" + email + "|" + address + "|" + nationality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Role: " + getRole() + ", ID: " + id + ", Name: " + name + ", Username: " + username;
    }
}