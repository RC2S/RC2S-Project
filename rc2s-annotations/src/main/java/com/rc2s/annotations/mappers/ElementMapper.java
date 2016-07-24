package com.rc2s.annotations.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

/**
 * ElementMapper
 * 
 * @author RC2S
 */
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
	
	// Fields, Constructors, Methods
	private final List<ElementMapper> fields;
	private final List<ElementMapper> constructors;
	private final List<ElementMapper> methods;
	
	// Class Annotations
	private final List<String> annotations;

	/**
	 * Constructs an ElementMapper
	 * 
	 * @param packageName
	 * @param modifiers
	 * @param kind
	 * @param name 
	 */
    public ElementMapper(final String packageName, final Set<Modifier> modifiers,
        final ElementKind kind, final String name)
    {
        this.packageName    = packageName;
        this.modifiers      = modifiers;
        this.kind           = kind;
        this.name           = name;
		
		this.fields			= new ArrayList<>();
		this.constructors	= new ArrayList<>();
		this.methods		= new ArrayList<>();
		
		this.annotations	= new ArrayList<>();
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
	
	public List<ElementMapper> getFields()
	{
		return fields;
	}

	public List<ElementMapper> getConstructors()
	{
		return constructors;
	}

	public List<ElementMapper> getMethods()
	{
		return methods;
	}
	
	public List<String> getAnnotations()
	{
		return annotations;
	}
	
    public void setReturnType(final String returnType)
    {
        this.returnType = returnType;
    }

    public void setParameters(final List<ParameterMapper> parameters)
    {
        this.parameters = parameters;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }
}
