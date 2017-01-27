package degree.nano.ahmed.nanodegree.model;

/**
 * Created by ahmed on 16/01/17.
 */
public class UserModel {

    String userName;
    String email;
    String mobile;

    public UserModel(String userName, String email, String mobile) {
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
