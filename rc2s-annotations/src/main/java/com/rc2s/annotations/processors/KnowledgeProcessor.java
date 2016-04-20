package com.rc2s.annotations.processors;

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
        ElementMapper mainClass             = null;
        List<ElementMapper> fields          = new ArrayList();
        List<ElementMapper> constructors    = new ArrayList();
        List<ElementMapper> methods         = new ArrayList();
        
        for(TypeElement te : annotations)
        {           
            for(Element annotated : roundEnv.getElementsAnnotatedWith(te))
            {
                // Annotated element is the class
                if(annotated.getKind() == ElementKind.CLASS)
                {
                    mainClass = new ElementMapper(
                        elementUtils.getPackageOf(annotated).toString(),
                        annotated.getModifiers(),
                        ElementKind.CLASS,
                        annotated.getSimpleName().toString()
                    );
                    mainClass.setDescription(getAnnotationValue(annotated, "description()"));
                    
                    for(Element el : annotated.getEnclosedElements())
                    {
                        ElementMapper enclosed = new ElementMapper(
                            elementUtils.getPackageOf(el).toString(),
                            el.getModifiers(),
                            el.getKind(),
                            el.getSimpleName().toString());
                        
                        if(el.getKind() == ElementKind.FIELD)
                        {
                            enclosed.setDescription(getAnnotationValue(el, "description()"));
                            enclosed.setReturnType(el.asType().toString());
                            fields.add(enclosed);
                        }
                        else if(el.getKind() == ElementKind.CONSTRUCTOR)
                        {
                            ExecutableElement constructorElement = (ExecutableElement) el;
                            List<Parameter> params = new ArrayList();
                            
                            for(Element param : constructorElement.getParameters())
                            {
                                params.add(new Parameter(
                                    param.getSimpleName().toString(),
                                    param.asType().toString()
                                ));
                            }
                            enclosed.setParameters(params);
                            constructors.add(enclosed);
                        }
                        else if(el.getKind() == ElementKind.METHOD)
                        {
                            ExecutableElement methodElement = (ExecutableElement) el;
                            List<Parameter> params = new ArrayList();
                            
                            for(Element param : methodElement.getParameters())
                            {
                                params.add(new Parameter(
                                    param.getSimpleName().toString(),
                                    param.asType().toString()
                                ));
                            }
                            enclosed.setParameters(params);
                            enclosed.setReturnType(methodElement.getReturnType().toString());
                            methods.add(enclosed);
                        }
                    }
                }
            }
            
            messager.printMessage(Diagnostic.Kind.NOTE, "Main Class");
            messager.printMessage(Diagnostic.Kind.NOTE, mainClass.toString());

            messager.printMessage(Diagnostic.Kind.NOTE, "Fields");
            for(ElementMapper el : fields)
                messager.printMessage(Diagnostic.Kind.NOTE, el.toString());

            messager.printMessage(Diagnostic.Kind.NOTE, "Constructors");
            for(ElementMapper el : constructors)
                messager.printMessage(Diagnostic.Kind.NOTE, el.toString());

            messager.printMessage(Diagnostic.Kind.NOTE, "Methods");
            for(ElementMapper el : methods)
                messager.printMessage(Diagnostic.Kind.NOTE, el.toString());
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
    
    private String getAnnotationValue(Element element, String key)
    {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors())
        {
            Map<? extends ExecutableElement, ? extends AnnotationValue> properties;
            
            properties = elementUtils.getElementValuesWithDefaults(annotationMirror);
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> param : properties.entrySet())
            {
                if(param.getKey().toString().equals(key))
                    return param.getValue().toString();
            }
        }
        return null;
    }
}
