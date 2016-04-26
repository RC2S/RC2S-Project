package com.rc2s.client.components;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class LedCube extends Group
{
    private static final double MIN_SIZE = 3.;
    private static final double MAX_SIZE = 20.;

    private final Parent parent;
	
    private double mx, my;
    private double x, y, z;
    private Rotate rx, ry, rz;
    private double size;
    
    private Color color;
	private boolean actionEvents;
	
	public LedCube(Parent parent, double x, double y, double z, double size, Color color)
	{
		this(parent, x, y, z, size, color, true);
	}
    
    public LedCube(Parent parent, double x, double y, double z, double size, Color color, boolean actionEvents)
    {
        this.parent = parent;
		this.actionEvents = actionEvents;
        drawCube(x, y, z, size, color);
		
        this.parent.setOnMouseDragged((MouseEvent e) -> {
            if(e.isPrimaryButtonDown())
            {
                double oldx = mx;
                double oldy = my;
                mx = e.getX();
                my = e.getY();

                double dx = mx - oldx;
                double dy = my - oldy;
                double nx = this.rx.getAngle() - Math.toRadians(dy) * 2.;
                double ny = this.ry.getAngle() + Math.toRadians(dx) * 2.;
				
                this.rx.setAngle(nx);
                this.ry.setAngle(ny);
            }
        });
		
		if(actionEvents)
		{
			this.parent.setOnScroll((ScrollEvent e) -> {
				if(e.getDeltaY() == 0
				|| (this.size <= LedCube.MIN_SIZE && e.getDeltaY() < 0)
				|| (this.size >= LedCube.MAX_SIZE && e.getDeltaY() > 0))
					return;

				double delta = e.getDeltaY() > 0 ? 1. : -1.;
				this.setSize(this.size + delta);
			});
		}
		
        this.setCursor(Cursor.HAND);
    }
	
    private void drawCube(double x, double y, double z, double size, Color color)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        
        if(this.getTransforms().isEmpty())
        {
            this.rx = new Rotate(
                0.,
                ((x-1) * size * Led.SIZE_MODIFIER) / 2,
                ((y-1) * size * Led.SIZE_MODIFIER) / 2,
                ((z-1) * size * Led.SIZE_MODIFIER) / 2,
                Rotate.X_AXIS
            );
            this.ry = new Rotate(
                0.,
                ((x-1) * size * Led.SIZE_MODIFIER) / 2,
                ((y-1) * size * Led.SIZE_MODIFIER) / 2,
                ((z-1) * size * Led.SIZE_MODIFIER) / 2,
                Rotate.Y_AXIS
            );
            this.rz = new Rotate(
                0.,
                ((x-1) * size * Led.SIZE_MODIFIER) / 2,
                ((y-1) * size * Led.SIZE_MODIFIER) / 2,
                ((z-1) * size * Led.SIZE_MODIFIER) / 2,
                Rotate.Z_AXIS
            );
            this.getTransforms().addAll(rx, ry, rz);
        }
        else
        {
            updateAxis();
        }
        
        this.size = size;
        this.color = color;
        
        for(int i = 0 ; i < x ; i++)
        {
            for(int j = 0 ; j < y ; j++)
            {
                for(int k = 0 ; k < z ; k++)
                {
                    Led led = new Led(i, j, k, size, true, color, this.actionEvents);
                    this.getChildren().add(led);
                }
            }
        }
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
        getTransforms().set(0, rx);
    }

    public Rotate getRy()
    {
        return ry;
    }

    public void setRy(Rotate ry)
    {
        this.ry = ry;
        getTransforms().set(1, ry);
    }

    public Rotate getRz()
    {
        return rz;
    }

    public void setRz(Rotate rz)
    {
        this.rz = rz;
        getTransforms().set(2, rz);
    }
	
    public void updateAxis()
    {
        for(int i = 0 ; i < 3 ; i++)
        {
            Rotate axis = (Rotate)this.getTransforms().get(i);
            axis.setPivotX(((x-1) * size * Led.SIZE_MODIFIER) / 2);
            axis.setPivotY(((y-1) * size * Led.SIZE_MODIFIER) / 2);
            axis.setPivotZ(((z-1) * size * Led.SIZE_MODIFIER) / 2);
        }
    }

    public double getSize()
    {
        return size;
    }

    public void setSize(double size)
    {
        this.size = size;

        for(Node n : this.getChildren())
        {
            Led l = (Led)n;
            l.setSize(this.size);
        }
        updateAxis();
    }
}
