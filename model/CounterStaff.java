package model;

public class CounterStaff extends User {

    public CounterStaff() {
        super();
    }

    public CounterStaff(String id, String name, String username, String password, String gender, int age,
                        String phone, String email, String address, String nationality) {
        super(id, name, username, password, gender, age, phone, email, address, nationality);
    }

    @Override
    public String getRole() {
        return "CounterStaff";
    }
}