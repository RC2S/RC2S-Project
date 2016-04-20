package com.rc2s.annotations.processors;

public class Parameter
{
    private final String name;
    
    private final String type;
    
    private final String description;

    public Parameter(String name, String type, String description)
    {
        this.name = name;
        this.type = type;
        this.description = description;
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

    @Override
    public String toString()
    {
        return "Parameter{" + "name=" + name + ", type=" + type + ", description=" + description + '}';
    }
}
