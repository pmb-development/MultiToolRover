package MainClass;

import Data.Data;
import Pins.Pins;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import logging.Logger;

/**
 *
 * @author muri
 */
public class MTRCom implements Runnable {

  private static final Logger LOG = Logger.getLogger(MTRCom.class.getName());

  private final Socket socket;
  private final Pins pins = Pins.getInstance();
  private static boolean isConnected;
  private static boolean interrupt = false;

  static void setInterrupt(boolean interrupt) {
    MTRCom.interrupt = interrupt;
  }

  public static boolean isIsConnected() {
    return isConnected;
  }

  private final String stopLine = "P-0;0;0;0;0;0;0;|L-0;0;0;0;|O-0;0;0;0;|D-0;|M-100;100;";
  private String start;
  private String data;
  private String stop;
  private String response = "";
  private String inputLine = "";
  private String password = "1234";

  public MTRCom(Socket socket) {
    this.socket = socket;
  }

  private synchronized String getResponse() {
    String pneumatic = ("P-" + String.format("%1.1f", pins.getPreasure()) + "|");
    String batVoltage = ("B-" + String.format("%2.1f", pins.getBatVoltage()) + "|");

    LOG.fine("MTRComn-> Response: " + "START|" + pneumatic + "" + batVoltage + "STOP");
    return ("START|" + pneumatic + "" + batVoltage + "STOP");

//    return "START|P-1.2|B-12.3|STOP"; for tests
  }

  @Override
  public void run() {
    LOG.info("MTRCom-> Kommunikations Thread wird gestartet");

    try {
      final InputStream is = socket.getInputStream();
      final OutputStream os = socket.getOutputStream();

      BufferedReader r = new BufferedReader(new InputStreamReader(is));
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(os));

      isConnected = false;
      long startTime = System.currentTimeMillis();
      while (!interrupt) {

        if (r.ready()) {
          inputLine = r.readLine();
//          LOG.fine("MTRCom-> InputLine: " + inputLine);

          if (!inputLine.isEmpty() && inputLine != null) {

            // StartConnection
            if (inputLine.equals("Connect-" + password) && !isConnected) {
              startTime = System.currentTimeMillis();
              response = "Connected-MTR1";
              LOG.info("MTRCom-> Verbindung bestätigt");
              w.write(response + "\n");
              w.flush();
              isConnected = true;

              // Disconnect
            } else if (inputLine.equals("STOP")) {
              LOG.warning("MTRCom-> STOP erhalten, Thread wird beendet!");
              w.write("Disconnected-MTR1\n");
              w.flush();
              return;

              // StandartConnection
            } else if (isConnected == true && !inputLine.isEmpty() && inputLine != null && !inputLine.equals("Connect-1234")) {
              startTime = System.currentTimeMillis();

              if (inputLine.length() > 60) {
                start = inputLine.substring(0, 6);
                data = inputLine.substring(6, 61);
                stop = inputLine.substring(61);

                if (start.equals("START|") && stop.equals("STOP")) {
                  LOG.fine("MTRCom-> InputLine: " + inputLine);
                  Data split = new Data(data);
                  response = getResponse();

                } else {
                  response = "Disconnected-MTR1";
                }
              } else {
                response = getResponse();
              }

              w.write(response + "\n");
              w.flush();
            }
          }
        }

        if ((startTime + 1000) < System.currentTimeMillis() && socket.isConnected()) {
          LOG.severe("MTRCom-> Verbindungs Timeout");
          return;
        }
      }
    } catch (IOException | InterruptedException ex) {
      LOG.severe("MTRCom-> Exception: " + ex.getMessage());
    } finally {
      try {
        LOG.warning("MTRCom-> Kommunikations Thread wird beendet");
        new Data(stopLine);
        socket.close();

      } catch (IOException | InterruptedException ex) {
        LOG.severe("MTRCom-> Exception: " + ex.getMessage());
      }
    }
  }
}
