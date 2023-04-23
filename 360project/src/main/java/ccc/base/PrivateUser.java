/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccc.base;

/**
 *
 * @author KonZioutos
 */
public class PrivateUser extends User {

    private String firstname;
    private String surname;
    private String fathername;

    public PrivateUser(String firstname, String surname, String fathername, String afm) {
        super(afm);
        this.firstname = firstname;
        this.surname = surname;
        this.fathername = fathername;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }


}
