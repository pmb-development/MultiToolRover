package MainClass;

import Pins.AnalogMeasurementThread;
import Pins.Pins;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import logging.LogBackgroundHandler;
import logging.LogOutputStreamHandler;
import logging.Logger;

/**
 *
 * @author muri
 */
public class MainClass {

  private static final Logger LOG;
  private static final Logger LOGP;

  static {
    System.setProperty("logging.LogOutputStreamHandler.showRecordHashcode", "false");
    System.setProperty("logging.LogRecordDataFormattedText.Terminal", "LINUX");
    System.setProperty("logging.Logger.Level", "SEVERE");
    System.setProperty("logging.Logger.Level", "WARNING");
    System.setProperty("logging.Logger.Level", "INFO");
    System.setProperty("logging.Logger.Level", "FINE");
    System.setProperty("logging.Logger.Level", "FINER");
    System.setProperty("logging.Logger.Level", "FINEST");
    System.setProperty("test.Test.Logger.Level", "FINER");
    System.setProperty("test.*.Logger.Level", "FINE");

    LOG = Logger.getLogger(MainClass.class.getName());
    LOGP = Logger.getParentLogger();
  }

  private final int port;
  private static final Pins pins = Pins.getInstance();

  public MainClass(int port) {
    addLogFileHandler(true);
    this.port = port;
  }

  private void addLogFileHandler(boolean enabled) {
    if (enabled) {
      try {
        File logfile = null;
        File home;
        File folder;
        Date date = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yy.MM.dd-HH.mm.ss.SSS");

        try {
          home = new File(System.getProperty("user.home"));
        } catch (Exception e) {
          home = null;
        }

        if (home != null && home.exists()) {
          folder = new File(home + File.separator + "MTR_Files");
          if (!folder.exists()) {
            if (!folder.mkdir()) {
              throw new Exception("Internal Error");
            }
          }
          logfile = new File(folder + File.separator + "Log_" + df.format(date) + ".log");
        }
        LOGP.addHandler(new LogBackgroundHandler(new LogOutputStreamHandler(new BufferedOutputStream(new FileOutputStream(logfile.getPath())))));
      } catch (Exception ex) {
        LOG.warning("MTR-> Exception bei LogFile: " + ex.getMessage());
      }
    }
  }

  static void init() {
    pins.setPneumatic(false);
    pins.setPneumaticV1(false);
    pins.setPneumaticV2(false);
    pins.setPneumaticV3(false);
    pins.setPneumaticV4(false);
    pins.setPneumaticV5(false);
    pins.setPneumaticV6(false);
    pins.setInteriorLight(false);
    pins.setHeadLight(false);
    pins.setFloodLight(false);
    pins.setWarningLight(false);
    pins.setOutput1Front(false);
    pins.setOutput2Front(false);
    pins.setOutput3Back(false);
    pins.setOutput4Back(false);
    pins.setDisplay(false);
    pins.setRestRelai1(false);
    pins.setRestRelai2(false);
    pins.setRestRelai3(false);
    pins.setRestRelai4(false);
    pins.setRestRelai5(false);
    pins.setRestRelai6(false);

    final AnalogMeasurementThread mThread = new AnalogMeasurementThread();
    mThread.start();
  }

  public void start() throws IOException {
    final ServerSocket serverSocket = new ServerSocket(port);
    LOG.info("MAIN-> Start Server am Raspberry auf dem port: " + port);

    while (true) {
      Socket socket = serverSocket.accept(); //blockiert, bis sich Client verbindet, liefert neuen Socket
      new Thread(new MTRCom(socket)).start();
    }
  }

  public static void main(String[] args) throws InterruptedException, IOException {
    LOGP.addHandler(new LogBackgroundHandler(new LogOutputStreamHandler(System.out)));
    init();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        LOG.info("MAIN-> Programm wird beendet");
        try {
          MTRCom.setInterrupt(false);
          Thread.sleep(1000);
          pins.shutdown();
          System.exit(0);
        } catch (InterruptedException ex) {
        }
      }
    });

    final MainClass server = new MainClass(1804);
    server.start();

  }
}
