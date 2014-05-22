/*
 * HintTextField.java
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
     * JtextField with a description.
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
