/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author Sylvain MORIN
 */
public class HintTextField extends JTextField implements FocusListener {

    private final String hint;

    /**
     * Un JtextField avec une description.
     */
    public HintTextField(final String hint)
    {
        super.setForeground(Color.gray);
        super.setText(hint);
        this.hint = hint;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        if(this.getForeground().equals(Color.gray))
        {
            super.setForeground(Color.BLACK);
            super.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e)
    {
        if(this.getText().isEmpty())
        {
            super.setForeground(Color.gray);
            super.setText(hint);
        }
    }
}
