
package cityclub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Member {
      private int idm;
      private String nomM ;
      private String prenomM ;
      private String dateNaissanceM;
      private String sexeM ;
      private String emailM;
      private String numéroM ;
      private String sportM ;
      private String dateDebutM;
      private String dateFinM ;
      private int typeabonnement ;
      public Member(int idm ,String nomM ,String prenomM ,String dateNaissanceM ,String sexeM ,String emailM,String numéroM, String sportM ,String dateDebutM ,int typeabonnement,String dateFinM){
          this.idm = idm;
          this.nomM= nomM;
          this.prenomM = prenomM;
          this.dateNaissanceM =dateNaissanceM ;
          this.sexeM =sexeM ;
          this.emailM =emailM ;
          this.numéroM =numéroM ;
          this.sportM =sportM ;
          this.dateDebutM =dateDebutM ;
          this.typeabonnement =typeabonnement;
          this.dateFinM=dateFinM;
      }


    public int getIdm() {
        return idm;
    }

    public String getNomM() {
        return nomM;
    }

    public String getPrenomM() {
        return prenomM;
    }

    public String getDateNaissanceM() {
        return dateNaissanceM;
    }

    public String getSexeM() {
        return sexeM;
    }

    public String getEmailM() {
        return emailM;
    }

    public String getNuméroM() {
        return numéroM;
    }

    public String getSportM() {
        return sportM;
    }

  

    public String getDateDebutM() {
        return dateDebutM;
    }
    public int getTypeAbonnement(){
        return typeabonnement ;
    }
    public String getDateFinM() {
        return dateFinM;
    }
    
    
    public long jourRest() throws ParseException{
        
       Date datefin = new SimpleDateFormat("dd/MM/yyyy").parse(this.dateFinM);
       Date dateActuel= new Date();
       long diff = Math.abs(datefin.getTime() - dateActuel.getTime());
       long diffDays = diff / (24 * 60 * 60 * 1000);
       return diffDays;
    }
    
    
    public boolean isEndABon() throws ParseException{
      //return jourRest()<= 0;
      Date datefin = new SimpleDateFormat("dd/MM/yyyy").parse(this.dateFinM);
      Date dateActuel= new Date();
      long diff = (datefin.getTime() - dateActuel.getTime());
      return diff >= 0;
      
    }
}
