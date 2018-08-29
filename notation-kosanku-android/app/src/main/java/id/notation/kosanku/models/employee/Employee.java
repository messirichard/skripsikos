package id.notation.kosanku.models.employee;

import id.notation.kosanku.models.User;

public class Employee {

    private int id_employee;
    private int id_indekos;
    private int id_admin;
    private int id_user;

    private User user;

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

    public int getId_indekos() {
        return id_indekos;
    }

    public void setId_indekos(int id_indekos) {
        this.id_indekos = id_indekos;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
