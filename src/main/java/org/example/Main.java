package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import bank.*;
import bank.exceptions.AccountAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) throws Exception {

        PrivateBank bank = new PrivateBank("TestBank", 0.1, 0.3, "testdata");

        System.out.println("Accounts vorher:");
        bank.createAccount("Max");
        System.out.println(bank.getAllAccounts());
        bank.deleteAccount("Max");
        System.out.println("Accounts nach dem Löschen:");
        System.out.println(bank.getAllAccounts());
    }
}
























