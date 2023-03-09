
package audioserver;
/**
 *
 * @author Slam
 */
/*
import java.net.DatagramPacket;
import java.net.DatagramSocket;
public static void main(String args[]) throws Exception {
 DatagramSocket serverSocket = new DatagramSocket(8081);
 byte[] receiveData = new byte[1024];
 byte[] sendData = new byte[1024];
 while (true) {
 DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
 serverSocket.receive(receivePacket);
 String sentence = new String(receivePacket.getData().toString());

 System.out.println("RECEIVED: " + sentence);
 }
 }*/
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

class Server {

    AudioInputStream audioInputStream;
    static AudioInputStream ais;
    static AudioFormat format;
    static boolean status = true;
    static int port = 8081;
    static int sampleRate = 16000;

    static DataLine.Info dataLineInfo;
    static SourceDataLine sourceDataLine;

    public static void main(String args[]) throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(port);
        
        /**
         * Formula para lag = (byte_size/sample_rate)*2  
		 * lag = 9728 produce retardo de ~ 0.45 segundos. La voz se escuchara ronca.
		 * lag = 1400 produce retardo de ~ 0.06 segundos. Voz extremadamente ronca.
		 * lag = 4000 produce retardo de ~ 0.18 segundos. Voz menos ronca que 9728.
         */
        byte[] receiveData = new byte[2048];

        format = new AudioFormat(sampleRate, 16, 1, true, false);
        dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(format);
        sourceDataLine.start();

        FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(1.0f);

        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                receiveData.length);
        ByteArrayInputStream baiss = new ByteArrayInputStream(
                receivePacket.getData());
        while (status == true) {
            serverSocket.receive(receivePacket);
            ais = new AudioInputStream(baiss, format, receivePacket.getLength());
            toSpeaker(receivePacket.getData());
        }
        sourceDataLine.drain();
        sourceDataLine.close();
    }

    public static void toSpeaker(byte soundbytes[]) {
        try {
            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        } catch (Exception e) {
            System.out.println("Not working in speakers...");
            e.printStackTrace();
        }
    }
}
