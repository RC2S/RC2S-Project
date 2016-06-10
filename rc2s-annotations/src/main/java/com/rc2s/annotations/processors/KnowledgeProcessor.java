package com.rc2s.annotations.processors;

import com.rc2s.annotations.Knowledge;
import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.utils.Analysor;
import com.rc2s.annotations.utils.HtmlFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
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
		Analysor analysor = new Analysor(elementUtils, messager);
		ElementMapper mainClass = null;
		
        for(TypeElement te : annotations)
        {           
            for(Element annotated : roundEnv.getElementsAnnotatedWith(te))
            {
                if(annotated.getKind() == ElementKind.CLASS)
                	mainClass = analysor.classAnalysor(annotated);
                else
                	mainClass = analysor.classAnalysor(annotated.getEnclosingElement());
            }
        }
        
		HtmlFile.createJavaDocFile(mainClass);
		
		messager.printMessage(Diagnostic.Kind.NOTE, "Documentation for the " + mainClass.getName() + " class created.");
		
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