/*
 * OptionsAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import View.Options;
import View.hmi;
import View.hmi;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author JunXcola
 */
public class OptionsAction extends AbstractAction
{
    public static hmi window;
    private Options OpWindow;
    
    
    public OptionsAction(hmi window, String text)
    {
        super(text);

        OptionsAction.window = window;
    }

    public void actionPerformed(ActionEvent ae) 
    {
        OpWindow = new Options(OptionsAction.window);
        OpWindow.setVisible(true);
    }
    
}
