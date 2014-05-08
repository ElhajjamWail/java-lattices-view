/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    * Permet d'ajouter un attribut dans la fenêtre IS, si cet attribut n'est pas vide ou est de couleur grise (description).
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