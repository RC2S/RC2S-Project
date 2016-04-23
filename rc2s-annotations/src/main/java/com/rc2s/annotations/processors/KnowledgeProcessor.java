package com.rc2s.annotations.processors;

import com.rc2s.annotations.utils.HtmlFile;
import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.mappers.ParameterMapper;
import com.rc2s.annotations.Knowledge;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class KnowledgeProcessor extends AbstractProcessor
{
    private Types       typeUtils;
    private Elements    elementUtils;
    private Filer       filer;      // Création dynamique de classe, package, res, ...
    private Messager    messager;   // Message lors de la compilation
	
	public static List<String> processedClasses = new ArrayList();
	
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        typeUtils       = processingEnv.getTypeUtils();
        elementUtils    = processingEnv.getElementUtils();
        filer           = processingEnv.getFiler();
        messager        = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        for(TypeElement te : annotations)
        {           
            for(Element annotated : roundEnv.getElementsAnnotatedWith(te))
            {
                if(annotated.getKind() == ElementKind.CLASS)
                	classAnalysor(annotated);
                else
                	classAnalysor(annotated.getEnclosingElement());
            }
        }
        
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> annotations = new HashSet<>();
        annotations.add(Knowledge.class.getCanonicalName());
        return annotations;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }
    
    private void classAnalysor(Element mainElement)
    {
    	ElementMapper mainClass;
        List<ElementMapper> fields          = new ArrayList();
        List<ElementMapper> constructors    = new ArrayList();
        List<ElementMapper> methods         = new ArrayList();
        
        if(mainElement.getKind() != ElementKind.CLASS
        || processedClasses.contains(mainElement.getSimpleName().toString()))
        	return;
        
        mainClass = new ElementMapper(
            elementUtils.getPackageOf(mainElement).toString(),
            mainElement.getModifiers(),
            ElementKind.CLASS,
            mainElement.getSimpleName().toString()
        );
        
        processedClasses.add(mainElement.getSimpleName().toString());
        
        // Get Annotation Description for the class
        if(getAnnotationValue(mainElement, "description()") != null)
    		mainClass.setDescription(getAnnotationValue(mainElement, "description()").toString());
        
        // Get elements in the class (fields, constructors, methods)
        for(Element el : mainElement.getEnclosedElements())
        {
            ElementMapper enclosed = new ElementMapper(
                elementUtils.getPackageOf(el).toString(),
                el.getModifiers(),
                el.getKind(),
                el.getSimpleName().toString()
            );
            
            // Get Annotation Description for the current element
            if(getAnnotationValue(el, "description()") != null)
            	enclosed.setDescription(getAnnotationValue(el, "description()").toString());
            
            if(el.getKind() == ElementKind.FIELD)
            {
                enclosed.setReturnType(el.asType().toString());
                fields.add(enclosed);
            }
            else if(el.getKind() == ElementKind.CONSTRUCTOR)
            {
                enclosed.setParameters(getElementParameters(el));
                constructors.add(enclosed);
            }
            else if(el.getKind() == ElementKind.METHOD)
            {
                enclosed.setParameters(getElementParameters(el));
                enclosed.setReturnType(((ExecutableElement)el).getReturnType().toString());
                methods.add(enclosed);
            }
        }
        
        HtmlFile.createJavaDocFile(mainClass, fields, constructors, methods);
        
        messager.printMessage(Diagnostic.Kind.NOTE, "Documentation for the " + mainClass.getName() + " class created.");
    }
    
    private Object getAnnotationValue(Element element, String key)
    {
        Map<? extends ExecutableElement, ? extends AnnotationValue> properties;
        
	    for (AnnotationMirror annotationMirror : element.getAnnotationMirrors())
        {
            properties = elementUtils.getElementValuesWithDefaults(annotationMirror);
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> param : properties.entrySet())
            {
                if(param.getKey().toString().equals(key))
                    return param.getValue();
            }
        }
        return null;
    }
    
    private List<ParameterMapper> getElementParameters(Element el)
    {
    	ExecutableElement execElement           = (ExecutableElement) el;
        List<ParameterMapper> params            = new ArrayList();
        List<AnnotationValue> paramsAnnoDesc    = null;
        int i = 0;
        
        if(getAnnotationValue(el, "parametersDescription()") != null)
        {
            AnnotationValue val = (AnnotationValue) getAnnotationValue(el, "parametersDescription()");
            paramsAnnoDesc = (List<AnnotationValue>)val.getValue();
        }
        
        for(Element param : execElement.getParameters())
        {
            String desc = null;
            if(paramsAnnoDesc != null && i < paramsAnnoDesc.size())
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