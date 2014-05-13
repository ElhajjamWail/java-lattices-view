/*
 * AddObservationCAction.java
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
public class AddObservationCAction extends AbstractAction
{
    private ContextInterface face;
    /**
     * Adds an observation and also the line in context.
     */
    public AddObservationCAction(ContextInterface face){ this.face = face;}

    public void actionPerformed(ActionEvent ae)
    {
        String observation = face.txtObservation.getText().replaceAll(" ", "");
        if(!observation.isEmpty() && !face.txtObservation.getForeground().equals(Color.gray))
        {
            if(face.getContext().addToObservations(observation))
            {
                face.txtObservation.setText("");
                face.add(new STextFieldC(face, observation, false), 0, 2);
                face.getContext().addToObservations(observation);
                face.addRow(face.layout.getRowMax()+1, observation);
                face.txtObservation.requestFocus();
            }
            else
                hmi.Toast("This attribute already exists.");
        }
    }
}
