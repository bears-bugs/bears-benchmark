package hu.oe.nik.szfmv.visualization;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.bus.packets.car.CarPacket;
import hu.oe.nik.szfmv.environment.World;
import hu.oe.nik.szfmv.environment.WorldObject;
import hu.oe.nik.szfmv.environment.models.Movable;
import hu.oe.nik.szfmv.environment.models.Stationary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CourseDisplay is for providing a viewport to the virtual world where the simulation happens.
 */
public class CourseDisplay extends JPanel {

    private static Map<String, Point> referencePoints = new HashMap<>();
    private static final String referencePointsURI = "./src/main/resources/reference_points.xml";
    private static final Logger LOGGER = LogManager.getLogger();

    private final float scale = 0.5F;
    private final int width = 770;
    private final int height = 700;
    private final int backgroundColor = 0xEEEEEE;
    private final int carWidth = 102;
    private final int carHeight = 208;
    private final int courseWidth = 5120;
    private final int courseHeight = 3000;

    private CarPacket carPacket = null;
    private World world;
    // roadsigns and trees
    private BufferedImage staticEnvironmentZ1 = null;
    // other static objects
    private BufferedImage staticEnvironmentZ0 = null;

    /**
     * Initialize the course display
     */
    public CourseDisplay() {
        // Not using any layout manager, but fixed coordinates
        setLayout(null);
        setBounds(0, 0, width, height);
        setBackground(new Color(backgroundColor));
        // Loa reference points from xml
        try {
            DrawUtils.loadReferencePoints(referencePoints, referencePointsURI);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Draws a WorldObject to the correct place, with the correct scaling and rotating
     *
     * @param object  object to draw
     * @param g       graphics object
     * @param offsetX x offset value for course moving
     * @param offsetY y offset value for course moving
     */
    private void drawWorldObject(WorldObject object, Graphics g, int offsetX, int offsetY) {
        BufferedImage image = null;
        // read file from resources
        try {
            image = ImageIO.read(new File(ClassLoader.getSystemResource(object.getImageFileName()).getFile()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        // referencePoint is the red dot on the pictures from wiki, stored in xml, default
        // point is (0,0).
        Point referencePoint = referencePoints.getOrDefault(object.getImageFileName(), null);
        if (referencePoint == null) {
            referencePoint = new Point(0, 0);
        }

        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        at.rotate(-object.getRotation(), object.getX() + offsetX, object.getY() + offsetY);
        at.translate(object.getX() - referencePoint.x + offsetX, object.getY() - referencePoint.y + offsetY);

        ((Graphics2D) g).drawImage(image, at, this);
    }

    /**
     * Draws the static course from the given xml file to an image
     * Draw crossable objects and stationary objects on 2 separated images
     */
    public void drawEnvironment() {
        staticEnvironmentZ0 = new BufferedImage((int) (courseWidth * scale),
                (int) (courseHeight * scale), BufferedImage.TYPE_INT_ARGB);
        staticEnvironmentZ1 = new BufferedImage((int) (courseWidth * scale),
                (int) (courseHeight * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphicsZ0 = staticEnvironmentZ0.createGraphics();
        Graphics2D graphicsZ1 = staticEnvironmentZ1.createGraphics();

        // draw not movable objects only once to an image
        for (WorldObject object : world.getWorldObjects()) {
            if (!Movable.class.isAssignableFrom(object.getClass()) &&
                    !AutomatedCar.class.isAssignableFrom(object.getClass())) {
                if (Stationary.class.isAssignableFrom(object.getClass())) {
                    drawWorldObject(object, graphicsZ1, 0, 0);
                } else {
                    drawWorldObject(object, graphicsZ0, 0, 0);
                }
            }
        }
    }

    /**
     * Draws the world to the course display
     *
     * @param world     {@link World} object that describes the virtual world
     * @param carPacket {@link CarPacket} Packet that contains the location of the automated car
     */
    public void drawWorld(World world, CarPacket carPacket) {
        invalidate();
        this.world = world;
        this.carPacket = carPacket;
        validate();
        repaint();
    }

    /**
     * Gets the offset value to move the camera
     *
     * @param scaledWidth  width of the viewport  multiplied by scaling
     * @param scaledHeight height of the viewport  multiplied by scaling
     * @return offset value to move camera with
     */
    private Point getOffset(int scaledWidth, int scaledHeight) {
        int offsetX = 0;
        int offsetY = 0;
        int diffX = (scaledWidth / 2) - carPacket.getX() - carWidth / 2;
        if (diffX < 0) {
            offsetX = diffX;
        }
        int diffY = scaledHeight / 2 - carPacket.getY() - carHeight / 2;
        if (diffY < 0) {
            offsetY = diffY;
        }
        return new Point(offsetX, offsetY);
    }

    /**
     * Inherited method that can paint on the JPanel.
     *
     * @param g {@link Graphics} object that can draw to the canvas
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when the car reach the half width of the viewport the course move, and the car stay on center
        int scaledWidth = (int) (width / scale);
        int scaledHeight = (int) (height / scale);
        Point offset = getOffset(scaledWidth, scaledHeight);
        if (staticEnvironmentZ0 == null && staticEnvironmentZ1 == null) {
            drawEnvironment();
        }
        // draw the lower layer (crossable objects)
        g.drawImage(staticEnvironmentZ0, (int) (offset.x * scale), (int) (offset.y * scale), this);
        // draw moving objects
        for (WorldObject object : this.world.getWorldObjects()) {
            if (AutomatedCar.class.isAssignableFrom(object.getClass()) ||
                    Movable.class.isAssignableFrom(object.getClass())) {
                drawWorldObject(object, g, offset.x, offset.y);
            }
        }
        // draw stationary children (Tree, Road sign)
        g.drawImage(staticEnvironmentZ1, (int) (offset.x * scale), (int) (offset.y * scale), this);
        drawShapesDebug(g, offset.x, offset.y);
    }

    private void drawShapesDebug(Graphics g, int offsetX, int offsetY) {
        for (WorldObject object : world.getWorldObjects()) {
            g.setColor(Color.BLUE);
            AffineTransform at1 = new AffineTransform();
            at1.scale(scale, scale);
            at1.translate(offsetX, offsetY);

            Shape s = object.getShape();
            if (s != null) {
                ((Graphics2D) g).draw(at1.createTransformedShape(s));
            }
        }
    }

}
