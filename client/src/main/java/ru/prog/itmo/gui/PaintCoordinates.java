package ru.prog.itmo.gui;

import ru.prog.itmo.spacemarine.AstartesCategory;
import ru.prog.itmo.spacemarine.SpaceMarine;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class PaintCoordinates extends JPanel {
    private Store store;
    private int width = 900;
    private int height = 350;
    private int centerX = width / 2;
    private int centerY = height / 2;

    public PaintCoordinates(Store store) {
        setMinimumSize(new Dimension(850, 300));
        setPreferredSize(new Dimension(900, 400));
        setMaximumSize(new Dimension(950, 450));
        this.store = store;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.draw(new Line2D.Double(0, centerY, width, centerY));
        graphics2D.draw(new Line2D.Double(centerX, 0, centerX, height));
        for (int i = 1; i <= 5; i++) {
            var circleHalfWidth = width * i / 10;
            var circleHalfHeight = height * i / 10;
            graphics2D.draw(new Ellipse2D.Double(
                    centerX - circleHalfWidth, centerY - circleHalfHeight,
                    2 * circleHalfWidth, 2 * circleHalfHeight));
        }
        paintMarines(graphics2D);
    }

    private void paintMarines(Graphics2D graphics2D) {
        for (SpaceMarine marine : store.getMarines()) {
            var marineX = marine.getCoordinatesX();
            var marineY = marine.getCoordinatesY();
            var shapeRadius = 15;
            double shapeX = centerX - (double) shapeRadius / 2 + marineX;
            double shapeY = centerY - (double) shapeRadius / 2 - marineY;
//            var colorNum = marine.getOwnerUser().hashCode() % 254;
//            var color = new Color(colorNum, colorNum % 5, colorNum % 20);
//            graphics2D.setColor(color);
            if (marine.getCategory() == AstartesCategory.SCOUT) {
                var shape = new Ellipse2D.Double(shapeX, shapeY, shapeRadius, shapeRadius);
                graphics2D.draw(shape);
//                graphics2D.fill(shape);
            }
            if (marine.getCategory() == AstartesCategory.LIBRARIAN) {
                var shape = new Rectangle2D.Double(shapeX, shapeY, shapeRadius, shapeRadius);
                graphics2D.draw(shape);
//                graphics2D.fill(shape);
            }
            if (marine.getCategory() == AstartesCategory.CHAPLAIN) {
                var shape = new Rectangle2D.Double(shapeX, shapeY, shapeRadius, shapeRadius);
                graphics2D.rotate(Math.toRadians(45));
                graphics2D.draw(shape);
                graphics2D.rotate(Math.toRadians(-45));
//                graphics2D.fill(shape);
            }
            if (marine.getCategory() == AstartesCategory.APOTHECARY) {
                var shape = new Ellipse2D.Double(shapeX, shapeY, shapeRadius, shapeRadius*3);
                graphics2D.draw(shape);
//                graphics2D.fill(shape);
            }
            graphics2D.drawString("(" + marineX + ", " + marineY + ")", (int) shapeX, (int) shapeY);
        }
    }


}