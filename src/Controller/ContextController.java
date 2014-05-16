/*
 * ContextController.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import View.ContextInterface;
import static View.ContextInterface.TWO_POINT;
import View.SComboBox;
import View.hmi;
import javax.swing.JComboBox;

/**
 * @author Sylvain MORIN
 */
public class ContextController {
    
    private ContextInterface face;
    
    public ContextController(ContextInterface face){ this.face = face;}
    
    /**
     * Ajoute l'élément sélectionné par le combo, dans un nouveau comboBox.
     * @param combo
     * The combo who should select an element.
     * @param observation 
     * The comboBox is added in function of this observation.
     */
    public void addCombo(SComboBox combo, Comparable observation)
    {
        if( combo.getSelectedIndex() > 0)
        {
            face.add(new SComboBox(face.getContext().getAttributes(), true, face, combo.getSelectedItem().toString()), combo);
            face.getContext().addExtentIntent(observation,(Comparable) combo.getSelectedItem());
            combo.setSelectedIndex(0);
        }
    }
    
    /**
     * Met à jour le comboBox.
     * @param combo 
     * Le comboBox.
     */
    public void update(SComboBox combo)
    {
        Object item = combo.getSelectedItem();
        if(item != combo.oldItem)
        {
            removeExtendIntent(combo);
            if(combo.getSelectedItem() != null)
            {
                addExtendIntent(combo);
                combo.oldItem = item;
            }
        }
    }
    
    /**
     * Supprime un ExtendIntent si l'attribut n'existe que sur le combo et sur sa ligne. Permet de coordonner la vue avec le modèle.
     * @param combo 
     * Le combo contenant l'item sera supprimé, si l'attribut n'existe que sur ce combo, alors l'attribut sera supprimé de l'observation.
     */
    private void removeExtendIntent(JComboBox combo)
    {
        String observation  = face.layout.searchLabel(combo).split(TWO_POINT)[0].trim();
        Comparable[] set    = face.layout.searchItemInCombo(combo).toArray(new Comparable[]{}); //Conversion en tableau pour éviter d'utiliser un iterator
        Comparable[] intent = face.getContext().getIntent(observation).toArray(new Comparable[]{});
        
        try{
        if(set.length != intent.length)
        {
            int i;
            for(i = 0; i < set.length; i++)
                if(!intent[i].equals(set[i]))
                {
                    face.getContext().removeExtentIntent(observation, intent[i]);
                    return;
                }
            face.getContext().removeExtentIntent(observation, intent[i]);
        }
        }catch(Exception e){ hmi.Toast(e.toString());}
    }
    
    /**
     * Ajoute un extendIntent en fonction du combo.
     * @param combo 
     * Sa ligne et son élément sélectionné sont utilisés. 
     */
    private void addExtendIntent(JComboBox combo){
        face.getContext().addExtentIntent(face.layout.searchLabel(combo).split(TWO_POINT)[0].trim(), combo.getSelectedItem().toString());}  
}
