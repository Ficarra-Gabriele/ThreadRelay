/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package threadrelay;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author ironm
 */
public class StaffettaVisual extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StaffettaVisual.class.getName());

    private Corsia[] corsie = new Corsia[4];
    private JComboBox<String> cVelocita;
    private JButton btnAvvia, btnSospende, btnRiprende, btnFerma;
    private Gestore gestore;

    public StaffettaVisual() {
        gestore = new Gestore(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        creaAreaGara();
        creaBarraControlli();
        setStatoBottoni(true);
    }

    private void creaAreaGara() {
        JPanel panelCentrale = new JPanel(new GridLayout(4, 1, 0, 10));
        panelCentrale.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCentrale.setBackground(new Color(50, 50, 50));
        for (int i = 0; i < 4; i++) {
            corsie[i] = new Corsia("Runner " + (i + 1));
            panelCentrale.add(corsie[i]);
        }
        add(panelCentrale, BorderLayout.CENTER);
    }

    private void creaBarraControlli() {
        JPanel panelControlli = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelControlli.setBackground(new Color(230, 230, 230));
        panelControlli.setBorder(BorderFactory.createRaisedBevelBorder());
        panelControlli.add(new JLabel("Velocità:"));
        cVelocita = new JComboBox<>(new String[]{"Slow", "Regular", "Fast"});
        cVelocita.setSelectedIndex(1);
        panelControlli.add(cVelocita);

        btnAvvia = new JButton("Avvia");
        btnSospende = new JButton("Sospende");
        btnRiprende = new JButton("Riprende");
        btnFerma = new JButton("Ferma");

        Dimension btnDim = new Dimension(100, 35);
        JButton[] bottoni = {btnAvvia, btnSospende, btnRiprende, btnFerma};
        for (JButton b : bottoni) {
            b.setPreferredSize(btnDim);
            panelControlli.add(b);
        }

        btnAvvia.addActionListener(e -> avviaSimulazione());
        btnSospende.addActionListener(e -> gestore.sospendi());
        btnSospende.addActionListener(e -> {
            btnSospende.setEnabled(false);
            btnRiprende.setEnabled(true);
        });
        btnRiprende.addActionListener(e -> gestore.riprendi());
        btnRiprende.addActionListener(e -> {
            btnSospende.setEnabled(true);
            btnRiprende.setEnabled(false);
        });
        btnFerma.addActionListener(e -> fermaSimulazione());

        add(panelControlli, BorderLayout.SOUTH);
    }

    private void setStatoBottoni(boolean inattesa) {
        btnAvvia.setEnabled(inattesa);
        cVelocita.setEnabled(inattesa);
        btnSospende.setEnabled(!inattesa);
        btnRiprende.setEnabled(false);
        btnFerma.setEnabled(!inattesa);
    }

    public Corsia getCorsia(int index) {
        return corsie[index];
    }

    private void avviaSimulazione() {
        setStatoBottoni(false);
        int v = 50;
        String sel = (String) cVelocita.getSelectedItem();
        if (sel.equals("Slow")) {
            v = 100;
        }
        if (sel.equals("Regular")) {
            v = 50;
        }
        if (sel.equals("Fast")) {
            v = 10;
        }

        for (Corsia c : corsie) {
            c.update(0);
        }
        gestore.avviaGara(v);
    }

    private void fermaSimulazione() {
        gestore.ferma();
        setStatoBottoni(true);
    }

    public void fineGara() {
        SwingUtilities.invokeLater(() -> setStatoBottoni(true));
    }

    public class Corsia extends JPanel implements Observer {

        private int progresso = 0;
        private String nomeRunner;
        private JLabel lblInfo;
        private JPanel areaPista;

        public Corsia(String nome) {
            this.nomeRunner = nome;
            setupUI();
        }

        private void setupUI() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            areaPista = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(220, 220, 220));
                    for (int i = 0; i < areaPista.getWidth(); i += 50) {
                        g.drawLine(i, 0, i, getHeight());
                    }
                    int widthPista = areaPista.getWidth() - 50;
                    int x = (int) ((progresso / 100.0) * widthPista);
                    int y = areaPista.getHeight() / 2 - 15;
                    g.setColor(new Color(200, 0, 0));
                    g.fillOval(x, y, 30, 30);
                    g.setColor(Color.WHITE);
                    g.drawString("🏃", x + 10, y + 20);
                }
            };
            areaPista.setBackground(new Color(245, 245, 245));
            JPanel panelInfo = new JPanel(new GridLayout(2, 1));
            panelInfo.setPreferredSize(new Dimension(150, 0));
            panelInfo.setBackground(new Color(240, 240, 240));
            JLabel lblNome = new JLabel(nomeRunner, SwingConstants.CENTER);
            lblNome.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblInfo = new JLabel("Metri: 0", SwingConstants.CENTER);
            lblInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
            panelInfo.add(lblNome);
            panelInfo.add(lblInfo);
            add(areaPista, BorderLayout.CENTER);
            add(panelInfo, BorderLayout.EAST);
        }

        @Override
        public void update(int valore) {
            SwingUtilities.invokeLater(() -> {
                this.progresso = valore;
                if (valore >= 100) {
                    lblInfo.setText("FINE");
                    lblInfo.setForeground(new Color(0, 150, 0));
                } else {
                    lblInfo.setText("Metri: " + valore);
                    lblInfo.setForeground(Color.BLACK);
                }
                repaint();
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> new StaffettaVisual().setVisible(true));
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
