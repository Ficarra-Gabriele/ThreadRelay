/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ficarra.gabriele
 */
public class Atleta extends Thread implements Subject {

    private volatile int metri = 0;
    private int velocita = 50;
    private boolean inPausa = false;
    private List<Observer> observers = new ArrayList<>();


    public void setVelocita(int v) {
        this.velocita = v;
    }

    public synchronized void sospendi() {
        inPausa = true;
    }

    public synchronized void riprendi() {
        inPausa = false;
        notify();
    }

    @Override
    public void run() {
        while (metri < 100 && !isInterrupted()) {
            synchronized (this) {
                while (inPausa) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            }

            if (!isInterrupted()) {
                metri++;
                notifyObservers();
                try {
                    Thread.sleep(velocita);
                } catch (InterruptedException ex) {
                    interrupt();
                }
            }
        }
    }

    public int getMetri() {
        return metri;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public synchronized void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        
        List<Observer> copia;
        
        synchronized (this) {
            copia = new ArrayList<>(observers);
        }

        for (Observer o : copia) {
            o.update(metri);
        }
    }
}
