package data;

import java.awt.Cursor;
import java.util.regex.Pattern;
import logging.Logger;

/**
 *
 * @author muri
 */
public class ThreadMessage {

  private static final Logger LOG = Logger.getLogger(ThreadMessage.class.getName());
  
  private final String message;
  private final Cursor cursor;
  private final boolean connected;
  private final String jOptionPane;
  private final String preasure;
  private final String voltage;

  private String start, data, stop;

  public ThreadMessage(String message, Cursor cursor, boolean connection, String jOptionPane, String response) {
    this.message = message;
    this.cursor = cursor;
    this.connected = connection;
    this.jOptionPane = jOptionPane;

    if (!response.isEmpty() && response.length() > 21) {
      start = response.substring(0, 6);
      data = response.substring(6, 19);
      stop = response.substring(19);
      
      String[] splitedData = data.split(Pattern.quote("|"));
      
      if (splitedData[0].startsWith("P")) {
        splitedData[0] = splitedData[0].replaceAll("P-", "");
        this.preasure = splitedData[0];
      } else {
        this.preasure = "0.0";
      }

      if (splitedData[1].startsWith("B")) {
        splitedData[1] = splitedData[1].replaceAll("B-", "");
        this.voltage = splitedData[1];
      } else {
        this.voltage = "0.0";
      }
    } else {
      this.voltage = "0.0";
      this.preasure = "0.0";
    }
  }

  public String getMessage() {
    return message;
  }

  public Cursor getCursor() {
    return cursor;
  }

  public boolean isConnected() {
    return connected;
  }

  public String getjOptionPane() {
    return jOptionPane;
  }

  public String getPreasure() {
    return preasure;
  }

  public String getVoltage() {
    return voltage;
  }
}
