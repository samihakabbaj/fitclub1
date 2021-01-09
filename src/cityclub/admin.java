/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityclub;

/**
 *
 * @author Ahlame SEHLAOUI
 */
public class admin {
    private String user ;
    private String pass;
    public admin(String user ,String pass){
        this.user=user;
        this.pass=pass;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
    public boolean isValidePass(){
        return true;
    }
    
}
