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
public class Company extends User {

    private String name;
    private String afm;

    public Company(String name, String afm) {
        super(afm);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
