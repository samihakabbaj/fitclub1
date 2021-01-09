
package cityclub;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ahlame SEHLAOUI
 */
public class GYM_login extends javax.swing.JFrame {

    /**
     * Creates new form pharmacy_login
     */
    public GYM_login() {
        initComponents();
        this.setLocationRelativeTo(this);
        this.setOpacity(0.9f);
    }
    public Connection getConnection(){
        Connection con ;
        try{
            con= (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3324/gym", "root", "");
            return con ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // partie admin
    public ArrayList<admin> getadminList()
    {
        ArrayList<admin> adminList = new ArrayList<admin>() ;
        Connection connection = getConnection();
        String query ="SELECT * FROM `admin` WHERE 1";
        Statement st=null ;
        ResultSet rs ;
        admin ad;
        try {
            st = connection.createStatement();
            rs =st.executeQuery(query);
            while (rs.next()){
                    ad = new admin(rs.getString("user"),rs.getString("password")); 
                    adminList.add(ad);
                
            }
            
       }catch (Exception e){
            e.printStackTrace();
       }
       return adminList;
        
    }



    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        logo_pan = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        user_txt = new javax.swing.JTextField();
        password_txt = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        login_btn = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Cancel_btn = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        forget_btn = new javax.swing.JLabel();
        txt_userORpass = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setOpacity(getOpacity());

        logo_pan.setBackground(new java.awt.Color(255, 255, 255));
        logo_pan.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                logo_panMouseDragged(evt);
            }
        });
        logo_pan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logo_panMousePressed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/dumbbell (3).png"))); // NOI18N

        javax.swing.GroupLayout logo_panLayout = new javax.swing.GroupLayout(logo_pan);
        logo_pan.setLayout(logo_panLayout);
        logo_panLayout.setHorizontalGroup(
            logo_panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logo_panLayout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(jLabel5)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        logo_panLayout.setVerticalGroup(
            logo_panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logo_panLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(228, 67, 13));
        jPanel3.setForeground(new java.awt.Color(228, 67, 13));

        user_txt.setBackground(new java.awt.Color(228, 67, 13));
        user_txt.setForeground(new java.awt.Color(255, 255, 255));
        user_txt.setText("Username");
        user_txt.setBorder(null);

        password_txt.setBackground(new java.awt.Color(228, 67, 13));
        password_txt.setForeground(new java.awt.Color(255, 255, 255));
        password_txt.setText("Pasword");
        password_txt.setBorder(null);

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/password (1).png"))); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_proj/user-profile.png"))); // NOI18N

        login_btn.setBackground(new java.awt.Color(228, 67, 13));
        login_btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        login_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                login_btnMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Log in");
        jLabel3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel3AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jLabel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel3KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout login_btnLayout = new javax.swing.GroupLayout(login_btn);
        login_btn.setLayout(login_btnLayout);
        login_btnLayout.setHorizontalGroup(
            login_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        login_btnLayout.setVerticalGroup(
            login_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Cancel_btn.setBackground(new java.awt.Color(228, 67, 13));
        Cancel_btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Cancel");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Cancel_btnLayout = new javax.swing.GroupLayout(Cancel_btn);
        Cancel_btn.setLayout(Cancel_btnLayout);
        Cancel_btnLayout.setHorizontalGroup(
            Cancel_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        Cancel_btnLayout.setVerticalGroup(
            Cancel_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        forget_btn.setFont(new java.awt.Font("Microsoft JhengHei", 0, 11)); // NOI18N
        forget_btn.setForeground(new java.awt.Color(204, 204, 255));
        forget_btn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        forget_btn.setText("Forget password ?");

        txt_userORpass.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 12)); // NOI18N
        txt_userORpass.setForeground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Cancel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(password_txt)
                                .addComponent(user_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                .addComponent(jSeparator1)
                                .addComponent(jSeparator2))
                            .addComponent(txt_userORpass, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(forget_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(user_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(password_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(txt_userORpass)
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(login_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Cancel_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(forget_btn)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(logo_pan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logo_pan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private int xMouse ,yMouse ;
    private void logo_panMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logo_panMousePressed
         xMouse=evt.getX();
         yMouse=evt.getY();
    }//GEN-LAST:event_logo_panMousePressed

    private void logo_panMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logo_panMouseDragged
        int x =evt.getXOnScreen();
        int y =evt.getYOnScreen() ;
        this.setLocation(x-xMouse,y-yMouse);
    }//GEN-LAST:event_logo_panMouseDragged

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        System.exit(0);
        
    }//GEN-LAST:event_jLabel4MouseClicked
  
    private void jLabel3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel3KeyPressed

    }//GEN-LAST:event_jLabel3KeyPressed

    private void jLabel3AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel3AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3AncestorAdded

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        ArrayList<admin> list = getadminList() ;
        String user=user_txt.getText();
        String  pass=String.valueOf(password_txt.getPassword());
        for (int i=0 ;i<list.size();i++){
            String u = list.get(i).getUser();
            String p = list.get(i).getPass();
            if (u.equals(user) & p.equals(pass)){
                 try {
                        new home().setVisible(true);
                    } catch (ParseException ex) {
                        Logger.getLogger(GYM_login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.hide();
                    break;
            }
            
        }
        txt_userORpass.setText("user or password incorrect !");
//        try {
//            new home().setVisible(true);
//        } catch (ParseException ex) {
//            Logger.getLogger(GYM_login.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        this.hide();
    }//GEN-LAST:event_jLabel3MouseClicked
    
    private void login_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_btnMouseClicked
//        try {
//            new home().setVisible(true);
//        } catch (ParseException ex) {
//            Logger.getLogger(GYM_login.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        this.hide();
    }//GEN-LAST:event_login_btnMouseClicked

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GYM_login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GYM_login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Cancel_btn;
    private javax.swing.JLabel forget_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel login_btn;
    private javax.swing.JPanel logo_pan;
    private javax.swing.JPasswordField password_txt;
    private javax.swing.JLabel txt_userORpass;
    private javax.swing.JTextField user_txt;
    // End of variables declaration//GEN-END:variables
}
