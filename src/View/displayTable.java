/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;



/**
 *
 * @author smameri
 */
public class displayTable extends JInternalFrame
{
    private int FRAME_X = 0;
    private int FRAME_Y = 250;

    private int FRAME_WIDTH = 500;
    private int FRAME_HEIGHT = 290;

    private static JTable tableView;

    private JScrollPane pane;


    private final int INITIAL_ROWHEIGHT = 33;

    /**
     * displayFile constructor
     */
    public displayTable(String file_name)
    {
        this.setClosable(false);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);

        this.tableView = new JTable();
        this.tableView.setShowGrid(true);
        this.tableView.setColumnSelectionAllowed(true);
        this.tableView.setRowSelectionAllowed(true);
        this.tableView.setRowHeight(INITIAL_ROWHEIGHT);
        this.tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        pane = new JScrollPane(this.tableView,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setContentPane(pane);
        
        
        //set this internal frame to be selected
        try
        {
            this.setSelected(true);
        }catch(java.beans.PropertyVetoException e){}
        this.show();
    }

    public JTable getJTable()
    {
        return tableView;
    }
    
    
}
