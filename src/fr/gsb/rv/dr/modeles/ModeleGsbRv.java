package fr.gsb.rv.dr.modeles;

import fr.gsb.rv.dr.GSBRVDR;
import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.entites.RapportVisite;
import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.technique.ConnexionBD;
import fr.gsb.rv.technique.ConnexionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeleGsbRv {
    
    public static Visiteur seConnecter( String matricule , String mdp ) throws ConnexionException{
        
        Connection connexion = ConnexionBD.getConnexion() ;
        
        String requete = "select v.vis_matricule, v.vis_nom, v.vis_prenom, t.tra_role, t.jjmmaa \n" +
                            "from Travailler t  \n" +
                            "inner join Visiteur v\n" +
                            "on v.vis_matricule = t.vis_matricule\n" +
                            "where t.tra_role = \"Délégué\"\n" +
                            "and jjmmaa = (\n" +
                            "select max(jjmmaa)\n" +
                            "from Travailler t2\n" +
                            "where t2.vis_matricule = t.vis_matricule) \n" +
                            "and v.vis_matricule = \""+matricule+"\" \n" +
                            "and v.vis_mdp = \""+mdp+"\";" ;
        
        try {
            
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement( requete ) ;
            ResultSet resultat = requetePreparee.executeQuery() ;
            if( resultat.next() ){
                Visiteur visiteur = new Visiteur() ;
                visiteur.setMatricule( matricule );
                visiteur.setNom( resultat.getString( "vis_nom" ) ); 
                visiteur.setPrenom( resultat.getString( "vis_prenom" ) );
                
                requetePreparee.close() ;
                return visiteur ;
            }
            else {
                return null ;
            }
        }
        catch( SQLException e ){
            
            return null;
        } 
    }

    public static List<Praticien> getPraticiensHesitants() throws ConnexionException, SQLException{
        Connection connexion = ConnexionBD.getConnexion() ;
        Statement requetePreparee = (Statement) connexion.createStatement();
        
        String requete = "select *  \n" +
                            "from RapportVisite r, Praticien p\n" +
                            "where p.pra_num = r.pra_num\n" +
                            "and r.rap_coefConfiance < 5 \n" +
                            "and r.rap_date_visite = ( \n" +
                            "select max(r2.rap_date_visite) \n" +
                            "from RapportVisite r2 \n" +
                            "where r.pra_num = r2.pra_num \n" +
                            "and r2.vis_matricule = r.vis_matricule );";
        
        try {
            
            ResultSet resultat = requetePreparee.executeQuery(requete) ;
            List<Praticien> listPr = new ArrayList<>();
            while( resultat.next() ){
                Praticien pr = new Praticien();
                pr.setNumero(resultat.getInt( "pra_num" ));
                pr.setNom(resultat.getString( "pra_nom" ) );
                pr.setVille(resultat.getString("pra_ville"));
                pr.setCoefNotoriete(resultat.getDouble( "pra_coefnotoriete" ));
                pr.setDateDerniereVisite(resultat.getDate( "rap_date_visite" ).toLocalDate());
                pr.setDernierCoefConfiance(resultat.getInt("rap_coefConfiance"));
                listPr.add(pr);
            }
            return listPr;
        }
        catch( SQLException e ){
            
            return null;
        } 
    }
    
    public static List<Visiteur> getVisiteur() throws ConnexionException, SQLException{
        Connection connexion = ConnexionBD.getConnexion() ;
        Statement requetePreparee = (Statement) connexion.createStatement();
        
        String requete = "Select vis_matricule, vis_nom, vis_prenom "
                            + "from Visiteur;";
        
        try {
            
            ResultSet resultat = requetePreparee.executeQuery(requete) ;
            List<Visiteur> listVisi = new ArrayList<>();
            while( resultat.next() ){
                Visiteur visiteur = new Visiteur() ;
                visiteur.setMatricule( resultat.getString("vis_matricule") );
                visiteur.setNom( resultat.getString( "vis_nom" ) ); 
                visiteur.setPrenom( resultat.getString( "vis_prenom" ) );
                listVisi.add(visiteur);
            }
            return listVisi;
        }
        catch( SQLException e ){
            
            return null;
        } 
    }
    
    public static List<RapportVisite> getRapportVisite(String matricule, int mois, int annee) throws ConnexionException, SQLException{
        Connection connexion = ConnexionBD.getConnexion() ;
        Statement requetePreparee = (Statement) connexion.createStatement();
        
        LocalDate date = LocalDate.of(annee, mois, 1);
        String laDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String requete = "select * \n" +
                            "from RapportVisite r, Motif m, Visiteur v, Praticien p \n" +
                            "where m.mo_code = r.mo_code \n" +
                            "and p.pra_num = r.pra_num \n"+
                            "and v.vis_matricule = r.vis_matricule \n"+
                            "and v.vis_matricule = \""+matricule+"\" \n" +
                            "and rap_date_visite like '"+laDate+"%';";
        
        try {
            
            ResultSet resultat = requetePreparee.executeQuery(requete) ;
            List<RapportVisite> listRapport = new ArrayList<>();
            
            while( resultat.next() ){
                RapportVisite rapport = new RapportVisite() ; 
                rapport.setNumero( resultat.getInt( "rap_num" ) );
                rapport.setDateVisite( resultat.getDate( "rap_date_visite" ).toLocalDate() );
                rapport.setDateRedaction( resultat.getDate( "rap_date_redaction" ).toLocalDate() );
                rapport.setBilan( resultat.getString( "rap_bilan" ) );
                rapport.setMotif( resultat.getString( "mo_libelle" ) );
                rapport.setCoefConfiance( resultat.getInt( "rap_coefConfiance" ) );
                rapport.setLu( resultat.getBoolean( "rap_lu" ) );
                
                Visiteur visiteur = new Visiteur() ;
                visiteur.setMatricule( resultat.getString("vis_matricule") );
                visiteur.setNom(resultat.getString( "vis_nom" ));
                visiteur.setPrenom(resultat.getString( "vis_prenom" ));
                rapport.setLeVisiteur( visiteur );
                
                Praticien pr = new Praticien();
                pr.setNumero(resultat.getInt( "pra_num" ));
                pr.setVille(resultat.getString( "pra_ville" ));
                pr.setNom(resultat.getString( "pra_nom" ));
                pr.setPrenom(resultat.getString( "pra_prenom" ));
                rapport.setLePraticien( pr );
                
                listRapport.add(rapport);
                
            }
            return listRapport;
        }
        catch( SQLException e ){
            Logger.getLogger(GSBRVDR.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } 
    }
    
    public static void setRapportVisite(String matricule, int numRapport) throws ConnexionException, SQLException{
       
        
        String requete = "update RapportVisite \n" +
                            "set rap_lu = 1 \n" +
                            "where rap_num = ? \n" +
                            "and vis_matricule = ?;";
        
        try { 
            Connection connexion = ConnexionBD.getConnexion() ;
            PreparedStatement pstmt = connexion.prepareStatement(requete);
            pstmt.setInt(1,numRapport);
            pstmt.setString(2, matricule);
            
            pstmt.executeUpdate();
            
        }
        catch( SQLException e ){
            Logger.getLogger(GSBRVDR.class.getName()).log(Level.SEVERE, null, e);
        } 
    }
}
