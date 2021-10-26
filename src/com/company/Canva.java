package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Canva extends JPanel {

    private boolean isAllowedToDraw = false;
    private final ArrayList<Point> pointsOfPolygon = new ArrayList<>();
    private int counter = 0;


    private boolean isEnabledToRotate = false;
    private boolean isEnabledToScale = false;
    private boolean isEnabledToTranslate = true;

    private Point prevPoint = new Point(0,0);

    private boolean isAddingPointEnabled =false;

    private Point relative = new Point(-5,-5);

    private final JButton execute = new JButton();
    private final JButton addPoint = new JButton();
    private final JButton resetButton = new JButton();

    private final JButton startButton = new JButton();
    private final JSlider numberOfPoints = new JSlider(3,20);


    private final JTextField addXPoint = new JTextField();
    private final JTextField addYPoint = new JTextField();
    private final JButton addButton = new JButton();


    private final JTextField translateXPoint = new JTextField();
    private final JTextField translateYPoint = new JTextField();
    private final JButton translateButton= new JButton();


    private final JTextField rotateXPoint = new JTextField();
    private final JTextField rotateYPoint = new JTextField();
    private JSlider angle = new JSlider(0,360);
    private final JButton angleButton = new JButton();



    private final JTextField scaleX = new JTextField();
    private final JTextField scaleY = new JTextField();
    private  final JButton scaleButton = new JButton();


    {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                prevPoint = e.getPoint();
            }



            @Override
            public void mouseReleased(MouseEvent e) {
                if (isAllowedToDraw) {
                    pointsOfPolygon.add(e.getPoint());
                    counter++;
                    checkEndingOfDrawing();
                }
                if(isAddingPointEnabled)
                {
                    isAddingPointEnabled=false;
                    relative=e.getPoint();
                }


            }


        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x ,y;
                if(isEnabledToTranslate)
                {
                    x = e.getX()- prevPoint.x;
                    y = e.getY()- prevPoint.y;
                    translateShape(x,y);
                    prevPoint=e.getPoint();
                }
                if(isEnabledToRotate)
                {
                    x = e.getX()-  prevPoint.x;
                    y = e.getY()- prevPoint.y;
                    rotateShape(x,y,e.getPoint());

                    prevPoint = e.getPoint();
                }
                if(isEnabledToScale)

                {
                    double scaleX = ((relative.x -e.getX())/(relative.x- prevPoint.getX()));
                    double scaleY = ((relative.y -e.getY())/(relative.y- prevPoint.getY()));

                    scaleShape(scaleX,scaleY);
                    prevPoint=e.getPoint();
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });


    }
    private void scaleShape(double x, double y) {

        for (Point p : pointsOfPolygon
             ) {
            p.x = (int) (relative.x +(p.x -relative.x)*x);
            p.y = (int) (relative.y +(p.y -relative.y)*y);
        }
    }

    private  void rotateShape(double angle)
    {
        double ang = Math.toRadians(angle);
        for (Point p : pointsOfPolygon
        ) {
            p.x = (int) (relative.x +(p.x-relative.x)*Math.cos(ang)-(p.y- relative.y)*Math.sin(ang));
            p.y = (int) (relative.y +(p.x-relative.x)*Math.sin(ang)+(p.y- relative.y)*Math.cos(ang));
        }
    }

    private void rotateShape(int x, int y, Point point) {
        double deltaAngle = Math.atan2(y- relative.y,x- relative.x)-
                Math.atan2(point.getY()- relative.y,point.getX()- relative.x);

       rotateShape(deltaAngle);
    }

    private void translateShape(int x, int y) {
        for (Point p: pointsOfPolygon
             ) {
            p.x=p.x+x;
            p.y=p.y+y;
        }
    }

    private void checkEndingOfDrawing() {
        if(numberOfPoints.getValue()==counter)
        {
            isAllowedToDraw = false;
            counter = 0;
        }
    }




    Canva()
    {
        this.setLayout(null);
        setTextFields();
        setButtons();
    }



    private void executeAction() {
        if(isEnabledToTranslate)
        {
            int x =Integer.parseInt(translateXPoint.getText());
            int y =Integer.parseInt(translateYPoint.getText());
            translateShape(x,y);
        }

        if(isEnabledToRotate)
        {
            rotateShape(angle.getValue());
        }
        if(isEnabledToScale)
        {
            scaleShape(Double.parseDouble(scaleX.getText()),Double.parseDouble(scaleY.getText()));
        }
    }

    private void setTranslateMode() {
        isEnabledToTranslate=true;
        isEnabledToScale=false;
        isEnabledToRotate=false;
    }
    private void setRotateMode() {
        isEnabledToTranslate=false;
        isEnabledToScale=false;
        isEnabledToRotate=true;
    }
    private void setScaleMode() {
        isEnabledToTranslate=false;
        isEnabledToScale=true;
        isEnabledToRotate=false;
    }

    private void addPoint() {
        Point point = new Point(
                Integer.parseInt(addXPoint.getText()),
                Integer.parseInt(addYPoint.getText()));
        if (isAllowedToDraw) {
            pointsOfPolygon.add(point);
        checkEndingOfDrawing();
        }
        else
        {
            relative=point;
        }
    }

    private void setButtons() {
        addButton.setBounds(900,75,90,25);
        addButton.setText("Add");
        addButton.addActionListener(e->addPoint());
        translateButton.setBounds(900,175,90,25);
        translateButton.setText("Translate");
        translateButton.addActionListener(e-> setTranslateMode());
        angleButton.setBounds(900,305,90,25);
        angleButton.setText("Rotate");
        angleButton.addActionListener(e->setRotateMode());
        scaleButton.setBounds(900,435,90,25);
        scaleButton.setText("Scale");
        scaleButton.addActionListener(e->setScaleMode());
        startButton.setBounds(900,650,90,25);
        startButton.setText("Start");
        numberOfPoints.setBounds(885,700,120,25);
        resetButton.setBounds(900,500,90,25);
        resetButton.setText("Reset");
        resetButton.addActionListener(e-> reset());
        execute.setBounds(900,550,90,25);
        execute.addActionListener(e-> executeAction());
        addPoint.setBounds(900,600,90,25);
        addPoint.addActionListener(e-> isAddingPointEnabled = true);
        execute.setText("Execute");
        addPoint.setText("Add Point");

        startButton.addActionListener(e -> this.isAllowedToDraw = true);

        this.add(numberOfPoints);
        this.add(startButton);
        this.add(execute);
        this.add(addPoint);
        this.add(addButton);
        this.add(translateButton);
        this.add(angleButton);
        this.add(scaleButton);
        this.add(resetButton);

    }

    private void reset() {
         isAllowedToDraw = false;
          pointsOfPolygon.clear();

         isEnabledToRotate = false;
         isEnabledToScale = false;
        isEnabledToTranslate = true;

         prevPoint = new Point(0,0);

         isAddingPointEnabled =false;

         relative = new Point(-5,-5);


    }

    private void setTextFields() {
        addXPoint.setBounds(900,30,40,25);
        addYPoint.setBounds(950,30,40,25);

        translateXPoint.setBounds(900,130,40,25);
        translateYPoint.setBounds(950,130,40,25);

        angle.setBounds(885,260,120,25);

        scaleX.setBounds(900,360,40,25);
        scaleY.setBounds(950,360,40,25);

        this.add(addXPoint);
        this.add(addYPoint);
        this.add(translateXPoint);
        this.add(translateYPoint);
        this.add(angle);
        this.add(rotateXPoint);
        this.add(rotateYPoint);
        this.add(scaleX);
        this.add(scaleY);

    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if(!pointsOfPolygon.isEmpty()) {
            for (int i = 0; i < pointsOfPolygon.size() - 1; i++) {
                graphics2D.drawLine(
                        (int) pointsOfPolygon.get(i).getX(),
                        (int) pointsOfPolygon.get(i).getY(),
                        (int) pointsOfPolygon.get(i + 1).getX(),
                        (int) pointsOfPolygon.get(i + 1).getY()
                );
                if (pointsOfPolygon.size() == numberOfPoints.getValue()) {
                    graphics2D.drawLine(
                            (int) pointsOfPolygon.get(0).getX(),
                            (int) pointsOfPolygon.get(0).getY(),
                            (int) pointsOfPolygon.get(numberOfPoints.getValue() - 1).getX(),
                            (int) pointsOfPolygon.get(numberOfPoints.getValue() - 1).getY()
                    );
                }

            }
            for (Point point : pointsOfPolygon) {
                graphics2D.drawOval(
                        (int) point.getX() - 5,
                        (int) point.getY() - 5,
                        10,
                        10
                );

            }
        }

        if(relative!=null)
        {
            graphics2D.setColor(Color.BLUE);
            graphics2D.drawOval(
                    (int) relative.getX() - 5,
                    (int) relative.getY() - 5,
                    10,
                    10
            );
        }
        repaint();
    }

}
