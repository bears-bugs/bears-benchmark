package hu.oe.nik.szfmv.environment;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.environment.interfaces.IWorldObject;
import org.apache.logging.log4j.LogManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class WorldObject implements IWorldObject {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(WorldObject.class);
    protected int width;
    protected int height;
    protected double rotation = 0f;
    protected String imageFileName;
    protected Point location;
    protected Shape shape;

    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public WorldObject(int x, int y, String imageFileName) {
        this.location = new Point(x, y);
        this.imageFileName = imageFileName;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getX() {
        return this.location.x;
    }

    public void setX(int x) {
        this.location.x = x;
    }

    public int getY() {
        return this.location.y;
    }

    public void setY(int y) {
        this.location.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getRotation() {
        return this.rotation;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }

    public Shape getShape() {
        return this.shape;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * This method get and store attribute from imageFile
     *
     * @throws IOException when image not found
     */
    public void generateDimens() {
        try {
            BufferedImage image = ImageIO.read(
                    new File(
                            ClassLoader.getSystemResource(this.getImageFileName())
                                    .getFile()));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * This method create Rectangle
     */
    public void generateShape() {
        AffineTransform tx = new AffineTransform();
        tx.rotate(-this.getRotation(), this.getX(), this.getY());
        if (!AutomatedCar.class.isInstance(this)) {
            this.shape = tx.createTransformedShape(
                    new Rectangle(
                            this.getX(), this.getY(),
                            this.getWidth(), this.getHeight()));
        } else {
            this.shape = tx.createTransformedShape(
                    new Rectangle(
                            this.getX() - this.getWidth() / 2,
                            this.getY() - this.getHeight() / 2,
                            this.getWidth(), this.getHeight()));
        }
    }

    @Override
    public String toString() {
        return "WorldObject{" +
                "width=" + width +
                ", height=" + height +
                ", rotation=" + rotation +
                ", imageFileName='" + imageFileName + '\'' +
                ", location=" + location +
                ", shape=" + shape +
                '}';
    }

}

