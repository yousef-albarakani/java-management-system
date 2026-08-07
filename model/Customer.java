package model;

public class Customer extends User {

    public Customer() {
        super();
    }

    public Customer(String id, String name, String username, String password, String gender, int age,
                    String phone, String email, String address, String nationality) {
        super(id, name, username, password, gender, age, phone, email, address, nationality);
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}