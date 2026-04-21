/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author ficarra.gabriele
 */
public class Gestore {

    private Staffetta s1 = new Staffetta();
    private Atleta a1 = new Atleta(s1);
    private Atleta a2 = new Atleta(s1);
    private Atleta a3 = new Atleta(s1);
    private Atleta a4 = new Atleta(s1);
    
    public void avviaGara() {
        a1.start();
        attendiSoglia(a1, 90);
       
        a2.start();
        attendiSoglia(a1, 100);
        attendiSoglia(a2, 90);

        a3.start();
        attendiSoglia(a2, 100);
        attendiSoglia(a3, 90);

        a4.start();
        attendiSoglia(a3, 100);
        attendiSoglia(a4, 100);
    }

    private void attendiSoglia(Atleta a, int soglia) {
        while (a.getMetri() < soglia) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }  
}
