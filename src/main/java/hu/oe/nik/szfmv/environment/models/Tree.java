package hu.oe.nik.szfmv.environment.models;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Tree extends Stationary {

    //Ha a fához tartozó kör shape átmérője akkora lenne mint maga a kép szélessége, akkor az
    // azt jelentené, hogy a fa lombjának tudunk nekimenni, viszont mi a fa törzsét szeretnénk
    //ütköztethetővé tenni, ezért a CIRCLEDIAMETER változóval befolyásoljuk a shape méretét úgy
    // hogy az a fa törzsének méretét közelítse meg.
    private static double CIRCLEDIAMETER = 25;

    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public Tree(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public Tree() {
        super(0, 0, null);
    }

    @Override
    public void generateShape() {
        //Circle radius for shape property
        //For more information please see Issue #221
        AffineTransform tx = new AffineTransform();
        tx.rotate(-this.getRotation(), this.getX(), this.getY());
        this.shape = tx.createTransformedShape((Shape) new Ellipse2D.Double(
                this.getX() + this.getWidth() / 2 - CIRCLEDIAMETER / 2,
                this.getY() + this.getHeight() / 2 - CIRCLEDIAMETER / 2,
                CIRCLEDIAMETER, CIRCLEDIAMETER));
    }
}
