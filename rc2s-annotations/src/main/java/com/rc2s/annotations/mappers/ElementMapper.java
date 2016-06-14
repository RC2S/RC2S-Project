package com.rc2s.annotations.mappers;

import java.util.List;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

public class ElementMapper
{
    // Element Package name
    private final String packageName;
    
    // Element modifiers (public, private, protected)
    private final Set<Modifier> modifiers;
    
    // Element returnType (primitive, object)
    private String returnType;
    
    // Element kind (class, package, method, field)
    private final ElementKind kind;
    
    // Element name
    private final String name;
    
    // Element parameters
    private List<ParameterMapper> parameters;
    
    // Annotated element description
    private String description;

    public ElementMapper(String packageName, Set<Modifier> modifiers,
        ElementKind kind, String name)
    {
        this.packageName    = packageName;
        this.modifiers      = modifiers;
        this.kind           = kind;
        this.name           = name;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public Set<Modifier> getModifiers()
    {
        return modifiers;
    }
    
    public String getReturnType()
    {
        return returnType;
    }

    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }

    public ElementKind getKind()
    {
        return kind;
    }

    public String getName()
    {
        return name;
    }

    public List<ParameterMapper> getParameters()
    {
        return parameters;
    }

    public String getDescription()
    {
        return description;
    }

    public void setParameters(List<ParameterMapper> parameters)
    {
        this.parameters = parameters;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}