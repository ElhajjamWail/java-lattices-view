/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lattice.Context;

/**
 *
 * @author Sylvain MORIN
 */
public class ContextInterface extends JPanel{
   
    public static String TWO_POINTS = " :";
    public static String TWO_POINT = ":";
    public JTextField  txtObservation  , txtAttribute;
    public SLayout layout;
    private Context context;
    public ContextController controller;
    
    public ContextInterface() {
        layout  = new SLayout(this);
        this.setLayout(layout);
        this.setBackground(Color.white);
        
        initComponent();
                
        context = new Context();
        setText();
        controller = new ContextController(this);
       /* this.setText("observations : chien chat\nAttributes : moustache poilSoyeux poilTresTresSoyeux oeil\nchat : moustache\nchien :");*/
    }
    /**
     * Retourne le contexte.
     * @return 
     * Le contexte.
     */
    public Context getContext(){ return context;}
    
    /**
     * Modifie le contexte et met à jour la vue.
     * @param newContext 
     * Le nouveau contexte.
     */
    public void setContext(Context newContext){ context = newContext; setText();}
    
    /**
     * Met à jour la vue en fonction du context.
     */
    private void setText()
    {
        removeAll();
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Ligne des observations
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        for (Comparable o : context.getObservations())
            add(new STextFieldC(this, o.toString(), false), 0, 0);
        
        add(txtObservation, 0 , 0);
        txtObservation.setVisible(true);
        add(new JLabel("Observations" + TWO_POINTS), 0 , 0);
 
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Ligne des attributs
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        for (Comparable a : context.getAttributes())
            add(new  STextFieldC(this, a.toString(), true), 1 , 0);
        
        add(txtAttribute, 1 , 0);
        txtAttribute.setVisible(true);
        add(new JLabel("Attributes     " + TWO_POINTS), 1 , 0);
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //les autres lignes
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        int i = 2;
        for (Comparable o : context.getObservations())
            addRow(i++, o);	
    }
    
    /**
     * Ajoute une ligne avec une observation et ses attributs, du type : "a : b c d"
     * @param row
     * La ligne de la ligne.
     * @param o 
     * L'observation.
     */
    public void addRow(final int row, final Comparable o)
    {
        for (Comparable a : context.getIntent(o))
            add(new SComboBox(context.getAttributes(), true, this, a.toString()), row, 0);
            
        final SComboBox combo = new SComboBox(context.getAttributes(), true, this, o);        
        add(combo, row, 0);
        add(new JLabel(o + TWO_POINTS), row, 0);
    }
    
    
    /**
     * Recommence toute la vue en fonction du texte.
     * @param text 
     * Il faut que ca soit un context.toString().
     * @deprecated 
     */
    private void setText(String text)
    {
        removeAll();

        String[] lines = text.split("\n");
        
        try{
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Ligne des observations
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            String[] observation = lines[0].split(TWO_POINT);
            String[] observations = observation[1].split(" ");

            for(int i = observations.length -1; i > 0; i--)
            {
                String s = observations[i];
                context.addToObservations(s);
                add(new STextFieldC(this, s, false), 0, 0);
            }

            add(txtObservation, 0 , 0);
            txtObservation.setVisible(true);
            add(new JLabel(observation[0] + TWO_POINTS), 0 , 0);


            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Ligne des attributs
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            String[] attributes = lines[1].split(TWO_POINT);

            String[] attribute = attributes[1].split(" ");
            for(int i = attribute.length -1; i > 0; i--)
            {
                String s = attribute[i];
                context.addToAttributes(s);
                add(new  STextFieldC(this, s, true), 1 , 0);
            }

            add(txtAttribute, 1 , 0);
            txtAttribute.setVisible(true);
            add(new JLabel(attributes[0] + "     " +TWO_POINTS), 1 , 0);


            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //les autres lignes
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            for(int line = 2; line < lines.length; line++)
                addRowed(line, lines[line]);
        }
        catch(Exception e) { hmi.Toast(e.toString()); }
    }
    
    /**
     * Ajoute une ligne du type : "chat : moustache poilSoyeux"<br>
     * L'observation et les deux points sont obligatoires, pas les attributs
     * @param row
     * La ligne de la ligne
     * @param line 
     * La ligne à ajoutée
     * @deprecated 
     */
    private void addRowed(final int row, String line)
    {
        final String[] observation = line.split(TWO_POINTS);
        
        if(observation.length > 1)
        {
            String[] attributes = observation[1].split(" ");

            if(attributes.length > 0)
            {
                for(int i = attributes.length -1; i > 0; i--)
                {
                    final SComboBox sCombo = new SComboBox(context.getAttributes(), true, this, attributes[i]);
                    add(sCombo, row, 0);
                    context.addExtentIntent(observation[0], attributes[i]);
                }
            }
        }
        
        final SComboBox combo = new SComboBox(context.getAttributes(), true, this, observation[0]);
        add(combo, row, 0);

        add(new JLabel(observation[0]+ TWO_POINTS), row, 0);
    }
    
    private void initComponent()
    {
        txtObservation   = new HintTextField("New");
        txtAttribute     = new HintTextField("New");
        
        txtObservation.setToolTipText("New observation");
        txtAttribute.setToolTipText("New attribute");
        
        txtObservation.addActionListener(new AddObservationCAction(this));
        txtAttribute.addActionListener(  new AddAttributeCAction(  this));
    }
    
    public void add(Component c, int row, int column){ add(c); layout.add(c, row, column);}    
    public void add(Component newC, Component c){ add(newC); layout.add(newC, c);}
}
