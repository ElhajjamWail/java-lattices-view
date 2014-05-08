/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import lattice.Rule;
import View.ISInterface;
import View.SComboBox;

/**
 *
 * @author Sylvain MORIN
 */
public class ISController {
    
    private ISInterface face;
    
    public ISController(ISInterface face){this.face = face;}  
    
    /**
     * Met à jour la ligne. Supprime le comboBox si celui-ci choisi l'élément null. Supprime la ligne s'il reste moins de 5 éléments (Ce combo y compris).
     * @param parent
     * Le parent.
     * @param isPremise 
     * Si c'est une premisse ou une conclusion.
     */
    public void updateRow(SComboBox combo, boolean isPremise)
    {
        Object item = combo.getSelectedItem();
        if(item != combo.oldItem)
        {
            int row  = face.layout.getRow(combo);
            Rule r  = face.layout.getRule(row, combo);
            Rule r2 = new Rule(r.getPremise(), r.getConclusion());
            
            if(isPremise)
                r.addToPremise((Comparable)combo.oldItem);
            else
                r.addToConclusion((Comparable)combo.oldItem);
               
            
            if(item != null)
                if(isPremise)
                    r2.addToPremise((Comparable)  item);
                else
                    r2.addToConclusion((Comparable)  item);
            
            if(!containRule(r, row))
                face.getIS().removeRule(r);
                
            if(combo.getSelectedItem() == null)      
                if(face.layout.getColumnMax(row) < 4)                 
                    face.layout.removeLineComponent(row);
                else
                    face.getIS().addRule(r2); //And remove by repaint()
            else
            {
                face.getIS().addRule(r2);
                combo.oldItem = item.toString();
            }
        }
    }
    
    /**
     * Permet de savoir si la règle est contenu ou pas dans le panel, puisqu'elle n'est contenu qu'une seule fois dans le IS et peut être contenu plusieurs fois dans le panel.
     * @param parent
     * Le parent.
     * @param r
     * la règle.
     * @param row
     * La ligne où se situe la règle cherchée, pour éviter de la retrouver.
     * @return 
     * Vrai si elle est contenue.
     */
    private boolean containRule(Rule r, int row)
    {
        for(int i = 1; i < face.layout.getRowMax(); i++)
            if(i != row)
                if(face.layout.getRule(i).equals(r))
                    return true;        
        return false;
    }
    
}
