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
    private Staffetta s1;

    public Atleta(Staffetta s1) {
        this.s1 = s1;
    }

    @Override
    public void run() {
        while (metri < 100) {
            metri++;
            try {
                Thread.sleep(50);
                System.out.println(metri);
            } catch (InterruptedException ex) {
                System.err.println("thread non lanciato");
            }
        }

    }

    public int getMetriPercorsi() {
        return metri;
    }

}
