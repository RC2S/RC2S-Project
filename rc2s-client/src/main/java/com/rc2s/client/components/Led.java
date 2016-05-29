package com.rc2s.client.components;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Led extends Sphere
{
    public static final double SIZE_MODIFIER = 4.;
    
    private double x, y, z;
    private double size;
    private boolean activated;
    private Color color;
	private boolean actionEvents;
    
	public Led(double x, double y, double z, double size, boolean activated, Color color)
	{
		this(x, y, z, size, activated, color, true);
	}
	
    public Led(double x, double y, double z, double size, boolean activated, Color color, boolean actionEvents)
    {
        super(size);
        
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.activated = activated;
        this.color = (color != null) ? color : Color.BLACK;
		this.actionEvents = actionEvents;
        
        this.setTranslateX(x * size * SIZE_MODIFIER);
        this.setTranslateY(y * size * SIZE_MODIFIER);
        this.setTranslateZ(z * size * SIZE_MODIFIER);
        
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(this.color);
        material.setSpecularColor(Color.BLACK);
        this.setMaterial(material);
		
		if(this.actionEvents)
		{
			this.setOnMouseClicked((e) -> {
				PhongMaterial newColor = new PhongMaterial();
				newColor.setSpecularColor(Color.BLACK);
				newColor.setDiffuseColor(this.activated ? Color.BLACK : this.color);

				this.setMaterial(newColor);
				this.activated = !this.activated;
				
				e.consume();
			});
		}
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
        this.setTranslateX(x * size * Led.SIZE_MODIFIER);
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
        this.setTranslateY(y * size * Led.SIZE_MODIFIER);
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
        this.setTranslateZ(z * size * Led.SIZE_MODIFIER);
    }

    public double getSize()
    {
        return size;
    }

    public void setSize(double size)
    {
        this.size = size;
        super.setRadius(size);
        setX(getX());
        setY(getY());
        setZ(getZ());
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        PhongMaterial newColor = new PhongMaterial();
        newColor.setSpecularColor(Color.BLACK);
        newColor.setDiffuseColor(color);

        this.setMaterial(newColor);
        this.color = color;
    }
}
