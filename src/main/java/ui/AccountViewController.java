package ui;
import bank.PrivateBank;
import bank.Transaction;
import bank.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

    public class AccountViewController {

        @FXML
        private Label accountNameLabel;

        @FXML
        private Label balanceLabel;

        @FXML
        private ListView<Transaction> transactionList;

        private PrivateBank bank;
        private String accountName;



        public void initData(PrivateBank bank, String accountName) {
            this.bank = bank;
            this.accountName = accountName;

            accountNameLabel.setText("Kontoname: " + accountName);

            updateBalance();
            updateTransactions();
            setupContextMenu();
        }

        private void updateBalance() {
            double balance = bank.getAccountBalance(accountName);
            balanceLabel.setText(String.format("Kontostand: %.2f €", balance));
        }

        private void updateTransactions() {
            List<Transaction> transactions = bank.getTransactions(accountName);
            transactionList.getItems().setAll(transactions);//was wird angezeigt
        }


        @FXML
        private void goBack() {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
                Parent anwendung = loader.load();
                Scene scene = new Scene(anwendung);
                Stage stage = (Stage) accountNameLabel.getScene().getWindow();
                stage.setScene(scene);

            } catch (Exception e) {
                showError("FEHLER beim goback", e.getMessage());
            }
        }



        @FXML
        private void sortAsc() {
            List<Transaction> sorted = bank.getTransactionsSorted(accountName, true);
            transactionList.getItems().setAll(sorted);
        }

        @FXML
        private void sortDesc() {
            List<Transaction> sorted = bank.getTransactionsSorted(accountName, false);
            transactionList.getItems().setAll(sorted);
        }

        @FXML
        private void sortPos() {
            List<Transaction> vorzeichen = bank.getTransactionsByType(accountName, true);
            transactionList.getItems().setAll(vorzeichen);
        }

        @FXML
        private void sortNeg() {
            List<Transaction> vorzeichen = bank.getTransactionsByType(accountName, false);
            transactionList.getItems().setAll(vorzeichen);
        }



        private void setupContextMenu() {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Transaktion löschen");
            contextMenu.getItems().add(deleteItem);
            transactionList.setContextMenu(contextMenu);
            deleteItem.setOnAction(e -> deleteTransaction());
        }

        @FXML
        private void deleteTransaction() {
            Transaction transaction = transactionList.getSelectionModel().getSelectedItem();//was ist ausgewählt
            if (transaction == null) {
                showError("Fehler", "Keine Transaktion ausgewählt");
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Transaktion löschen");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Möchten Sie diese Transaktion wirklich löschen?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    bank.removeTransaction(accountName, transaction);
                    updateTransactions();
                    updateBalance();
                } catch (Exception e) {
                    showError("Fehler beim Löschen", e.getMessage());
                }
            }
        }

        @FXML
        private void addTransaction() {

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Neue Transaktion");
            dialog.setHeaderText("Neue Transaktion fuer "+accountName+" erstellen");

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(
                    okButton, ButtonType.CANCEL
            );

            // Typ-Auswahl
            ChoiceBox<String> typeChoice = new ChoiceBox<>();
            typeChoice.getItems().addAll("Payment", "Transfer");
            typeChoice.setValue("Payment");

            TextField dateField = new TextField();
            TextField amountField = new TextField();
            TextField descField = new TextField();
            TextField senderField = new TextField();
            TextField receiverField = new TextField();


            senderField.setDisable(true);
            receiverField.setDisable(true);

            typeChoice.setOnAction(e -> {
                if (typeChoice.getValue().equals("Transfer")) {
                    senderField.setDisable(false);
                    receiverField.setDisable(false);
                } else {
                    senderField.setDisable(true);
                    receiverField.setDisable(true);
                }
            });


            GridPane grid = new GridPane();
            grid.setHgap(10);//horizontaler
            grid.setVgap(10);//vertikal

            grid.add(new Label("Typ:"), 0, 0);
            grid.add(typeChoice, 1, 0);

            grid.add(new Label("Datum:"), 0, 1);
            grid.add(dateField, 1, 1);

            grid.add(new Label("Betrag:"), 0, 2);
            grid.add(amountField, 1, 2);

            grid.add(new Label("Beschreibung:"), 0, 3);
            grid.add(descField, 1, 3);

            grid.add(new Label("Sender:"), 0, 4);
            grid.add(senderField, 1, 4);

            grid.add(new Label("Empfänger:"), 0, 5);
            grid.add(receiverField, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.showAndWait().ifPresent(result -> {
                if (result == okButton) {
                    try {
                        String type = typeChoice.getValue();


                        if (dateField.getText().trim().isEmpty() ||
                                amountField.getText().trim().isEmpty() ||
                                descField.getText().trim().isEmpty()) {

                            showError("Fehler",
                                    "Datum, Betrag und Beschreibung sind Pflichtfelder.");
                            return;
                        }


                        if (type.equals("Transfer")) {
                            if (senderField.getText().trim().isEmpty() ||
                                    receiverField.getText().trim().isEmpty()) {

                                showError("Fehler",
                                        "Sender und Empfänger sind bei Transfer Pflicht.");
                                return;
                            }
                        }

                        double amount = Double.parseDouble(amountField.getText());

                        Transaction transaction;

                        if (type.equals("Payment")) {

                            if (amount == 0) {
                                showError("Fehler", "Der Betrag darf nicht 0 sein.");
                                return;
                            }

                            transaction = new Payment(
                                    dateField.getText(),
                                    amount, // 🔥 bleibt genau wie eingegeben
                                    descField.getText(),
                                    bank.get_incomingInterest(),
                                    bank.get_outgoingInterest()
                            );
                        } else {
                            if (amount <= 0) {
                                showError(
                                        "Fehler",
                                        "Bei Transfer muss der Betrag positiv eingegeben werden."
                                );
                                return;
                            }

                            String sender = senderField.getText().trim();
                            String receiver = receiverField.getText().trim();


                            if (!sender.equals(accountName) && !receiver.equals(accountName)) {
                                showError(
                                        "Fehler",
                                        "Sender oder Empfänger muss dem aktuellen Konto entsprechen!"
                                );
                                return;
                            }

                            if (sender.equals(accountName)) {
                                transaction = new OutgoingTransfer(
                                        dateField.getText(),
                                        amount,
                                        descField.getText(),
                                        sender,
                                        receiver
                                );
                            } else {
                                transaction = new IncomingTransfer(
                                        dateField.getText(),
                                        amount,
                                        descField.getText(),
                                        sender,
                                        receiver
                                );
                            }
                        }

                        bank.addTransaction(accountName, transaction);
                        updateTransactions();
                        updateBalance();

                        showInfo("Erfolg", "Transaktion hinzugefügt.");

                    } catch (NumberFormatException e) {
                        showError("Fehler", "Betrag muss eine Zahl sein.");
                    } catch (Exception e) {
                        showError("Fehler", e.getMessage());
                    }
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