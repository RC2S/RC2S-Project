package com.rc2s.client.components;

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
	private LedEvent ledEvent;
	
    public Led(final double x, final double y, final double z, 
            final double size, final boolean activated, 
            final Color color, final LedEvent ledEvent)
    {
        super(size);
        
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.activated = activated;
        this.color = (color != null) ? color : Color.BLACK;
		this.ledEvent = ledEvent;
        
        this.setTranslateX(x * size * SIZE_MODIFIER);
        this.setTranslateY(y * size * SIZE_MODIFIER);
        this.setTranslateZ(z * size * SIZE_MODIFIER);
        
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(this.color);
        material.setSpecularColor(Color.BLACK);
        this.setMaterial(material);
		
		if(this.ledEvent != null)
		{
			this.setOnMouseClicked((e) -> {
				this.setActivated(!isActivated());
				this.ledEvent.setLed(this);
				this.ledEvent.run();
				e.consume();
			});
		}
    }

    public double getX()
    {
        return x;
    }

    public void setX(final double x)
    {
        this.x = x;
        this.setTranslateX(x * size * Led.SIZE_MODIFIER);
    }

    public double getY()
    {
        return y;
    }

    public void setY(final double y)
    {
        this.y = y;
        this.setTranslateY(y * size * Led.SIZE_MODIFIER);
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(final double z)
    {
        this.z = z;
        this.setTranslateZ(z * size * Led.SIZE_MODIFIER);
    }

    public double getSize()
    {
        return size;
    }

    public void setSize(final double size)
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

    public void setActivated(final boolean activated)
    {
		PhongMaterial newColor = new PhongMaterial();
		newColor.setSpecularColor(Color.BLACK);
		newColor.setDiffuseColor(activated ? this.color : Color.WHITE);

		this.setMaterial(newColor);
        this.activated = activated;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(final Color color)
    {
        PhongMaterial newColor = new PhongMaterial();
        newColor.setSpecularColor(Color.BLACK);
        newColor.setDiffuseColor(color);

        this.setMaterial(newColor);
        this.color = color;
    }
}
