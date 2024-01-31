package org.example;

public class Account {
    private int accountNumber;
    private int balance;

    public Account(int accountNumber, int initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public synchronized void updateBalance(int amount) {
        if (balance + amount >= 0) {
            balance += amount;
            System.out.println("Account " + accountNumber + ": Updated balance by " + amount + ". New balance: " + balance);
        } else {
            throw new IllegalArgumentException("Updating balance would result in a negative balance");
        }
    }
}