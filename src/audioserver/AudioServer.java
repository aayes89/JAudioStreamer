/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Slam
 */
public class AudioServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        int intDisplays;
        String HEADER_LOCATION = "LOCATION";
        /**
         * The service header sent back in the DIAL discovery response
         */
        String HEADER_ST = "ST";
        final String SEARCH_TARGET = "urn:dial-multiscreen-org:service:dial:1";
        /**
         * The DIAL service Packet that should be sent out
         */
        //239.255.255.250:1900
        final String M_SEARCH = "M-SEARCH * HTTP/1.1\r\n" + "HOST: 239.255.100.100:5000\r\n"
                + "MAN: \"ssdp:discover\"\r\n" + "MX: 10\r\n" + "ST: " + SEARCH_TARGET + "\r\n\r\n";

        DatagramPacket pakSearch = new DatagramPacket(M_SEARCH.getBytes(), M_SEARCH.getBytes().length);
        pakSearch.setPort(1900);
        pakSearch.setAddress(InetAddress.getByName("192.168.0.163"));//239.255.255.250"));
        byte[] buffer = new byte[1024];
        DatagramPacket pakReceive = new DatagramPacket(buffer, buffer.length);
        //DatagramSocket serverSocket = new DatagramSocket(); serverSocket.connect(InetAddress.getByName("192.168.0.111"), 7236);
        DatagramSocket udpSocket = new DatagramSocket();
        System.out.println("Sending discover message");
        udpSocket.setBroadcast(true);
        udpSocket.send(pakSearch);
        udpSocket.setSoTimeout(4000);
        udpSocket.receive(pakReceive);
        String strPacket = new String(pakReceive.getData(), 0, pakReceive.getLength());
        String strTokens[] = strPacket.trim().split("\\n");

        Boolean booLocation = false;
        Boolean booService = false;

        for (String strToken1 : strTokens) {

            String strToken = strToken1.trim();
            if (strToken.startsWith(HEADER_LOCATION)) {
                booLocation = true;
            } else if (strToken.startsWith(HEADER_ST)) {

                String strService = strToken.substring(4).trim();
                if (strService.equals(SEARCH_TARGET)) {
                    booService = true;
                }
            }
        }
        if (!booService || !booLocation) {
            System.out.println("Cancel");
            intDisplays = 0;
            //ChromerWidget.mgrNotifications.cancel(114);

        } else {

            System.out.println("Received a DIAL response packet");
            intDisplays = 1;
            //ChromerWidget.mgrNotifications.notify(114, ChromerWidget.notBuilder.build());
        }

//      NewJFrame v=new NewJFrame();
//      v.setVisible(true);
    }

}
