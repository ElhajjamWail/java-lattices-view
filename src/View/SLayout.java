/*
 * SLayout.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import lattice.Rule;

/**
 *
 * @author Sylvain MORIN
 */
public class SLayout implements LayoutManager{
    
    private ArrayList<ManageLayout> components;
    private Container parent;
    private int longueur, hauteur;
    
    /**
     * Ce layout range les éléments en ligne et en fonction des frères sur cette même ligne.
     * @param parent 
     * Le panel dans lequel est utilisé SLayout.
     */
    public SLayout(Container parent)
    {
        this.parent = parent;
        components = new ArrayList<ManageLayout>();
    }
    
    /**
     * Add a component depending on its row and its column
     * @param c
     * the component added
     * @param row
     * The row in the panel
     * @param column 
     * The column in the panel
     * @exception 
     * 1 Si un ancien composant à la même ligne et la même colonne que le composant à ajouté, l'ancien sera déplacer d'une colonne (+1).
     * Ainsi que ceux qui auront la même ligne et une colonne supérieure.
     */
    public void add(Component c, int row, int column)
    {
        int index  = exist(row, column);
        if(index != -1)
        {
            for(ManageLayout m : components)
                if(m.row == row)
                    if(m.column >= column) 
                        m.column += 1; // Si un composant avec le meme r et c est ajouté, alors on décale d'une colonne tout les autres de cette ligne
            components.add(index, new ManageLayout(c, row, column)); // Et on l'ajoute à l'index de l'ancien
            layoutContainer(parent);
        }
        else
        {
            for(ManageLayout m : components)
            {
                index += 1;
                if(m.row == row)
                {
                    if(m.column > column)
                    {
                        components.add(index, new ManageLayout(c, row, column)); //Si un composant avec le meme r et c n'existe pas on cherche si il y en a un sur la meme ligne, avec une colonne superieure, si oui on prend sa place
                        layoutContainer(parent);
                        return;
                    }
                }
                else if(m.row > row)
                {
                    components.add(index, new ManageLayout(c, row, column)); //S'il existe un composant avec une ligne supérieur, on prend sa place.
                    layoutContainer(parent);
                    return;
                }
            }
            components.add(new ManageLayout(c, row, column));//Sinon on l'ajoute à la fin
            layoutContainer(parent);
        }
    }
    
     /**
     * Ajoute un nouveau composant à la ligne et à la colonne (+1) de l'autre composant. Les composants sur la même ligne et avec une colonne supérieure seront décalés (+1).
     * @param newComponent
     * the component added.
     * @param component
     * L'autre composant.
     */
    public void add(Component newComponent, Component component)
    {
        int index = -1;
        for(ManageLayout m : components)
        {
            index += 1;
            if(m.component.equals(component))
            {
                int column = m.column +1;
                for(ManageLayout m2 : components)
                    if(m2.row == m.row)
                        if(m2.column >= column) 
                            m2.column += 1; // Si un composant avec le meme r et c est ajouté, alors on décale d'une colonne tout les autres de cette ligne
                
                components.add(index+1, new ManageLayout(newComponent, m.row, column)); // Et on l'ajoute à l'index de l'ancien +1
                
                layoutContainer(parent);
                return;
            }
        }
    }
    
    /**
     * Réorganise les composants.
     */
    public void layoutContainer(Container cntnr) 
    {
        int height      = 18;
        int stringWidth = 0;
        int totalWidth  = 1;
        int row         = 0;
        JLabel lbl;
        JTextField txt;
        JButton bt;
        JComboBox combo;
        
        longueur = 0;
        for(ManageLayout m : components)
        {       
            if(row != m.row)
            {
                row = m.row;
                totalWidth = 1;
            }
            if(m.component instanceof JComboBox)
            {
                combo = (JComboBox) m.component;
                if(combo.getSelectedIndex() > 0)
                    stringWidth = combo.getFontMetrics(combo.getFont()).stringWidth(combo.getSelectedItem().toString()) + 30;
                else
                    stringWidth = 60;
            }
            else if(m.component instanceof JTextField)
            {
                txt = (JTextField) m.component;
                if(!txt.getText().isEmpty() && !txt.getForeground().equals(Color.gray))
                    stringWidth = txt.getFontMetrics(txt.getFont()).stringWidth(txt.getText()) + 10;
                else
                    stringWidth = 60;
            }
            else if(m.component instanceof JLabel)
            {
                lbl = (JLabel) m.component;
                stringWidth = lbl.getFontMetrics(lbl.getFont()).stringWidth(lbl.getText());
            }
            else if(m.component instanceof JButton)
            {
                bt = (JButton) m.component; 
                stringWidth = bt.getFontMetrics(bt.getFont()).stringWidth(bt.getText()) + 40;
            }  
            
            m.component.setBounds(totalWidth, 1+m.row*(height+4),  stringWidth, height);
            m.component.validate();
            totalWidth += stringWidth + 4;//4 : distance entre chaque composants.
            if(longueur < totalWidth)
                longueur = totalWidth;
        }
        hauteur = (row+1)*(height+5);
        parent.revalidate();
    }
       
