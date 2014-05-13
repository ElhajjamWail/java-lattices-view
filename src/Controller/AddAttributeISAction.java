/*
 * AddAttributeISAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import View.ISInterface;
import View.hmi;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Sylvain MORIN
*/
public class AddAttributeISAction extends AbstractAction
{
    private ISInterface face;
    
    /**
    * Adds an attribut in the window IS, if it isn't empty or gray (description).
    */
    public AddAttributeISAction(ISInterface face){this.face = face;}
    
    public void actionPerformed(ActionEvent ae)
    {
        String attribut = face.txtAttribute.getText().replaceAll(" ", "");
        if(!attribut.isEmpty() && !face.txtAttribute.getForeground().equals(Color.gray))
        {
            if(face.getIS().addElement(attribut))
            {
                face.txtAttribute.setText("");
                face.add(new STextFieldIS(face, attribut), 0, 1);
                face.layout.addItemInCombo(attribut);
            }
            else
                hmi.Toast("This attribute already exists.");
        }
    }
}    