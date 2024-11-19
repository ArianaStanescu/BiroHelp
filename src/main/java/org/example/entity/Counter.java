package org.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(mappedBy = "counter", cascade = CascadeType.ALL)
//    private List<Client> clients;

//    @ManyToMany
//    @JoinTable(
//            name = "client_counter",
//            joinColumns = @JoinColumn(name = "counter_id"),
//            inverseJoinColumns = @JoinColumn(name = "client_id")
//    )
//    private List<Client> clients = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    private String name;
    private boolean busy = false;
    private boolean onBreak = false;

    private Random rand = new Random();


    public Counter() {}

    public Long getId() {
        return id;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isOnBreak() {
        return onBreak;
    }

    public void setOnBreak(boolean onBreak) {
        this.onBreak = onBreak;
    }

    public synchronized void processClient(Client client) throws InterruptedException {
        while (onBreak || busy) {
            wait();
        }

        busy = true;
        System.out.println("Ghiseul " + name + " procesează clientul " + client.getName());

        Thread.sleep(2000);
        busy = false;

        if (rand.nextInt(10) < 2) {
            takeBreak();
            System.out.println("Ghiseul " + name + " a intrat în pauză.");
            Thread.sleep(5000);
            endBreak();
            System.out.println("Ghiseul " + name + " a ieșit din pauză.");
        }

        notifyAll();
    }

    public synchronized void takeBreak() {
        onBreak = true;
    }

    public synchronized void endBreak() {
        onBreak = false;
        notifyAll();
    }

//    public void addClient(Client client) {
//        this.clients.add(client);
//    }
//
//    public void removeClient(Client client) {
//        this.clients.remove(client);
//    }

    public void setId(Long id) {
        this.id = id;
    }
}