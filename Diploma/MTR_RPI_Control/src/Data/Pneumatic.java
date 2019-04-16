package Data;

import Pins.Pins;

/**
 *
 * @author muri
 */
public final class Pneumatic {

  private final String input;
  private final Pins pins = Pins.getInstance();

  public Pneumatic(String input) {
    this.input = input.substring(2);
    start();
  }

  private void start() {
    String[] split = input.split(";");

    if (split[0].equals("0") && pins.getStatePneumatic()) {
      pins.setPneumatic(false);
    } else if (split[0].equals("1") && pins.getStatePneumatic() == false) {
      pins.setPneumatic(true);
    }

    if (split[1].equals("0") && pins.getStatePneumaticValve1()) {
      pins.setPneumaticV1(false);
    } else if (split[1].equals("1") && pins.getStatePneumaticValve1() == false) {
      pins.setPneumaticV1(true);
    }

    if (split[2].equals("0") && pins.getStatePneumaticValve2()) {
      pins.setPneumaticV2(false);
    } else if (split[2].equals("1") && pins.getStatePneumaticValve2() == false) {
      pins.setPneumaticV2(true);
    }

    if (split[3].equals("0") && pins.getStatePneumaticValve3()) {
      pins.setPneumaticV3(false);
    } else if (split[3].equals("1") && pins.getStatePneumaticValve3() == false) {
      pins.setPneumaticV3(true);
    }

    if (split[4].equals("0") && pins.getStatePneumaticValve4()) {
      pins.setPneumaticV4(false);
    } else if (split[4].equals("1") && pins.getStatePneumaticValve4() == false) {
      pins.setPneumaticV4(true);
    }

    if (split[5].equals("0") && pins.getStatePneumaticValve5()) {
      pins.setPneumaticV5(false);
    } else if (split[5].equals("1") && pins.getStatePneumaticValve5() == false) {
      pins.setPneumaticV5(true);
    }

    if (split[6].equals("0") && pins.getStatePneumaticValve6()) {
      pins.setPneumaticV6(false);
    } else if (split[6].equals("1") && pins.getStatePneumaticValve6() == false) {
      pins.setPneumaticV6(true);
    }
  }
}
