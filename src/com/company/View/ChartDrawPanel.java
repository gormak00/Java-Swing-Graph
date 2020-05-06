package com.company.View;


import com.company.Controllers.ChartDraw;
import com.company.Model.PointsComparatorXAxis;
import com.company.Model.PointsComparatorYAxis;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartDrawPanel extends JPanel {
    private int prefW = 1100;
    private int prefH = 900;
    private int borderGap = 55;
    private final Color graphColorB = Color.blue;
    private final Color graphColorA = Color.red;
    private Color graphPointColor = Color.black;
    private Stroke graphStroke = new BasicStroke(3f);
    private int graphPointWidth = 10;
    private int yHatchCnt = 10;
    private int fontSize = 10;
    private double zoom;
    private List<Point> pointsA;
    private List<Point> pointsB;


    public ChartDrawPanel(ChartDraw controller) {
        this.pointsA = controller.getPointList("A");
        this.pointsA.sort(new PointsComparatorXAxis());
        this.pointsB = controller.getPointList("B");
        this.pointsB.sort(new PointsComparatorXAxis());
        this.zoom = 0.5;
        this.setVisible(true);
    }


    public void setZoom(double zoom) {
        if (zoom < 0.5) {
            this.zoom = 0.5;
        } else if (zoom > 1.5) {
            this.zoom = 1.5;
        } else {
            this.zoom = zoom;
        }
    }

    public double getZoom() {
        return zoom;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (pointsA.size() >= 0 && pointsB.size() >= 0) {
            double hatchNumber = 0;
            double maxXB = findStep((int) Collections.max(pointsB, new PointsComparatorXAxis()).getX());
            double maxYB = findStep((int) Collections.max(pointsB, new PointsComparatorYAxis()).getY());
            double maxXA = findStep((int) Collections.max(pointsA, new PointsComparatorXAxis()).getX());
            double maxYA = findStep((int) Collections.max(pointsA, new PointsComparatorYAxis()).getY());
            double maxX = Math.max(maxXB, maxXA);
            double maxY = Math.max(maxYB, maxYA);
            int border_gap = (int) (borderGap * zoom);
            int hatchLength = (int) (graphPointWidth * zoom);
            double xScale = (((double) getWidth() - 2 * border_gap) / maxX);
            double yScale = (((double) getHeight() - 2 * border_gap) / maxY);
            int fontSize;
            if (zoom < 3) {
                fontSize = (int) (this.fontSize * zoom);
            } else {
                fontSize = this.fontSize * 3;
            }
            List<Point> zoomedPointsListA = new ArrayList<>();
            List<Point> zoomedPointsListB = new ArrayList<>();

            for (Point point : pointsA) {
                Point newPoint = new Point();
                newPoint.x = (int) (border_gap + point.x * xScale);
                newPoint.y = (int) (getHeight() - point.y * yScale - border_gap);
                zoomedPointsListA.add(newPoint);
            }

            for (Point point : pointsB) {
                Point newPoint = new Point();
                newPoint.x = (int) (border_gap + point.x * xScale);
                newPoint.y = (int) (getHeight() - point.y * yScale - border_gap);
                zoomedPointsListB.add(newPoint);
            }

            g2.drawLine(border_gap, getHeight() - border_gap, border_gap, border_gap);
            g2.drawLine(border_gap, getHeight() - border_gap, border_gap, border_gap);
            g2.drawLine(border_gap, getHeight() - border_gap, getWidth() - border_gap, getHeight() - border_gap);


            if (pointsA.size() < 2) {
                hatchNumber = maxY / 100000;
            } else {
                hatchNumber = maxY / 100000;
            }

            for (int index = 0; index < yHatchCnt; index++) {
                int x0 = border_gap - hatchLength / 2;
                int x1 = x0 + hatchLength;
                int y0 = getHeight() - (((index + 1) * (getHeight() - border_gap * 2)) / yHatchCnt + border_gap);
                g2.drawLine(x0, y0, x1, y0);
                g2.drawString(String.format("%.4f", hatchNumber * (index + 1)), 0, y0 + fontSize / 2);
            }

            if (pointsA.size() < 2) {
                hatchNumber = maxX / 1000;
            } else {
                hatchNumber = maxX / 1000;
            }

            for (int index = 0; index < yHatchCnt; index++) {
                int x0 = (index + 1) * (getWidth() - border_gap * 2) / yHatchCnt + border_gap;
                int y0 = getHeight() - border_gap + hatchLength / 2;
                int y1 = y0 - hatchLength;
                g2.drawLine(x0, y0, x0, y1);
                g2.drawString(String.format("%.4f", hatchNumber * (index + 1)), x0 - fontSize / 2, y0 + border_gap / 2);
            }
            g2.drawString("0", border_gap, getHeight() - border_gap / 2);
            Stroke oldStroke = g2.getStroke();
            g2.setColor(graphColorA);
            g2.setStroke(graphStroke);
            for (int i = 0; i < zoomedPointsListA.size() - 1; i++) {
                Point point = zoomedPointsListA.get(i);
                int x1 = point.x;
                int y1 = point.y;
                point = zoomedPointsListA.get(i + 1);
                int x2 = point.x;
                int y2 = point.y;
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.setStroke(oldStroke);
            g2.setColor(graphPointColor);
            for (Point point : zoomedPointsListA) {
                int x = (point.x - graphPointWidth / 2);
                int y = (point.y - graphPointWidth / 2);
                int ovalW = graphPointWidth;
                int ovalH = graphPointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
            }
            g2.setColor(graphColorB);
            g2.setStroke(graphStroke);
            for (int i = 0; i < zoomedPointsListB.size() - 1; i++) {
                Point point = zoomedPointsListB.get(i);
                int x1 = point.x;
                int y1 = point.y;
                point = zoomedPointsListB.get(i + 1);
                int x2 = point.x;
                int y2 = point.y;
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.setStroke(oldStroke);
            g2.setColor(graphPointColor);
            for (Point point : zoomedPointsListB) {
                int x = point.x - graphPointWidth / 2;
                int y = point.y - graphPointWidth / 2;
                int ovalW = graphPointWidth;
                int ovalH = graphPointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
            }
        } else {
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2.drawString("Введите значения и нажмите на кнопку \"построить\"", 200, 300);
        }
    }

    public int findStep(int number) {
        int step = 10;
        while (step < number) {
            step = step + 10;
        }
        return step;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (prefW * zoom), (int) (prefH * zoom));
    }
}