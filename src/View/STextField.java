/*
 * STextField.java
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
 *
 * @author Sylvain MORIN
 */
public abstract class STextField extends JTextField implements MouseListener, ActionListener{
 
        private String oldName;
        private JPopupMenu menu, menuFocusable;

        /**
         * Ce textField ignore les espaces.<br/><br/>
         * Pour pouvoir modifier : <ul><li>Double clique lorsqu'il a pas le focus</li><li>Clique droit, puis item "Update"</li></ul>
         * Pour Supprimer : <ul><li>Clique droit, puis item "Delete"</li><li>Lorsque l'on modifie, on enlève tout le texte et on valide</li></ul>
         * Pour valider la modification : <ul><li>Cliquer sur "Entrée"</li><li>Double clique</li><li>Clique droit, puis item "Validate"</li></ul>
         * Pour annuler la modification : <ul><li>Clique droit, puis item "Cancel"</li><li>Valider sans modifier le texte</li></ul>
         * @param text
         * Le texte
         */
        public STextField(String text)
        {
            super(text);
            super.addMouseListener(this);
            super.addActionListener(this);
            initComponent();
            editFocus(false);
        }       
        
        public abstract void itemValidate();
        
        public abstract void itemDelete();
        
        public void actionPerformed(ActionEvent ae) 
        {
            itemValidate();
        }
                
        public void mouseClicked(MouseEvent me)
        {
            if(me.getClickCount() == 2) //Double clic
                if(this.isFocusable())
                    itemValidate();
                else
                    itemUpdate();
            
            if(me.getButton() == MouseEvent.BUTTON3) //Clic droit
                if(this.isFocusable())                
                    menuFocusable.show(me.getComponent(), me.getX(), me.getY());
                else
                    menu.show(me.getComponent(), me.getX(), me.getY());
        }       
        
        private void itemCancel()
        {
            this.setText(oldName);
            editFocus(false);
        }
        
        private void itemUpdate()
        {
            editFocus(true);
            this.requestFocus();
            oldName = this.getText().toString();
        }
        
        public final void editFocus(Boolean b)
        {
            this.setEditable(b);
            this.setFocusable(b);
        }
        
        public String getOldName(){ return oldName; }

        private void initComponent()
        {
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Menu utilisé lorsque le textField est editable et focusable.
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            menuFocusable           = new JPopupMenu();
            JMenuItem itemCancel    = new JMenuItem("Cancel");
            JMenuItem itemValidate  = new JMenuItem("Validate");
            itemCancel.addActionListener(  new ActionListener() { public void actionPerformed(ActionEvent ae) { itemCancel();  }});
            itemValidate.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { itemValidate();}});

            menuFocusable.add(itemCancel);
            menuFocusable.add(itemValidate);
            
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Menu utilisé lorsque le textField n'est pas editable et pas focusable.
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            menu                  = new JPopupMenu();
            JMenuItem itemUpdate  = new JMenuItem("Update");
            JMenuItem itemDelete  = new JMenuItem("Delete");
            itemUpdate.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { itemUpdate(); }});
            itemDelete.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { itemDelete(); }});

            menu.add(itemUpdate);
            menu.add(itemDelete);
        }
        
        public void mousePressed(MouseEvent me) {}
        public void mouseReleased(MouseEvent me) {}
        public void mouseEntered(MouseEvent me) {}
        public void mouseExited(MouseEvent me) {}
    }
