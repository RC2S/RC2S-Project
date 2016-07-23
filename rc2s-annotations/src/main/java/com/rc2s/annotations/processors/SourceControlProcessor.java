package com.rc2s.annotations.processors;

import com.rc2s.annotations.SourceControl;
import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.utils.Analysor;
import com.rc2s.annotations.utils.SourceUtil;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

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
    private Elements    elementUtils;

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
		
        elementUtils    = processingEnv.getElementUtils();
    }
    
	/**
	 * Process the annotation
	 * 
	 * Uses the Analysor to get datas from the source
	 * Uses SourceUtil to verify the integrity of the source
	 * 
	 * @param annotations
	 * @param roundEnv
	 * @return true
	 */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv)
    {
		Analysor analysor = new Analysor(elementUtils);
		
		ElementMapper mainClass;
		
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
    
	/**
	 * @return Set<String> supported annotation types
	 */
    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> annotations = new HashSet<>();
        annotations.add(SourceControl.class.getCanonicalName());
        return annotations;
    }
    
	/**
	 * @return SourceVersion the supported source version
	 */
    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }
}
