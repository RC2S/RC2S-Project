package com.rc2s.common.client.utils;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class Dialog
{
    public static void message(final String msg)
    {
        message("RC2S Client", null, msg, AlertType.INFORMATION);
    }
    
    public static void message(final String title, final String msg)
    {
        message(title, null, msg, AlertType.INFORMATION);
    }
    
    public static void message(final String title, final String msg, final AlertType type)
    {
        message(title, null, msg, type);
    }
    
    public static void message(final String title, final String header, 
            final String msg, final AlertType type)
    {
        Alert alert;
        
        alert = new Alert(type);
        
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);

        alert.showAndWait();
    }
    
    public static ButtonType confirm(final String msg)
    {
        return confirm("RC2S Client", null, msg, null);
    }
    
    public static ButtonType confirm(final String title, final String msg)
    {
        return confirm(title, null, msg, null);
    }
    
    public static ButtonType confirm(final String title, final String msg, final String header)
    {
        return confirm(title, header, msg, null);
    }
    
    public static ButtonType confirm(final String title, final String header, 
            final String msg, final ArrayList<ButtonType> buttons)
    {
        Alert                   alert;
        Optional<ButtonType>    result;
        
        alert = new Alert(AlertType.CONFIRMATION);
        
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        
        if(buttons != null)
            alert.getButtonTypes().addAll(buttons);

        result = alert.showAndWait();
        
        return result.get();
    }
    
    public static String input(final String text)
    {
        return input("RC2S Client", null, text, null);
    }
    
    public static String input(final String title, final String text)
    {
        return input(title, null, text, null);
    }
    
    public static String input(final String title, final String header, final String text)
    {
        return input(title, header, text, null);
    }
    
    public static String input(final String title, final String header, 
            final String text, final String inputContent)
    {
        TextInputDialog     dialog;
        Optional<String>    result;
        
        dialog = new TextInputDialog(inputContent);
        
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);

        result = dialog.showAndWait();
        
        if(result.isPresent())    
            return result.get();
        else
            return null;
    }
}
