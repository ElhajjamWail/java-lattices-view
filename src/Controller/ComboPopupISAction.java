/*
 * ComboPopupISAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
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
     * Action to :<ul><li>add a comboBox and a rule if the action is "premise"</li><li>add a comboBox and a rule if the action is "conclusion"</li><li>lower the empty line and add a rule with a conclusion if the action is "premisseEmpty"</li><li>lower the empty line and add a rule with a conclusion if the action is "conclusionEmpty"</li></ul>
     * @param face
     * The panel.
     * @param action
     * The action.
     * @param combo
     * the combo which the action is used.
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