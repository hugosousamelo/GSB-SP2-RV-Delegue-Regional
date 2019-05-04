/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.technique.ConnexionException;
import fr.gsb.rv.technique.Session;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.swing.JOptionPane;

/**
 *
 * @author etudiant
 */
public class GSBRVDR extends Application {
    
     //Creer les 3 panneaux
     
     PanneauRapports vueRapports = new PanneauRapports();
     PanneauPraticiens vuePraticiens = new PanneauPraticiens();
    @Override
    public void start(Stage primaryStage) throws ConnexionException, SQLException, MalformedURLException {
        MenuBar barreMenus = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        Menu menuRapports = new Menu("Rapports");
        Menu menuPraticiens = new Menu("Praticiens");
        MenuItem itemSeConnecter = new MenuItem("Se Connecter");
        MenuItem itemSeDeConnecter = new MenuItem("Se Deconnecter");
        SeparatorMenuItem separatorQuitter = new SeparatorMenuItem();
        MenuItem itemQuitter = new MenuItem("Quitter");
        MenuItem itemConsulter = new MenuItem("Consulter");
        MenuItem itemHesitants = new MenuItem("Hésitants");
        //Creer une pile
        StackPane pile = new StackPane();
        //
        PanneauAccueil vueAccueil = new PanneauAccueil();
        
        Session.fermer();
         itemQuitter.setOnAction((ActionEvent event) -> {
             Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
             ButtonType btnOui = new ButtonType("Oui",OK_DONE);
             ButtonType btnNon = new ButtonType("Non",CANCEL_CLOSE);
             alertQuitter.setTitle("Quitter");
             alertQuitter.setHeaderText("Demande de confirmation");
             alertQuitter.setContentText("Voulez-vous quitter l'application ? \n");
             alertQuitter.getButtonTypes().setAll(btnOui , btnNon);
             Optional<ButtonType> reponse = alertQuitter.showAndWait();
             if(reponse.get() == btnOui){
                 primaryStage.close();
             }
        });
        itemSeConnecter.setOnAction((ActionEvent event) -> {
            try {
                vueConnexion boiteDiag = new vueConnexion();
                Optional<Pair<String, String>> response = boiteDiag.showAndWait();
                if (response.isPresent()){
                    Visiteur visiteur1 = ModeleGsbRv.seConnecter(response.get().getKey(), response.get().getValue());
                    if (visiteur1 != null){
                        Session.ouvrir(visiteur1);
                        itemSeDeConnecter.setDisable(false);
                        itemSeConnecter.setDisable(true);
                        itemConsulter.setDisable(false);
                        itemHesitants.setDisable(false);
                        menuRapports.setDisable(false);
                        menuPraticiens.setDisable(false);
                        primaryStage.setTitle(Session.getSession().getLeVisiteur().getNom()+"-GSB-RV-DR");
                        
                    }else {
                        vueAccueil.toFront();
                        JOptionPane.showMessageDialog(null, "Erreur de connexion !!");
                    }
                }
            } catch (ConnexionException ex) {
                Logger.getLogger(GSBRVDR.class.getName()).log(Level.SEVERE, null, ex); 
            }
        });
        itemSeDeConnecter.setOnAction((ActionEvent event) -> {
            Session.fermer();
            
            itemSeConnecter.setDisable(false);
            itemSeDeConnecter.setDisable(true);
            itemConsulter.setDisable(true);
            itemHesitants.setDisable(true);
            menuRapports.setDisable(true);
            menuPraticiens.setDisable(true);
            
            
            vuePraticiens = new PanneauPraticiens();
            vueRapports = new PanneauRapports();
            
            pile.getChildren().addAll(vueRapports,vuePraticiens);
            
            vueAccueil.toFront();
            primaryStage.setTitle("GSB-RV-DR");
        });
        itemHesitants.setOnAction((ActionEvent event) -> {
            if (Session.estOuverte() == true){
                try {
                    List<Praticien> praticiens = ModeleGsbRv.getPraticiensHesitants();
                    
                    vuePraticiens.toFront();
                } catch (ConnexionException | SQLException ex) {
                    Logger.getLogger(GSBRVDR.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        itemConsulter.setOnAction((ActionEvent event) -> {
            if (Session.estOuverte() == true){
                try {
                    List<Visiteur> visiteurs= ModeleGsbRv.getVisiteur();
                    //List<RapportVisite> rapports = ModeleGsbRv.getRapportVisite("c14",03,2019);
                    ModeleGsbRv.setRapportVisite("c14",9);
                } catch (ConnexionException | SQLException ex) {
                    Logger.getLogger(GSBRVDR.class.getName()).log(Level.SEVERE, null, ex);
                }
                vueRapports.toFront();
            }
        });        
        
        BorderPane root = new BorderPane();
        menuFichier.getItems().add(itemSeConnecter);
        itemSeDeConnecter.setDisable(true);
        itemConsulter.setDisable(true);
        itemHesitants.setDisable(true);
        menuRapports.setDisable(true);
        menuPraticiens.setDisable(true);
        menuFichier.getItems().add(itemSeDeConnecter);
        menuFichier.getItems().add(separatorQuitter);
        menuFichier.getItems().add(itemQuitter);
        menuRapports.getItems().add(itemConsulter);
        menuPraticiens.getItems().add(itemHesitants);
        itemQuitter.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        itemConsulter.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        itemHesitants.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        itemSeDeConnecter.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        itemSeConnecter.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        barreMenus.getMenus().add(menuFichier);
        barreMenus.getMenus().add(menuRapports);
        barreMenus.getMenus().add(menuPraticiens);
        //Ajouter les 3 panneaux a la pile
        pile.getChildren().add(vueAccueil);
        pile.getChildren().add(vueRapports);
        pile.getChildren().add(vuePraticiens);
        //
        //Positionner Accueil en premier
        vueAccueil.toFront();
        //
        
        Scene scene = new Scene(root, 600, 550);
        root.setTop(barreMenus);
        //Positionner la pille au centre
        root.setCenter(pile);
        //
        primaryStage.setTitle("GSB-RV-DR");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        // 

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
