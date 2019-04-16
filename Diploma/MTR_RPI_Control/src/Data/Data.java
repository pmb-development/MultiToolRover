package Data;

import Pins.Pins;
import java.io.IOException;
import java.util.regex.Pattern;
import logging.Logger;

/**
 *
 * @author muri Diese Klasse teilt die Stringdaten in die einzelne Strings für
 * Pneumatic, Light, Outputs, Display und Motor
 */
public class Data {

  private static final Logger LOG = Logger.getLogger(Data.class.getName());

  public String pneumatic;
  public String light;
  public String output;
  public String display;
  public String motor;
  private final Pins pins = Pins.getInstance();

  public Data(String data) throws InterruptedException, IOException {

    String[] splitedData = data.split(Pattern.quote("|"));

    if (splitedData[0].startsWith("P")) {
      this.pneumatic = splitedData[0];
      Pneumatic pneumaticClass = new Pneumatic(this.pneumatic);
    } else {
      LOG.warning("DATA-> Problem bei splitPneumatic");
    }

    if (splitedData[1].startsWith("L")) {
      this.light = splitedData[1];
      Light lightClass = new Light(this.light);
    } else {
      LOG.warning("DATA-> Problem bei splitLight");
    }

    if (splitedData[2].startsWith("O")) {
      this.output = splitedData[2];
      Output outputClass = new Output(this.output);
    } else {
      LOG.warning("DATA-> Problem bei splitOutput");
    }

    if (splitedData[3].startsWith("D")) {
      this.display = splitedData[3];
      display();
    } else {
      LOG.warning("DATA-> Problem bei splitDisplay");
    }

    if (splitedData[4].startsWith("M")) {
      this.motor = splitedData[4];
      motor();
    } else {
      LOG.warning("DATA-> Problem bei splitMotor");
    }
  }

  private void display() {
    display = display.substring(2);
    String[] split = display.split(";");

    if (split[0].equals("0") && pins.getStateDisplay()) {
      pins.setDisplay(false);
    } else if (split[0].equals("1") && pins.getStateDisplay() == false) {
      pins.setDisplay(true);
    }
  }

  private void motor() throws InterruptedException, IOException {
    pins.setMotor(motor);
  }
}
