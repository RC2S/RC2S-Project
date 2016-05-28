package com.rc2s.annotations.processors;

import com.rc2s.annotations.SourceControl;
import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.utils.Analysor;
import com.rc2s.annotations.utils.SourceControlException;
import com.rc2s.annotations.utils.SourceUtil;
import java.io.IOException;
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

public class SourceControlProcessor extends AbstractProcessor
{
    private Types       typeUtils;
    private Elements    elementUtils;
    private Filer       filer;
    private Messager    messager;

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
		Analysor analysor = new Analysor(elementUtils, messager);
		ElementMapper mainClass = null;
		
        for (TypeElement te : annotations)
        {
            for (Element annotated : roundEnv.getElementsAnnotatedWith(te))
            {
				if (annotated.getKind() == ElementKind.CLASS)
				{
					mainClass = analysor.classAnalysor(annotated);
					
					try
					{
						SourceUtil.verifySource(mainClass);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
            }
        }
		
        return true;
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> annotations = new HashSet<>();
        annotations.add(SourceControl.class.getCanonicalName());
        return annotations;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }
	
	private void tests(Element annotated)
	{
		// Get the name of annotated element
		messager.printMessage(Diagnostic.Kind.NOTE, annotated.getSimpleName()); // Main

		// Get the type of annotated element
		messager.printMessage(Diagnostic.Kind.NOTE, "type : " + annotated.getKind().toString()); // CLASS

		// Get the full name of annotated element
		messager.printMessage(Diagnostic.Kind.NOTE, "full name with package : " + annotated.asType().toString()); // com.rc2s.client.Main

		// Get all elements into the annotated element (ex : methods, fields, constructors in the annotated class)
		List<? extends Element> el = annotated.getEnclosedElements();
		for (Element e : el) {
			messager.printMessage(Diagnostic.Kind.NOTE, "test : " + e.getSimpleName().toString()); // <init>, stage, main, start, stop
		}

		// Get the parent element of the annotated element 
		// (package name if annotated element is a class, class name if annotated element is field, constructor, method)
		messager.printMessage(Diagnostic.Kind.NOTE, "enclosing : " + annotated.getEnclosingElement().getSimpleName().toString()); // client

		// Get the Package of annotated element
		messager.printMessage(Diagnostic.Kind.NOTE, elementUtils.getPackageOf(annotated).toString()); // com.rc2s.client

		// Get the annotation properties for annotated element
		for (AnnotationMirror annotationMirror : annotated.getAnnotationMirrors())
		{
			Map<? extends ExecutableElement, ? extends AnnotationValue> properties = elementUtils.getElementValuesWithDefaults(annotationMirror);
			for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> param : properties.entrySet())
			{
				AnnotationValue val = param.getValue();
				messager.printMessage(Diagnostic.Kind.NOTE, val.toString());
			}
		}
	}
}
