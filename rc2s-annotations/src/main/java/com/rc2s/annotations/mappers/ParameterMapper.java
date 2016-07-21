package com.rc2s.annotations.mappers;

/**
 * ParameterMapper
 * 
 * @author RC2S
 */
public class ParameterMapper
{
    private final String name;
    
    private final String type;
    
    private final String description;

	/**
	 * Constructs a ParameterMapper
	 * 
	 * @param name
	 * @param type
	 * @param description 
	 */
    public ParameterMapper(final String name, final String type, final String description)
    {
        this.name           = name;
        this.type           = type;
        this.description    = description;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }
    
    public String getDescription()
    {
        return description;
    }
}
