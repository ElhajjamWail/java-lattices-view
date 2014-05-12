/*
 * STextFieldC.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import View.ContextInterface;
import static View.ContextInterface.TWO_POINTS;
import View.STextField;
import java.util.TreeSet;

/**
 *
 * @author Sylvain MORIN
 */
   public class STextFieldC extends STextField{

        private boolean isAttribute;
        private ContextInterface face;
        
        /**
         * Ajoute, permet de Modifier et de supprimer un attribut/observation pour la vue ContextInferface. Voir description de STextField.
         * @param face
         * La vue.
         * @param text 
         * Le texte de l'attribut à sa création.
         * @param isAttribute
         * true si c'est un attribut, false si c'est une observation.
         */
        public STextFieldC(ContextInterface face, String text, boolean isAttribute)
        {
            super(text);
            this.face = face;
            this.isAttribute = isAttribute;
        }
        
        @Override
        public void itemValidate()
        {
            String text = this.getText().replaceAll(" ", "");
            
            if(!text.equals(getOldName()))
                if(isAttribute)
                    validateA(text);
                else
                    validateO(text);
            else
                this.setText(getOldName());
            editFocus(false);
        }

        @Override
        public void itemDelete()
        {
            if(isAttribute)
                deleteA();
            else
                deleteO();
            
            face.remove(this);
        }

        private void validateO(String observation)
        {
            TreeSet<Comparable> set = face.getContext().getIntent(getOldName());
            face.getContext().removeFromObservations(getOldName());
            
            if(observation.isEmpty())
            {
                face.layout.removeLineComponent(getOldName() + TWO_POINTS);
                face.remove(this);
            }
            else
            {
                face.getContext().addToObservations(observation);
                for(Comparable c : set)
                    face.getContext().addExtentIntent(observation, c);
                
                face.layout.updateLabelComponent(getOldName() + TWO_POINTS, observation + TWO_POINTS);
                this.setText(observation);
            }
        }
        
        private void validateA(String attribute)
        {
            TreeSet<Comparable> set = face.getContext().getExtent(getOldName());
            face.getContext().removeFromAttributes(getOldName());
            
            if(attribute.isEmpty())
            {
                face.layout.removeItemInCombo(getOldName());
                face.remove(this);
            }
            else
            {
                face.getContext().addToAttributes(attribute);
                for(Comparable c : set)
                    face.getContext().addExtentIntent(c, attribute);
                
                face.layout.updateItemInCombo(getOldName(), attribute);
                this.setText(attribute);
            }
        }
        
        private void deleteO()
        {
            face.getContext().removeFromObservations(this.getText());
            face.layout.removeLineComponent(this.getText() + TWO_POINTS);
        }
        
        private void deleteA()
        {
            face.getContext().removeFromAttributes(this.getText().toString());
            face.layout.removeItemInCombo(this.getText().toString());
        }   
    }
