package Data;

import Pins.Pins;

/**
 *
 * @author muri
 */
public class Light {

  private final String input;
  private final Pins pins = Pins.getInstance();

  public Light(String input) {
    this.input = input.substring(2);
    start();
  }

  private void start() {

    String[] split = input.split(";");

    if (split[0].equals("0") && pins.getStateInteriorLight()) {
      pins.setInteriorLight(false);
    } else if (split[0].equals("1") && pins.getStateInteriorLight() == false) {
      pins.setInteriorLight(true);
    }

    if (split[1].equals("0") && pins.getStateHeadLight()) {
      pins.setHeadLight(false);
    } else if (split[1].equals("1") && pins.getStateHeadLight() == false) {
      pins.setHeadLight(true);
    }

    if (split[2].equals("0") && pins.getStateFloodLight()) {
      pins.setFloodLight(false);
    } else if (split[2].equals("1") && pins.getStateFloodLight() == false) {
      pins.setFloodLight(true);
    }

    if (split[3].equals("0") && pins.getStateWarningLight()) {
      pins.setWarningLight(false);
    } else if (split[3].equals("1") && pins.getStateWarningLight() == false) {
      pins.setWarningLight(true);
    }
  }
}
