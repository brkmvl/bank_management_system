package ui;

import bank.IncomingTransfer;
import bank.PrivateBank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainViewController {

    @FXML
    private ListView<String> accountList;

    private PrivateBank bank;

    @FXML
    public void initialize() {

        try {
            bank = new PrivateBank("MyBank", 0.1, 0.3, "testdata");
            List<String> accounts = bank.getAllAccounts();
            accountList.getItems().setAll(accounts);

            ContextMenu contextMenu = new ContextMenu();
            MenuItem selectItem = new MenuItem("Auswählen");
            MenuItem deleteItem = new MenuItem("Löschen");
            contextMenu.getItems().addAll(selectItem, deleteItem);

            // Menü der ListView zuweisen
            accountList.setContextMenu(contextMenu);

            selectItem.setOnAction(e -> selectAccount());
            deleteItem.setOnAction(e -> deleteAccount());

        } catch (Exception e) {
            showError("Fehler beim Laden", e.getMessage());
        }
    }

    private void deleteAccount() {

        String selected = accountList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Fehler", "Kein Account ausgewählt!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Account löschen");
        confirm.setHeaderText(null);
        confirm.setContentText(
                "Möchten Sie den Account \"" + selected + "\" wirklich löschen?"
        );

        Optional<ButtonType> result = confirm.showAndWait();//	Es läuft nichts weiter, bis der Benutzer reagiert

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                bank.deleteAccount(selected);
                accountList.getItems().remove(selected);

            } catch (Exception e) {
                showError("Fehler beim Löschen", e.getMessage());
            }
        }
    }



    private void selectAccount() {
        String selectedAccount = accountList.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showError("Fehler", "Kein Account ausgewählt!");
            return;
        }
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
            Parent anwendung=loader.load();
            AccountViewController controller = loader.getController();
            controller.initData(bank, selectedAccount);
            Scene scene=new Scene(anwendung);
           Stage stage=(Stage) accountList.getScene().getWindow();
           stage.setScene(scene);

        }
        catch (Exception e){
            showError("Fehler  exception ", e.getMessage());
        }


    }

    @FXML
    private void createAccount() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Neues Bankkonto ");
        dialog.setHeaderText("Bankkonto eröffnen");
        dialog.setContentText("Bitte geben Sie den Namen des Bankkontos ein:");


        Optional<String> result = dialog.showAndWait();
//„Wenn ein Wert vorhanden ist, dann führe diesen Code aus.“
        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showError("Fehler", "Der Kontoname darf nicht leer sein.");
                return;
            }
            try {
                bank.createAccount(name);
                accountList.getItems().add(name);

                showInfo("Erfolgreich","Bankkonto"+name+" erfolgreichrt eroeffnet " );

            } catch (Exception e) {

                showError("FEHLER", e.getMessage());
            }
        });
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}