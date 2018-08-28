package hu.oe.nik.szfmv.visualization;

import javax.swing.*;
import java.awt.*;


public class Gui extends JFrame {

    private final int windowWidth = 1020;
    private final int windowHeight = 700;


    private CourseDisplay courseDisplay;
    private Dashboard dashboard;

    /**
     * Initialize the GUI class
     */
    public Gui() {
        setTitle("AutomatedCar");
        setLocation(0, 0); // default is 0,0 (top left corner)
        addWindowListener(new GuiAdapter());
        setPreferredSize(new Dimension(windowWidth, windowHeight)); // inner size
        setResizable(false);
        pack();

        // Icon downloaded from: http://www.iconarchive.com/show/toolbar-2-icons-by-shlyapnikova/car-icon.html
        // and available under the licence of: https://creativecommons.org/licenses/by/4.0/
        ImageIcon carIcon = new ImageIcon(ClassLoader.getSystemResource("car-icon.png"));
        setIconImage(carIcon.getImage());

        // Not using any layout manager, but fixed coordinates
        setLayout(null);

        courseDisplay = new CourseDisplay();
        add(courseDisplay);

        dashboard = new Dashboard();
        add(dashboard);

        setVisible(true);
    }

    public CourseDisplay getCourseDisplay() {
        return courseDisplay;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }
}
