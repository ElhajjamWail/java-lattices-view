/*
 * AddattributecAction.java
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
import View.hmi;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Sylvain MORIN
 */
public class AddAttributeCAction extends AbstractAction
{
    private ContextInterface face;
    
    /**
     * Permet d'ajouter un attribut dans le contexte, si celui-ci n'est pas vide ou de couleur grise (description).
     */
    public AddAttributeCAction(ContextInterface face){ this.face = face;}
    
    public void actionPerformed(ActionEvent ae)
    {
        String attribut = face.txtAttribute.getText().replaceAll(" ", "");
        if(!attribut.isEmpty() && !face.txtAttribute.getForeground().equals(Color.gray))
        {
            if(face.getContext().addToAttributes(attribut))
            {
                face.txtAttribute.setText("");
                face.add(new STextFieldC(face, attribut, true), 1, 2);
                face.layout.addItemInCombo(attribut);
                face.txtAttribute.requestFocus();
            }
            else
                hmi.Toast("This attribute already exists.");
        }
    }
}
