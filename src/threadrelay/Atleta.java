/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author ficarra.gabriele
 */

public class Atleta extends Thread {

    private int metri = 0;
    private int velocita = 50;
    private boolean inPausa = false;
    private StaffettaVisual.Corsia corsia;

    public Atleta(StaffettaVisual.Corsia corsia) {
        this.corsia = corsia;
    }

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
                corsia.setProgresso(metri);
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
}
