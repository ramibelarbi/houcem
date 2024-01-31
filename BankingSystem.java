package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BankingSystem {

    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            accounts.add(new Account(i, 100000));
        }

        HeadOffice headOffice = new HeadOffice(accounts);
        Queue<Transfer> transferQueue = new ConcurrentLinkedQueue<>();

        List<Branch> branches = new ArrayList<>();
        List<Thread> branchThreads = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Branch branch = new Branch(transferQueue, headOffice);
            branches.add(branch);

            Thread branchThread = new Thread(branch);
            branchThreads.add(branchThread);
        }

        headOffice.startProcessing();

        for (Thread branchThread : branchThreads) {
            branchThread.start();
        }

        for (Branch branch : branches) {
            branch.generateTransfers(1000);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while sleeping");
        }

        headOffice.terminateProcessing();

        for (Thread branchThread : branchThreads) {
            try {
                branchThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error while waiting for branch thread to complete");
            }
        }

        int totalAmount = accounts.stream().mapToInt(Account::getBalance).sum();
        System.out.println("Final total amount of all accounts: " + totalAmount + " cents");
    }
}
