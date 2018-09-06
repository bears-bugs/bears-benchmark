package hu.oe.nik.szfmv.visualization;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiAdapter extends WindowAdapter {

    /**
     * This method is invoked when the Gui window is closed.
     *
     * @param e is a {@WindowEvent}
     */
    public void windowClosing(WindowEvent e) {
        // Other things can be done here before the termination.
        System.exit(0);
    }

}