    /**
     * Remove a component. Tout les composants sur la même ligne ayant une colonne supérieur à l'élément supprimé, seront déplacés d'une colonne (-1).
     * @param cmpnt
     * The component.
     */
    public void removeLayoutComponent(Component cmpnt)
    {
        Point p = new Point(-1, -1);
        for(ManageLayout m : components)
            if(cmpnt.equals(m.component))
            {
                p.setLocation(m.row, m.column);
                m.component.setVisible(false);
                components.remove(m);
                break;
            }  
        
        if(p.x != -1 && p.y != -1)
            for(ManageLayout m : components)
                   if(m.row == p.x)
                       if(m.column > p.y) 
                           m.column -= 1;
         
         layoutContainer(parent);
    }
    
    /**
     * Remove a component. Tout les composants sur la même ligne ayant une colonne supérieur à l'elément supprimé, seront déplacés d'une colonne (-1).
     * @param row
     * The row of the component
     * @param column 
     * The column of the column
     */
    public void removeLayoutComponent(int row, int column)
    {
        for(ManageLayout m : components)
            if(row == m.row)
                if(column == m.column)
                {
                    m.component.setVisible(false);
                    components.remove(m);
                    break;
                }  
        
        for(ManageLayout m : components)
               if(m.row == row)
                   if(m.column > column) 
                       m.column -= 1;
         
         layoutContainer(parent);
    }
    
    /**
     * Permet de trouver où se trouve le dernier élément sur une ligne.
     * @param row
     * La ligne.
     * @return
     * Retourne le maximum des colonnes sur une ligne. Si aucune élément existe sur cette ligne, retourne -1.
     */
    public int getColumnMax(int row)
    {
        for(int i = components.size() -1; i > -1; i--)
            if(components.get(i).row == row)
                return components.get(i).column;
        return -1;
    }
    
    /**
     * Permet de connaitre le nombre de la dernière ligne.
     * @return 
     * -1 s'il y a aucun composant.
     */
    public int getRowMax()
    {
        if(components.size() > 0)
            return components.get(components.size()-1).row;
        return -1;
    }
    
    /**
     * Retourne la ligne d'un composant.
     * @param component
     * Le composant
     * @return 
     * La ligne du composant. Si aucun composant n'est trouvé retourne -1.
     */
    public int getRow(Object component)
    {
        for(ManageLayout m : components)
            if(m.component.equals(component))
                return m.row;
        return -1;
    }
    
    /**
     * Permet de déterminer si un élément existe à une position donné par la ligne et la colonne
     * @param row
     * La ligne de l'élement cherché
     * @param column
     * La colonne de l'élement cherché 
     * @return 
     * Retourne la position de l'élement dans la liste des composant.<br/>
     * Retourne -1, si aucun élément existe à cette position.
     */
    public int exist(int row, int column)
    {
        int index = -1;
        for(ManageLayout m : components)
        {
            index += 1;
            if(m.row == row)
                if(m.column == column)
                    return index;
        }
        return -1;
    }

    public Dimension preferredLayoutSize(Container cntnr) { return new Dimension(longueur, hauteur);}
    public Dimension minimumLayoutSize(Container cntnr) { return new Dimension(0, 0);}
    public void addLayoutComponent(String rowColoumn, Component cmpnt) {
        if(rowColoumn == null)
            return;
        String[] s = rowColoumn.split(","); 
        add(cmpnt, Integer.parseInt(s[0]), Integer.parseInt(s[1]));
    }
    
    /**
     * Ajoute l'item dans la liste de chaque comboBox.
     * @param item 
     * L'item ajouté.
     */
    public void addItemInCombo(Object item)
    {
        for(ManageLayout m : components)
            if(m.component instanceof JComboBox)
                ((JComboBox)m.component).addItem(item);
    }
    
