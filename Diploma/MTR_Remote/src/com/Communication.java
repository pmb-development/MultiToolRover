package com;

import data.ThreadMessage;
import gui.MultiToolRover;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author muri
 */
public class Communication extends SwingWorker<Object, ThreadMessage> {

  private static final Logger LOG = Logger.getLogger(Communication.class.getName());

  private final MultiToolRover mtr = MultiToolRover.getInstance();

  private String hostname;
  private int port;
  private String password;

  private SocketAddress socketAddress;

  private boolean isConnected = false;

  private volatile String response = "";
  private volatile String request;

  public synchronized String getResponse() {
    return this.response;
  }

  public boolean getConnection() {
    return isConnected;
  }

  public void initCommunication(String hostname, int port, String password) {
    this.hostname = hostname;
    this.port = port;
    this.password = password;
  }

  @Override
  protected Object doInBackground() throws Exception {

    LOG.info("COM-> CommunicationThread start");
    publish(new ThreadMessage("Kommunikation wird gestartet", Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), isConnected, "", response));
    socketAddress = new InetSocketAddress(hostname, port);

    try (Socket socket = new Socket()) {

      publish(new ThreadMessage("Verbindung wird aufgebaut...", Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), isConnected, "", response));
      LOG.info("COM-> Vebindung wird aufgebaut");
      socket.connect(socketAddress, 1000);
      socket.setSoTimeout(1000);

      final InputStream is = socket.getInputStream();
      final OutputStream os = socket.getOutputStream();
      BufferedReader r = new BufferedReader(new InputStreamReader(is));
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(os));

      int i = 0;

      // before connection
      if (!isConnected) {
        do {
          LOG.info("COM-> Verbindungsbestätigung start");
          w.append("Connect-" + password + "\n").flush();

          response = r.readLine().trim();
          LOG.fine("response: " + response);

          if (response.equals("Connected-MTR1")) {
            LOG.info("COM-> Verbindungsbestätigung funktioniert");
            publish(new ThreadMessage("Verbindung wurde bestätigt", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR),
                    isConnected, "", response));
            isConnected = true;
            break;
          } else if (!response.isEmpty()) {
            LOG.warning("COM-> Verbindungsbestätigung fehlgeschalgen");
            publish(new ThreadMessage("Verbindung fehlgeschlagen", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), isConnected,
                    "Die Verbindung zum MultiToolRover konnte nicht hergestellt werden", response));
            isConnected = false;
          }
          TimeUnit.MILLISECONDS.sleep(100);
          i++;
        } while (i < 10);
      }

      // after connection
      if (isConnected) {
        LOG.info("COM-> Standart Kommunikation start");
        request = mtr.getRequest();
        w.append(request + "\n").flush();
        while (!isCancelled()) {

          String rawResponse = r.readLine().trim();

          if (!rawResponse.isEmpty()) {

            // Disconnect
            if (rawResponse.equals("Disconnected-MTR1")) {
              LOG.info("COM-> Verbindung zu MTR wird getrennt");
              publish(new ThreadMessage("Verbindung beendet", Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), isConnected,
                      "Die Verbindung mit dem MultiToolRover wird beendet!", response));
              isConnected = false;
              socket.close();
              break;
            }
            
            response = rawResponse;
            LOG.info("COM-> Response: " + response);
            publish(new ThreadMessage("Verbunden mit MTR1", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), isConnected, "", response));
            
            request = mtr.getRequest();
            LOG.info("COM-> Request: " + request);
            w.append(request + "\n").flush();
          }
          TimeUnit.MILLISECONDS.sleep(100);
        }
      }
    } catch (UnknownHostException ex) {
      LOG.severe("COM-> Host not found: " + ex.getMessage());
      publish(new ThreadMessage("Verbindung fehlgeschalgen", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), isConnected,
              "Verbindung mit MTR fehlgeschlagen! MultiToolRover wurde nicht gefunden", response));
      isConnected = false;

    } catch (IOException ioe) {
      LOG.severe("COM-> I/O Error: " + ioe);
      publish(new ThreadMessage("Verbindung fehlgeschalgen", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), isConnected,
              "Verbindung mit MTR fehlgeschlagen!", response));
      isConnected = false;

    } catch (InterruptedException ex) {
      LOG.severe("COM-> Interrupted Exception: " + ex.getMessage());
      isConnected = false;

    } finally {
      publish(new ThreadMessage("Nicht Verbunden", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), isConnected,
              "", response));
      isConnected = false;
    }
    return null;
  }
}
