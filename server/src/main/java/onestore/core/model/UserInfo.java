package onestore.core.model;

import java.util.Objects;

public class UserInfo {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String birthday;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public UserInfo username(String username) {
        this.username = username;
        return this;
    }

    public UserInfo firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public UserInfo lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public UserInfo birthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public UserInfo password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserInfo)) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(username, userInfo.username) && Objects.equals(firstname, userInfo.firstname) && Objects.equals(lastname, userInfo.lastname) && Objects.equals(password, userInfo.password) && Objects.equals(birthday, userInfo.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstname, lastname, password, birthday);
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", password='" + getPassword() + "'" +
            ", birthday='" + getBirthday() + "'" +
            "}";
    }

}