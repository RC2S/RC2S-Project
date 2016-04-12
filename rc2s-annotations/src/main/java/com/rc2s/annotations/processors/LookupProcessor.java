package com.rc2s.annotations.processors;

import com.rc2s.annotations.Lookup;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class LookupProcessor extends AbstractProcessor
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
        for(Element annotated : roundEnv.getElementsAnnotatedWith(Lookup.class))
        {
            System.out.println("YEAAH annotation");
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> annotations = new HashSet<>();
        annotations.add(Lookup.class.getCanonicalName());
        return annotations;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }
}
