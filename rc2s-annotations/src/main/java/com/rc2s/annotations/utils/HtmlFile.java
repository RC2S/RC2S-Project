package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.mappers.ParameterMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlFile
{
	public static boolean createPluginInfosFile()
	{
		return false;
	}
	
	public static boolean createJavaDocFile(ElementMapper mainClass, List<ElementMapper> fields, 
		List<ElementMapper> constructors, List<ElementMapper> methods)
	{
		Path path                       = Paths.get("rc2s-doc/" + mainClass.getName() + ".html");
        StringBuilder builder 			= new StringBuilder();
		List<ElementMapper> mergedList 	= new ArrayList();
		
		mergedList.addAll(constructors);
		mergedList.addAll(methods);
		
		builder.append("<html><head>");
		builder.append("<title>" + mainClass.getName() + " Documentation</title>");
		builder.append("</head><body>");
		builder.append(generateClassInfos(mainClass));
		builder.append(generateFieldsSummary(fields));
		builder.append(generateMethodsSummary(mergedList));
		builder.append(generateDetails(mergedList));
		builder.append("</body></html>");
        
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, String.valueOf(builder).getBytes());
        } catch (IOException ex) {
            Logger.getLogger(HtmlFile.class.getName()).log(Level.SEVERE, null,
                ex);
            return false;
        }
		return true;
	}
	
	private static String generateClassInfos(ElementMapper mainClass)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("<div class=\"classInfos\">");
		if(mainClass.getPackageName() != null)
			builder.append("<div class=\"packageName\">" + mainClass.getPackageName() + "</div>");
		
		if(mainClass.getName() != null)
			builder.append("<h2 class=\"className\">Class " + mainClass.getName() + "</h2>");
		
		if(mainClass.getDescription() != null)
			builder.append("<div class=\"classDesc\">" + mainClass.getDescription() + "</div>");
		builder.append("</div>");
		
		return builder.toString();
	}
	
	private static String generateFieldsSummary(List<ElementMapper> fields)
	{
		StringBuilder builder = new StringBuilder();
		
		if(!fields.isEmpty())
		{
			// Title Section
			builder.append("<div class=\"fieldsSummary\">");
			builder.append("<h3>Fields Summary</h3>");
			
			// Table Section
			builder.append("<table><thead><th>Modifier and Type</th><th>Field</th></thead><tbody>");
			
			for(ElementMapper el : fields)
			{
				// First column : modifier + type
				builder.append("<tr><td>" + el.getModifiers().toString() + " " + el.getReturnType() + "</td>");
				
				// Second column : Name + Description
				builder.append("<td>" + el.getName());
				if(el.getDescription() != null)
					builder.append("<div class=\"fieldDesc\">" + el.getDescription() + "</div>");
				builder.append("</td></tr>");
			}
			builder.append("</tbody></table></div>");
		}
		
		return builder.toString();
	}
	
	private static String generateMethodsSummary(List<ElementMapper> list)
	{
		StringBuilder builder = new StringBuilder();
		
		if(!list.isEmpty())
		{
			// Title Section
			builder.append("<div class=\"methodsSummary\">");
			builder.append("<h3>Methods Summary</h3>");
			
			// Table Section
			builder.append("<table><thead><th>Modifier and Type</th><th>Method</th></thead><tbody>");
			
			// Constructors / Methods
			for(ElementMapper el : list)
			{
				builder.append("<tr>");
				
				// First column : modifier + returnType
				builder.append("<td>" + el.getModifiers().toString() + " " + el.getReturnType() + "</td>");
				
				// Second column : name + parameter returnType + parameter Name
				builder.append("<td>" + el.getName() + "(");
				if(el.getParameters() != null)
					for(ParameterMapper parameter : el.getParameters())
						builder.append(parameter.getType() + " " + parameter.getName() + ",");
				builder.append(")</td>");
				builder.append("</tr>");
			}
			builder.append("</tbody></table></div>");
		}
		return builder.toString();
	}
	
	private static String generateDetails(List<ElementMapper> list)
	{
		StringBuilder builder = new StringBuilder();
		
		if(!list.isEmpty())
		{
			// Title Section
			builder.append("<div class=\"methodsDetails\">");
			builder.append("<h3>Methods Details</h3>");
			
			// Constructors / Methods
			for(ElementMapper el : list)
			{
				// Name Section
				builder.append("<div class=\"method\">");
				builder.append("<h4>" + el.getName() + "</h4>");
				
				// How to use Section
				builder.append("<div class=\"methodUse\">");
				builder.append(el.getReturnType() + " " + el.getName() + "(");
				if(el.getParameters() != null)
				{
					// Virgule à revoir
					for(ParameterMapper parameter : el.getParameters())
						builder.append(parameter.getType() + " " + parameter.getName() + ",");
				}
				builder.append(")");
				builder.append("</div>");
				
				// Description Section
				if(el.getDescription() != null)
					builder.append("<div class=\"methodDesc\">" + el.getDescription() + "</div>");
				
				// Parameters Section
				if(el.getParameters() != null)
				{
					builder.append("<div class=\"parameters\">");
					for(ParameterMapper parameter : el.getParameters())
						builder.append("<div>" + parameter.getName() + ((parameter.getDescription() != null) ? " - " + parameter.getDescription() : "") + "</div>");
					builder.append("</div>");
				}
				builder.append("</div>");
			}
			builder.append("</div>");
		}
		return builder.toString();
	}
}