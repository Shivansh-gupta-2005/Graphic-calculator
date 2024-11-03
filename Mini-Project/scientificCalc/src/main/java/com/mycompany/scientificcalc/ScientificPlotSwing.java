package com.mycompany.scientificcalc;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class ScientificPlotSwing extends JPanel {
    private double xMin = -10;
    private double xMax = 10;
    private double yMin = -10;
    private double yMax = 10;
    public String selectedFunction = "Sine";

    public void setRanges(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    private int toScreenX(double x) {
        return 0;
    }
    
    private int toScreenY(double y) {
        return (int) (getHeight() * (yMax - y) / (yMax - yMin));
    }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawAxes(g2);
        drawFunction(g2);
    }

    private void drawGrid(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();

        // Draw grid lines
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(1f));

        // Vertical grid lines
        for (double x = xMin; x <= xMax; x += 1.0) {
            int screenX = toScreenX(x);
            g2.drawLine(screenX, 0, screenX, height);
        }

        // Horizontal grid lines
        for (double y = yMin; y <= yMax; y += 1.0) {
            int screenY = toScreenY(y);
            g2.drawLine(0, screenY, width, screenY);
        }
    }

    private void drawAxes(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));

        // X-axis
        int yAxisPos = toScreenY(0);
        g2.drawLine(0, yAxisPos, width, yAxisPos);

        // Y-axis
        int xAxisPos = toScreenX(0);
        g2.drawLine(xAxisPos, 0, xAxisPos, height);
    }

    private void drawFunction(Graphics2D g2) {
        int width = getWidth();
        Path2D path = new Path2D.Double();
        boolean first = true;

        for (int i = 0; i < width; i++) {
            double x = xMin + (i * (xMax - xMin) / width);
            double y = evaluateFunction(x);

            // Skip invalid values
            if (!Double.isFinite(y) || y > yMax || y < yMin) {
                first = true;
                continue;
            }

            int screenY = toScreenY(y);

            if (first) {
                path.moveTo(i, screenY);
                first = false;
            } else {
                path.lineTo(i, screenY);
            }
        }

        g2.setColor(new Color(0, 0, 204));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(path);
    }

    private double evaluateFunction(double x) {
        return switch (selectedFunction) {
            case "Sine" -> Math.sin(x);
            case "Cosine" -> Math.cos(x);
            case "Tangent" -> Math.tan(x);
            case "Exponential" -> Math.exp(x);
            case "Logarithm" -> Math.log(x);
            default -> 0;
        };
    }
}
