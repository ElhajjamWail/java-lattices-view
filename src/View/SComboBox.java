/*
 * SComboBox.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

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
     * The SComboBox adds its items alphabetically. In addition, it adds the null element as the first item. <br/>
     * This comboBox adds a line in the table if the last row has selected an other element than null.<br/>
     * This comboBox removes the line to which it is included if :<ul><li>It has selected the null element</li><li>it is not the last line of Table</li></ul>
     * @param table
     * The table where the ComboBox is the default cell.
     * @param set
     * The list of values ​​must take comboBox.
     * @param decorate 
     * If the ComboBox should be autocomplete.
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
     * The SComboBox adds its items alphabetically. In addition, it adds the null element as the first item. <br/>
     * This combo is removed if it is null.
     * @param set
     * The list of values ​​must take comboBox.
     * @param decorate 
     * If the ComboBox should be autocomplete.
     * @param parent
     * The panel that contains the ComboBox.
     * @param itemSelected
     * The element to be selected by the combo.
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
     * The SComboBox adds its items alphabetically. In addition, it adds the null element as the first item. <br/>
     * @param set
     * The list of values ​​must take comboBox.
     * @param decorate 
     * If the ComboBox should be autocomplete.
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
     * The SComboBox adds its items alphabetically. In addition, it adds the null element as the first item.<br/>
     * This combo is removed if it is null.<br/>
     * This combo deletes the row to which it belongs if less than 4 components (including him) on this line.<br/>
     * This combo is autocompleted.
     * @param set
     * The list of values ​​must take comboBox.
     * @param parent
     * The parent who must be a IsInterface.
     * @param isPremise
     * If it is a combo set premise or conclusion.
     * @param itemSelected
     * The element to be selected by the combo.
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
     * The SComboBox adds its items alphabetically. In addition, it adds the null element as the first item. <br/>
     * @param set
     * The list of values ​​must take comboBox.
     * @param decorate 
     * If the ComboBox should be autocomplete.
     */
    public SComboBox(TreeSet<Comparable> set, Boolean decorate, final JPanel parent)
    {
        super.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { repaint(parent, false); }});
        decorated(decorate);
        initialiseItem(set, true);
    }   
    

    /**
     * Adds the object alphabetically.
     * @param object 
     * Object to add.
     */
    @Override
    public void addItem(Object object)
    {
        int size = super.getItemCount();

        for (int i=1; i<size; i++) //i=1 car il faut Ã©viter le premier Ã©lÃ©ment qui est null
            if (object.toString().compareToIgnoreCase(super.getItemAt(i).toString()) <= 0) // if object less than or equal obj
            {
                super.insertItemAt(object, i);
                return;
            }
        super.addItem(object);
    }     
    
      
    /**
     * Refreshed the window to the size of the combo is fit the text.
     * @param parent 
     * The panel that contains the ComboBox.
     * @param isRemoved
     * If true and if the combo selects the null element when the combo is deleted. If false combo will not be deleted.
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
     * Initializes the list of items, with the first-null or none
     * @param set 
     * The list of items
     * @param nullable
     * If the first element must be null, else it is to "<< none >>".
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
     * Adds autocompletion on comboBox
     * @param decorate 
     * Enables autocompletion
     */
    private void decorated(Boolean decorate)
    {
         if(decorate)
            AutoCompleteDecorator.decorate(this);
    }
   
    /**
     * To add or delete a row in the table "table" based on ComboBox and other table rows.
     * @param table
     * The table where the ComboBox is the default cell.
     */
    private void ancestorTable(JTable table)
    {
        int index = table.getSelectedRow();
        DefaultTableModel def = (DefaultTableModel) table.getModel();

        if(table.getValueAt(index, 0) != AddClosure.NONE)
        {
            if(table.getRowCount() == index+1)
                def.addRow(new Object[]{AddClosure.NONE});//If this element is not none and if it is the last line that is modified
        }
        else
        {
            if(table.getRowCount() != index+1)
                def.removeRow(index);// If this element is none and that is not the last line of Table
        }
    }  
}
