package org.example;

import java.util.Queue;
import java.util.Random;

public class Branch implements Runnable {
    private Queue<Transfer> transferQueue;
    private HeadOffice headOffice;

    public Branch(Queue<Transfer> transferQueue, HeadOffice headOffice) {
        this.transferQueue = transferQueue;
        this.headOffice = headOffice;
    }

    public void generateTransfers(int numberOfTransfers) {
        System.out.println("Generating transfers...");
        Random random = new Random();

        for (int i = 0; i < numberOfTransfers; i++) {
            int fromAccount = random.nextInt(1000) + 1; // Assuming 1000 accounts
            int toAccount = random.nextInt(1000) + 1;
            int amount = random.nextInt(10001); // Maximum amount of 10,000 cents

            Transfer transfer = new Transfer(fromAccount, toAccount, amount);
            transferQueue.offer(transfer);
        }
    }

    public void forwardToHeadOffice() {
        System.out.println("Forwarding transfers to Head Office...");
        headOffice.enqueueTransfers(transferQueue);
        transferQueue.clear();
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long elapsedTimeMillis;

        try {
            System.out.println("Branch thread started...");
            while (true) {
                generateTransfers(1000);
                forwardToHeadOffice();

                elapsedTimeMillis = System.currentTimeMillis() - startTime;

                if (elapsedTimeMillis >= 60000) {  // Stop after 1 minute (adjust as needed)
                    break;
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Branch thread interrupted while sleeping");
        }
    }


}
