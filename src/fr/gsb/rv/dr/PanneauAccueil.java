/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import java.io.File;
import java.net.MalformedURLException;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author etudiant
 */
public class PanneauAccueil extends Pane{
    public PanneauAccueil(){
        super();
        this.setStyle("-fx-alignment: center; -fx-background-color: white;");
        VBox vb = new VBox(50);
        ImageView imageAccueil = new ImageView("file:/home/etudiant/Téléchargements/accueil.png");
        ImageView image = new ImageView("file:/home/etudiant/Téléchargements/index.jpeg");
        
        
        image.setLayoutX(160);
        image.setLayoutY(190);
        
        imageAccueil.setFitHeight(250);
        imageAccueil.setFitWidth(650);
        imageAccueil.setLayoutX(-25);
        imageAccueil.setLayoutY(0);
        
        this.getChildren().addAll(imageAccueil, image);
        
        
    }
}
