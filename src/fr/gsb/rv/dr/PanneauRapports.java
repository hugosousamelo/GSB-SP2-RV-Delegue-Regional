/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.entites.RapportVisite;
import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.technique.ConnexionException;
import fr.gsb.rv.technique.Mois;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author etudiant
 */
public class PanneauRapports extends Pane{
    static TableView<RapportVisite> tabRapports ;
    static ComboBox<Visiteur> cbVisiteurs = new ComboBox<Visiteur>();
    static ComboBox<Mois> cbMois = new ComboBox<Mois>();
    static ComboBox<Integer> cbAnnee = new ComboBox<Integer>();
    public PanneauRapports(){
        super();
        this.setStyle("-fx-alignment: center; -fx-background-color: white;");
        List<Visiteur> visiteurs = null;
        try {
            visiteurs = ModeleGsbRv.getVisiteur();
        } catch (ConnexionException | SQLException ex) {
            Logger.getLogger(PanneauRapports.class.getName()).log(Level.SEVERE, null, ex);
        }
        VBox vb = new VBox(15);
        HBox hb = new HBox(15);
        Button btValider = new Button("Valider");
        tabRapports = new TableView(); 
        
        visiteurs.forEach((unVisiteur) -> {
            cbVisiteurs.getItems().addAll(unVisiteur);
        });
        cbMois.getItems().addAll(Arrays.asList(Mois.values()));
        
        LocalDate aujourdhui = LocalDate.now();
        int anneeCourante = aujourdhui.getYear();
        int annee = anneeCourante;
        int anneeLimite = annee - 6;
        while( annee != anneeLimite){
            cbAnnee.getItems().add(annee);
            annee = annee -1;
        }
        
        TableColumn<RapportVisite,Integer> colNumero = new TableColumn<>("Numero");
        TableColumn<RapportVisite,String> colNom = new TableColumn<>("Praticien");
        TableColumn<RapportVisite,String> colVille = new TableColumn<>("Ville");
        TableColumn<RapportVisite,LocalDate> colVisite = new TableColumn<>("Visite");
        TableColumn<RapportVisite,LocalDate> colRedaction= new TableColumn<>("Redaction");
        
        colNumero.setCellValueFactory( new PropertyValueFactory<>( "numero" ));
        
        colNom.setCellValueFactory(
            param -> {
                String nom = param.getValue().getLePraticien().getNom();
                return new SimpleStringProperty(nom);
        });
        
        colVille.setCellValueFactory(
                param -> {
                    String ville = param.getValue().getLePraticien().getVille();
                    return new SimpleStringProperty(ville);
                }
        );
        colVisite.setCellValueFactory(new PropertyValueFactory<>("dateVisite"));
        colVisite.setCellFactory(
                colonne -> {
                    return new TableCell<RapportVisite,LocalDate>(){
                        @Override
                        protected void updateItem( LocalDate item, boolean empty ){
                            super.updateItem( item, empty );
                            
                            if(empty){
                                setText("");
                            }
                            else{
                                DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                                setText( item.format(formateur) );
                            }
                        }
                    };
                }
        );
        
        colRedaction.setCellValueFactory(new PropertyValueFactory<>("dateRedaction"));
        colRedaction.setCellFactory(
                colonne -> {
                    return new TableCell<RapportVisite,LocalDate>(){
                        @Override
                        protected void updateItem( LocalDate item, boolean empty ){
                            super.updateItem( item, empty );
                            
                            if(empty){
                                setText("");
                            }
                            else{
                                DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                                setText( item.format(formateur) );
                            }
                        }
                    };
                }
        );
        
        tabRapports.setRowFactory(
                ligne -> {
                    return new TableRow<RapportVisite>(){
                        @Override
                        protected void updateItem( RapportVisite item, boolean empty ){
                            super.updateItem( item,empty );
                            
                            if( item != null ){
                                if( item.getLu() == true ){
                                    setStyle ( "-fx-background-color: gold" );
                                }
                                if( item.getLu() == false ){
                                    setStyle ( "-fx-background-color: cyan" );
                                }
                            }
                            if ( item == null ){
                                setStyle ( "-fx-background-color: white" );
                            }
                        }
                    };
                }
        );
        
        tabRapports.setOnMouseClicked(
                (MouseEvent event) -> {
                    if( event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ){
                        int indiceRapport = tabRapports.getSelectionModel().getSelectedIndex();
                        Visiteur leVisiteur = (Visiteur) cbVisiteurs.getSelectionModel().getSelectedItem();
                        RapportVisite rv = tabRapports.getItems().get(indiceRapport);
                        
                        try {
                            ModeleGsbRv.setRapportVisite(rv.getLeVisiteur().getMatricule(), rv.getNumero());
                            
                            this.rafraichir();
                            
                            VueRapport vueRapport = new VueRapport(rv);
                            vueRapport.showAndWait();
                            
                        } catch (ConnexionException | SQLException ex) {
                            Logger.getLogger(PanneauRapports.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
        );
        
        btValider.setOnAction( (ActionEvent event)->{  
            try {
                this.rafraichir();
            } catch (ConnexionException | SQLException ex) {
                Logger.getLogger(PanneauRapports.class.getName()).log(Level.SEVERE, null, ex);
            }
                 }
        );
        
        
        
        tabRapports.getColumns().addAll(colNumero, colNom, colVille, colVisite, colRedaction);
        tabRapports.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hb.getChildren().addAll(cbVisiteurs,cbMois,cbAnnee);
        vb.getChildren().addAll(hb,btValider,tabRapports);
        vb.setLayoutX(60);
        vb.setLayoutY(10);
        this.getChildren().add(vb);
    }
    
    public static void PanneauRapports(){
    
    }
    
    public static void rafraichir() throws ConnexionException, SQLException{
        if(cbVisiteurs.getSelectionModel().isEmpty() || cbAnnee.getSelectionModel().isEmpty() || cbMois.getSelectionModel().isEmpty()){
            Alert alertChamp = new Alert(Alert.AlertType.INFORMATION);
            alertChamp.setTitle("Champ vide");
            alertChamp.setHeaderText("Attention");
            alertChamp.setContentText("La selection est incomplete ! ");
            
            Optional<ButtonType> reponse = alertChamp.showAndWait();
        }
        else{
            Visiteur leVisiteur;
            leVisiteur = (Visiteur) cbVisiteurs.getSelectionModel().getSelectedItem();
            int mois = cbMois.getSelectionModel().getSelectedIndex()+1;
            int annee = (int) cbAnnee.getSelectionModel().getSelectedItem();
            
            List<RapportVisite> rapport = ModeleGsbRv.getRapportVisite(leVisiteur.getMatricule(),mois,annee);
            ObservableList<RapportVisite> obListe = FXCollections.observableArrayList(rapport);
            
            tabRapports.setItems(obListe);
            
        }
    }
}
