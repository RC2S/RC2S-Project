package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.mappers.ParameterMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;

/**
 * Analysor (class analysor)
 * 
 * @author RC2S
 */
public class Analysor
{
	private final Elements elementUtils;

	public static List<String> processedClasses = new ArrayList();
	
	/**
	 * Construct an Analysor
	 * 
	 * @param els
	 * @param msgr 
	 */
	public Analysor(final Elements els)
	{
		this.elementUtils = els;
	}
	
	/**
	 * classAnalysor
	 * 
	 * @param mainElement
	 * @return ElementMapper containing class elements 
	 */
	public ElementMapper classAnalysor(final Element mainElement)
    {
    	ElementMapper mainClass;
        
        if (mainElement.getKind() != ElementKind.CLASS
        || processedClasses.contains(mainElement.getSimpleName().toString()))
        	return null;
        
        mainClass = new ElementMapper(
            elementUtils.getPackageOf(mainElement).toString(),
            mainElement.getModifiers(),
            ElementKind.CLASS,
            mainElement.getSimpleName().toString()
        );
        
        processedClasses.add(mainElement.getSimpleName().toString());
        
        // Get Annotation Description for the class
        if (getAnnotationValue(mainElement, "description()") != null)
    		mainClass.setDescription(getAnnotationValue(mainElement, "description()").toString());
        
		// Get all Annotations on class
		if (getClassAnnotations(mainElement) != null)
			mainClass.getAnnotations().addAll(getClassAnnotations(mainElement));
		
        // Get elements in the class (fields, constructors, methods)
        for (Element el : mainElement.getEnclosedElements())
        {
            ElementMapper enclosed = new ElementMapper(
                elementUtils.getPackageOf(el).toString(),
                el.getModifiers(),
                el.getKind(),
                el.getSimpleName().toString()
            );
            
            // Get Annotation Description for the current element
            if (getAnnotationValue(el, "description()") != null)
            	enclosed.setDescription(getAnnotationValue(el, "description()").toString());
            
            if (el.getKind() == ElementKind.FIELD)
            {
                enclosed.setReturnType(el.asType().toString());
                mainClass.getFields().add(enclosed);
            }
            else if (el.getKind() == ElementKind.CONSTRUCTOR)
            {
                enclosed.setParameters(getElementParameters(el));
                mainClass.getConstructors().add(enclosed);
            }
            else if (el.getKind() == ElementKind.METHOD)
            {
                enclosed.setParameters(getElementParameters(el));
                enclosed.setReturnType(((ExecutableElement)el).getReturnType().toString());
                mainClass.getMethods().add(enclosed);
            }
        }
        
        return mainClass;
    }
	
	/**
	 * getAnnotationValue
	 * 
	 * @param element
	 * @param key
	 * @return Object value of the annotation 
	 */
	private Object getAnnotationValue(final Element element, final String key)
    {
        Map<? extends ExecutableElement, ? extends AnnotationValue> properties;
        
	    for (AnnotationMirror annotationMirror : element.getAnnotationMirrors())
        {
			properties = elementUtils.getElementValuesWithDefaults(annotationMirror);
			
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> param : properties.entrySet())
            {
				if (param.getKey().toString().equals(key))
                    return param.getValue();
            }
        }
        return null;
    }
	
	/**
	 * getClassAnnotations
	 * 
	 * @param element
	 * @return List<String> all annotations on the class 
	 */
	private List<String> getClassAnnotations(final Element element)
	{
		Map<? extends ExecutableElement, ? extends AnnotationValue> properties;
        List<String> annotations = new ArrayList<>();
		
	    for (AnnotationMirror annotationMirror : element.getAnnotationMirrors())
        {
			if (!annotations.contains(annotationMirror.getAnnotationType().toString()))
				annotations.add(annotationMirror.getAnnotationType().toString());
		}
		
		if (annotations.isEmpty())
			return null;
		
		return annotations;
	}
    
	/**
	 * getElementParameters
	 * 
	 * @param el
	 * @return List<ParameterMapper> tuples representing the class elements  
	 */
    private List<ParameterMapper> getElementParameters(final Element el)
    {
    	ExecutableElement execElement           = (ExecutableElement) el;
        List<ParameterMapper> params            = new ArrayList();
        List<AnnotationValue> paramsAnnoDesc    = null;
        int i = 0;
        
        if (getAnnotationValue(el, "parametersDescription()") != null)
        {
            AnnotationValue val = (AnnotationValue) getAnnotationValue(el, "parametersDescription()");
            paramsAnnoDesc = (List<AnnotationValue>) val.getValue();
        }
        
        for (Element param : execElement.getParameters())
        {
            String desc = null;
            if (paramsAnnoDesc != null && i < paramsAnnoDesc.size())
                desc = (String) paramsAnnoDesc.get(i++).getValue();
            
            params.add(new ParameterMapper(
                param.getSimpleName().toString(),
                param.asType().toString(),
                desc
            ));
        }
        
        return params;
    }
}
