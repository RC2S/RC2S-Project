package com.rc2s.annotations.processors;

import com.rc2s.annotations.SourceControl;
import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.utils.Analysor;
import com.rc2s.annotations.utils.SourceUtil;
import java.util.HashSet;
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

/**
 * SourceControlProcessor
 * 
 * Processes the annotation @SourceControl
 * See com.rc2s.utils.SourceUtil for more details
 * 
 * @author RC2S
 */
public class SourceControlProcessor extends AbstractProcessor
{
    private Types       typeUtils;
    private Elements    elementUtils;
    private Filer       filer;
    private Messager    messager;

	/**
	 * Initialize SourceControlProcessor
	 * 
	 * Setup the environment to access Class informations
	 * 
	 * @param processingEnv 
	 */
    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        typeUtils       = processingEnv.getTypeUtils();
        elementUtils    = processingEnv.getElementUtils();
        filer           = processingEnv.getFiler();
        messager        = processingEnv.getMessager();
    }
    
	/**
	 * Process the annotation
	 * 
	 * Uses the Analysor to get datas from the source
	 * Uses SourceUtil to verify the integrity of the source
	 * 
	 * @param annotations
	 * @param roundEnv
	 * @return 
	 */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv)
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
						SourceUtil sourceUtil = new SourceUtil();
						sourceUtil.verifySource(mainClass);
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
}
