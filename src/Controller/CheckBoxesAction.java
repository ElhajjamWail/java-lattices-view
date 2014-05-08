
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
import lattice.ImplicationalSystem;
import View.GraphPanel;
import View.hmi;

/**
 *
 * @author smameri
 */
public class CheckBoxesAction extends AbstractAction
{
    /** The closed set lattice of the closure system when closure system is an implicational system **/
    /** The concept lattice of the closure system when closure system is a context **/

    private hmi window;
    
    private File outL;

    int checkBox; // integer to figure out which checkBox is triggered

    //get the absolute path of the project
    public static final String projectPath = hmi.projectPath;
    //location of the temporay file
    public static final String tempFile = projectPath+"\\temp\\temp";


    /**
     * The Constructor of the CheckBoxesAction class
     * @param window
     * @param cb
     */
    public CheckBoxesAction(hmi window,int cb)
    {

        this.window = window;
        checkBox = cb;
    }


    /**
     * Performed action when the abstract action is triggered
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        /** get the OpenFileAction static variabl file_name **/
        int index = OpenFileAction.file_name.lastIndexOf(".");
        String s = OpenFileAction.file_name.substring(0, index);
        int index2 = s.lastIndexOf("\\");
        String s2 = s.substring(index2+1);

        /** switch used to determin the selected checkBox **/
        switch(checkBox)
        {
            case 0:
                // the parameters of the internal frame for the initial closure system
                window.getCheckBoxFrame().getIsOrContextiF().setClosable(false);
                window.getCheckBoxFrame().getIsOrContextiF().setMaximizable(true);
                window.getCheckBoxFrame().getIsOrContextiF().setIconifiable(true);
                window.getCheckBoxFrame().getIsOrContextiF().setResizable(true);

                /**
                 * if the checkBox is selected
                 * then it generates display the initial closure system
                 */
                if(window.getCheckBoxFrame().isOrContext.isSelected())
                {
                    try {
                        window.getCheckBoxFrame().getIsOrContextiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    //calculate and set the position of the internal frame
                    if(window.getCheckBoxFrame().getIsOrContextiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getIsOrContextiF().getBounds());
                    window.getCheckBoxFrame().getIsOrContextiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getIsOrContextiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);
                    
                    // if initial closure system is context or implicationnal system then initialise
                    if(OpenFileAction.isContext())
                    {
                         window.getCheckBoxFrame().getIsOrContextiF().coris = window.getContextInterface().getContext();
                         window.getCheckBoxFrame().getIsOrContextiF().setTitle(s2+"_Context");
                         window.getCheckBoxFrame().getIsOrContextiF().buildMenuBarContext();
                    }
                    if(OpenFileAction.isIS())
                    {
                         window.getCheckBoxFrame().getIsOrContextiF().coris = window.getISInterface().getIS();
                         window.getCheckBoxFrame().getIsOrContextiF().setTitle(s2+"_Implicational_System");
                         window.getCheckBoxFrame().getIsOrContextiF().buildMenuBarIS();
                    }

                    window.getCheckBoxFrame().getIsOrContextiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getIsOrContextiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getIsOrContextiF().getTextfield().setText(window.getCheckBoxFrame().getIsOrContextiF().coris.toString());
                    window.getCheckBoxFrame().getIsOrContextiF().setContentPane(new JScrollPane(window.getCheckBoxFrame().getIsOrContextiF().getTextfield()));
                    try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getIsOrContextiF()); // we remove the frame from the container's list

                    }catch(Exception ex){}
                    
