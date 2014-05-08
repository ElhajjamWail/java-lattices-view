/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * Permet d'ajouter une observation ainsi que sa ligne dans un contexte.
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
