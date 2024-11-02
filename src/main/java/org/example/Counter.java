package org.example;
import java.util.*;

class Counter {
    private final String name;
    private boolean busy = false;
    private boolean onBreak = false;
    private Random rand = new Random();

    public Counter(String name) {
        this.name = name;
    }

    protected boolean isBusy() { return busy; }

    protected boolean isOnBreak() { return onBreak; }

    protected synchronized void processClient(Client client) throws InterruptedException {
        while (onBreak || busy) {
            wait(); //se asteapta pana cand ghiseul devine disponibil
        }

        busy = true;
        System.out.println("Ghiseul " + name + " proceseaza clientul " + client.getName());

        //simulam timpul necesar procesarii
        //Thread.sleep(2000);
        busy = false;

        //simulam pauza
        if (rand.nextInt(10) < 2) {
            takeBreak();
            System.out.println("Ghiseul " + name + " a intrat în pauză.");
            Thread.sleep(5000);
            endBreak();
            System.out.println("Ghiseul " + name + " a ieșit din pauză.");
        }

        notifyAll(); //notifica alti clienti (fire) ca ghiseul e disponibil
    }

    //metoda e synchronized pentru ca un alt thread sa nu il modifice simultan cu threadul curent
    public synchronized void takeBreak() {
        onBreak = true;
    }

    public synchronized void endBreak() {
        onBreak = false;
        notifyAll(); //clientii (firele) sunt notificatati atunci cand ghiseul iese din pauza
    }

}
