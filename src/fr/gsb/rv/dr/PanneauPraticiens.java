/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import static fr.gsb.rv.dr.PanneauRapports.tabRapports;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.technique.ConnexionException;
import fr.gsb.rv.utilitaires.ComparateurCoefConfiance;
import fr.gsb.rv.utilitaires.ComparateurCoefNotoriete;
import fr.gsb.rv.utilitaires.ComparateurDateVisite;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.persistence.Table;

/**
 *
 * @author etudiant
 */
public class PanneauPraticiens extends Pane{
    public Integer CRITERE_COEF_CONFIANCE = 1;
    public Integer CRITERE_COEF_NOTORIETE = 2;
    public Integer CRITERE_DATE_VISITE = 3;
    
    private Integer critereTri = CRITERE_COEF_CONFIANCE;
    private TableView<Praticien> table ;
    
    
    public PanneauPraticiens(){
        super();
        this.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-weight: bold;");
        
        VBox vb = new VBox(15);
        RadioButton rbCoefConfiance = new RadioButton("Confiance");
        RadioButton rbCoefNotoriete = new RadioButton("Notoriété");
        RadioButton rbDateVisite = new RadioButton("Date");
        table = new TableView(); 
        HBox hb = new HBox(15);
        ToggleGroup tg = new ToggleGroup();
        
        List<Praticien> praticiens = null;
        try {
            praticiens = ModeleGsbRv.getPraticiensHesitants();
        } catch (ConnexionException | SQLException ex) {
            Logger.getLogger(PanneauPraticiens.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<Praticien> ol = FXCollections.observableArrayList(praticiens);
        
        Label text = new Label("Séléctionner un critère de tri :"); 
        TableColumn<Praticien,Integer> colNumero = new TableColumn<Praticien,Integer>("Numero");
        TableColumn<Praticien,String> colNom = new TableColumn<Praticien,String>("Nom");
        TableColumn<Praticien,String> colVille = new TableColumn<Praticien,String>("Ville");
        
        colNumero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("Ville"));  
        
        table.getColumns().addAll(colNumero,colNom,colVille);
        this.rafraichir();
        rbCoefConfiance.setOnAction( (ActionEvent event)->{
                        critereTri = CRITERE_COEF_CONFIANCE;
                        this.rafraichir();
                    }
        );
        
        rbCoefNotoriete.setOnAction( (ActionEvent event)->{
                        critereTri = CRITERE_COEF_NOTORIETE;
                        this.rafraichir();
                    }
        );
        
        rbDateVisite.setOnAction( (ActionEvent event)->{
                        critereTri = CRITERE_DATE_VISITE;
                        this.rafraichir();
                    }
        );
        
        rbCoefConfiance.setToggleGroup(tg);
        rbCoefNotoriete.setToggleGroup(tg);
        rbDateVisite.setToggleGroup(tg);
        rbCoefConfiance.setSelected(true);
        
        hb.getChildren().addAll(rbCoefConfiance, rbCoefNotoriete, rbDateVisite);
        
        vb.setSpacing(15);
        vb.setLayoutX(153);
        vb.setLayoutY(10);
        vb.getChildren().addAll(text , hb, table);
        
        tabRapports.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.getChildren().add(vb);
    }
    
     public void rafraichir(){

            List<Praticien> p ;
            try {
                p = ModeleGsbRv.getPraticiensHesitants();
                ObservableList<Praticien> ol = FXCollections.observableArrayList(p); 

                if( critereTri == 1 ){
                    table.setItems( ol );

                }
                else if(critereTri == 2){
                    Collections.sort( ol , new ComparateurCoefNotoriete()); 
                    Collections.reverse(ol);
                    table.setItems( ol );
                }
                else if(critereTri == 3){
                    Collections.sort( ol , new ComparateurDateVisite()); 
                    Collections.reverse(ol);
                    table.setItems( ol );
                }
                
            } catch (ConnexionException ex) {
                Logger.getLogger(PanneauPraticiens.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(PanneauPraticiens.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    public Integer getCritereTri() {
        return critereTri;
    }
    
    
    
}






