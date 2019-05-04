/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.utilitaires;

import fr.gsb.rv.entites.Praticien;
import java.util.Comparator;

/**
 *
 * @author etudiant
 */
public class ComparateurDateVisite implements Comparator<Praticien>{

    @Override
    public int compare(Praticien o1, Praticien o2) {
        if( o1.getDateDerniereVisite().isEqual(o2.getDateDerniereVisite())){
           return 0;
       }
       else if( o1.getDateDerniereVisite().isAfter(o2.getDateDerniereVisite())){
           return 1;
       }
       else {
           return -1;
       }
    }
    
}
