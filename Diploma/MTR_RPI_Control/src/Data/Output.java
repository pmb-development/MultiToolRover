package Data;

import Pins.Pins;

/**
 *
 * @author muri
 */
public class Output {

  private final String input;
  private final Pins pins = Pins.getInstance();

  public Output(String input) {
    this.input = input.substring(2);
    start();
  }

  private void start() {

    String[] split = input.split(";");

    if (split[0].equals("0") && pins.getStateOutput1Front()) {
      pins.setOutput1Front(false);
    } else if (split[0].equals("1") && pins.getStateOutput1Front() == false) {
      pins.setOutput1Front(true);
    }

    if (split[1].equals("0") && pins.getStateOutput2Front()) {
      pins.setOutput2Front(false);
    } else if (split[1].equals("1") && pins.getStateOutput2Front() == false) {
      pins.setOutput2Front(true);
    }

    if (split[2].equals("0") && pins.getStateOutput3Back()) {
      pins.setOutput3Back(false);
    } else if (split[2].equals("1") && pins.getStateOutput3Back() == false) {
      pins.setOutput3Back(true);
    }

    if (split[3].equals("0") && pins.getStateOutput4Back()) {
      pins.setOutput4Back(false);
    } else if (split[3].equals("1") && pins.getStateOutput4Back() == false) {
      pins.setOutput4Back(true);
    }
  }
}