                    hmi.desktopPane.add(window.checkBoxesFrame.getIsOrContextiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getIsOrContextiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windowCount is incremented to set the position of the next JInternalFrame to be displayed
                }
                // when the checkBox is unchecked the frame is set to visible(false)
                else if(!window.getCheckBoxFrame().isOrContext.isSelected())
                {
                    window.checkBoxesFrame.getIsOrContextiF().dispose(); // frame closed and set visible to false
                    window.checkBoxesFrame.getIsOrContextiF().setVisible(false);
                    hmi.desktopPane.remove(window.checkBoxesFrame.getIsOrContextiF()); // we remove the frame from the container's list
                    window.getCheckBoxFrame().windowCount--;

                }
                break;
            case 1:
                try{
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
                if(window.getCheckBoxFrame().conceptLattice.isSelected())
                {
                    
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

                    //if the isOrContext checkBox isn't selected, then build the concept lattice using the opened File closureSystem
                    //else we use the closureSystem of the IsOrContext internal frame
                    if(!window.getCheckBoxFrame().isOrContext.isSelected())
                    {
                        if(OpenFileAction.isIS())
                            window.getCheckBoxFrame().getConceptLiF().conceptLattice = window.getISInterface().getIS().closedSetLattice(enabled);
                        else
                            window.getCheckBoxFrame().getConceptLiF().conceptLattice = window.getContextInterface().getContext().conceptLattice(enabled);
                    }
                    else
                    {
                        if(window.getCheckBoxFrame().getIsOrContextiF().coris instanceof ImplicationalSystem)
                            window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((ImplicationalSystem)window.getCheckBoxFrame().getIsOrContextiF().coris).closedSetLattice(enabled);
                        else
                            window.getCheckBoxFrame().getConceptLiF().conceptLattice = ((Context)window.getCheckBoxFrame().getIsOrContextiF().coris).conceptLattice(enabled);

                    }

                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setBackground(Color.white);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setEditable(false);
                    window.getCheckBoxFrame().getConceptLiF().getTextfield().setText(window.getCheckBoxFrame().getConceptLiF().conceptLattice.toString());
                    window.getCheckBoxFrame().getConceptLiF().setTitle(s2+"_Concept_Lattice");
                    window.getCheckBoxFrame().getConceptLiF().conceptLattice.save(s+"_lattice_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_lattice_Graph.dot");
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
                    });                     try
                    {
                        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list
                        
                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.checkBoxesFrame.getConceptLiF(),new Integer(2)); // adding the frame to the desktop and displaying it
                    window.getCheckBoxFrame().getConceptLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                }
                // when the checkBox is unchecked the frame is set to visible(false)
                else if(!window.getCheckBoxFrame().conceptLattice.isSelected())
                {
                    window.checkBoxesFrame.getConceptLiF().dispose(); // frame closed and set visible to false
                    window.checkBoxesFrame.getConceptLiF().setVisible(false);
                    hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list
                    window.getCheckBoxFrame().windowCount--;
                    
                }
                }catch(IOException ep){}
                break;
            case 4:
                if(window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
                {
                    try {
                        window.getCheckBoxFrame().getCDBiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    // the parameters of the internal frame for concept lattice
                    window.getCheckBoxFrame().getCDBiF().setClosable(false);
                    window.getCheckBoxFrame().getCDBiF().setMaximizable(true);
                    window.getCheckBoxFrame().getCDBiF().setIconifiable(true);
                    window.getCheckBoxFrame().getCDBiF().setResizable(true);
                    if(window.getCheckBoxFrame().getCDBiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getCDBiF().getBounds());
                    window.getCheckBoxFrame().getCDBiF().setBounds(r);
                    }
                    else //calculate and set the position of the internal frame
                        window.getCheckBoxFrame().getCDBiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);

                      // if initial closure system is context or implicationnal system then initialise
                        if(OpenFileAction.isContext())
                            window.checkBoxesFrame.getCDBiF().coris = window.getContextInterface().getContext().conceptLattice(enabled).getCanonicalDirectBasis();
                        if(OpenFileAction.isIS())
                            window.checkBoxesFrame.getCDBiF().coris = window.getISInterface().getIS().closedSetLattice(enabled).getCanonicalDirectBasis();
                        
                        window.getCheckBoxFrame().getCDBiF().buildMenuBarCDB();

                    window.checkBoxesFrame.getCDBiF().getTextfield().setBackground(Color.white);
                    window.checkBoxesFrame.getCDBiF().getTextfield().setEditable(false);
                    window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString()); //create the direct canonical basis of the concept lattice
                    window.getCheckBoxFrame().getCDBiF().setTitle(s2+"_Canonical_Direct_Basis");
                    window.getCheckBoxFrame().getCDBiF().setContentPane(new JScrollPane(window.checkBoxesFrame.getCDBiF().getTextfield()));
                    try{ hmi.desktopPane.remove(window.checkBoxesFrame.getCDBiF()); // we remove the frame from the container's list if component exist
                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.getCheckBoxFrame().getCDBiF(),new Integer(2));
                    window.getCheckBoxFrame().getCDBiF().setVisible(true);
                     window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                }
                else if(!window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
                {
                    window.getCheckBoxFrame().getCDBiF().setVisible(false); // frame closed and set visible to false
                    window.getCheckBoxFrame().getCDBiF().dispose();
                    hmi.desktopPane.remove(window.checkBoxesFrame.getCDBiF()); // we remove the frame from the container's list
                    window.getCheckBoxFrame().windowCount--;
                }
                break;
            case 6:
                if(window.getCheckBoxFrame().canonicalBasis.isSelected())
                {
                    try {
                        window.getCheckBoxFrame().getCBLiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    // the parameters of the internal frame for concept lattice
                    window.getCheckBoxFrame().getCBLiF().setClosable(false);
                    window.getCheckBoxFrame().getCBLiF().setMaximizable(true);
                    window.getCheckBoxFrame().getCBLiF().setIconifiable(true);
                    window.getCheckBoxFrame().getCBLiF().setResizable(true);
                    if(window.getCheckBoxFrame().getCBLiF().isShowing())
                        {
                        Rectangle r = new Rectangle(window.getCheckBoxFrame().getCBLiF().getBounds());
                        window.getCheckBoxFrame().getCBLiF().setBounds(r);
                        }
                        else //calculate and set the position of the internal frame
                            window.getCheckBoxFrame().getCBLiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);
                     // if initial closure system is context or implicationnal system then initialise
                

                    if(OpenFileAction.isContext())
                        window.checkBoxesFrame.getCDBiF().coris = window.getContextInterface().getContext().conceptLattice(enabled).getCanonicalDirectBasis();
                    if(OpenFileAction.isIS())
                        window.checkBoxesFrame.getCDBiF().coris = window.getISInterface().getIS().closedSetLattice(enabled).getCanonicalDirectBasis();
                    
                    ((ImplicationalSystem)window.checkBoxesFrame.getCBLiF().coris).makeCanonicalBasis();
                    window.getCheckBoxFrame().getCBLiF().buildMenuBarCB();
                    

                    window.checkBoxesFrame.getCBLiF().getTextfield().setBackground(Color.white);
                    window.checkBoxesFrame.getCBLiF().getTextfield().setEditable(false);
                    window.checkBoxesFrame.getCBLiF().getTextfield().setText(window.checkBoxesFrame.getCBLiF().coris.toString());
                    window.getCheckBoxFrame().getCBLiF().setTitle(s2+"_Canonical_Basis");
                    window.getCheckBoxFrame().getCBLiF().setContentPane(new JScrollPane(window.checkBoxesFrame.getCBLiF().getTextfield()));
                    try{ hmi.desktopPane.remove(window.checkBoxesFrame.getCBLiF()); // we remove the frame from the container's list if component exist
                        }catch(Exception ex){}
                    hmi.desktopPane.add(window.getCheckBoxFrame().getCBLiF(),new Integer(2));
                    window.getCheckBoxFrame().getCBLiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                }
                else if(!window.getCheckBoxFrame().canonicalBasis.isSelected())
                {
                    window.getCheckBoxFrame().getCBLiF().setVisible(false); // frame closed and set visible to false
                    window.getCheckBoxFrame().getCBLiF().dispose();
                    hmi.desktopPane.remove(window.checkBoxesFrame.getCBLiF()); // we remove the frame from the container's list
                    window.getCheckBoxFrame().windowCount--;
                }
                break;           
        }
    }
}
