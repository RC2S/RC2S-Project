package com.rc2s.client.utils;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import com.rc2s.client.Config;

public class Dialog
{
    public static void message(String msg)
    {
        message(Config.APP_NAME, null, msg, AlertType.INFORMATION);
    }
    
    public static void message(String title, String msg)
    {
        message(title, null, msg, AlertType.INFORMATION);
    }
    
    public static void message(String title, String msg, AlertType type)
    {
        message(title, null, msg, type);
    }
    
    public static void message(String title, String header, String msg, AlertType type)
    {
        Alert alert;
        
        alert = new Alert(type);
        
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);

        alert.showAndWait();
    }
    
    public static ButtonType confirm(String msg)
    {
        return confirm(Config.APP_NAME, null, msg, null);
    }
    
    public static ButtonType confirm(String title, String msg)
    {
        return confirm(title, null, msg, null);
    }
    
    public static ButtonType confirm(String title, String msg, String header)
    {
        return confirm(title, header, msg, null);
    }
    
    public static ButtonType confirm(String title, String header, String msg, ArrayList<ButtonType> buttons)
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
    
    public static String input(String text)
    {
        return input(Config.APP_NAME, null, text, null);
    }
    
    public static String input(String title, String text)
    {
        return input(title, null, text, null);
    }
    
    public static String input(String title, String header, String text)
    {
        return input(title, header, text, null);
    }
    
    public static String input(String title, String header, String text, String inputContent)
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
