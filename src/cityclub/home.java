package cityclub;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class home extends javax.swing.JFrame {

    /**
     * Creates new form home
     *
     * @throws java.text.ParseException
     */
    public home() throws ParseException {
        initComponents();
        affichage_abonnement_en_cours();
        abonnement_termine();
        impayes();
        renouvlerAbonnementMember();
        historique();
        reglementMember();
        affichage_admin();
        statist();
        this.setLocationRelativeTo(this);
    }

    public Connection getConnection() {
        Connection con;
        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3324/gym", "root", "");
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // table des abonnement en cours
    public void affichage_abonnement_en_cours() throws ParseException {
        DefaultTableModel model = (DefaultTableModel) jTable_abonnementsencours.getModel();
        Object[] row = new Object[5];
        Connection connection = getConnection();
        int i = 0;
        String query = "SELECT `nomM`, `prenomM`, `DateDebut`,`DateFin` FROM `member`,`abonnement` WHERE idm=ID_MA and DateFin >= DATE( NOW() )";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                row[0] = 1 + jourRest(rs.getDate("DateFin"));
                row[1] = rs.getString("prenomM");
                row[2] = rs.getString("nomM");
                row[3] = rs.getDate("DateDebut");
                row[4] = rs.getDate("DateFin");
                model.addRow(row);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tabel des abonnemt terminer
    public void abonnement_termine() throws ParseException {
        DefaultTableModel model = (DefaultTableModel) jTable_abonnementtermine.getModel();
        Object[] row = new Object[4];
        Connection connection = getConnection();
        int i = 0;
        String query = "SELECT `nomM`, `prenomM`, `DateDebut`,`DateFin` FROM `member`,`abonnement` WHERE idm=ID_MA and DateFin < DATE( NOW() )";
        //SELECT * FROM produit WHERE date_validite_produit > CURDATE()
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                row[0] = rs.getString("nomM");
                row[1] = rs.getString("prenomM");
                row[2] = rs.getDate("DateDebut");
                row[3] = rs.getDate("DateFin");
                model.addRow(row);
                i++;
            }
            nb_abonnemetTermine.setText(String.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // table des impayes :
    public void impayes() {

        DefaultTableModel model = (DefaultTableModel) jTable_impayé.getModel();
        Object row[] = new Object[10];
        Connection connection = getConnection();
        int i = 0;
        String query = "SELECT `idm`, `nomM`, `prenomM`, `Sport`, `typeabon`,`MontantPayé`,`recus`,`DateDebut`,`DateFin` FROM `member`,`abonnement` WHERE idm=ID_MA ";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next() & doitPaye(rs.getDouble("MontantPayé"), rs.getInt("typeabon"))>0) {
                row[0] = doitPaye(rs.getDouble("MontantPayé"), rs.getInt("typeabon"));
                row[1] = rs.getDouble("MontantPayé");
                row[2] = rs.getString("prenomM");
                row[3] = rs.getString("nomM");
                row[4] = rs.getString("Sport");
                row[5] = rs.getInt("idm");
                row[6] = prixAbonnement(rs.getInt("typeabon"));
                row[7] = rs.getDate("DateDebut");
                row[8] = rs.getDate("DateFin");
                row[9] = rs.getInt("recus");
                model.addRow(row);
                i++;
            }
            nb_abonnementTR.setText(String.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // table des historique

    public void historique() {

        DefaultTableModel model = (DefaultTableModel) table_historique.getModel();
        Object row[] = new Object[6];
        Connection connection = getConnection();

        String query = "SELECT `nomM`, `prenomM`, `sport`, `typeabon`,`datePay`, `montantPayé` FROM `member`,`payement`,`abonnement` WHERE idm=ID_MP and ID_MP=ID_MA";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                row[0] = rs.getString("nomM");
                row[1] = rs.getString("prenomM");
                row[2] = rs.getString("sport");
                row[3] = rs.getInt("typeabon");
                row[4] = rs.getDate("datePay");
                row[5] = rs.getDouble("montantPayé");
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //table  de reglement 
    public void reglementMember() throws ParseException {
        DefaultTableModel model = (DefaultTableModel) tbl_memberReg.getModel();
        Object[] row = new Object[3];

        Connection connection = getConnection();
        int i = 0;
        String query = "SELECT `idm`, `nomM`, `prenomM` FROM `member`";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                row[0] = rs.getInt("idm");
                row[1] = rs.getString("prenomM");
                row[2] = rs.getString("nomM");

                model.addRow(row);
                i++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tabel de rehlement payement par id
    public void reglementP(int id) {

        DefaultTableModel model = (DefaultTableModel) tbl_regelementP.getModel();
        model.setRowCount(0);
        Object row[] = new Object[3];
        Connection connection = getConnection();
        int i = 0;
        String query = "SELECT `datePay`, `montantPayéP` FROM `payement` WHERE ID_MP =" + id;
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                row[0] = id;
                row[1] = rs.getString("datePay");
                row[2] = rs.getDouble("montantPayéP");
                model.addRow(row);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // partie admin
    public ArrayList<admin> getadminList() {
        ArrayList<admin> adminList = new ArrayList<admin>();
        Connection connection = getConnection();
        String query = "SELECT * FROM `admin` WHERE 1";
        Statement st = null;
        ResultSet rs;
        admin ad;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                ad = new admin(rs.getString("user"), rs.getString("password"));
                adminList.add(ad);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminList;

    }
 
    // affichage des info sur les admin :
    public void affichage_admin() {
        ArrayList<admin> list = getadminList();
        DefaultTableModel model = (DefaultTableModel) table_admin.getModel();
        Object row[] = new Object[2];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getUser();
            row[1] = list.get(i).getPass();
            model.addRow(row);

        }

    }

    public void executeSQLQuery(String query, String message) throws SQLException {
        Connection con = getConnection();
        Statement st;
        try {
            st = (Statement) con.createStatement();
            if (st.executeUpdate(query) == 1) {
                JOptionPane.showMessageDialog(null, message);
            } else {
                JOptionPane.showMessageDialog(null, "!" + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void refrech_table() {
        DefaultTableModel model = (DefaultTableModel) table_admin.getModel();
        model.setRowCount(0);
        affichage_admin();
    }
    
    // partie member
    public ArrayList<member> getmemberList() {
        ArrayList<member> memberList = new ArrayList<member>();
        Connection connection = getConnection();
        String query = "SELECT * FROM `member`";
        Statement st = null;
        ResultSet rs;
        member mb;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                mb = new member(rs.getInt("Idm"), rs.getString("NomM"),rs.getString("PrenomM"),rs.getString("DateNaissanceM"),rs.getString("SexeM"),rs.getString("EmailM"),rs.getString("NuméroM"),rs.getString("SportM"),rs.getString("DateDebutM"),rs.getInt("TypeAbonnement"),rs.getString("DateFinM"));
                memberList.add(mb);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberList;

    }
 
    // affichage des info sur les membres :
    public void affichage_member() {
        ArrayList<member> list = getmemberList();
        DefaultTableModel model = (DefaultTableModel) table_members.getModel();
        Object row[] = new Object[11];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getIdm();
            row[1] = list.get(i).getNomM();
            row[2] = list.get(i).getPrenomM();
            row[3] = list.get(i).getDateNaissanceM();
            row[4] = list.get(i).getSexeM();
            row[5] = list.get(i).getEmailM();
            row[6] = list.get(i).getNuméroM();
            row[7] = list.get(i).getSportM();
            row[8] = list.get(i).getDateDebutM();
            row[9] = list.get(i).getTypeAbonnement();
            row[10] = list.get(i).getDateFinM();
            
            
            
            model.addRow(row);

        }

    }

    public void executeSQLQuery(String query, String message) throws SQLException {
        Connection con = getConnection();
        Statement st;
        try {
            st = (Statement) con.createStatement();
            if (st.executeUpdate(query) == 1) {
                JOptionPane.showMessageDialog(null, message);
            } else {
                JOptionPane.showMessageDialog(null, "!" + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void refrech_table() {
        DefaultTableModel model = (DefaultTableModel) table_members.getModel();
        model.setRowCount(0);
        affichage_member();
    }
    
    // partie coach
    public ArrayList<coach> getcoachList() {
        ArrayList<coach> coachList = new ArrayList<coach>();
        Connection connection = getConnection();
        String query = "SELECT * FROM `coach`";
        Statement st = null;
        ResultSet rs;
        coach cc;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                cc = new coach(rs.getInt("Idc"), rs.getString("NomC"),rs.getString("PrenomC"),rs.getString("DateNaissanceC"),rs.getString("SexeC"),rs.getString("EmailC"),rs.getString("NuméroC"),rs.getString("SportC"));
                coachList.add(mb);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coachList;

    }
 
    // affichage des info sur les coachs :
    public void affichage_coach() {
        ArrayList<coach> list = getcoachList();
        DefaultTableModel model = (DefaultTableModel) table_coach.getModel();
        Object row[] = new Object[8];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getIdc();
            row[1] = list.get(i).getNomC();
            row[2] = list.get(i).getPrenomC();
            row[3] = list.get(i).getDateNaissanceC();
            row[4] = list.get(i).getSexeC();
            row[5] = list.get(i).getEmailC();
            row[6] = list.get(i).getNuméroC();
            row[7] = list.get(i).getSportC();
            
            
            
            model.addRow(row);

        }

    }

    public void executeSQLQuery(String query, String message) throws SQLException {
        Connection con = getConnection();
        Statement st;
        try {
            st = (Statement) con.createStatement();
            if (st.executeUpdate(query) == 1) {
                JOptionPane.showMessageDialog(null, message);
            } else {
                JOptionPane.showMessageDialog(null, "!" + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void refrech_table() {
        DefaultTableModel model = (DefaultTableModel) table_coach.getModel();
        model.setRowCount(0);
        affichage_coach();
    }

    //Renouvler abonnement : table_renouvlerabonnement
    public void renouvlerAbonnementMember() throws ParseException {
        DefaultTableModel model = (DefaultTableModel) table_renouvlerabonnement.getModel();
        Object[] row = new Object[3];

        Connection connection = getConnection();

        String query = "SELECT `idm`, `nomM`, `prenomM` FROM `member`";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                row[0] = rs.getInt("idm");
                row[1] = rs.getString("prenomM");
                row[2] = rs.getString("nomM");

                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
////////////////////////////////////////////////////////////////////Statistique :

    @SuppressWarnings("deprecation")
    public double numberMember() {
        Connection connection = getConnection();
        String query = "SELECT COUNT(*) FROM `member`,`abonnement` WHERE idm=ID_MA and DateFin >= DATE( NOW() ) ";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            String s = null ;
            while(rs.next()){
                 s=rs.getString("count(*)");
            }
            return new Double(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;

    }

    public double numberMemberM() {
        Connection connection = getConnection();
        String query = "SELECT COUNT(*) FROM `member`,`abonnement` WHERE idm=ID_MA and DateFin >= DATE( NOW() ) and (sexe='m'or sexe='M') ";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            return new Double(rs.getString("count(*)"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;

    }

    public double numberMemberF() {
        Connection connection = getConnection();
        String query = "SELECT COUNT(*) FROM `member`,`abonnement` WHERE idm=ID_MA and DateFin >= DATE( NOW() ) and sexe='f'";
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            return new Double(rs.getString("count(*)"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;

    }

    public void statist() {
        // pourcentage des hommes actif :
        double P_Homme = (numberMemberM() / numberMember()) * 100;
        double P_Femme = (numberMemberF() / numberMember()) * 100;
        double Totale = numberMember();
        txt_pHomme.setText(String.valueOf(P_Homme)+"%");
        txt_pFemme.setText(String.valueOf(P_Femme)+"%");
        txt_Totale.setText(String.valueOf(Totale));
    }

    public long jourRest(Date dat) throws ParseException {

        java.util.Date datefin = dat;
        java.util.Date dateActuel = new java.util.Date();
        long diff = Math.abs(datefin.getTime() - dateActuel.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }

    public boolean isEndABon(String date) throws ParseException {
        //return jourRest()<= 0;
        java.util.Date datefin = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        java.util.Date dateActuel = new java.util.Date();
        long diff = (datefin.getTime() - dateActuel.getTime());
        return diff >= 0;

    }

  //  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        home = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_Exit = new javax.swing.JLabel();
        btn_admin = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        table = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        table_members = new javax.swing.JTable();
        jLabel72 = new javax.swing.JLabel();
        txt_nomm = new javax.swing.JTextField();
        txt_prenomm = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jPanel40 = new javax.swing.JPanel();
        btn_modifierm = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        btn_ajouterm = new javax.swing.JLabel();
        btn_supprimerm = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txt_datenaissancem = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel83 = new javax.swing.JLabel();
        txt_sexem = new javax.swing.JTextField();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel84 = new javax.swing.JLabel();
        txt_emailm = new javax.swing.JTextField();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel85 = new javax.swing.JLabel();
        txt_numm = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        txt_sportm = new javax.swing.JTextField();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel87 = new javax.swing.JLabel();
        txt_datedebutm = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        txt_typeabonnementm = new javax.swing.JTextField();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        txt_idm = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        table_coach = new javax.swing.JTable();
        jLabel89 = new javax.swing.JLabel();
        txt_nomc = new javax.swing.JTextField();
        txt_prenomc = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jPanel43 = new javax.swing.JPanel();
        btn_modifierc = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        btn_ajouterc = new javax.swing.JLabel();
        btn_supprimerc = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        txt_datenaissancec = new javax.swing.JTextField();
        jSeparator18 = new javax.swing.JSeparator();
        jLabel93 = new javax.swing.JLabel();
        txt_sexec = new javax.swing.JTextField();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel94 = new javax.swing.JLabel();
        txt_emailc = new javax.swing.JTextField();
        jSeparator20 = new javax.swing.JSeparator();
        jLabel95 = new javax.swing.JLabel();
        txt_numéroc = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jSeparator21 = new javax.swing.JSeparator();
        txt_sportc = new javax.swing.JTextField();
        jSeparator22 = new javax.swing.JSeparator();
        txt_idc = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        btn_reglements = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        btn_historique = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        btn_regler = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btn_abonnemetencours = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btn_abonnementtermines = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        btn_lesimpayés = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        panelrechercher = new javax.swing.JPanel();
        rbtn_nompredat = new javax.swing.JRadioButton();
        txt_nompredat = new javax.swing.JTextField();
        rbtn_ID = new javax.swing.JRadioButton();
        txt_ID = new javax.swing.JTextField();
        rbt_sports = new javax.swing.JRadioButton();
        cb_sport = new javax.swing.JComboBox<>();
        rbtn_homme = new javax.swing.JRadioButton();
        rbtn_femme = new javax.swing.JRadioButton();
        jLabel47 = new javax.swing.JLabel();
        btn_recherche = new javax.swing.JLabel();
        req = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_abonnementsencours = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_historique = new javax.swing.JTable();
        jLabel56 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jpanel_rechregler = new javax.swing.JPanel();
        rbtn_nompredatregler = new javax.swing.JRadioButton();
        txt_nompredatregler = new javax.swing.JTextField();
        rbtn_IDregler = new javax.swing.JRadioButton();
        txt_IDregler = new javax.swing.JTextField();
        rbt_sportsregler = new javax.swing.JRadioButton();
        cb_spoetregler = new javax.swing.JComboBox<>();
        rbtn_hommeregler = new javax.swing.JRadioButton();
        rbtn_femmeregler = new javax.swing.JRadioButton();
        alb_sexeregler = new javax.swing.JLabel();
        btn_rechercheregler = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable_abonnementtermine = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        nb_abonnemetTermine = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        msg_renouvler1 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jpanel_rech = new javax.swing.JPanel();
        rbtn_nompredatAT = new javax.swing.JRadioButton();
        txt_nompredatAT = new javax.swing.JTextField();
        rbtn_IDAT = new javax.swing.JRadioButton();
        txt_IDAT = new javax.swing.JTextField();
        rbt_sportsAT = new javax.swing.JRadioButton();
        cb_spoetAT = new javax.swing.JComboBox<>();
        rbtn_hommeAT = new javax.swing.JRadioButton();
        rbtn_femmeAT = new javax.swing.JRadioButton();
        alb_sexeAT = new javax.swing.JLabel();
        btn_rechercheAT = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_impayé = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nb_abonnementTR = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        msg_renouvler = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_memberReg = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_regelementP = new javax.swing.JTable();
        jpanel_rech1 = new javax.swing.JPanel();
        rbtn_nompredatREG = new javax.swing.JRadioButton();
        txt_nompredatREG = new javax.swing.JTextField();
        rbtn_IDREG = new javax.swing.JRadioButton();
        txt_IDREG = new javax.swing.JTextField();
        rbt_sportsREG = new javax.swing.JRadioButton();
        cb_spoetREG = new javax.swing.JComboBox<>();
        rbtn_hommeREG = new javax.swing.JRadioButton();
        rbtn_femmeREG = new javax.swing.JRadioButton();
        alb_sexeREG = new javax.swing.JLabel();
        btn_rechercheREG = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        table_admin = new javax.swing.JTable();
        jLabel57 = new javax.swing.JLabel();
        txt_user = new javax.swing.JTextField();
        txt_pass = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel28 = new javax.swing.JPanel();
        btn_modifier = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        btn_ajouter = new javax.swing.JLabel();
        btn_supprimer = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        table_renouvlerabonnement = new javax.swing.JTable();
        jLabel60 = new javax.swing.JLabel();
        txt_abonementRA = new javax.swing.JTextField();
        txt_MontantPayéRA = new javax.swing.JTextField();
        txt_dateDebutRA = new javax.swing.JFormattedTextField();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        btn_confirmerRenouvlerAb = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        txt_sportRA = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel68 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        txt_pHomme = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        txt_pFemme = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        txt_Totale = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btn_members = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btn_abonnement = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btn_paiments = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btn_consultation = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btn_statistiques = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btn_statistiques1 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        home.setForeground(new java.awt.Color(102, 255, 102));
        home.setMaximumSize(new java.awt.Dimension(1076, 32767));
        home.setOpaque(false);
        home.setPreferredSize(new java.awt.Dimension(1248, 625));
        home.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                homeMouseDragged(evt);
            }
        });
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                homeMousePressed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("City Club");

        btn_Exit.setBackground(new java.awt.Color(0, 0, 0));
        btn_Exit.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btn_Exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/cancel (1).png"))); // NOI18N
        btn_Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ExitMouseClicked(evt);
            }
        });

        btn_admin.setForeground(new java.awt.Color(255, 255, 255));
        btn_admin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/adjust (2).png"))); // NOI18N
        btn_admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_adminMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_admin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_Exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
            .addComponent(btn_admin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(1199, 670));

        table.setBackground(new java.awt.Color(242, 237, 237));

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));

        table_members.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 11)); // NOI18N
        table_members.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "NOM", "PRENOM", "DATE NAISSANCE", "SEXE", "EMAIL", "NUMERO", "SPORT", "DATE DEBUT", "TYPE ABONNEMENT", "DATE FIN"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_members.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_membersMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(table_members);

        jLabel72.setBackground(new java.awt.Color(255, 255, 255));
        jLabel72.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 48)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(228, 67, 13));
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("MEMBERS");

        txt_nomm.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_nomm.setActionCommand("<Not Set>");
        txt_nomm.setBorder(null);

        txt_prenomm.setBorder(null);
        txt_prenomm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_prenommActionPerformed(evt);
            }
        });

        jLabel74.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel74.setText("NOM :");

        jLabel81.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel81.setText("PRENOM  :");

        jSeparator7.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator7.setForeground(new java.awt.Color(228, 67, 13));

        jSeparator8.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator8.setForeground(new java.awt.Color(228, 67, 13));

        jPanel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel40MouseClicked(evt);
            }
        });

        btn_modifierm.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        btn_modifierm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_modifierm.setText("Modifier");
        btn_modifierm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_modifiermMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_modifierm, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_modifierm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btn_ajouterm.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        btn_ajouterm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_ajouterm.setText("Ajouter");
        btn_ajouterm.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btn_ajoutermAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btn_ajouterm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajoutermMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_ajouterm, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_ajouterm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
        );

        btn_supprimerm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_supprimerm.setText("      supprimer membre !!");
        btn_supprimerm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_supprimermMouseClicked(evt);
            }
        });

        jLabel82.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel82.setText("DATE DE NAISSANCE :");

        txt_datenaissancem.setBorder(null);
        txt_datenaissancem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_datenaissancemActionPerformed(evt);
            }
        });

        jSeparator9.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator9.setForeground(new java.awt.Color(228, 67, 13));

        jLabel83.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel83.setText("SEXE :");

        txt_sexem.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_sexem.setActionCommand("<Not Set>");
        txt_sexem.setBorder(null);

        jSeparator10.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator10.setForeground(new java.awt.Color(228, 67, 13));

        jLabel84.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel84.setText("EMAIL :");

        txt_emailm.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_emailm.setActionCommand("<Not Set>");
        txt_emailm.setBorder(null);

        jSeparator11.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator11.setForeground(new java.awt.Color(228, 67, 13));

        jLabel85.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel85.setText("NUMERO TELEPHONE :");

        txt_numm.setBorder(null);
        txt_numm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nummActionPerformed(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel86.setText("SPORT :");

        jSeparator12.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator12.setForeground(new java.awt.Color(228, 67, 13));

        txt_sportm.setBorder(null);
        txt_sportm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sportmActionPerformed(evt);
            }
        });

        jSeparator13.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator13.setForeground(new java.awt.Color(228, 67, 13));

        jLabel87.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel87.setText("DATE DEBUT :");

        txt_datedebutm.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_datedebutm.setActionCommand("<Not Set>");
        txt_datedebutm.setBorder(null);

        jLabel88.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel88.setText("TYPE ABONNEMENT (MOIS) :");

        txt_typeabonnementm.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_typeabonnementm.setActionCommand("<Not Set>");
        txt_typeabonnementm.setBorder(null);

        jSeparator15.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator15.setForeground(new java.awt.Color(228, 67, 13));

        jSeparator14.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator14.setForeground(new java.awt.Color(228, 67, 13));

        txt_idm.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_idm.setActionCommand("<Not Set>");
        txt_idm.setBorder(null);

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addComponent(btn_supprimerm)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80))
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel39Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel82)
                                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_datenaissancem, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_sexem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                                    .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_prenomm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                                    .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_nomm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                                    .addComponent(txt_idm, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel39Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel86)
                            .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_sportm, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator13, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_datedebutm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_numm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_emailm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel85)
                            .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator15, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_typeabonnementm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)))
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_emailm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_numm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_sportm, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_datedebutm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_typeabonnementm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(126, 126, 126))
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nomm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel81)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_prenomm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_datenaissancem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_sexem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_idm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addComponent(btn_supprimerm, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1068, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        table.addTab("0", jPanel26);

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));

        table_coach.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 11)); // NOI18N
        table_coach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "NOM", "PRENOM", "DATE NAISSANCE", "SEXE", "EMAIL", "NUMERO", "SPORT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_coach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_coachMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(table_coach);

        jLabel89.setBackground(new java.awt.Color(255, 255, 255));
        jLabel89.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 48)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(228, 67, 13));
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("COACH");

        txt_nomc.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_nomc.setActionCommand("<Not Set>");
        txt_nomc.setBorder(null);

        txt_prenomc.setBorder(null);
        txt_prenomc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_prenomcActionPerformed(evt);
            }
        });

        jLabel90.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel90.setText("NOM :");

        jLabel91.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel91.setText("PRENOM  :");

        jSeparator16.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator16.setForeground(new java.awt.Color(228, 67, 13));

        jSeparator17.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator17.setForeground(new java.awt.Color(228, 67, 13));

        jPanel43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel43MouseClicked(evt);
            }
        });

        btn_modifierc.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        btn_modifierc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_modifierc.setText("Modifier");
        btn_modifierc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_modifiercMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_modifierc, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_modifierc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btn_ajouterc.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        btn_ajouterc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_ajouterc.setText("Ajouter");
        btn_ajouterc.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btn_ajoutercAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btn_ajouterc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajoutercMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_ajouterc, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_ajouterc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
        );

        btn_supprimerc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_supprimerc.setText("      supprimer coach !!");
        btn_supprimerc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_supprimercMouseClicked(evt);
            }
        });

        jLabel92.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel92.setText("DATE DE NAISSANCE :");

        txt_datenaissancec.setBorder(null);
        txt_datenaissancec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_datenaissancecActionPerformed(evt);
            }
        });

        jSeparator18.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator18.setForeground(new java.awt.Color(228, 67, 13));

        jLabel93.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel93.setText("SEXE :");

        txt_sexec.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_sexec.setActionCommand("<Not Set>");
        txt_sexec.setBorder(null);

        jSeparator19.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator19.setForeground(new java.awt.Color(228, 67, 13));

        jLabel94.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 10)); // NOI18N
        jLabel94.setText("EMAIL :");

        txt_emailc.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_emailc.setActionCommand("<Not Set>");
        txt_emailc.setBorder(null);

        jSeparator20.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator20.setForeground(new java.awt.Color(228, 67, 13));

        jLabel95.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel95.setText("NUMERO TELEPHONE :");

        txt_numéroc.setBorder(null);
        txt_numéroc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_numérocActionPerformed(evt);
            }
        });

        jLabel96.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 10)); // NOI18N
        jLabel96.setText("SPORT :");

        jSeparator21.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator21.setForeground(new java.awt.Color(228, 67, 13));

        txt_sportc.setBorder(null);
        txt_sportc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sportcActionPerformed(evt);
            }
        });

        jSeparator22.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator22.setForeground(new java.awt.Color(228, 67, 13));

        txt_idc.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_idc.setActionCommand("<Not Set>");
        txt_idc.setBorder(null);

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addComponent(btn_supprimerc)
                .addContainerGap(728, Short.MAX_VALUE))
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(135, 135, 135))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel42Layout.createSequentialGroup()
                                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel92)
                                            .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_datenaissancec, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jSeparator18, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jSeparator19, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txt_sexec, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                                            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jSeparator16, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txt_prenomc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                                            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jSeparator17, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txt_nomc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                                            .addGroup(jPanel42Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(txt_idc, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel96)
                                            .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_sportc, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jSeparator21, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txt_numéroc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jSeparator20, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txt_emailc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel95)))))
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(60, 60, 60)))
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addComponent(txt_nomc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel91)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_prenomc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_datenaissancec, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_sexec, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_emailc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel95)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_numéroc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_sportc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_idc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(126, 126, 126)
                .addComponent(btn_supprimerc, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        table.addTab("1", jPanel5);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setBackground(new java.awt.Color(228, 67, 13));
        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 48)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Paiments");

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/folder (1).png"))); // NOI18N

        btn_reglements.setBackground(new java.awt.Color(255, 255, 255));
        btn_reglements.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 67, 13)));
        btn_reglements.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_reglementsMouseClicked(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(228, 67, 13));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Regelemnts");

        javax.swing.GroupLayout btn_reglementsLayout = new javax.swing.GroupLayout(btn_reglements);
        btn_reglements.setLayout(btn_reglementsLayout);
        btn_reglementsLayout.setHorizontalGroup(
            btn_reglementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_reglementsLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        btn_reglementsLayout.setVerticalGroup(
            btn_reglementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_reglementsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btn_reglements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_reglements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/online-payment.png"))); // NOI18N

        btn_historique.setBackground(new java.awt.Color(255, 255, 255));
        btn_historique.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 67, 13)));
        btn_historique.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_historiqueMouseClicked(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(228, 67, 13));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Historique");

        javax.swing.GroupLayout btn_historiqueLayout = new javax.swing.GroupLayout(btn_historique);
        btn_historique.setLayout(btn_historiqueLayout);
        btn_historiqueLayout.setHorizontalGroup(
            btn_historiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_historiqueLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        btn_historiqueLayout.setVerticalGroup(
            btn_historiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_historiqueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btn_historique, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_historique, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/payment.png"))); // NOI18N

        btn_regler.setBackground(new java.awt.Color(255, 255, 255));
        btn_regler.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 67, 13)));
        btn_regler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_reglerMouseClicked(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(228, 67, 13));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("Régler");

        javax.swing.GroupLayout btn_reglerLayout = new javax.swing.GroupLayout(btn_regler);
        btn_regler.setLayout(btn_reglerLayout);
        btn_reglerLayout.setHorizontalGroup(
            btn_reglerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_reglerLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        btn_reglerLayout.setVerticalGroup(
            btn_reglerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_reglerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btn_regler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_regler, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(337, 337, 337)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.addTab("2", jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setBackground(new java.awt.Color(255, 51, 0));
        jLabel15.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 48)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Consultation");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel11.setForeground(new java.awt.Color(255, 255, 255));

        btn_abonnemetencours.setBackground(new java.awt.Color(255, 255, 255));
        btn_abonnemetencours.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 67, 13)));
        btn_abonnemetencours.setForeground(new java.awt.Color(228, 67, 13));
        btn_abonnemetencours.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_abonnemetencoursMouseClicked(evt);
            }
        });
        btn_abonnemetencours.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_abonnemetencoursKeyPressed(evt);
            }
        });

        jLabel18.setBackground(new java.awt.Color(228, 67, 13));
        jLabel18.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(228, 67, 13));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Abonnement en cours");

        javax.swing.GroupLayout btn_abonnemetencoursLayout = new javax.swing.GroupLayout(btn_abonnemetencours);
        btn_abonnemetencours.setLayout(btn_abonnemetencoursLayout);
        btn_abonnemetencoursLayout.setHorizontalGroup(
            btn_abonnemetencoursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_abonnemetencoursLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addContainerGap())
        );
        btn_abonnemetencoursLayout.setVerticalGroup(
            btn_abonnemetencoursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_abonnemetencoursLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addContainerGap())
        );

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/responsibility (3).png"))); // NOI18N

        jLabel19.setBackground(new java.awt.Color(228, 67, 13));
        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(228, 67, 13));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel19.setText("° ");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel20.setBackground(new java.awt.Color(228, 67, 13));
        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(228, 67, 13));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel20.setText("° ");
        jLabel20.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel21.setBackground(new java.awt.Color(228, 67, 13));
        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(228, 67, 13));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel21.setText("° ");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel22.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel22.setText("Identité");

        jLabel23.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel23.setText("Jous restants");

        jLabel24.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel24.setText("Date debut/fin");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_abonnemetencours, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(btn_abonnemetencours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel13.setForeground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(258, 346));

        btn_abonnementtermines.setBackground(new java.awt.Color(255, 255, 255));
        btn_abonnementtermines.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 67, 13)));
        btn_abonnementtermines.setForeground(new java.awt.Color(228, 67, 13));
        btn_abonnementtermines.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_abonnementterminesMouseClicked(evt);
            }
        });

        jLabel25.setBackground(new java.awt.Color(228, 67, 13));
        jLabel25.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(228, 67, 13));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Abonnement termines");

        javax.swing.GroupLayout btn_abonnementterminesLayout = new javax.swing.GroupLayout(btn_abonnementtermines);
        btn_abonnementtermines.setLayout(btn_abonnementterminesLayout);
        btn_abonnementterminesLayout.setHorizontalGroup(
            btn_abonnementterminesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_abonnementterminesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );
        btn_abonnementterminesLayout.setVerticalGroup(
            btn_abonnementterminesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_abonnementterminesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addContainerGap())
        );

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/fast (1).png"))); // NOI18N

        jLabel27.setBackground(new java.awt.Color(228, 67, 13));
        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(228, 67, 13));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel27.setText("° ");
        jLabel27.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel28.setBackground(new java.awt.Color(228, 67, 13));
        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(228, 67, 13));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel28.setText("° ");
        jLabel28.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel30.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel30.setText("Identité");

        jLabel31.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel31.setText("Date debut/fin");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btn_abonnementtermines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(btn_abonnementtermines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel15.setForeground(new java.awt.Color(255, 255, 255));
        jPanel15.setPreferredSize(new java.awt.Dimension(258, 346));

        btn_lesimpayés.setBackground(new java.awt.Color(255, 255, 255));
        btn_lesimpayés.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 67, 13)));
        btn_lesimpayés.setForeground(new java.awt.Color(228, 67, 13));
        btn_lesimpayés.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_lesimpayésMouseClicked(evt);
            }
        });

        jLabel33.setBackground(new java.awt.Color(228, 67, 13));
        jLabel33.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(228, 67, 13));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Les impayés");

        javax.swing.GroupLayout btn_lesimpayésLayout = new javax.swing.GroupLayout(btn_lesimpayés);
        btn_lesimpayés.setLayout(btn_lesimpayésLayout);
        btn_lesimpayésLayout.setHorizontalGroup(
            btn_lesimpayésLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_lesimpayésLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addContainerGap())
        );
        btn_lesimpayésLayout.setVerticalGroup(
            btn_lesimpayésLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_lesimpayésLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addContainerGap())
        );

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/payment-method (1).png"))); // NOI18N

        jLabel35.setBackground(new java.awt.Color(228, 67, 13));
        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(228, 67, 13));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel35.setText("° ");
        jLabel35.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel36.setBackground(new java.awt.Color(228, 67, 13));
        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(228, 67, 13));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel36.setText("° ");
        jLabel36.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel37.setBackground(new java.awt.Color(228, 67, 13));
        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(228, 67, 13));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel37.setText("° ");
        jLabel37.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel38.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel38.setText("Identité");

        jLabel39.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel39.setText("Doit payer");

        jLabel40.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabel40.setText("Date debut/fin");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_lesimpayés, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(btn_lesimpayés, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(311, 311, 311))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.addTab("3", jPanel8);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel16.setText("Statistiques");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(370, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
        );

        table.addTab("4", jPanel9);

        jPanel10.setBackground(new java.awt.Color(242, 237, 237));

        panelrechercher.setBackground(new java.awt.Color(255, 255, 255));
        panelrechercher.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelrechercher.setForeground(new java.awt.Color(255, 51, 0));

        rbtn_nompredat.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_nompredat.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_nompredat.setText("Nom/prenom/Date Naissance");
        rbtn_nompredat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_nompredatActionPerformed(evt);
            }
        });

        rbtn_ID.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_ID.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        rbtn_ID.setText("ID");

        txt_ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IDActionPerformed(evt);
            }
        });

        rbt_sports.setBackground(new java.awt.Color(255, 255, 255));
        rbt_sports.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbt_sports.setText("Sport");

        cb_sport.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 11)); // NOI18N
        cb_sport.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GYM", "KARATE", "PICINE" }));

        rbtn_homme.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_homme.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_homme.setText("Homme");

        rbtn_femme.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_femme.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_femme.setText("Femme");

        jLabel47.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel47.setText("Sexe :");

        btn_recherche.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_recherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/magnifying-glass.png"))); // NOI18N
        btn_recherche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_rechercheMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelrechercherLayout = new javax.swing.GroupLayout(panelrechercher);
        panelrechercher.setLayout(panelrechercherLayout);
        panelrechercherLayout.setHorizontalGroup(
            panelrechercherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelrechercherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtn_nompredat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_nompredat, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_ID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbt_sports)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_sport, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_homme)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_femme, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_recherche)
                .addGap(73, 73, 73))
        );
        panelrechercherLayout.setVerticalGroup(
            panelrechercherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelrechercherLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelrechercherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_recherche)
                    .addGroup(panelrechercherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtn_nompredat)
                        .addComponent(txt_nompredat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_ID)
                        .addComponent(txt_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbt_sports)
                        .addComponent(cb_sport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_homme)
                        .addComponent(rbtn_femme)
                        .addComponent(jLabel47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        req.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        req.setText("requete");

        jTable_abonnementsencours.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JOUR RESTANTS", "PRENOM", "NOM", "DATE DEBUT", "DATE FIN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_abonnementsencours.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(jTable_abonnementsencours);
        jTable_abonnementsencours.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelrechercher, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(252, 252, 252)
                                .addComponent(req, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1031, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(panelrechercher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(req, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        table.addTab("5", jPanel10);

        jPanel12.setBackground(new java.awt.Color(242, 237, 237));

        table_historique.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Prenom", "Nom", "Sport", "Abonnement(mois)", "Date payement", "Montant payé"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_historique.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(table_historique);

        jLabel56.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("HISTORIQUE");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(414, Short.MAX_VALUE)
                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(398, 398, 398))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addContainerGap())
        );

        table.addTab("6", jPanel12);

        jPanel24.setBackground(new java.awt.Color(242, 237, 237));
        jPanel24.setPreferredSize(new java.awt.Dimension(1376, 670));

        jpanel_rechregler.setBackground(new java.awt.Color(255, 255, 255));
        jpanel_rechregler.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpanel_rechregler.setForeground(new java.awt.Color(255, 51, 0));

        rbtn_nompredatregler.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_nompredatregler.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_nompredatregler.setText("Nom/prenom/Date Naissance");
        rbtn_nompredatregler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_nompredatreglerActionPerformed(evt);
            }
        });

        rbtn_IDregler.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_IDregler.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        rbtn_IDregler.setText("ID");

        txt_IDregler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IDreglerActionPerformed(evt);
            }
        });

        rbt_sportsregler.setBackground(new java.awt.Color(255, 255, 255));
        rbt_sportsregler.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbt_sportsregler.setText("Sport");

        cb_spoetregler.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 11)); // NOI18N
        cb_spoetregler.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GYM", "KARATE", "PICINE" }));

        rbtn_hommeregler.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_hommeregler.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_hommeregler.setText("Homme");

        rbtn_femmeregler.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_femmeregler.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_femmeregler.setText("Femme");

        alb_sexeregler.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        alb_sexeregler.setText("Sexe :");

        btn_rechercheregler.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_rechercheregler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/magnifying-glass.png"))); // NOI18N
        btn_rechercheregler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_recherchereglerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpanel_rechreglerLayout = new javax.swing.GroupLayout(jpanel_rechregler);
        jpanel_rechregler.setLayout(jpanel_rechreglerLayout);
        jpanel_rechreglerLayout.setHorizontalGroup(
            jpanel_rechreglerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_rechreglerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtn_nompredatregler)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_nompredatregler, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_IDregler)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_IDregler, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbt_sportsregler)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_spoetregler, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(alb_sexeregler, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_hommeregler)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_femmeregler, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_rechercheregler)
                .addGap(73, 73, 73))
        );
        jpanel_rechreglerLayout.setVerticalGroup(
            jpanel_rechreglerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_rechreglerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpanel_rechreglerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_rechercheregler)
                    .addGroup(jpanel_rechreglerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtn_nompredatregler)
                        .addComponent(txt_nompredatregler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_IDregler)
                        .addComponent(txt_IDregler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbt_sportsregler)
                        .addComponent(cb_spoetregler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_hommeregler)
                        .addComponent(rbtn_femmeregler)
                        .addComponent(alb_sexeregler)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_abonnementtermine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PRENOM", "NOM", "DATE DEBUT  ", "DATE FIN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable_abonnementtermine);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setForeground(new java.awt.Color(255, 153, 153));

        jLabel51.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel51.setText("Abonnement terminés");

        nb_abonnemetTermine.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 48)); // NOI18N
        nb_abonnemetTermine.setText("124");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nb_abonnemetTermine, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(nb_abonnemetTermine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel22.setBackground(new java.awt.Color(228, 67, 13));
        jPanel22.setPreferredSize(new java.awt.Dimension(5, 0));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        msg_renouvler1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel52.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 14)); // NOI18N
        jLabel52.setText("Cliquer deux foix sur la ligne du member pour renouvler l'abonement");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(204, 204, 0));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("!");

        javax.swing.GroupLayout msg_renouvler1Layout = new javax.swing.GroupLayout(msg_renouvler1);
        msg_renouvler1.setLayout(msg_renouvler1Layout);
        msg_renouvler1Layout.setHorizontalGroup(
            msg_renouvler1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(msg_renouvler1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        msg_renouvler1Layout.setVerticalGroup(
            msg_renouvler1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, msg_renouvler1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(msg_renouvler1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(msg_renouvler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jpanel_rechregler, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpanel_rechregler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                            .addComponent(msg_renouvler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27))
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );

        table.addTab("7", jPanel14);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setPreferredSize(new java.awt.Dimension(1371, 642));

        jpanel_rech.setBackground(new java.awt.Color(255, 255, 255));
        jpanel_rech.setForeground(new java.awt.Color(255, 51, 0));

        rbtn_nompredatAT.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_nompredatAT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_nompredatAT.setText("Nom/prenom/Date Naissance");
        rbtn_nompredatAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_nompredatATActionPerformed(evt);
            }
        });

        rbtn_IDAT.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_IDAT.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        rbtn_IDAT.setText("ID");

        txt_IDAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IDATActionPerformed(evt);
            }
        });

        rbt_sportsAT.setBackground(new java.awt.Color(255, 255, 255));
        rbt_sportsAT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbt_sportsAT.setText("Sport");

        cb_spoetAT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 11)); // NOI18N
        cb_spoetAT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GYM", "KARATE", "PICINE" }));

        rbtn_hommeAT.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_hommeAT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_hommeAT.setText("Homme");

        rbtn_femmeAT.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_femmeAT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_femmeAT.setText("Femme");

        alb_sexeAT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        alb_sexeAT.setText("Sexe :");

        btn_rechercheAT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_rechercheAT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/magnifying-glass.png"))); // NOI18N
        btn_rechercheAT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_rechercheATMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpanel_rechLayout = new javax.swing.GroupLayout(jpanel_rech);
        jpanel_rech.setLayout(jpanel_rechLayout);
        jpanel_rechLayout.setHorizontalGroup(
            jpanel_rechLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_rechLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtn_nompredatAT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_nompredatAT, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_IDAT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_IDAT, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbt_sportsAT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_spoetAT, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(alb_sexeAT, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_hommeAT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_femmeAT, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_rechercheAT)
                .addGap(73, 73, 73))
        );
        jpanel_rechLayout.setVerticalGroup(
            jpanel_rechLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_rechLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpanel_rechLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_rechercheAT)
                    .addGroup(jpanel_rechLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtn_nompredatAT)
                        .addComponent(txt_nompredatAT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_IDAT)
                        .addComponent(txt_IDAT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbt_sportsAT)
                        .addComponent(cb_spoetAT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_hommeAT)
                        .addComponent(rbtn_femmeAT)
                        .addComponent(alb_sexeAT)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_impayé.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DOIt PAYER", " TOTAL PAYE ", " PRENOM", "NOM", "SPORT", "ID", "PRIX", "DATE DEBUT", "DATE FIN ", "recus"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_impayé.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_impayéMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_impayé);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setForeground(new java.awt.Color(255, 153, 153));

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel2.setText("Les impayés");

        nb_abonnementTR.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 48)); // NOI18N
        nb_abonnementTR.setText("9");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nb_abonnementTR, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(nb_abonnementTR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel18.setBackground(new java.awt.Color(228, 67, 13));
        jPanel18.setPreferredSize(new java.awt.Dimension(5, 0));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        msg_renouvler.setBackground(new java.awt.Color(255, 255, 204));

        jLabel48.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 14)); // NOI18N
        jLabel48.setText("Cliquer deux foix sur la ligne du member pour régler les paiements");

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(204, 204, 0));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("!");

        javax.swing.GroupLayout msg_renouvlerLayout = new javax.swing.GroupLayout(msg_renouvler);
        msg_renouvler.setLayout(msg_renouvlerLayout);
        msg_renouvlerLayout.setHorizontalGroup(
            msg_renouvlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(msg_renouvlerLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        msg_renouvlerLayout.setVerticalGroup(
            msg_renouvlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, msg_renouvlerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(msg_renouvlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(msg_renouvler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jpanel_rech, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpanel_rech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                            .addComponent(msg_renouvler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27))
                        .addGroup(jPanel23Layout.createSequentialGroup()
                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))))
        );

        table.addTab("8", jPanel23);

        jPanel3.setBackground(new java.awt.Color(242, 237, 237));

        tbl_memberReg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID members", "Prenom", "Nom "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_memberReg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_memberRegMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tbl_memberReg);

        tbl_regelementP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID members", "Date paiment ", "Montant payé"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tbl_regelementP);

        jpanel_rech1.setBackground(new java.awt.Color(255, 255, 255));
        jpanel_rech1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpanel_rech1.setForeground(new java.awt.Color(255, 51, 0));

        rbtn_nompredatREG.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_nompredatREG.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_nompredatREG.setText("Nom/prenom/Date Naissance");
        rbtn_nompredatREG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_nompredatREGActionPerformed(evt);
            }
        });

        rbtn_IDREG.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_IDREG.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        rbtn_IDREG.setText("ID");

        txt_IDREG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IDREGActionPerformed(evt);
            }
        });

        rbt_sportsREG.setBackground(new java.awt.Color(255, 255, 255));
        rbt_sportsREG.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbt_sportsREG.setText("Sport");

        cb_spoetREG.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 11)); // NOI18N
        cb_spoetREG.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GYM", "KARATE", "PICINE" }));

        rbtn_hommeREG.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_hommeREG.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_hommeREG.setText("Homme");

        rbtn_femmeREG.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_femmeREG.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        rbtn_femmeREG.setText("Femme");

        alb_sexeREG.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        alb_sexeREG.setText("Sexe :");

        btn_rechercheREG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_rechercheREG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/magnifying-glass.png"))); // NOI18N
        btn_rechercheREG.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_rechercheREGMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpanel_rech1Layout = new javax.swing.GroupLayout(jpanel_rech1);
        jpanel_rech1.setLayout(jpanel_rech1Layout);
        jpanel_rech1Layout.setHorizontalGroup(
            jpanel_rech1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_rech1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtn_nompredatREG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_nompredatREG, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_IDREG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_IDREG, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbt_sportsREG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_spoetREG, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(alb_sexeREG, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_hommeREG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtn_femmeREG, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_rechercheREG)
                .addGap(73, 73, 73))
        );
        jpanel_rech1Layout.setVerticalGroup(
            jpanel_rech1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_rech1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpanel_rech1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_rechercheREG)
                    .addGroup(jpanel_rech1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtn_nompredatREG)
                        .addComponent(txt_nompredatREG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_IDREG)
                        .addComponent(txt_IDREG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbt_sportsREG)
                        .addComponent(cb_spoetREG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_hommeREG)
                        .addComponent(rbtn_femmeREG)
                        .addComponent(alb_sexeREG)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.setBackground(new java.awt.Color(102, 102, 102));
        jPanel25.setPreferredSize(new java.awt.Dimension(2, 460));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpanel_rech1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpanel_rech1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        table.addTab("9", jPanel3);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        table_admin.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 11)); // NOI18N
        table_admin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "USER", "PASSWORD"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_adminMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(table_admin);

        jLabel57.setBackground(new java.awt.Color(255, 255, 255));
        jLabel57.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 48)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(228, 67, 13));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("SIGN UP");

        txt_user.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_user.setActionCommand("<Not Set>");
        txt_user.setBorder(null);

        txt_pass.setBorder(null);
        txt_pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passActionPerformed(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        jLabel58.setText("USER :");

        jLabel59.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jLabel59.setText("PASSWORD  :");

        jSeparator2.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator2.setForeground(new java.awt.Color(228, 67, 13));

        jSeparator1.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator1.setForeground(new java.awt.Color(228, 67, 13));

        btn_modifier.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        btn_modifier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_modifier.setText("Modifier");
        btn_modifier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_modifierMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_modifier, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_modifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btn_ajouter.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 12)); // NOI18N
        btn_ajouter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_ajouter.setText("Ajouter");
        btn_ajouter.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btn_ajouterAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btn_ajouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajouterMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_ajouter, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_ajouter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
        );

        btn_supprimer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_supprimer.setText("      supprimer admin !!");
        btn_supprimer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_supprimerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(0, 17, Short.MAX_VALUE)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_user)
                                    .addComponent(txt_pass, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addComponent(btn_supprimer)
                .addContainerGap(728, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(126, 126, 126))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(btn_supprimer, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        table.addTab("10", jPanel27);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setForeground(new java.awt.Color(228, 67, 13));

        table_renouvlerabonnement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Memeber", "Prenom", "NOM "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_renouvlerabonnement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_renouvlerabonnementMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(table_renouvlerabonnement);

        jLabel60.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Information sur l'abonnement");

        txt_abonementRA.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        txt_abonementRA.setBorder(null);

        txt_MontantPayéRA.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        txt_MontantPayéRA.setBorder(null);
        txt_MontantPayéRA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MontantPayéRAActionPerformed(evt);
            }
        });

        txt_dateDebutRA.setBorder(null);
        txt_dateDebutRA.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd-MM-y"))));

        jLabel61.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel61.setText("Abonnement(mois) :");

        jLabel62.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel62.setText("Montant payé :");

        jLabel63.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel63.setText("Date de Debut :");

        jLabel64.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel64.setText("Rest a payé(DH) :");

        jLabel65.setBackground(new java.awt.Color(228, 67, 13));
        jLabel65.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 36)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(228, 67, 13));
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("100 ");

        btn_confirmerRenouvlerAb.setBackground(new java.awt.Color(228, 67, 13));
        btn_confirmerRenouvlerAb.setForeground(new java.awt.Color(228, 67, 13));

        jLabel66.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 18)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("CONFIRMER");

        javax.swing.GroupLayout btn_confirmerRenouvlerAbLayout = new javax.swing.GroupLayout(btn_confirmerRenouvlerAb);
        btn_confirmerRenouvlerAb.setLayout(btn_confirmerRenouvlerAbLayout);
        btn_confirmerRenouvlerAbLayout.setHorizontalGroup(
            btn_confirmerRenouvlerAbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        );
        btn_confirmerRenouvlerAbLayout.setVerticalGroup(
            btn_confirmerRenouvlerAbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jLabel67.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jLabel67.setText("< Annuler");

        jSeparator3.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator3.setForeground(new java.awt.Color(228, 67, 13));

        jSeparator4.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator4.setForeground(new java.awt.Color(228, 67, 13));

        jSeparator5.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator5.setForeground(new java.awt.Color(228, 67, 13));

        txt_sportRA.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        txt_sportRA.setBorder(null);

        jSeparator6.setBackground(new java.awt.Color(228, 67, 13));
        jSeparator6.setForeground(new java.awt.Color(228, 67, 13));

        jLabel68.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel68.setText("Sport :");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel30Layout.createSequentialGroup()
                            .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_confirmerRenouvlerAb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txt_abonementRA, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_MontantPayéRA, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel60, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_sportRA, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel63, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_dateDebutRA, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_sportRA, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txt_abonementRA, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel62)
                        .addGap(0, 0, 0)
                        .addComponent(txt_MontantPayéRA, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel63)
                        .addGap(0, 0, 0)
                        .addComponent(txt_dateDebutRA, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel65)
                                    .addComponent(jLabel64))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_confirmerRenouvlerAb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.addTab("11", jPanel30);

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel69.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel69.setText("Homme :");

        txt_pHomme.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 48)); // NOI18N
        txt_pHomme.setText("100%");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(txt_pHomme, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 43, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_pHomme, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel71.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel71.setText("Femme :");

        txt_pFemme.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 48)); // NOI18N
        txt_pFemme.setText("100%");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_pFemme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_pFemme, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel73.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jLabel73.setText("Total:");

        txt_Totale.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 48)); // NOI18N
        txt_Totale.setText("100");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_Totale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Totale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jPanel37.setBackground(new java.awt.Color(204, 204, 204));
        jPanel37.setPreferredSize(new java.awt.Dimension(3, 0));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel38.setBackground(new java.awt.Color(204, 204, 204));
        jPanel38.setPreferredSize(new java.awt.Dimension(3, 0));

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel75.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel75.setText("Admin :");

        jLabel76.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel76.setText("Coach :");

        jLabel77.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 12)); // NOI18N
        jLabel77.setText("Sport :");

        jLabel78.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 48)); // NOI18N
        jLabel78.setText("18");

        jLabel79.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 48)); // NOI18N
        jLabel79.setText("10");

        jLabel80.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 48)); // NOI18N
        jLabel80.setText("10");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/stati.png"))); // NOI18N

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel70, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 325, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(31, 31, 31))
        );

        table.addTab("12", jPanel31);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(228, 67, 13));

        btn_members.setBackground(new java.awt.Color(228, 67, 13));
        btn_members.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_membersMouseClicked(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/community.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Members");

        javax.swing.GroupLayout btn_membersLayout = new javax.swing.GroupLayout(btn_members);
        btn_members.setLayout(btn_membersLayout);
        btn_membersLayout.setHorizontalGroup(
            btn_membersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_membersLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btn_membersLayout.setVerticalGroup(
            btn_membersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_membersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btn_membersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        btn_abonnement.setBackground(new java.awt.Color(228, 67, 13));
        btn_abonnement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_abonnementMouseClicked(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/date (1).png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Abonnement");

        javax.swing.GroupLayout btn_abonnementLayout = new javax.swing.GroupLayout(btn_abonnement);
        btn_abonnement.setLayout(btn_abonnementLayout);
        btn_abonnementLayout.setHorizontalGroup(
            btn_abonnementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_abonnementLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btn_abonnementLayout.setVerticalGroup(
            btn_abonnementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_abonnementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btn_abonnementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btn_paiments.setBackground(new java.awt.Color(228, 67, 13));
        btn_paiments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_paimentsMouseClicked(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/hand (1).png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Paiements");

        javax.swing.GroupLayout btn_paimentsLayout = new javax.swing.GroupLayout(btn_paiments);
        btn_paiments.setLayout(btn_paimentsLayout);
        btn_paimentsLayout.setHorizontalGroup(
            btn_paimentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_paimentsLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btn_paimentsLayout.setVerticalGroup(
            btn_paimentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_paimentsLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(btn_paimentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btn_consultation.setBackground(new java.awt.Color(228, 67, 13));
        btn_consultation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_consultationMouseClicked(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/chat (1).png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Consultation");

        btn_statistiques.setBackground(new java.awt.Color(228, 67, 13));
        btn_statistiques.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_statistiquesMouseClicked(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/bar-chart.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Statistiques");

        btn_statistiques1.setBackground(new java.awt.Color(228, 67, 13));
        btn_statistiques1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_statistiques1MouseClicked(evt);
            }
        });

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/bar-chart.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("Coach");

        javax.swing.GroupLayout btn_statistiques1Layout = new javax.swing.GroupLayout(btn_statistiques1);
        btn_statistiques1.setLayout(btn_statistiques1Layout);
        btn_statistiques1Layout.setHorizontalGroup(
            btn_statistiques1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_statistiques1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btn_statistiques1Layout.setVerticalGroup(
            btn_statistiques1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_statistiques1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btn_statistiques1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout btn_statistiquesLayout = new javax.swing.GroupLayout(btn_statistiques);
        btn_statistiques.setLayout(btn_statistiquesLayout);
        btn_statistiquesLayout.setHorizontalGroup(
            btn_statistiquesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_statistiquesLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btn_statistiques1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        btn_statistiquesLayout.setVerticalGroup(
            btn_statistiquesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_statistiquesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btn_statistiquesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(btn_statistiques1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout btn_consultationLayout = new javax.swing.GroupLayout(btn_consultation);
        btn_consultation.setLayout(btn_consultationLayout);
        btn_consultationLayout.setHorizontalGroup(
            btn_consultationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_consultationLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(btn_statistiques, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        btn_consultationLayout.setVerticalGroup(
            btn_consultationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_consultationLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(btn_consultationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(btn_statistiques, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/exercise.png"))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 10)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/logout.png"))); // NOI18N
        jLabel32.setText("Logout");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_members, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_abonnement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_paiments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_consultation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btn_members, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_abonnement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_paiments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_consultation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addContainerGap())
        );

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1058, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 1251, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(home, 515, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_membersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_membersMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(0);
    }//GEN-LAST:event_btn_membersMouseClicked

    private void btn_abonnementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_abonnementMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(1);
    }//GEN-LAST:event_btn_abonnementMouseClicked

    private void btn_paimentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paimentsMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(2);
    }//GEN-LAST:event_btn_paimentsMouseClicked

    private void btn_consultationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_consultationMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(3);
    }//GEN-LAST:event_btn_consultationMouseClicked

    private void btn_statistiquesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_statistiquesMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(12);
    }//GEN-LAST:event_btn_statistiquesMouseClicked

    private void btn_rechercheMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rechercheMouseClicked
        // TODO add your handling code here:
        if (rbtn_nompredat.isSelected()) {
            rq_nom = txt_nompredat.getText();
        }
        if (rbtn_nompredat.isSelected()) {
            rq_prenom = txt_nompredat.getText();
        }
        if (rbtn_nompredat.isSelected()) {
            rq_dateNaissance = txt_nompredat.getText();
        }
        if (rbtn_ID.isSelected()) {
            rq_ID = Integer.parseInt(txt_ID.getText());
        }
        if (rbtn_homme.isSelected()) {
            rq_sexe = "homme";
            rbtn_femme.setSelected(false);
        }
        if (rbtn_femme.isSelected()) {
            rq_sexe = "femme";
            rbtn_homme.setSelected(false);
        }
        req.setText("requte fiha :" + rq_nom + rq_prenom + rq_ID + rq_dateNaissance + rq_sexe + "BRAVO");

    }//GEN-LAST:event_btn_rechercheMouseClicked

    private void txt_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IDActionPerformed

    private void rbtn_nompredatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_nompredatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtn_nompredatActionPerformed

    private void btn_abonnemetencoursKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_abonnemetencoursKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_btn_abonnemetencoursKeyPressed

    private void btn_abonnemetencoursMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_abonnemetencoursMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(5);
    }//GEN-LAST:event_btn_abonnemetencoursMouseClicked

    private void btn_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ExitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btn_ExitMouseClicked

    private void rbtn_nompredatATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_nompredatATActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtn_nompredatATActionPerformed

    private void txt_IDATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IDATActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IDATActionPerformed

    private void btn_rechercheATMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rechercheATMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_rechercheATMouseClicked
    private int xMouse, yMouse;
    private void homeMousePressed(java.awt.event.MouseEvent evt) {                                         private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMousePressed
           xMouse=evt.getX();
           yMouse=evt.getY();
    }//GEN-LAST:event_homeMousePressed

    private void homeMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseDragged
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        setLocation(x - xMouse, y - yMouse);

    }//GEN-LAST:event_homeMouseDragged

    private void btn_abonnementterminesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_abonnementterminesMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(7);

    }//GEN-LAST:event_btn_abonnementterminesMouseClicked

    private void btn_lesimpayésMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_lesimpayésMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(8);

    }//GEN-LAST:event_btn_lesimpayésMouseClicked

    private void rbtn_nompredatreglerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_nompredatreglerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtn_nompredatreglerActionPerformed

    private void txt_IDreglerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IDreglerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IDreglerActionPerformed

    private void btn_recherchereglerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_recherchereglerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_recherchereglerMouseClicked

    private void btn_statistiques1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_statistiques1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_statistiques1MouseClicked

    private void btn_reglerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reglerMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(8);

    }//GEN-LAST:event_btn_reglerMouseClicked

    private void btn_reglementsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reglementsMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(9);
    }//GEN-LAST:event_btn_reglementsMouseClicked

    private void btn_historiqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_historiqueMouseClicked
        // TODO add your handling code here:
        table.setSelectedIndex(6);
    }//GEN-LAST:event_btn_historiqueMouseClicked

    private void rbtn_nompredatREGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_nompredatREGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtn_nompredatREGActionPerformed

    private void txt_IDREGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IDREGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IDREGActionPerformed

    private void btn_rechercheREGMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rechercheREGMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_rechercheREGMouseClicked

    private void jTable_impayéMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_impayéMouseClicked
        int i = jTable_impayé.getSelectedRow();
        TableModel model = jTable_impayé.getModel();
        String nom = (String) model.getValueAt(i, 3);
        String prenom = (String) model.getValueAt(i, 2);
        int recus = (int) model.getValueAt(i, 9);
        double montantApaye = (double) model.getValueAt(i, 0);
        System.out.println("naaadi");
        int id =(int) model.getValueAt(i, 5);
        new reglement(nom, prenom, recus, montantApaye,id).setVisible(true);
            
    }//GEN-LAST:event_jTable_impayéMouseClicked

    private void tbl_memberRegMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_memberRegMouseClicked
        int i = tbl_memberReg.getSelectedRow();
        TableModel model = tbl_memberReg.getModel();
        int id = (int) model.getValueAt(i, 0);
        
        reglementP(id);
    }//GEN-LAST:event_tbl_memberRegMouseClicked

    private void txt_passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passActionPerformed

    private void table_adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_adminMouseClicked
        int i = table_admin.getSelectedRow();
        TableModel model = table_admin.getModel();
        txt_user.setText(model.getValueAt(i, 0).toString());
        txt_pass.setText(model.getValueAt(i, 1).toString());

    }//GEN-LAST:event_table_adminMouseClicked

    private void btn_ajouterAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btn_ajouterAncestorAdded

    }//GEN-LAST:event_btn_ajouterAncestorAdded

    private void btn_ajouterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajouterMouseClicked
        try {
            String query = "INSERT INTO `admin`(`user`, `password`) VALUES ('" + txt_user.getText() + "','" + txt_pass.getText() + "')";
            executeSQLQuery(query, "Ajouter admin avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_ajouterMouseClicked

    private void btn_modifierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_modifierMouseClicked
        String query = "UPDATE `admin` SET `password`='" + txt_pass.getText() + "' WHERE `user`='" + txt_user.getText() + "'";
        try {
            executeSQLQuery(query, "Modifier admin avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_modifierMouseClicked

    private void btn_supprimerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_supprimerMouseClicked
        String query = "DELETE FROM `admin` WHERE user='" + txt_user.getText() + "'";
        try {
            executeSQLQuery(query, "Supprimer admin avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_supprimerMouseClicked

    private void txt_MontantPayéRAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MontantPayéRAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MontantPayéRAActionPerformed

    private void table_renouvlerabonnementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_renouvlerabonnementMouseClicked
        int i = table_renouvlerabonnement.getSelectedRow();
        TableModel model = table_renouvlerabonnement.getModel();
        int id_ra = (int) model.getValueAt(i, 0);
        Connection connection = getConnection();
        String query = "SELECT `typeAbon`, `Sport` FROM `abonnement` WHERE DateFin < DATE( NOW() ) and ID_MA =" + id_ra;
        Statement st = null;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                txt_sportRA.setText(rs.getString("Sport"));
                txt_abonementRA.setText(String.valueOf(rs.getInt("typeAbon")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_table_renouvlerabonnementMouseClicked

    private void btn_adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_adminMouseClicked
        table.setSelectedIndex(10);
    }//GEN-LAST:event_btn_adminMouseClicked

    private void txt_prenommActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_prenommActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_prenommActionPerformed

    private void btn_modifiermMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_modifiermMouseClicked

        String query = "UPDATE `member` SET `nomM`='" + txt_nomm.getText() + "',`prenomM`='" + txt_prenomm.getText() + "',`datenaissanceM`='" + txt_datenaissancem.getText() + "',`sexeM`='" + txt_sexem.getText() + "',`emailM`='" + txt_emailm.getText() + "',`muméroM`='" + txt_numm.getText() + "',`sportM`='" + txt_sportm.getText() + "',`datedebutM`='" + txt_datedebutm.getText() + "',`typeabonnementM`='" + txt_typeabonnementm.getText() + "' WHERE `idm`='" + txt_idm.getText() + "'";
        try {
            executeSQLQuery(query, "Membre modifié avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_modifiermMouseClicked

    private void btn_ajoutermAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btn_ajoutermAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ajoutermAncestorAdded

    private void btn_ajoutermMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajoutermMouseClicked
        
        try {
            String query = "INSERT INTO `member`(`nomM`, `prenomM`,`datenaissanceM`,`sexeM`,`emailM`,`muméroM`,`sportM`,`datedebutM`,`typeabonnementM`) VALUES ('" + txt_nomm.getText() + "','" + txt_prenomm.getText() + "','" + txt_datenaissancem.getText() + "','" + txt_sexem.getText() + "','" + txt_emailm.getText() + "','" + txt_numm.getText() + "','" + txt_sportm.getText() + "','" + txt_datedebutm.getText() + "','" + txt_typeabonnementm.getText() + "')";
            executeSQLQuery(query, "Membre ajouté avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_ajoutermMouseClicked

    private void btn_supprimermMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_supprimermMouseClicked

         String query = "DELETE FROM `member` WHERE idm='" + txt_idm.getText() + "'";
        try {
            executeSQLQuery(query, "Membre supprimé avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_supprimermMouseClicked

    private void txt_datenaissancemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_datenaissancemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_datenaissancemActionPerformed

    private void txt_nummActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nummActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nummActionPerformed

    private void txt_sportmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sportmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_sportmActionPerformed

    private void jPanel40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel40MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel40MouseClicked

    private void table_coachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_coachMouseClicked
    
        int i = table_coach.getSelectedRow();
        TableModel model = table_coach.getModel();
        txt_idc.setText((int)model.getValueAt(i, 0));
        txt_nomc.setText(model.getValueAt(i, 1).toString());
        txt_prenomc.setText(model.getValueAt(i, 2).toString());
        txt_datenaissancec.setText(model.getValueAt(i, 3).toString());
        txt_sexec.setText(model.getValueAt(i, 4).toString());
        txt_emailc.setText(model.getValueAt(i, 5).toString());
        txt_numc.setText(model.getValueAt(i, 6).toString());
        txt_sportc.setText(model.getValueAt(i, 7).toString());
        
        
    }//GEN-LAST:event_table_coachMouseClicked

    private void txt_prenomcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_prenomcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_prenomcActionPerformed

    private void btn_modifiercMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_modifiercMouseClicked
    
        String query = "UPDATE `coach` SET `nomC`='" + txt_nomc.getText() + "',`prenomC`='" + txt_prenomc.getText() + "',`datenaissanceC`='" + txt_datenaissancec.getText() + "',`sexeC`='" + txt_sexec.getText() + "',`emailC`='" + txt_emailc.getText() + "',`muméroC`='" + txt_numc.getText() + "',`sportC`='" + txt_sportc.getText() + "' WHERE `idc`='" + txt_idc.getText() + "'";
        try {
            executeSQLQuery(query, "Coach modifié avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_modifiercMouseClicked

    private void jPanel43MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel43MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel43MouseClicked

    private void btn_ajoutercAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btn_ajoutercAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ajoutercAncestorAdded

    private void btn_ajoutercMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajoutercMouseClicked
    
        try {
            String query = "INSERT INTO `coach`(`nomC`, `prenomC`,`datenaissanceC`,`sexeC`,`emailC`,`muméroC`,`sportC`) VALUES ('" + txt_nomc.getText() + "','" + txt_prenomc.getText() + "','" + txt_datenaissancec.getText() + "','" + txt_sexec.getText() + "','" + txt_emailc.getText() + "','" + txt_numc.getText() + "','" + txt_sportc.getText() + "')";
            executeSQLQuery(query, "Coach ajouté avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_ajoutercMouseClicked

    private void btn_supprimercMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_supprimercMouseClicked

         String query = "DELETE FROM `coach` WHERE idc='" + txt_idc.getText() + "'";
        try {
            executeSQLQuery(query, "Coach supprimé avec succes");
            refrech_table();
        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btn_supprimercMouseClicked

    private void txt_datenaissancecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_datenaissancecActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_datenaissancecActionPerformed

    private void txt_numérocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_numérocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_numérocActionPerformed

    private void txt_sportcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sportcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_sportcActionPerformed

    private void table_membersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_membersMouseClicked

        int i = table_members.getSelectedRow();
        TableModel model = table_members.getModel();
        txt_idm.setText((int)model.getValueAt(i, 0));
        txt_nomm.setText(model.getValueAt(i, 1).toString());
        txt_prenomm.setText(model.getValueAt(i, 2).toString());
        txt_datenaissancem.setText(model.getValueAt(i, 3).toString());
        txt_sexem.setText(model.getValueAt(i, 4).toString());
        txt_emailm.setText(model.getValueAt(i, 5).toString());
        txt_numm.setText(model.getValueAt(i, 6).toString());
        txt_sportm.setText(model.getValueAt(i, 7).toString());
        txt_datedebutm.setText(model.getValueAt(i, 8).toString());
        txt_typeabonnementm.setText((int)model.getValueAt(i, 9));
        
    }//GEN-LAST:event_table_membersMouseClicked
    private String rq_nom;
    private String rq_prenom;
    private String rq_dateNaissance;
    private String rq_sexe;
    private String rq_sport;
    private int rq_ID;

    ;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new home().setVisible(true);
            } catch (ParseException ex) {
                Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alb_sexeAT;
    private javax.swing.JLabel alb_sexeREG;
    private javax.swing.JLabel alb_sexeregler;
    private javax.swing.JLabel btn_Exit;
    private javax.swing.JPanel btn_abonnement;
    private javax.swing.JPanel btn_abonnementtermines;
    private javax.swing.JPanel btn_abonnemetencours;
    private javax.swing.JLabel btn_admin;
    private javax.swing.JLabel btn_ajouter;
    private javax.swing.JLabel btn_ajouterc;
    private javax.swing.JLabel btn_ajouterm;
    private javax.swing.JPanel btn_confirmerRenouvlerAb;
    private javax.swing.JPanel btn_consultation;
    private javax.swing.JPanel btn_historique;
    private javax.swing.JPanel btn_lesimpayés;
    private javax.swing.JPanel btn_members;
    private javax.swing.JLabel btn_modifier;
    private javax.swing.JLabel btn_modifierc;
    private javax.swing.JLabel btn_modifierm;
    private javax.swing.JPanel btn_paiments;
    private javax.swing.JLabel btn_recherche;
    private javax.swing.JLabel btn_rechercheAT;
    private javax.swing.JLabel btn_rechercheREG;
    private javax.swing.JLabel btn_rechercheregler;
    private javax.swing.JPanel btn_reglements;
    private javax.swing.JPanel btn_regler;
    private javax.swing.JPanel btn_statistiques;
    private javax.swing.JPanel btn_statistiques1;
    private javax.swing.JLabel btn_supprimer;
    private javax.swing.JLabel btn_supprimerc;
    private javax.swing.JLabel btn_supprimerm;
    private javax.swing.JComboBox<String> cb_spoetAT;
    private javax.swing.JComboBox<String> cb_spoetREG;
    private javax.swing.JComboBox<String> cb_spoetregler;
    private javax.swing.JComboBox<String> cb_sport;
    private javax.swing.JPanel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable_abonnementsencours;
    private javax.swing.JTable jTable_abonnementtermine;
    private javax.swing.JTable jTable_impayé;
    private javax.swing.JPanel jpanel_rech;
    private javax.swing.JPanel jpanel_rech1;
    private javax.swing.JPanel jpanel_rechregler;
    private javax.swing.JPanel msg_renouvler;
    private javax.swing.JPanel msg_renouvler1;
    private javax.swing.JLabel nb_abonnementTR;
    private javax.swing.JLabel nb_abonnemetTermine;
    private javax.swing.JPanel panelrechercher;
    private javax.swing.JRadioButton rbt_sports;
    private javax.swing.JRadioButton rbt_sportsAT;
    private javax.swing.JRadioButton rbt_sportsREG;
    private javax.swing.JRadioButton rbt_sportsregler;
    private javax.swing.JRadioButton rbtn_ID;
    private javax.swing.JRadioButton rbtn_IDAT;
    private javax.swing.JRadioButton rbtn_IDREG;
    private javax.swing.JRadioButton rbtn_IDregler;
    private javax.swing.JRadioButton rbtn_femme;
    private javax.swing.JRadioButton rbtn_femmeAT;
    private javax.swing.JRadioButton rbtn_femmeREG;
    private javax.swing.JRadioButton rbtn_femmeregler;
    private javax.swing.JRadioButton rbtn_homme;
    private javax.swing.JRadioButton rbtn_hommeAT;
    private javax.swing.JRadioButton rbtn_hommeREG;
    private javax.swing.JRadioButton rbtn_hommeregler;
    private javax.swing.JRadioButton rbtn_nompredat;
    private javax.swing.JRadioButton rbtn_nompredatAT;
    private javax.swing.JRadioButton rbtn_nompredatREG;
    private javax.swing.JRadioButton rbtn_nompredatregler;
    private javax.swing.JLabel req;
    private javax.swing.JTabbedPane table;
    private javax.swing.JTable table_admin;
    private javax.swing.JTable table_coach;
    private javax.swing.JTable table_historique;
    private javax.swing.JTable table_members;
    private javax.swing.JTable table_renouvlerabonnement;
    private javax.swing.JTable tbl_memberReg;
    private javax.swing.JTable tbl_regelementP;
    private javax.swing.JTextField txt_ID;
    private javax.swing.JTextField txt_IDAT;
    private javax.swing.JTextField txt_IDREG;
    private javax.swing.JTextField txt_IDregler;
    private javax.swing.JTextField txt_MontantPayéRA;
    private javax.swing.JLabel txt_Totale;
    private javax.swing.JTextField txt_abonementRA;
    private javax.swing.JFormattedTextField txt_dateDebutRA;
    private javax.swing.JTextField txt_datedebutm;
    private javax.swing.JTextField txt_datenaissancec;
    private javax.swing.JTextField txt_datenaissancem;
    private javax.swing.JTextField txt_emailc;
    private javax.swing.JTextField txt_emailm;
    private javax.swing.JTextField txt_idc;
    private javax.swing.JTextField txt_idm;
    private javax.swing.JTextField txt_nomc;
    private javax.swing.JTextField txt_nomm;
    private javax.swing.JTextField txt_nompredat;
    private javax.swing.JTextField txt_nompredatAT;
    private javax.swing.JTextField txt_nompredatREG;
    private javax.swing.JTextField txt_nompredatregler;
    private javax.swing.JTextField txt_numm;
    private javax.swing.JTextField txt_numéroc;
    private javax.swing.JLabel txt_pFemme;
    private javax.swing.JLabel txt_pHomme;
    private javax.swing.JTextField txt_pass;
    private javax.swing.JTextField txt_prenomc;
    private javax.swing.JTextField txt_prenomm;
    private javax.swing.JTextField txt_sexec;
    private javax.swing.JTextField txt_sexem;
    private javax.swing.JTextField txt_sportRA;
    private javax.swing.JTextField txt_sportc;
    private javax.swing.JTextField txt_sportm;
    private javax.swing.JTextField txt_typeabonnementm;
    private javax.swing.JTextField txt_user;
    // End of variables declaration//GEN-END:variables

    void setVisible() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private double doitPaye(double mant, int type) {
        switch (type) {
            case 1:
                return 100 - mant;
            case 6:
                return 600 - mant;
            case 3:
                return 300 - mant;
            case 12:
                return 1200 - mant;
        }
        return 0;

    }

    private int prixAbonnement(int typeAbonnement) {
        switch (typeAbonnement) {

            case 1:
                return 100;
            case 6:
                return 600;
            case 3:
                return 300;
            case 12:
                return 1200;

        }
        return 0;

    }
}
