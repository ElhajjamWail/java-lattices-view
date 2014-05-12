/*
 * ClosureSystemAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */


package Controller;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import lattice.Context;
import dgraph.DGraph;
import lattice.ImplicationalSystem;
import View.GraphPanel;
import View.hmi;


/**
 *
 * @author smameri
 */
public class ClosureSystemAction extends AbstractAction{

    private static hmi window;

    private int numAction;

    /** The dependance graphe of the implicational system **/
    public DGraph dependanceGraph = null;
    /** The representative graphe of the implicational system **/
    public DGraph representativeGraph = null;
    
    /** The precedence graphe of the context **/
    public DGraph precedenceGraph = null;

    /** Output files for the dot and the svg files of the closure system **/
    public static File dot,out,dotDp,outDp,dotPG,outPG,outL,dotL =null;

    //get the absolute path of the project
    public final static String projectPath = hmi.projectPath;
    //location of the temporay file
    public static String tempFile = projectPath+"\\temp\\temp";


    public ClosureSystemAction(hmi window,int num,String s)
    {
        super(s);
        this.window = window;
        numAction = num;

    }

    public void actionPerformed(ActionEvent e)
    {
        int index = OpenFileAction.file_name.lastIndexOf(".");
        String s = OpenFileAction.file_name.substring(0, index);
        int index2 = s.lastIndexOf("\\");
        String s2 = s.substring(index2+1);

        switch(numAction)
        {
            case 1:
                try{
                if(!window.getCheckBoxFrame().conceptLattice.isSelected())
                    window.getCheckBoxFrame().conceptLattice.setSelected(enabled);
                    // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getConceptLiF().setClosable(false);
                window.getCheckBoxFrame().getConceptLiF().setMaximizable(true);
                window.getCheckBoxFrame().getConceptLiF().setIconifiable(true);
                window.getCheckBoxFrame().getConceptLiF().setResizable(true);

                /**
                 * if the checkBox is selected
                 * then it generates the concept lattice when the initial closure system is a contex,
                 * else it generates the closed set lattice when the initial closure system is a implicationnal system
                 * then the ConceptLattice component is stored in a JTextArea
                 */
                    try {
                        window.getCheckBoxFrame().getConceptLiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    if(window.getCheckBoxFrame().getConceptLiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getConceptLiF().getBounds());
                    window.getCheckBoxFrame().getConceptLiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getConceptLiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 350);

                    // we use the closureSystem of the IsOrContext internal frame

                    window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((Context)window.getCheckBoxFrame().getIsOrContextiF().coris).conceptLattice(enabled);

                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setText(window.getCheckBoxFrame().getConceptLiF().conceptLattice.toString());
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice.save(s+"_lattice_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_lattice_Graph.dot");
                    dotL = new File(s+"_lattice_Graph.dot");
                    outL = new File(s+"_lattice_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outL );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_lattice_Graph.svg"),new File(tempFile+"_lattice_Graph.svg"));
                        SaveAction.copy(new File(s+"_lattice_Graph.dot"),new File(tempFile+"_lattice_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_lattice_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Lattice");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getConceptLiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });                    
                    try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list

                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.checkBoxesFrame.getConceptLiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getConceptLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ep){}
                break;
            case 2:
                ((Context)window.checkBoxesFrame.getIsOrContextiF().coris).reverse();
                window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_reverse_Context");;
                window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
                break;
            case 3:
                ((Context)window.checkBoxesFrame.getIsOrContextiF().coris).reduction();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_reduction_Context");;
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
                break;
            case 4:
                ((Context)window.checkBoxesFrame.getIsOrContextiF().coris).attributesReduction();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_attibutes_reduction_Context");;
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
                break;
            case 5:
                ((Context)window.checkBoxesFrame.getIsOrContextiF().coris).observationsReduction();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_observations_reduction_Context");;
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 6:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeProper();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Proper_IS");;
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 7:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeUnary();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Unary_IS");;
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 8:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeCompact();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Compact_IS");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 9:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeRightMaximal();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Right_Maximal_IS");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 10:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeLeftMinimal();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Left_Minimal_IS");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 11:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeDirect();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Direct_IS");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 12:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeMinimum();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Minimum_IS");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 13:
                ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeCanonicalDirectBasis();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_CDB_IS ");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 14:
                  ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).makeCanonicalBasis();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_CB_IS");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 15:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getRepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getRepGiF().setClosable(true);
                window.getCheckBoxFrame().getRepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getRepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getRepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getRepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getRepGiF().getBounds());
                    window.getCheckBoxFrame().getRepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getRepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.representativeGraph = ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).representativeGraph();
                    this.representativeGraph.save(s+"_Representative_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Representative_Graph.dot");
                    dot = new File(s+"_Representative_Graph.dot");
                    out = new File(s+"_Representative_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), out );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Representative_Graph.svg"),new File(tempFile+"_Representative_Graph.svg"));
                        SaveAction.copy(new File(s+"_Representative_Graph.dot"),new File(tempFile+"_Representative_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Representative_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getRepGiF().setTitle(s2+"_Representative_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getRepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getRepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getRepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getRepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}
               break;
           case 16:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getDepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getDepGiF().setClosable(true);
                window.getCheckBoxFrame().getDepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getDepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getDepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getDepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getDepGiF().getBounds());
                    window.getCheckBoxFrame().getDepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getDepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.dependanceGraph = ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).dependencyGraph();
                    this.dependanceGraph.save(s+"_Dependance_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Dependance_Graph.dot");
                    dotDp = new File(s+"_Dependance_Graph.dot");
                    outDp = new File(s+"_Dependance_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outDp );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Dependance_Graph.svg"),new File(tempFile+"_Dependance_Graph.svg"));
                        SaveAction.copy(new File(s+"_Dependance_Graph.dot"),new File(tempFile+"_Dependance_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Dependance_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getDepGiF().setTitle(s2+"_Dependance_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getDepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getDepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getDepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getDepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}

              break;
           case 17:
               ((ImplicationalSystem)window.checkBoxesFrame.getIsOrContextiF().coris).reduction();
               window.checkBoxesFrame.getIsOrContextiF().setTitle(s2+"_Reduction_Implicational_System ");
               window.checkBoxesFrame.getIsOrContextiF().getTextfield().setText(window.checkBoxesFrame.getIsOrContextiF().coris.toString());
               break;
           case 18:
               try{
               if(!window.getCheckBoxFrame().conceptLattice.isSelected())
                    window.getCheckBoxFrame().conceptLattice.setSelected(enabled);
                    // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getConceptLiF().setClosable(false);
                window.getCheckBoxFrame().getConceptLiF().setMaximizable(true);
                window.getCheckBoxFrame().getConceptLiF().setIconifiable(true);
                window.getCheckBoxFrame().getConceptLiF().setResizable(true);

                /**
                 * if the checkBox is selected
                 * then it generates the concept lattice when the initial closure system is a contex,
                 * else it generates the closed set lattice when the initial closure system is a implicationnal system
                 * then the ConceptLattice component is stored in a JTextArea
                 */
                    try {
                        window.getCheckBoxFrame().getConceptLiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    if(window.getCheckBoxFrame().getConceptLiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getConceptLiF().getBounds());
                    window.getCheckBoxFrame().getConceptLiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getConceptLiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 350);

                    // we use the closureSystem of the IsOrContext internal frame
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((ImplicationalSystem)window.getCheckBoxFrame().getIsOrContextiF().coris).closedSetLattice(enabled);

                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setText(window.getCheckBoxFrame().getConceptLiF().conceptLattice.toString());
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice.save(s+"_lattice_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_lattice_Graph.dot");
                    dotL = new File(s+"_lattice_Graph.dot");
                    outL = new File(s+"_lattice_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outL );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_lattice_Graph.svg"),new File(tempFile+"_lattice_Graph.svg"));
                        SaveAction.copy(new File(s+"_lattice_Graph.dot"),new File(tempFile+"_lattice_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_lattice_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Lattice");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getConceptLiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });                    
                    try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list

                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.checkBoxesFrame.getConceptLiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getConceptLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ep){}
                    break;
                case 19:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeProper();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Proper_IS");;
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 20:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeUnary();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Unary_IS");;
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 21:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeCompact();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Compact_IS");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 22:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeRightMaximal();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Right_Maximal_IS");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 23:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeLeftMinimal();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Left_Minimal_IS");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 24:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeDirect();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Direct_IS");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 25:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeMinimum();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Minimum_IS");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 26:
                ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeCanonicalDirectBasis();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_CDB_IS ");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 27:
                  ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).makeCanonicalBasis();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_CB_IS");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 28:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getRepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getRepGiF().setClosable(true);
                window.getCheckBoxFrame().getRepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getRepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getRepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getRepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getRepGiF().getBounds());
                    window.getCheckBoxFrame().getRepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getRepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.representativeGraph = ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).representativeGraph();
                    this.representativeGraph.save(s+"_Representative_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Representative_Graph.dot");
                    dot = new File(s+"_Representative_Graph.dot");
                    out = new File(s+"_Representative_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), out );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Representative_Graph.svg"),new File(tempFile+"_Representative_Graph.svg"));
                        SaveAction.copy(new File(s+"_Representative_Graph.dot"),new File(tempFile+"_Representative_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Representative_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getRepGiF().setTitle(s2+"_Representative_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getRepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getRepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getRepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getRepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}
               break;
           case 29:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getDepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getDepGiF().setClosable(true);
                window.getCheckBoxFrame().getDepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getDepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getDepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getDepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getDepGiF().getBounds());
                    window.getCheckBoxFrame().getDepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getDepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.dependanceGraph = ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).dependencyGraph();
                    this.dependanceGraph.save(s+"_Dependance_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Dependance_Graph.dot");
                    dotDp = new File(s+"_Dependance_Graph.dot");
                    outDp = new File(s+"_Dependance_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outDp );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Dependance_Graph.svg"),new File(tempFile+"_Dependance_Graph.svg"));
                        SaveAction.copy(new File(s+"_Dependance_Graph.dot"),new File(tempFile+"_Dependance_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Dependance_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getDepGiF().setTitle(s2+"_Dependance_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getDepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getDepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getDepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getDepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}

              break;
           case 30:
               ((ImplicationalSystem)window.checkBoxesFrame.getCDBiF().coris).reduction();
               window.checkBoxesFrame.getCDBiF().setTitle(s2+"_Reduction_Implicational_System ");
               window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString());
               break;
           case 31:
               try{
               if(!window.getCheckBoxFrame().conceptLattice.isSelected())
                    window.getCheckBoxFrame().conceptLattice.setSelected(enabled);
                    // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getConceptLiF().setClosable(false);
                window.getCheckBoxFrame().getConceptLiF().setMaximizable(true);
                window.getCheckBoxFrame().getConceptLiF().setIconifiable(true);
                window.getCheckBoxFrame().getConceptLiF().setResizable(true);

                /**
                 * if the checkBox is selected
                 * then it generates the concept lattice when the initial closure system is a contex,
                 * else it generates the closed set lattice when the initial closure system is a implicationnal system
                 * then the ConceptLattice component is stored in a JTextArea
                 */
                    try {
                        window.getCheckBoxFrame().getConceptLiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    if(window.getCheckBoxFrame().getConceptLiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getConceptLiF().getBounds());
                    window.getCheckBoxFrame().getConceptLiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getConceptLiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 350);

                    // we use the closureSystem of the IsOrContext internal frame
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((ImplicationalSystem)window.getCheckBoxFrame().getCDBiF().coris).closedSetLattice(enabled);

                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setText(window.getCheckBoxFrame().getConceptLiF().conceptLattice.toString());
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice.save(s+"_lattice_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_lattice_Graph.dot");
                    dotL = new File(s+"_lattice_Graph.dot");
                    outL = new File(s+"_lattice_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outL );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_lattice_Graph.svg"),new File(tempFile+"_lattice_Graph.svg"));
                        SaveAction.copy(new File(s+"_lattice_Graph.dot"),new File(tempFile+"_lattice_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_lattice_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Lattice");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getConceptLiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });
                    try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list

                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.checkBoxesFrame.getConceptLiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getConceptLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ep){}
                    break;
                case 32:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeProper();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Proper_IS");;
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 33:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeUnary();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Unary_IS");;
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 34:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeCompact();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Compact_IS");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 35:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeRightMaximal();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Right_Maximal_IS");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 36:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeLeftMinimal();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Left_Minimal_IS");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 37:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeDirect();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Direct_IS");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 38:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeMinimum();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Minimum_IS");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 39:
                ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeCanonicalDirectBasis();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_CDB_IS ");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 40:
                  ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeCanonicalBasis();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_CB_IS");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 41:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getRepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getRepGiF().setClosable(true);
                window.getCheckBoxFrame().getRepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getRepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getRepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getRepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getRepGiF().getBounds());
                    window.getCheckBoxFrame().getRepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getRepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.representativeGraph = ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).representativeGraph();
                    this.representativeGraph.save(s+"_Representative_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Representative_Graph.dot");
                    dot = new File(s+"_Representative_Graph.dot");
                    out = new File(s+"_Representative_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), out );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Representative_Graph.svg"),new File(tempFile+"_Representative_Graph.svg"));
                        SaveAction.copy(new File(s+"_Representative_Graph.dot"),new File(tempFile+"_Representative_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Representative_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getRepGiF().setTitle(s2+"_Representative_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getRepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getRepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getRepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getRepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}
               break;
           case 42:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getDepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getDepGiF().setClosable(true);
                window.getCheckBoxFrame().getDepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getDepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getDepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getDepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getDepGiF().getBounds());
                    window.getCheckBoxFrame().getDepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getDepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.dependanceGraph = ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).dependencyGraph();
                    this.dependanceGraph.save(s+"_Dependance_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Dependance_Graph.dot");
                    dotDp = new File(s+"_Dependance_Graph.dot");
                    outDp = new File(s+"_Dependance_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outDp );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Dependance_Graph.svg"),new File(tempFile+"_Dependance_Graph.svg"));
                        SaveAction.copy(new File(s+"_Dependance_Graph.dot"),new File(tempFile+"_Dependance_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Dependance_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getDepGiF().setTitle(s2+"_Dependance_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getDepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getDepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getDepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getDepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}

              break;
           case 43:
               ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).reduction();
               window.checkBoxesFrame.getCBLiF().setTitle(s2+"_Reduction_Implicational_System ");
               window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
               break;
           case 44:
               try{
               if(!window.getCheckBoxFrame().conceptLattice.isSelected())
                    window.getCheckBoxFrame().conceptLattice.setSelected(enabled);
                    // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getConceptLiF().setClosable(true);
                window.getCheckBoxFrame().getConceptLiF().setMaximizable(true);
                window.getCheckBoxFrame().getConceptLiF().setIconifiable(true);
                window.getCheckBoxFrame().getConceptLiF().setResizable(true);

                /**
                 * if the checkBox is selected
                 * then it generates the concept lattice when the initial closure system is a contex,
                 * else it generates the closed set lattice when the initial closure system is a implicationnal system
                 * then the ConceptLattice component is stored in a JTextArea
                 */
                    try {
                        window.getCheckBoxFrame().getConceptLiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    if(window.getCheckBoxFrame().getConceptLiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getConceptLiF().getBounds());
                    window.getCheckBoxFrame().getConceptLiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getConceptLiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 350);

                    // we use the closureSystem of the IsOrContext internal frame
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((ImplicationalSystem)window.getCheckBoxFrame().getCBLiF().coris).closedSetLattice(enabled);

                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setText(window.getCheckBoxFrame().getConceptLiF().conceptLattice.toString());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Concept_Lattice");
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice.save(s+"_lattice_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_lattice_Graph.dot");
                    dotL = new File(s+"_lattice_Graph.dot");
                    outL = new File(s+"_lattice_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outL );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_lattice_Graph.svg"),new File(tempFile+"_lattice_Graph.svg"));
                        SaveAction.copy(new File(s+"_lattice_Graph.dot"),new File(tempFile+"_lattice_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_lattice_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Lattice");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getConceptLiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });
                    try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list

                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.checkBoxesFrame.getConceptLiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getConceptLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ep){}
                    break;
                case 45:
                    try{
                if(!window.getCheckBoxFrame().conceptLattice.isSelected())
                    window.getCheckBoxFrame().conceptLattice.setSelected(enabled);
                    // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getConceptLiF().setClosable(false);
                window.getCheckBoxFrame().getConceptLiF().setMaximizable(true);
                window.getCheckBoxFrame().getConceptLiF().setIconifiable(true);
                window.getCheckBoxFrame().getConceptLiF().setResizable(true);

                /**
                 * if the checkBox is selected
                 * then it generates the concept lattice when the initial closure system is a contex,
                 * else it generates the closed set lattice when the initial closure system is a implicationnal system
                 * then the ConceptLattice component is stored in a JTextArea
                 */
                    try {
                        window.getCheckBoxFrame().getConceptLiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    if(window.getCheckBoxFrame().getConceptLiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getConceptLiF().getBounds());
                    window.getCheckBoxFrame().getConceptLiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getConceptLiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 350);

                    // we use the closureSystem of the IsOrContext internal frame

                    window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((Context)window.getCheckBoxFrame().getRedCiF().coris).conceptLattice(enabled);

                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setText(window.getCheckBoxFrame().getConceptLiF().conceptLattice.toString());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Concept_Lattice");
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice.save(s+"_lattice_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_lattice_Graph.dot");
                    dotL = new File(s+"_lattice_Graph.dot");
                    outL = new File(s+"_lattice_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outL );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_lattice_Graph.svg"),new File(tempFile+"_lattice_Graph.svg"));
                        SaveAction.copy(new File(s+"_lattice_Graph.dot"),new File(tempFile+"_lattice_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_lattice_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Lattice");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getConceptLiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    }); 
                    try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list

                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.checkBoxesFrame.getConceptLiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getConceptLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ep){}
                break;
            case 46:
                ((Context)window.checkBoxesFrame.getRedCiF().coris).reverse();
                window.checkBoxesFrame.getRedCiF().setTitle(s2+"_reverse_Context");;
                window.checkBoxesFrame.getRedCiF().getTextfield().setText(window.checkBoxesFrame.getRedCiF().coris.toString());
                break;
            case 47:
                ((Context)window.checkBoxesFrame.getRedCiF().coris).reduction();
               window.checkBoxesFrame.getRedCiF().setTitle(s2+"_reduction_Context");;
               window.checkBoxesFrame.getRedCiF().getTextfield().setText(window.checkBoxesFrame.getRedCiF().coris.toString());
                break;
            case 48:
                ((Context)window.checkBoxesFrame.getRedCiF().coris).attributesReduction();
               window.checkBoxesFrame.getRedCiF().setTitle(s2+"_attibutes_reduction_Context");;
               window.checkBoxesFrame.getRedCiF().getTextfield().setText(window.checkBoxesFrame.getRedCiF().coris.toString());
                break;
            case 49:
                ((Context)window.checkBoxesFrame.getRedCiF().coris).observationsReduction();
               window.checkBoxesFrame.getRedCiF().setTitle(s2+"_observations_reduction_Context");;
               window.checkBoxesFrame.getRedCiF().getTextfield().setText(window.checkBoxesFrame.getRedCiF().coris.toString());
               break;
            case 50:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getPGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getPGiF().setClosable(true);
                window.getCheckBoxFrame().getPGiF().setMaximizable(true);
                window.getCheckBoxFrame().getPGiF().setIconifiable(true);
                window.getCheckBoxFrame().getPGiF().setResizable(true);

                if(window.getCheckBoxFrame().getPGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getPGiF().getBounds());
                    window.getCheckBoxFrame().getPGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getPGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.precedenceGraph = ((Context)window.checkBoxesFrame.getIsOrContextiF().coris).precedenceGraph();
                    this.precedenceGraph.save(s+"_precedence_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_precedence_Graph.dot");
                    dotPG = new File(s+"_precedence_Graph.dot");
                    outPG = new File(s+"_precedence_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outPG );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_precedence_Graph.svg"),new File(tempFile+"_precedence_Graph.svg"));
                        SaveAction.copy(new File(s+"_precedence_Graph.dot"),new File(tempFile+"_precedence_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_precedence_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getPGiF().setTitle(s2+"_Precedence_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getPGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getPGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getPGiF(),new Integer(2));
                    window.getCheckBoxFrame().getPGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}
               break;
            case 51:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getPGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getPGiF().setClosable(true);
                window.getCheckBoxFrame().getPGiF().setMaximizable(true);
                window.getCheckBoxFrame().getPGiF().setIconifiable(true);
                window.getCheckBoxFrame().getPGiF().setResizable(true);

                if(window.getCheckBoxFrame().getPGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getPGiF().getBounds());
                    window.getCheckBoxFrame().getPGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getPGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);

                    // create the dependance graph from the initial implicational system and its dot file
                    this.precedenceGraph = ((Context)window.checkBoxesFrame.getRedCiF().coris).precedenceGraph();
                    this.precedenceGraph.save(s+"_precedence_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_precedence_Graph.dot");
                    dotPG = new File(s+"_precedence_Graph.dot");
                    outPG = new File(s+"_precedence_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outPG );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_precedence_Graph.svg"),new File(tempFile+"_precedence_Graph.svg"));
                        SaveAction.copy(new File(s+"_precedence_Graph.dot"),new File(tempFile+"_precedence_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_precedence_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getPGiF().setTitle(s2+"_Precedence_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getPGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getPGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getPGiF(),new Integer(2));
                    window.getCheckBoxFrame().getPGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}
               break;
        }
    }
}
