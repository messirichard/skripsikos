package id.notation.kosanku.models.employee;

import java.util.List;

public class EmployeeCollection {

    private List<Employee> data;
    private int status;

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
