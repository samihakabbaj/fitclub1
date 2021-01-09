
package cityclub;


public class Payement extends Member{
    private int recus ;
    private String datePay;
    private double  montantPayé ;
    private double  restPayé ;
    
    public Payement(int id, String nomM, String prenomM, String dateNaissanceM, String sexeM, String emailM, String numéroM, String sportM , String dateDebutM, int typeabonnement, String dateFinM , int recus ,String datePay , double montantPayé ,double restPayé) {
        super(id, nomM, prenomM, dateNaissanceM, sexeM, emailM, numéroM, sportM, dateDebutM, typeabonnement, dateFinM);
        this.recus = recus ;
        this.datePay = datePay ;
        this.montantPayé = montantPayé ;
        this.restPayé=restPayé ;
    } 



    public int getRecus() {
        return recus;
    }

    public String getDatePay() {
        return datePay;
    }

    public double getMontantPayé() {
        return montantPayé;
    }

    public double getRestPayé() {
        return restPayé;
    }
    
    
}
 