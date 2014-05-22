/*
 * IsInterface.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package View;

import Controller.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.StringTokenizer;
import java.util.TreeSet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lattice.ImplicationalSystem;
import lattice.Rule;

/**
 *
 * @author Sylvain MORIN
 */
public class ISInterface extends JPanel{

    public enum actionCombo { conclusionEmpty, premiseEmpty, conclusion, premise};
    public HintTextField txtAttribute;
    public ISController controller;
    private String ARROW  = " -> ";
    private String ARROWS = "->"; 
    public SLayout layout;
    private ImplicationalSystem is;
        
    public ISInterface()
    {
        controller = new ISController(this);
        this.setBackground(Color.white);
        layout = new SLayout(this);
        this.setLayout(layout);
        is = new ImplicationalSystem();
        
        txtAttribute = new HintTextField("New");
        txtAttribute.addActionListener(new AddAttributeISAction(this));
        
        setText();
        //setText("a b c d e f g\na b -> c d\nd a -> f g b\nd e f -> a");
    }
    
    /**
     * Return the IS.
     * @return 
     * The IS.
     */
    public ImplicationalSystem getIS(){ return is;}
    
    /**
     * Edit the IS.
     * @param newIS 
     * the new IS.
     * @param closed
     * if closedSetLattice.
     */
    public void setIS(ImplicationalSystem newIS, boolean closed)
    {
        if(closed)
            is  = newIS.closedSetLattice(true).getCanonicalDirectBasis();
        else
            is = newIS;
        setText();
    }
    
    /**
     * Agency the IS with the components.
     */
    private void setText()
    {
        removeAll();
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Line of attributes
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        for(Comparable c : is.getSet())
            add(new STextFieldIS(this, c.toString()), 0, 0);

        
        add(txtAttribute, 0, 0);     
        txtAttribute.setVisible(true);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Line of the rules
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        int i = 1;
        for (Rule r : is.getRules())
            addRule(i++, r); // the i increment after adding a rule.

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Empty line
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
        lineEmpty(i, actionCombo.conclusionEmpty);
        addArrow( i);
        lineEmpty(i, actionCombo.premiseEmpty);
    }
    
    /**
     * Add an element "->" like the first element to the desired line . 
     * @param row 
     * The line where the element is placed.
     */
    private void addArrow(int row)
    {
        JLabel lbl = new JLabel(ARROWS);
        lbl.setForeground(new Color(0, 0, 0, 50));
        lbl.setFont(new Font("Calibri" , Font.BOLD, 22));
        add(lbl, row, 0);
    }
    
    /**
     * Add a ComboBox to add a new line. It can be put in premise or in conclusionin function of <b>action</b>.
     * @param row
     * The line should be insert.
     * @param action 
     * To choose if it's premise or a conclusion which its insert.
     */
    private void lineEmpty(int row, actionCombo action)
    {
        final SComboBox combo = new SComboBox(is.getSet(), true, this);
        combo.addPopupMenuListener(new ComboPopupISAction(this, action, combo));
        add(combo, row, 0);
    }
    
     /**
     * Add a rule to a given line.
     * @param row
     * The location of the rule in the layout.
     * @param r 
     * The rule.
     */
    public void addRule(int row, Rule r)
    {
        //The Conclusions
        linePorC(row, r.getConclusion(), false);

        addArrow(row);
                
        //The premises
        linePorC(row, r.getPremise(), true);
    }
    
    /**
     * Adds the premises or the conclusions.
     * @param row 
     * The position in the layout.
     * @param set
     * The list of attributes.
     * @param isPremise
     * If it's in conclusion or in premise which its will be added the next attributes on this rule.
     */
    private void linePorC(int row, TreeSet<Comparable> set, boolean isPremise)
    {
        for (Object e : set)
        {
            StringTokenizer st = new StringTokenizer(e.toString());
            while(st.hasMoreTokens())
            {           
                String s  = st.nextToken();
                add(new SComboBox(is.getSet(), this, isPremise, s), row, 0);
            }
        } 

        final SComboBox combo = new SComboBox(is.getSet(), true, this);
        combo.addPopupMenuListener(new ComboPopupISAction(this, (isPremise ? actionCombo.premise : actionCombo.conclusion), combo));
        add(combo, row, 0);
    }
    
    /** @deprecated */
    private void setText(String text)
    {
        removeAll();
        //is = new IS();
        if(text.equals("\n") || text.isEmpty())
            text = " \n";
        String[] lines = text.split("\n");
        try{
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Line of attributes
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            String[] attributes = lines[0].split(" ");

            for(int i = attributes.length -1; i >= 0; i--)
            {
                String s = attributes[i];
                is.addElement(s);
                add(new STextFieldIS(this, s), 0, 0);
            }
            add(txtAttribute, 0, 0);     
            txtAttribute.setVisible(true);

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Lines of rules
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            for(int line = 1; line < lines.length; line++)
                addLine(line, lines[line]);

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Empty line
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            lineEmpty(lines.length, actionCombo.conclusionEmpty);
            addArrow(lines.length);
            lineEmpty(lines.length, actionCombo.premiseEmpty);
                
        }
        catch(Exception e) {hmi.Toast("is interface :" +e.toString()); }
    }
    
    /**
     * Adds an other line in addition to the lines of attributes.
     * @param row
     * The location of the line in the layout.
     * @param line 
     * This line must be in the form : "a b -> c d e".
     * @deprecated 
     */
    private void addLine(int row, String line)
    {
        String[] rules = line.split(ARROW);
        Rule r = new Rule();

        if(rules.length > 1)
           LinePorC(rules[1], row, r, false);
        else
            LinePorC(" ", row, r, false);

        addArrow(row);

        if(rules[0].isEmpty())
            LinePorC(" ", row, r, true);
        else
            LinePorC(rules[0], row, r, true);
        
        is.addRule(r);
    }
    
    /**
     * Adds a piece of the line in premise or in conclusion.
     * @param attributes
     * The piece of the line added, must be of the form : "a b c".
     * @param row 
     * The position in the layout.
     * @param rule
     * The rule in which are added the attributes.
     * @param isPremise
     * If it is in conclusion or premise that are added attributes.
     * @deprecated 
     */
    private void LinePorC(String attributes, final int row, Rule rule, boolean isPremise)
    {
        String[] attribute = attributes.split(" ");
        for(int i = attribute.length -1; i >= 0; i--)
        {
            String s = attribute[i];            
            add(new SComboBox(is.getSet(), this, isPremise, s), row, 0);

            if(isPremise)
                rule.addToPremise(s);
            else
                rule.addToConclusion(s);
        }   

        final SComboBox combo = new SComboBox(is.getSet(), true, this);
        combo.addPopupMenuListener(new ComboPopupISAction(this, (isPremise ? actionCombo.premise : actionCombo.conclusion), combo));
        add(combo, row, 0);
    }
    
    public void add(Component c, int row, int column){ add(c); layout.add(c, row, column);} 
    public void add(Component newC, Component c){ add(newC); layout.add(newC, c);} 
}
