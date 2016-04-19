package com.rc2s.annotations.processors;

import com.rc2s.annotations.Knowledge;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class KnowledgeProcessor extends AbstractProcessor
{
    private Types       typeUtils;
    private Elements    elementUtils;
    private Filer       filer; // Création dynamique de classe, package, res, ...
    private Messager    messager; // Message lors de la compilation

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
                // Get the name of annotated element
                messager.printMessage(Diagnostic.Kind.NOTE, annotated.getSimpleName());
                
                // Get the type of annotated element
                messager.printMessage(Diagnostic.Kind.NOTE, "type : " + annotated.getKind().toString());
                
                // Get the full name of annotated element
                messager.printMessage(Diagnostic.Kind.NOTE, "full name with package : " + annotated.asType().toString());
                
                // Get all elements into the annotated element (ex : methods, fields, constructors in the annotated class)
                List<? extends Element> el = annotated.getEnclosedElements();
                for(Element e : el) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "test : " + e.getSimpleName().toString());
                }
                
                // Get the parent element of the annotated element 
                // (package name if annotated element is a class, class name if annotated element is field, constructor, method)
                messager.printMessage(Diagnostic.Kind.NOTE, "enclosing : " + annotated.getEnclosingElement().getSimpleName().toString());

                // Get the Package of annotated element
                messager.printMessage(Diagnostic.Kind.NOTE,elementUtils.getPackageOf(annotated).toString());
                
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
}
