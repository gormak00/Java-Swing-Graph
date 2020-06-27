package com.company.View;

import com.company.Controllers.ChartDraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ChartOptionsViewPanel extends JPanel {
    private ChartDrawPanel chartDrawPanel;
    private JLabel scaleTextLabel;
    private JLabel graphALabel = new JLabel("Graph A");
    private JLabel graphBLabel = new JLabel("Graph B");
    private JSlider slider;

    public ChartOptionsViewPanel(ChartDraw controller) {
        super();
        this.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        slider = new JSlider(JSlider.HORIZONTAL, 50, 150, 100);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);

        chartDrawPanel = new ChartDrawPanel(controller);
        scaleTextLabel = new JLabel(chartDrawPanel.getZoom() * 100 + "%");

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout());


        JScrollPane scrollPanel = new JScrollPane(chartDrawPanel);

        scrollPanel.setAutoscrolls(true);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = e.getPoint();
            }


            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, chartDrawPanel);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX * 0.3;
                        view.y += deltaY * 0.2;
                        chartDrawPanel.scrollRectToVisible(view);
                    }
                }
            }
        };
        chartDrawPanel.addMouseListener(mouseAdapter);
        chartDrawPanel.addMouseMotionListener(mouseAdapter);
        scrollPanel.setPreferredSize(new Dimension(800, 600));
        scrollPanel.setVisible(true);

        graphALabel.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        graphALabel.setForeground(new Color(255, 12, 0));
        graphBLabel.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        graphBLabel.setForeground(new Color(0x0000C3));

        toolBar.add(graphALabel);
        toolBar.add(slider);
        toolBar.add(graphBLabel);
        this.add(scrollPanel, BorderLayout.WEST);
        this.add(toolBar, BorderLayout.SOUTH);
        this.setVisible(true);
        scrollPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        chartDrawPanel.setZoom(chartDrawPanel.getZoom() + 0.1);

                    } else {
                        chartDrawPanel.setZoom(chartDrawPanel.getZoom() - 0.1);
                    }
                    slider.setValue((int) (chartDrawPanel.getZoom() * 100));
                    chartDrawPanel.revalidate();
                }
            }
        });
    }

    public ChartDrawPanel getChartDrawPanel() {
        return chartDrawPanel;
    }
}
