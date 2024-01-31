package org.example;

public class Transfer {
    private int fromAccount;
    private int toAccount;
    private int amount;

    public Transfer(int fromAccount, int toAccount, int amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public int getAmount() {
        return amount;
    }
}
