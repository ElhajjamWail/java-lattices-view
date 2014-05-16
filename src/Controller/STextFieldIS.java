/*
 * STextFieldIS.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import View.ISInterface;
import View.STextField;

/**
 *
 * @author Sylvain MORIN
 */
public class STextFieldIS extends STextField{

    ISInterface face;
    /**
     * Add, it allows us to edit and delete an attribute/observation for the view ContextInterface.for further information go to STextField desc.
     * @param text 
     * The text of the attribrute when we creat it.
     */
    public STextFieldIS(ISInterface face, String text){ 
        super(text);
        this.face = face;}

    @Override
    public void itemValidate()
    {
        String attribute = this.getText().replaceAll(" ", "");
        if(!attribute.equals(getOldName()))
        {   
            if(attribute.isEmpty()) //If the attribut is empty, we delete, else we update
            {
                face.getIS().deleteElement(getOldName());
                face.layout.removeItemInCombo(getOldName());
                face.remove(this);
            }
            else
            {
//                face.getIS().updateElement(getOldName(), attribute);
                face.layout.updateItemInCombo(getOldName(), attribute);
                this.setText(attribute);
            }
        }
        else
            this.setText(getOldName());//if the spaces exist in the attribute, it removes.
        editFocus(false);
    }

    @Override
    public void itemDelete()
    {
        face.getIS().deleteElement(this.getText().toString());
        face.layout.removeItemInCombo(this.getText().toString());
        face.remove(this);
    }
}