/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import lattice.Rule;
import View.ISInterface;
import View.SComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * @author Sylvain MORIN
 */
public class ComboPopupISAction implements PopupMenuListener
{
    private ISInterface.actionCombo action;
    private ISInterface face;
    private SComboBox combo;

    /**
     * Action permettant :<ul><li>D'ajouter un comboBox et une règle si l'action est "premise"</li><li>D'ajouter un comboBox et une règle si l'action est "conclusion"</li><li>De descendre la ligne vide et d'ajouter une règle avec une conclusion si l'action est "premisseEmpty"</li><li>De descendre la ligne vide et d'ajouter une règle avec une conclusion si l'action est "conclusionEmpty"</li></ul>
     * @param face
     * Le panel.
     * @param action
     * L'action.
     * @param combo
     * Le combo sur lequel l'action est utilisée.
     */
    public ComboPopupISAction(ISInterface face, ISInterface.actionCombo action, SComboBox combo){
        this.action = action;
        this.combo = combo;
        this.face = face;
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent pme)
    {
        if(combo.getSelectedIndex() > 0)
            if(action == ISInterface.actionCombo.conclusion || action == ISInterface.actionCombo.premise)
            {
                face.add(new SComboBox(face.getIS().getSet(), face, (action == ISInterface.actionCombo.premise ? true : false), combo.getSelectedItem().toString()), combo);

                if(action == ISInterface.actionCombo.premise)
                    face.getIS().getRules().floor(face.layout.getRule(face.layout.getRow(combo))).addToPremise(   (Comparable)combo.getSelectedItem());
                else
                    face.getIS().getRules().floor(face.layout.getRule(face.layout.getRow(combo))).addToConclusion((Comparable)combo.getSelectedItem());
            }
            else
            {
                int rowMax = face.layout.getRowMax();
                face.layout.lineDown(rowMax);

                Rule r = new Rule();

                if(action == ISInterface.actionCombo.conclusionEmpty)
                    r.addToConclusion(combo.getSelectedItem().toString());
                else
                    r.addToPremise(combo.getSelectedItem().toString());

                face.addRule(rowMax, r);
                face.getIS().addRule(r);
            }
            combo.setSelectedIndex(0);
    }
    public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {}
    public void popupMenuCanceled(PopupMenuEvent pme) {}        
}