/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author ironm
 */
public class Gestore implements Observer {

    private Atleta[] atleti;
    private int atletaCorrente = 0;
    private StaffettaVisual form;

    public Gestore(StaffettaVisual form) {
        this.form = form;
        this.atleti = new Atleta[4];
    }

    public void avviaGara(int velocitaBase) {
        atletaCorrente = 0;
        for (int i = 0; i < 4; i++) {
            atleti[i] = new Atleta();
            atleti[i].setVelocita(velocitaBase);
            atleti[i].setDaemon(true);
            atleti[i].addObserver(form.getCorsia(i)); 
            atleti[i].addObserver(this); 
        }
        atleti[0].start();
    }

    @Override
public synchronized void update(int metri) {
    if (metri == 90 && atletaCorrente < 3) {
        atleti[atletaCorrente + 1].start();
    }

    if (metri >= 100) {
        if (atleti[3].getMetri() >= 100) {
            form.fineGara();
        } else {
            atletaCorrente++;
        }
    }
}
    public void sospendi() {
        for (Atleta a : atleti) {
            if (a != null) a.sospendi();
        }
    }

    public void riprendi() {
        for (Atleta a : atleti) {
            if (a != null) a.riprendi();
        }
    }

    public void ferma() {
        for (Atleta a : atleti) {
            if (a != null) a.interrupt();
        }
    }
}