    /**
     * Supprime l'item de la liste de chaque combo.<br/>
     * Si l'item est l'élement seléctionné de la combo, celle-ci est suprimée.
     * @param item 
     * L'item a supprimé.
     */
    public void removeItemInCombo(Object item)
    {
        int nb = components.size();
        for(int i = 0; i < nb;)
        {
            if(components.get(i).component instanceof JComboBox)
            {
                JComboBox combo = (JComboBox)components.get(i).component;
                if(combo.getSelectedIndex() > 0 && combo.getSelectedItem().equals(item))
                {
                    combo.setSelectedIndex(0); //Si c'est un scombo, il va se supprimer
                    if(nb  > components.size())
                    {
                        i -= (nb - components.size());
                        nb = components.size();
                    }
                }
                else
                    combo.removeItem(item); 
            }
            i += 1;
        }
    }
        
    /**
     * Met à jour les comboBox est supprimant l'ancien item et en ajoutant le nouvel item. <br/>
     * Si l'ancien item est l'élement seléctionné de la combo, alors celle-ci sélectionne le nouvel item.
     * @param oldItem
     * L'ancien item. Il doit être supprimé.
     * @param newItem
     * Le nouvel item. il doit être ajouté.
     */
    public void updateItemInCombo(Object oldItem, Object newItem)
    {
         for(int i = 0; i < components.size();i++)
            if(components.get(i).component instanceof JComboBox)
            {
                JComboBox combo = (JComboBox)components.get(i).component;
                
                combo.addItem(newItem);
                if(combo.getSelectedIndex() > 0 && combo.getSelectedItem().equals(oldItem)) //Pour éviter de supprimer l'élement sélectionné par le combo
                    combo.setSelectedItem(newItem);
                combo.removeItem(oldItem);
            }
    }
    
    /**
     * Retourne tous les items des comboBox,sur la même ligne que le combo.
     * @param combo
     * Le combo.
     * @return 
     * La liste des items avec aucun double.
     */
    public TreeSet<Comparable> searchItemInCombo(JComboBox combo)
    {
        TreeSet<Comparable> set = new TreeSet<Comparable>();
        int row = this.getRow(combo);
        for(ManageLayout m : components)
            if(m.row == row)
                if(!m.component.equals(combo))
                    if(m.component instanceof JComboBox)
                        if(((JComboBox) m.component).getSelectedIndex() > 0)
                            set.add(((JComboBox)m.component).getSelectedItem().toString());
        return set;
    }
    
    /**
     * Supprime une ligne de composant en fonction du label à la colonne 0.<br/>
     * Si une ligne est en dessous, celle-ci est remonté pour combler le vide.
     * @param label 
     * Le texte du label à la colonne 0.
     */
    public void removeLineComponent(String label){ removeLineComponent(searchRow(label));}
    
    /**
     * Supprime une ligne de composant.<br/>
     * Si une ligne est en dessous, celle-ci est remontée pour combler le vide.
     * @param row 
     * La ligne à suprimer.
     */
    public void removeLineComponent(int row)
    {
        if(row < 0)
            return;
        for(int i = 0; i < components.size();)
        {
            ManageLayout m = components.get(i);
            if(row == m.row)
            {
                m.component.setVisible(false);
                components.remove(m);
            }
            else
            {
                i += 1;
                if(row < m.row)
                    m.row -= 1;
            }
        }
    }
    
    /**
     * Récupère la règle correspondant à la ligne.
     * @param row
     * La ligne.
     * @return 
     * Une règle.
     */
    public Rule getRule(int row)
    {
        Rule r = new Rule();
        boolean isPremise = true;
        for(ManageLayout m : components)
            if(m.row > row)
                break;
            else 
                if(m.row == row)
                    if(m.component instanceof JLabel) //On recontre la flêche donc le prochain composant n'est pas une prémisse.
                        isPremise = false;
                    else
                        if(((JComboBox)m.component).getSelectedIndex() > 0)
                            if(isPremise)
                                r.addToPremise(((JComboBox)m.component).getSelectedItem().toString());
                            else
                                r.addToConclusion(((JComboBox)m.component).getSelectedItem().toString());

                
        return r;
    }
    
