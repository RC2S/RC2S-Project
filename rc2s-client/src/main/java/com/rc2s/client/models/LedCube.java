package com.rc2s.client.models;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class LedCube extends Group
{    
    private double mx, my;
    private double x, y, z;
    private Rotate rx, ry, rz;
    private double size;
    
    private final Color color;
    
    public LedCube(double x, double y, double z, double size, Color color)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.rx = new Rotate(0., ((x-1)*size*Led.SIZE_MODIFIER)/2, ((y-1)*size*Led.SIZE_MODIFIER)/2, ((z-1)*size*Led.SIZE_MODIFIER)/2, Rotate.X_AXIS);
        this.ry = new Rotate(0., ((x-1)*size*Led.SIZE_MODIFIER)/2, ((y-1)*size*Led.SIZE_MODIFIER)/2, ((z-1)*size*Led.SIZE_MODIFIER)/2, Rotate.Y_AXIS);
        this.rz = new Rotate(0., ((x-1)*size*Led.SIZE_MODIFIER)/2, ((y-1)*size*Led.SIZE_MODIFIER)/2, ((z-1)*size*Led.SIZE_MODIFIER)/2, Rotate.Z_AXIS);
        this.getTransforms().addAll(rz, ry, rx);
        
        this.size = size;
        this.color = color;
        
        for(int i = 0 ; i < x ; i++)
        {
            for(int j = 0 ; j < y ; j++)
            {
                for(int k = 0 ; k < z ; k++)
                {
                    Led led = new Led(i, j, k, size, true, color);
                    this.getChildren().add(led);
                }
            }
        }
		
        this.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("CLICKED");
        });
        
        this.setOnMouseDragged((MouseEvent e) -> {
			if(e.isPrimaryButtonDown())
            {
				double oldx = mx;
				double oldy = my;
				mx = e.getX();
				my = e.getY();

				double dx = mx - oldx;
				double dy = my - oldy;
				double nx = this.rx.getAngle() + dx;
				double ny = this.ry.getAngle() - dy;
            
                this.rx.setAngle(this.ry.getAngle() + 0.5);
                this.ry.setAngle(this.rx.getAngle() + 0.5);
            }
        });
		
        this.setCursor(Cursor.HAND);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public Rotate getRx()
    {
        return rx;
    }

    public void setRx(Rotate rx)
    {
        this.rx = rx;
        getTransforms().add(rx);
    }

    public Rotate getRy()
    {
        return ry;
    }

    public void setRy(Rotate ry)
    {
        this.ry = ry;
        getTransforms().add(ry);
    }

    public Rotate getRz()
    {
        return rz;
    }

    public void setRz(Rotate rz)
    {
        this.rz = rz;
        getTransforms().add(rz);
    }

    public double getSize()
    {
        return size;
    }

    public void setSize(double size)
    {
        this.size = size;
    }
}
