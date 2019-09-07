package Classes;

import java.io.Serializable;

import Utilities.Validation;
import Utilities.Verdict;


public class Information implements Serializable{
    private String userName;
    private String password;
    private String email;
    private boolean gender;

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Information(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Information(String userName, String password, String email, boolean gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.gender = gender;

    }



    public Information() {
    }

    public Information(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Verdict lexicalValidation(){

        boolean validName = Validation.validateUserName(this.userName);
        boolean validPassword = Validation.validatePassword(this.password);
        boolean validEmail = Validation.validateEmail(this.email);
//        Verdict verdict = new Verdict(false,0,null);
        StringBuilder builder = new StringBuilder();

        int status = 0;
        if(!validName){
            status|=1<<0;//first bit is used for userName checking
            builder.append("username");
        }
        if(!validPassword){
            if(status != 0)builder.append(", ");
            status|=1<<1;//second bit is used for password checking
            builder.append("password");
        }
        if(!validEmail){
            if(status != 0)builder.append(", ");
            status|=1<<2;//third bit is used for email checking
            builder.append("email");
        }
        if(status!=0)builder.append(" are not valid");
        else builder.append("ok");
        return new Verdict(status==0,status,builder.toString());
    }



    @Override
    public String toString() {
        return "Info{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                '}';
    }

    public boolean getGender() {
        return gender;
    }

}
