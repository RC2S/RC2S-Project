package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AccessManagementController
{
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
    void initialize(URL location, ResourceBundle resources) {}
}
