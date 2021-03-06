package vk.view;
import java.awt.*;

import javax.swing.*;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class SimulatorView extends JFrame
{
    /**
	 * Randomly Generated serialVersionUID zodat eclipse stopt met janken
	 * heeft wat van doen met compatibilty van instances
	 */
	private static final long serialVersionUID = 2113950054038469061L;
	private static SimView sim;
	private static ControlView controls;
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        
        sim = new SimView(height,width);
        setTitle("Fox and Rabbit Simulation");
        controls = new ControlView();
        setLocation(100, 50);        

        Container contents = getContentPane();
        contents.add(sim,BorderLayout.CENTER);
        contents.add(controls,BorderLayout.WEST);
        this.setJMenuBar(new VKMenuBar());
        pack();
        setVisible(true);
    }
    
    public SimView getSim()
    {
    	return sim;
    }

}
