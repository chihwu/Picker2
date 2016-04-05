package dataObjects;

/**
 * Created by ChihWu on 4/5/16.
 */
public class User {

    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String dateOfBirth;
    private String introduction;


    public User(String userName, String firstName, String lastName, String password, String email, String dateOfBirth, String introduction) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.introduction = introduction;
    }

    public User(int id, String userName, String firstName, String lastName, String password, String email, String dateOfBirth, String introduction) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.introduction = introduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return this.userName;
    }
}
