/*
 * GraphPanel.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package View;

import com.kitfox.svg.*;
import com.kitfox.svg.animation.AnimationElement;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
/**
 * @author smameri
 * @author Sylvain MORIN
 */
public class GraphPanel extends SVGDisplayPanel
{
    public  SVGDiagram diagram;
    public SVGRoot root;
    int zoom_width, zoom_height;
    
    public GraphPanel()
    {
        this.zoom_width  = 50;
        this.zoom_height = 10;
    }

    public void zoom_in(){  zoom(+1);}
    public void zoom_out(){ zoom(-1);}
  
    public void zoom_auto(int width, int height)
    {
        root = diagram.getRoot();
        try 
        {
            root.setAttribute("width",   AnimationElement.AT_XML, String.valueOf(width ));
            root.setAttribute( "height", AnimationElement.AT_XML, String.valueOf(height));
            root.build();
        }
        catch (SVGException ex) {}
        
        this.setSize(new Dimension(width, height));
        diagram.setDeviceViewport(new Rectangle(width, height));
        repaint();
    }
    
    private void zoom(int coeff)
    {
        Rectangle2D svgRect = diagram.getViewRect();
	int width  = (int)(svgRect.getWidth()  + (coeff * zoom_width ));
	int height = (int)(svgRect.getHeight() + (coeff * zoom_height));
        
        zoom_auto(width, height);
    }
    
    public  void setD(SVGDiagram d){ diagram = d;}
    public  SVGDiagram getD(){ return diagram;}
}