/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityclub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author SAMIHA
 */



public class Caoch {
      private int idc;
      private String nomC ;
      private String prenomC ;
      private String dateNaissanceC;
      private String sexeC ;
      private String emailC;
      private String numéroC ;
      private String sportC ;
      public Caoch(int id ,String nomC ,String prenomC ,String dateNaissanceC ,String sexeC ,String emailC,String numéroC, String sportC){
          this.idc = idc;
          this.nomC= nomC;
          this.prenomC = prenomC;
          this.dateNaissanceC =dateNaissanceC ;
          this.sexeC =sexeC ;
          this.emailC =emailC ;
          this.numéroC =numéroC ;
          this.sportC =sportC ;
      }


    public int getId() {
        return idc;
    }

    public String getNomC() {
        return nomC;
    }

    public String getPrenomC() {
        return prenomC;
    }

    public String getDateNaissanceC() {
        return dateNaissanceC;
    }

    public String getSexeC() {
        return sexeC;
    }

    public String getEmailC() {
        return emailC;
    }

    public String getNuméroC() {
        return numéroC;
    }

    public String getSportC() {
        return sportC;
    }

  

    
    

    

}
