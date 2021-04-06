package entreprisecorp.restservices.models.admin;

public class AdminFront {
    private String company;
    private String password;
    private String email;

    public AdminFront(String company, String password, String email) {
        this.company = company;
        this.password = password;
        this.email = email;
    }

    public AdminFront() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
