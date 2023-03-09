/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioserver;

import com.sun.media.jfxmedia.AudioClip;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.nio.channels.Channel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.AudioTrack;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;
import sun.audio.AudioData;
import sun.audio.AudioDevice;
import sun.audio.AudioPlayer;

/**
 *
 * @author Slam
 */
public class NewJFrame extends javax.swing.JFrame {

    static AudioInputStream audioin;
    static AudioFormat audiof;
    static boolean estado;// = true;
    static int puerto;// = 8081;
    static int sampleRate;// = 16000;

    static DataLine.Info dlInfo;
    static SourceDataLine sdlInfo;
    int scrollStatus = 0;
    Thread hilo;

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    public void toSpeaker(byte[] dataSounds) {
        sdlInfo.write(dataSounds, 0, dataSounds.length);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {        
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            switch(getDefaultCloseOperation()) {              
              case EXIT_ON_CLOSE:
                if(JOptionPane.showConfirmDialog(rootPane,"Desea salir?", "Advertencia",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE)==JOptionPane.OK_OPTION)
                    System.exit(0);
                break;
            }
        }
    }
    
    protected void finalize() throws Throwable {
        scrollStatus = 0;
        estado = false;
        sdlInfo.drain();
        sdlInfo.close();
        hilo.interrupt();
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }

    public Thread init(int sampleRate, int canal, int puerto) {
        hilo = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    jProgressBar1.setValue(scrollStatus);
                    DatagramSocket ds = new DatagramSocket(puerto);
                    System.out.println("Iniciando escucha de audio por ip:" + Inet4Address.getLocalHost().getHostAddress() + " puerto " + puerto);
                    scrollStatus += 10;
                    jProgressBar1.setValue(scrollStatus);
                    /**
                     * Formula for lag = (byte_size/sample_rate)*2 Byte size
                     * 9728 will produce ~ 0.45 seconds of lag. Voice slightly
                     * broken. Byte size 1400 will produce ~ 0.06 seconds of
                     * lag. Voice extremely broken. Byte size 4000 will produce
                     * ~ 0.18 seconds of lag. Voice slightly more broken then
                     * 9728.
                     */
                    byte[] recData = new byte[1400];
                    scrollStatus += 5;
                    jProgressBar1.setValue(scrollStatus);
                    audiof = new AudioFormat(sampleRate, 16, canal, true, false);
                    scrollStatus += 15;
                    jProgressBar1.setValue(scrollStatus);
                    dlInfo = new DataLine.Info(SourceDataLine.class, audiof);
                    scrollStatus += 15;
                    jProgressBar1.setValue(scrollStatus);
                    sdlInfo = (SourceDataLine) AudioSystem.getLine(dlInfo);
                    scrollStatus += 5;
                    jProgressBar1.setValue(scrollStatus);
                    sdlInfo.open(audiof);
                    scrollStatus += 15;
                    jProgressBar1.setValue(scrollStatus);
                    sdlInfo.start();
                    scrollStatus += 15;
                    jProgressBar1.setValue(scrollStatus);
                    System.out.println("Transmision de audio en vivo por puerto " + puerto + ", iniciado y a la espera...");
                    FloatControl volumenControl = (FloatControl) sdlInfo.getControl(FloatControl.Type.MASTER_GAIN);
                    volumenControl.setValue(1.00f);
                    scrollStatus += 10;
                    jProgressBar1.setValue(scrollStatus);

                    DatagramPacket dp = new DatagramPacket(recData, recData.length);
                    ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
                    System.out.println("Recibiendo datos...");
                    scrollStatus += 10;
                    jProgressBar1.setValue(scrollStatus);
                    while (estado == true) {
                        ds.receive(dp);
                        /*if(dp.getLength()<recData.length){
                         sdlInfo.drain();
                         }*/
                        audioin = new AudioInputStream(bais, audiof, dp.getLength());
                        toSpeaker(dp.getData());
                    }
                    System.out.println("Cerrada la transmisión en tiempo real.");
                    sdlInfo.drain();
                    sdlInfo.close();

                } catch (SocketException se) {
                } catch (LineUnavailableException | IOException lue) {
                }
            }
        });
        return hilo;//.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdMono = new javax.swing.JRadioButton();
        rdStereo = new javax.swing.JRadioButton();
        jcSampleRate = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AudioStreamer 1.0");
        setBackground(new java.awt.Color(255, 255, 255));

        rdMono.setSelected(true);
        rdMono.setText("Mono");
        rdMono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMonoActionPerformed(evt);
            }
        });

        rdStereo.setText("Estereo");
        rdStereo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdStereoActionPerformed(evt);
            }
        });

        jcSampleRate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "16000", "44100" }));

        jLabel1.setText("Canales: ");

        jLabel2.setText("Sample Rate: ");

        jToggleButton1.setText("Enviar a Speaker");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Puerto de Red para escucha:");

        txtPort.setToolTipText("Puerto de escucha para la red");
        txtPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPortActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(49, 49, 49)
                        .addComponent(jcSampleRate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(64, 64, 64)
                                .addComponent(rdMono)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdStereo))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdMono)
                        .addComponent(rdStereo)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jcSampleRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if (jToggleButton1.isSelected()) {
            String tp = txtPort.getText().toString();
            if (tp.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "No debe haber campos vacios!");
                jToggleButton1.setSelected(false);
            } else {
                puerto = Integer.valueOf(tp);
                estado = true;
                int canal = rdMono.isSelected() ? 1 : 2;
                int sampleRate = Integer.valueOf(jcSampleRate.getSelectedItem().toString());
                if (hilo != null) {
                    if (hilo.isInterrupted()) {
                        System.out.println("Estado actual: " + hilo.getState().toString());
                        switch (hilo.getState()) {
                            case BLOCKED:
                            case TERMINATED:
                            case RUNNABLE:
                                hilo.checkAccess();
                                hilo.run();
                                break;
                            case WAITING:
                                try {
                                    hilo.join();
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                break;
                        }
                    }
                } else {
                    hilo = init(sampleRate, canal, puerto);
                    hilo.start();
                }
                jToggleButton1.setText("Detener transmisión");
            }
        } else if (!jToggleButton1.isSelected()) {
            jToggleButton1.setText("Enviar a Speaker");
            estado = false;
            scrollStatus = 0;
            jProgressBar1.setValue(0);
            if (hilo.isAlive()) {
                try {
                    finalize();
                } catch (Throwable ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }                
                System.out.println("Cerrada la transmisión en tiempo real.");
            }
        }

    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void txtPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPortActionPerformed

    private void rdMonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMonoActionPerformed
        rdMono.setSelected(true);
        rdStereo.setSelected(false);
    }//GEN-LAST:event_rdMonoActionPerformed

    private void rdStereoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdStereoActionPerformed
        rdStereo.setSelected(true);
        rdMono.setSelected(false);
    }//GEN-LAST:event_rdStereoActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JComboBox jcSampleRate;
    private javax.swing.JRadioButton rdMono;
    private javax.swing.JRadioButton rdStereo;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables
}
