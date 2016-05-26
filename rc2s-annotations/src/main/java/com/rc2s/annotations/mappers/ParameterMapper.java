package com.rc2s.annotations.mappers;

public class ParameterMapper
{
    private final String name;
    
    private final String type;
    
    private final String description;

    public ParameterMapper(String name, String type, String description)
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
