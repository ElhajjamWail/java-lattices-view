package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 * @author Sylvain MORIN
 */
public class SComboBox extends JComboBox<Object>{
     
    public Object oldItem = "";
    /**
     * Le SComboBox ajoute ses items de façon alphabétique. De plus il ajoute l'élément null comme premier item. <br/>
     * Ce comboBox ajoute une ligne dans le tableau si la dernière ligne du tableau a sélectionné un élement autre que null.<br/>
     * Ce comboBox supprime la ligne auquel il est inclus si :<ul><li>Celle-ci a sélectionné l'élement null</li><li>Celle-ci n'est pas la dernière ligne du tableau</li></ul>
     * @param table
     * Le tableau où le comboBox est la cellule par défaut.
     * @param set
     * La liste des valeurs que doit prendre le comboBox.
     * @param decorate 
     * Si le comboBox doit être autocompleté.
     */
    public SComboBox(final JTable table, TreeSet<Comparable> set, Boolean decorate)
    {     
        super.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent ae) {}
            public void ancestorMoved(AncestorEvent ae) {}
            public void ancestorRemoved(AncestorEvent ae) { ancestorTable(table); }
        });
        
        TableColumn colPremise = table.getColumnModel().getColumn(0);
        colPremise.setCellEditor(new DefaultCellEditor(this));

        decorated(decorate);
        initialiseItem(set, false);
    } 
    
    /**
     * Le SComboBox ajoute ses items de façon alphabétique. De plus il ajoute l'élément null comme premier item. <br/>
     * Ce combo se supprime s'il prend la valeur null.
     * @param set
     * La liste des valeurs que doit prendre le comboBox.
     * @param decorate 
     * Si le comboBox doit être autocompleté.
     * @param parent
     * Le panel qui contient le comboBox.
     * @param itemSelected
     * L'élément qui doit être sélectionné par le combo.
     */
    public SComboBox(TreeSet<Comparable> set, Boolean decorate, final ContextInterface parent, String itemSelected)
    {        
        super.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { repaint(parent, true); }});
        super.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {}
            public void popupMenuCanceled(PopupMenuEvent pme) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme){  parent.controller.update(SComboBox.this);}});

        decorated(decorate);
        initialiseItem(set, true);
        super.setSelectedItem(itemSelected);
        oldItem = itemSelected;
    }
    
    /**
     * Le SComboBox ajoute ses items de façon alphabétique. De plus il ajoute l'élément null comme premier item. <br/>
     * @param set
     * La liste des valeurs que doit prendre le comboBox.
     * @param decorate 
     * Si le comboBox doit être autocompleté.
     */
    public SComboBox(TreeSet<Comparable> set, Boolean decorate, final ContextInterface parent, final Comparable observation)
    {
        super.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { repaint(parent, false); }});
        super.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {}
            public void popupMenuCanceled(PopupMenuEvent pme) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme){ parent.controller.addCombo(SComboBox.this, observation);}});
        
        decorated(decorate);
        initialiseItem(set, true);
    }   
    
    /**
     * Le SComboBox ajoute ses items de façon alphabétique. De plus il ajoute l'élément null comme premier item. <br/>
     * Ce combo se supprime si il prend la valeur null.<br/>
     * Ce combo supprime la ligne auquel il appartient s'il reste moins de 4 composants (lui y compris) sur cette ligne .<br/>
     * Ce combo est autocomplété.
     * @param set
     * La liste des valeurs que doit prendre le comboBox.
     * @param parent
     * Le parent qui doit être un ISInterface.
     * @param isPremise
     * Si c'est un combo mis en premisse ou en conclusion.
     * @param itemSelected
     * L'élément qui doit être sélectionné par le combo.
     */
    public SComboBox(TreeSet<Comparable> set, final ISInterface parent, final boolean isPremise, String itemSelected)
    {        
        super.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { repaint(parent, true); }});
        super.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {}
            public void popupMenuCanceled(PopupMenuEvent pme) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme){ parent.controller.updateRow(SComboBox.this, isPremise);}});
        
        decorated(true);
        initialiseItem(set, true);
        super.setSelectedItem(itemSelected);
        oldItem = itemSelected;
    }
 
    /**
     * Le SComboBox ajoute ses items de façon alphabétique. De plus il ajoute l'élément null comme premier item. <br/>
     * @param set
     * La liste des valeurs que doit prendre le comboBox.
     * @param decorate 
     * Si le comboBox doit être autocompleté.
     */
    public SComboBox(TreeSet<Comparable> set, Boolean decorate, final JPanel parent)
    {
        super.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { repaint(parent, false); }});
        decorated(decorate);
        initialiseItem(set, true);
    }   
    

    /**
     * Ajoute l'objet de façon alphabétique.
     * @param object 
     * Objet à ajouté.
     */
    @Override
    public void addItem(Object object)
    {
        int size = super.getItemCount();

        for (int i=1; i<size; i++) //i=1 car il faut éviter le premier élément qui est null
            if (object.toString().compareToIgnoreCase(super.getItemAt(i).toString()) <= 0) // if object less than or equal obj
            {
                super.insertItemAt(object, i);
                return;
            }
        super.addItem(object);
    }     
    
      
    /**
     * Rafraîchi la fenetre pour que la taille du combo soit adapter au texte.
     * @param parent 
     * Le panel qui contient le comboBox.
     * @param isRemoved
     * Si true et si le combo sélectionne l'élément null alors le combo est supprimé. Si false Le combo ne sera pas supprimé.
     */   
    private void repaint(JPanel parent, boolean isRemoved)
    {
        if(super.getSelectedItem() != null)
        {
            parent.repaint();
            parent.validate();
        }  
        else if(isRemoved)
            parent.remove(this);
    }
    
    /**
     * Initialise la liste des items, avec comme première valeur null ou none
     * @param set 
     * La liste des items, en dehors du premier item.
     * @param nullable
     * Si le premier élément doit être null, au sinon c'est "<< none >>".
     */
    private void initialiseItem(TreeSet<Comparable> set, boolean nullable)
    {
        if(nullable)
            super.addItem(null);
        else
            super.addItem(AddClosure.NONE);
        for(Comparable c : set)
            this.addItem(c);
    }
    
    /**
     * Permet d'ajouter l'autocompletion sur le comboBox
     * @param decorate 
     * Permet l'autoCompletion
     */
    private void decorated(Boolean decorate)
    {
         if(decorate)
            AutoCompleteDecorator.decorate(this);
    }
   
    /**
     * Permet d'ajouter ou de supprimer une ligne du tableau "table" en fonction du comboBox et des autres lignes du tableau.
     * @param table
     * Le tableau où le comboBox est la cellule par défaut.
     */
    private void ancestorTable(JTable table)
    {
        int index = table.getSelectedRow();
        DefaultTableModel def = (DefaultTableModel) table.getModel();

        if(table.getValueAt(index, 0) != AddClosure.NONE)
        {
            if(table.getRowCount() == index+1)
                def.addRow(new Object[]{AddClosure.NONE});//Si ce n'est pas l'élément none et si c'est la dernière ligne qui est modifié
        }
        else
        {
            if(table.getRowCount() != index+1)
                def.removeRow(index);// Si c'est l'élément none et que ce n'est pas la dernière ligne du tableau
        }
    }  
}
