package com.rc2s.client.components;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    
	private LedEvent ledEvent;
    private Color color;
    
    public LedCube(final Parent parent, final double x, final double y, 
            final double z, final double size, 
            final Color color, final LedEvent ledEvent)
    {
        this.parent = parent;
		this.ledEvent = ledEvent;
        drawCube(x, y, z, size, color);
		
        this.parent.setOnMouseDragged((e) -> {
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
				
				e.consume();
            }
        });
		
		if(this.ledEvent != null)
		{
			this.parent.setOnScroll((e) -> {
				if(e.getDeltaY() == 0
				|| (this.size <= LedCube.MIN_SIZE && e.getDeltaY() < 0)
				|| (this.size >= LedCube.MAX_SIZE && e.getDeltaY() > 0))
					return;

				double delta = e.getDeltaY() > 0 ? 1. : -1.;
				this.setSize(this.size + delta);
				
				e.consume();
			});
		}
		
        this.setCursor(Cursor.HAND);
    }
	
    private void drawCube(final double x, final double y, 
            final double z, final double size, final Color color)
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
                    Led led = new Led(i, j, k, size, true, color, this.ledEvent);
                    this.getChildren().add(led);
                }
            }
        }
    }

    public double getX()
    {
        return x;
    }

    public void setX(final double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(final double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(final double z)
    {
        this.z = z;
    }

    public Rotate getRx()
    {
        return rx;
    }

    public void setRx(final Rotate rx)
    {
        this.rx = rx;
        getTransforms().set(0, rx);
    }

    public Rotate getRy()
    {
        return ry;
    }

    public void setRy(final Rotate ry)
    {
        this.ry = ry;
        getTransforms().set(1, ry);
    }

    public Rotate getRz()
    {
        return rz;
    }

    public void setRz(final Rotate rz)
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

    public void setSize(final double size)
    {
        this.size = size;

        for(Node n : this.getChildren())
        {
            Led l = (Led)n;
            l.setSize(this.size);
        }
        updateAxis();
    }

    public void setColor(final Color color)
    {
        this.color = color;

        for(Node n : this.getChildren())
        {
            Led l = (Led)n;
            l.setColor(this.color);
        }
    }
	
	public void setActivated(final boolean activated)
	{
		for(Node n : this.getChildren())
        {
            Led l = (Led)n;
            l.setActivated(activated);
        }
	}

    public boolean[][][] getStateArray()
    {
        boolean[][][] states = new boolean[(int)getY()][(int)getZ()][(int)getX()];

        for(Node n : this.getChildren())
        {
            if(n instanceof Led)
            {
                Led l = (Led) n;
                states[(int)l.getY()][(int)l.getZ()][(int)l.getX()] = l.isActivated();
            }
        }

        return states;
    }
}
