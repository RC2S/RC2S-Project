package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AccessManagementController
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane accessAnchorPane;

    @FXML
    private GridPane anchorGridPane;
/*
    @FXML
    private TableView<?> gridTableView;

    @FXML
    private TableColumn<?, ?> usernameTableCol;

    @FXML
    private TableColumn<?, ?> statusTableCol;

    @FXML
    private TableColumn<?, ?> roleTableCol;

    @FXML
    private TableColumn<?, ?> cubicAccessTableCol;

    @FXML
    private TableColumn<?, ?> updateTableCol;

    @FXML
    private TableColumn<?, ?> deleteTableCol;
*/
    @FXML
    private BorderPane gridBorderPane;

    @FXML
    private Label bordPaneLabel;

    @FXML
    private GridPane borderPaneGridPane;

    @FXML
    private Label gridLabelUsername;

    @FXML
    private Label gridLabelPassword;

    @FXML
    private Label gridLabelConfirmPass;

    @FXML
    private TextField gridTextFieldUsername;

    @FXML
    private PasswordField gridPasswordFieldPassword;

    @FXML
    private PasswordField gridPasswordFieldConfirmPass;

    @FXML
    private Label gridLabelRole;

    @FXML
    private Label gridLabelCubicAccess;
/*
    @FXML
    private ComboBox<?> gridComboBoxRole;

    @FXML
    private ComboBox<?> gridComboBoxCubicAccess;
*/
    @FXML
    private Button gridButton;

    @FXML
    void initialize() {
        assert accessAnchorPane != null : "fx:id=\"accessAnchorPane\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert anchorGridPane != null : "fx:id=\"anchorGridPane\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert gridTableView != null : "fx:id=\"gridTableView\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert usernameTableCol != null : "fx:id=\"usernameTableCol\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert statusTableCol != null : "fx:id=\"statusTableCol\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert roleTableCol != null : "fx:id=\"roleTableCol\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert cubicAccessTableCol != null : "fx:id=\"cubicAccessTableCol\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert updateTableCol != null : "fx:id=\"updateTableCol\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert deleteTableCol != null : "fx:id=\"deleteTableCol\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridBorderPane != null : "fx:id=\"gridBorderPane\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert bordPaneLabel != null : "fx:id=\"bordPaneLabel\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert borderPaneGridPane != null : "fx:id=\"borderPaneGridPane\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridLabelUsername != null : "fx:id=\"gridLabelUsername\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridLabelPassword != null : "fx:id=\"gridLabelPassword\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridLabelConfirmPass != null : "fx:id=\"gridLabelConfirmPass\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridTextFieldUsername != null : "fx:id=\"gridTextFieldUsername\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridPasswordFieldPassword != null : "fx:id=\"gridPasswordFieldPassword\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridPasswordFieldConfirmPass != null : "fx:id=\"gridPasswordFieldConfirmPass\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridLabelRole != null : "fx:id=\"gridLabelRole\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridLabelCubicAccess != null : "fx:id=\"gridLabelCubicAccess\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert gridComboBoxRole != null : "fx:id=\"gridComboBoxRole\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        //assert gridComboBoxCubicAccess != null : "fx:id=\"gridComboBoxCubicAccess\" was not injected: check your FXML file 'AccessManagementView.fxml'.";
        assert gridButton != null : "fx:id=\"gridButton\" was not injected: check your FXML file 'AccessManagementView.fxml'.";

    }
}