    /**
     * Récupère la règle correspondant à la ligne, sans prendre en compte l'attribut du combo.
     * @param row
     * La ligne.
     * @param combo
     * Combo à ommettre.
     * @return 
     * Une règle.
     */
    public Rule getRule(int row, JComboBox combo)
    {
        Rule r = new Rule();
        boolean isPremise = true;
        for(ManageLayout m : components)
            if(m.row > row)
                break;
            else 
                if(m.row == row)
                    if(m.component instanceof JLabel) //On recontre la flêche donc le composant n'est une prémisse.
                        isPremise = false;
                    else
                        if(!m.component.equals(combo))
                            if(((JComboBox)m.component).getSelectedIndex() > 0)
                                if(isPremise)
                                    r.addToPremise(((JComboBox)m.component).getSelectedItem().toString());
                                else
                                    r.addToConclusion(((JComboBox)m.component).getSelectedItem().toString());

                
        return r;
    }
   
    
    /**
     * Met à jour le texte d'un label.
     * @param oldLabel
     * Le texte à remplacer.
     * @param newLabel 
     * Le nouveau texte du label.
     */
    public void updateLabelComponent(String oldLabel, String newLabel)
    {
        int row = searchRow(oldLabel);
        for(ManageLayout m : components)
            if(m.row == row && m.column == 0 && m.component instanceof JLabel)
            {
                ((JLabel)m.component).setText(newLabel);
                return;
            }
    }
    
    /**
     * Descend une ligne vers le bas (+1).<br/>
     * Si une ligne est en dessous, celle-ci est aussi descendue.
     * @param row 
     * La ligne à descendre.
     */
    public void lineDown(int row)
    {
        for(int i = 0; i < components.size();)
        {
            ManageLayout m = components.get(i);
            if(m.row >= row)
               m.row += 1;
            i += 1;
        }
    }
    
    /**
     * Recherche la ligne correspondant au label.
     * @param label
     * Le texte du label recherché.
     * @return 
     * Retourne la ligne correspondant au label.
     * Retourne -1 s'il ne trouve pas le label.
     */
    public int searchRow(String label)
    {
        for(ManageLayout m : components)
            if(m.component instanceof JLabel)
                if(((JLabel) m.component).getText().equals(label))
                    return m.row;
        return -1;
    }
    
    /**
     * Recherche la colonne correspondant au label sur la ligne donnée.
     * @param label
     * Le texte du label recherché.
     * @param row
     * La ligne sur lequel la colonne est recherchée.
     * @return 
     * Retourne la colonne correspondant au label.
     * Retourne -1 s'il ne trouve pas.
     */
    public int searchColumn(String label, int row)
    {
        for(ManageLayout m : components)
            if(m.row == row)
                if(m.component instanceof JLabel)
                    if(((JLabel) m.component).getText().equals(label))
                        return m.column;
        return -1;
    }
    
    /**
     * Recherche le premier label rencontré sur la ligne du combo.
     * @param combo
     * Permet de connaitre la ligne.
     * @return 
     * Le texte du label, ou null si aucun label n'est trouvé.
     */
    public String searchLabel(JComboBox combo)
    {
        int row = getRow(combo);
        for(ManageLayout m : components)
            if(m.row == row)
                if(m.component instanceof JLabel)
                    return ((JLabel)m.component).getText();
        return null;
    }
    
    /**
     * Recupère le texte de chaque composant<br/>
     * Composant pris en compte :<br/> <ul> <li> label </li> <li> Textfield </li> <li> ComboBox </li> </ul> 
     * @return
     * The text of the panel
     */
    public String toString()
    {
        String text = "";
        int row = 0;
        
        for(ManageLayout m : components)
        {
            if(row != m.row)
            {
                row  = m.row;
                text += "\n";
            }
            if(m.component instanceof JLabel)
                text += ((JLabel) m.component).getText();
            else if(m.component instanceof JTextField)
            {
                if(!((JTextField) m.component).getForeground().equals(Color.gray))
                    text += ((JTextField) m.component).getText();
            }
            else if(m.component instanceof JComboBox)
                if(((JComboBox) m.component).getSelectedIndex() > 0) //On n'inclut pas l'élément 0 qui correspond à null
                    text += ((JComboBox) m.component).getSelectedItem().toString();                
            
            text += " ";
        }

        return text;
    }
    
    private class ManageLayout{
        public Component component;
        public int row, column;
        
        /**
         * Permet de gérer un composant avec une ligne et une colonne.
         * @param c
         * Le composant
         * @param row
         * La ligne
         * @param column
         * La colonne
         */
        public ManageLayout(Component c, int row, int column)
        {
            this.component  = c;
            this.row        = row;
            this.column     = column;
        }
    }
}
