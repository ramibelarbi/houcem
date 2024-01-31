package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HeadOffice {
    private Queue<Transfer> transferQueue;
    private List<Account> accounts;
    private volatile boolean processing;
    private List<Thread> processingThreads;
    private static final int numberOfProcessingThreads = 5; // Adjust as needed

    public HeadOffice(List<Account> accounts) {
        this.transferQueue = new ConcurrentLinkedQueue<>();
        this.accounts = accounts;
        this.processing = true;
        this.processingThreads = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void enqueueTransfers(Queue<Transfer> branchTransfers) {
        transferQueue.addAll(branchTransfers);
    }
    public synchronized void processTransfer() {
        System.out.println("Processing transfers...");
        while (!transferQueue.isEmpty()) {
            Transfer transfer = transferQueue.poll();
            if (transfer != null) {
                if (executeTransfer(transfer)) {
                    updateAccounts(transfer);
                } else {
                    // Handle failed transfer (e.g., put back in the queue or another list)
                    // ...
                }
            }
        }
    }

    private boolean executeTransfer(Transfer transfer) {
        System.out.println("Executing transfer: " + transfer);
        int fromAccountNumber = transfer.getFromAccount();
        int toAccountNumber = transfer.getToAccount();
        int transferAmount = transfer.getAmount();

        Account fromAccount = findAccountByNumber(fromAccountNumber);
        Account toAccount = findAccountByNumber(toAccountNumber);

        if (fromAccount != null && toAccount != null) {
            // Ensure sufficient balance in the fromAccount
            if (fromAccount.getBalance() >= transferAmount) {
                // Transfer logic: Subtract from the sender's account and add to the receiver's account
                fromAccount.updateBalance(-transferAmount);  // Subtract from sender
                toAccount.updateBalance(transferAmount);      // Add to receiver
                return true; // Successful transfer
            } else {
                // Insufficient balance in the fromAccount, transfer failed
                return false;
            }
        } else {
            // Invalid account numbers, transfer failed
            return false;
        }
    }


    private void updateAccounts(Transfer transfer) {
        System.out.println("Updating accounts for transfer: " + transfer);
        int fromAccountNumber = transfer.getFromAccount();
        int toAccountNumber = transfer.getToAccount();
        int transferAmount = transfer.getAmount();

        Account fromAccount = findAccountByNumber(fromAccountNumber);
        Account toAccount = findAccountByNumber(toAccountNumber);

        if (fromAccount != null && toAccount != null) {
            // Ensure sufficient balance in the fromAccount
            if (fromAccount.getBalance() >= transferAmount) {
                // Perform the transfer by updating the balances
                fromAccount.updateBalance(-transferAmount); // Subtract from the sender's account
                toAccount.updateBalance(transferAmount);    // Add to the receiver's account
            } else {
                // Handle insufficient balance in the fromAccount (e.g., put back in the queue or another list)
                // ...
            }
        } else {
            // Handle invalid account numbers (e.g., put back in the queue or another list)
            // ...
        }
    }

    private Account findAccountByNumber(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null; // Account not found
    }


    public void startProcessing() {
        System.out.println("Head office processing started...");
        if (!processing) {
            processing = true;
            for (int i = 0; i < numberOfProcessingThreads; i++) {
                Thread processingThread = new Thread(() -> {
                    while (processing) {
                        processTransfer();
                    }
                });
                processingThreads.add(processingThread); // Add thread to the list
                processingThread.start();
            }
        }
    }


    public void terminateProcessing() {
        System.out.println("Head office processing terminated...");
        processing = false;

        for (Thread thread : processingThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error while waiting for processing thread to complete");
            }
        }
    }

}
