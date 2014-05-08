/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * Retourne le IS.
     * @return 
     * Le IS.
     */
    public ImplicationalSystem getIS(){ return is;}
    
    /**
     * Modifie le IS.
     * @param newIS 
     * Le nouveau IS.
     * @param closed
     * Si closedSetLattice.
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
     * Agence L'IS avec des composants.
     */
    private void setText()
    {
        removeAll();
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Ligne des attributs
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        for(Comparable c : is.getSet())
            add(new STextFieldIS(this, c.toString()), 0, 0);

        
        add(txtAttribute, 0, 0);     
        txtAttribute.setVisible(true);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Lignes des règles
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        int i = 1;
        for (Rule r : is.getRules())
            addRule(i++, r); // Le i s'incrémente après l'ajout de la règle.

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Ligne vide
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
        lineEmpty(i, actionCombo.conclusionEmpty);
        addArrow( i);
        lineEmpty(i, actionCombo.premiseEmpty);
    }
    
    /**
     * Ajoute l'élément "->" comme premier élément à la ligne voulue. 
     * @param row 
     * La ligne où l'élément est placé.
     */
    private void addArrow(int row)
    {
        JLabel lbl = new JLabel(ARROWS);
        lbl.setForeground(new Color(0, 0, 0, 50));
        lbl.setFont(new Font("Calibri" , Font.BOLD, 22));
        add(lbl, row, 0);
    }
    
    /**
     * Ajoute un ComboBox permettant d'ajouter une nouvelle ligne. Il peut être mis soit en prémisse ou soit en conclusion en fonction de <b>action</b>.
     * @param row
     * La ligne où elle doit être insérer.
     * @param action 
     * Permet de choisir si c'est en premisse ou en conclusion qu'il est insérer.
     */
    private void lineEmpty(int row, actionCombo action)
    {
        final SComboBox combo = new SComboBox(is.getSet(), true, this);
        combo.addPopupMenuListener(new ComboPopupISAction(this, action, combo));
        add(combo, row, 0);
    }
    
     /**
     * Ajoute une règle à la ligne donnée.
     * @param row
     * L'emplacement de la règle dans le layout.
     * @param r 
     * Une règle.
     */
    public void addRule(int row, Rule r)
    {
        //Les Conclusions
        linePorC(row, r.getConclusion(), false);

        addArrow(row);
                
        //Les premisses
        linePorC(row, r.getPremise(), true);
    }
    
    /**
     * Ajoute soit des premisses soit des conclusions.
     * @param row 
     * La position dans le layout.
     * @param set
     * La liste des attributs.
     * @param isPremise
     * Si c'est en conclusion ou premisse que seront ajouté les prochains attributs sur cette règle.
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
            //Ligne des attributs
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
            //Lignes des règles
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            for(int line = 1; line < lines.length; line++)
                addLine(line, lines[line]);

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Ligne vide
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            lineEmpty(lines.length, actionCombo.conclusionEmpty);
            addArrow(lines.length);
            lineEmpty(lines.length, actionCombo.premiseEmpty);
                
        }
        catch(Exception e) {hmi.Toast("is interface :" +e.toString()); }
    }
    
    /**
     * Ajoute une ligne autre que la ligne des attributs.
     * @param row
     * L'emplacement de la ligne dans le layout.
     * @param line 
     * Cette ligne doit être de la forme : "a b -> c d e".
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
     * Ajoute un bout de ligne soit en premisse soit en conclusion.
     * @param attributes
     * Le bout de ligne a ajouté, doit être de la forme : "a b c".
     * @param row 
     * La position dans le layout.
     * @param rule
     * La règle dans laquelle sont ajouté les attributs.
     * @param isPremise
     * Si c'est en conclusion ou premisse que sont ajouté les attributs.
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
