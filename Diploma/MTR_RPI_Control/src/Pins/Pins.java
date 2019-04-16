package Pins;

import com.pi4j.io.gpio.*;
import com.pi4j.io.serial.*;
import com.pi4j.util.Console;
import java.io.IOException;
import logging.Logger;

/**
 * @author muri Diese Klasse verwaltet alle Pin Ausgänge des Raspberrys
 *
 */
public class Pins {

  private static final Logger LOG = Logger.getLogger(Pins.class.getName());

  // Entwurfsmuster Singleton
  private static Pins instance;

  public static Pins getInstance() {
    if (instance == null) {
      instance = new Pins();
      LOG.severe();
    }
    return instance;
  }

  private Pins() {
  }

  public final GpioController gpio = GpioFactory.getInstance();
  public final GpioPinDigitalOutput pin11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00); // Kompressor
  public final GpioPinDigitalOutput pin12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01); // Ventil1
  public final GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02); // Ventil2
  public final GpioPinDigitalOutput pin15 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03); // Ventil3
  public final GpioPinDigitalOutput pin16 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04); // Ventil4
  public final GpioPinDigitalOutput pin18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05); // Ventil5
  public final GpioPinDigitalOutput pin22 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06); // Ventil6
  public final GpioPinDigitalOutput pin07 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07); // Innenlicht (grün)
//  public final GpioPinDigitalOutput pin03 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08); // Sensoren
//  public final GpioPinDigitalOutput pin05 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09); // Sensoren
  public final GpioPinDigitalOutput pin24 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10); // Fernlicht
  public final GpioPinDigitalOutput pin26 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11); // Flutlicht
  public final GpioPinDigitalOutput pin19 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12); // Warnlicht
  public final GpioPinDigitalOutput pin21 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13); // Vout1Front
  public final GpioPinDigitalOutput pin23 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14); // Vout2Front
//  public final GpioPinDigitalOutput pin08 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15); Motorenansteuerung
//  public final GpioPinDigitalOutput pin10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16); Motorenansteuerung
  public final GpioPinDigitalOutput pin29 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21); // Vout3Back
  public final GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22); // Vout4Back
  public final GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23); // Bildschirm
  public final GpioPinDigitalOutput pin35 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24); // ReserveRelai
  public final GpioPinDigitalOutput pin37 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25); // ReserveRelai
  public final GpioPinDigitalOutput pin32 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26); // ReserveRelai
  public final GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27); // ReserveRelai
  public final GpioPinDigitalOutput pin38 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28); // ReserveRelai
  public final GpioPinDigitalOutput pin40 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29); // ReserveRelai
  AnalogMeasurementThread measurement = new AnalogMeasurementThread();

  public void setMotor(String data) throws InterruptedException, IOException {
    final Console console = new Console();
    final Serial serial = SerialFactory.createInstance();

    try {
      SerialConfig config = new SerialConfig();

      config.device(SerialPort.getDefaultPort())
              .baud(Baud._9600)
              .dataBits(DataBits._8)
              .parity(Parity.NONE)
              .stopBits(StopBits._1)
              .flowControl(FlowControl.NONE);

      serial.open(config);
      try {

        serial.writeln(data);
      } catch (IllegalStateException ex) {
        LOG.warning("PINS-> Fehler bei UART: " + ex.getMessage());
      }
    } catch (IOException ex) {
      LOG.warning("PINS-> Fehler bei UART: " + ex.getMessage());
    }
  }

  public void initPins() {

  }

  public void setPneumatic(boolean on) {
    if (on) {
      pin33.setState(PinState.LOW);
    } else {
      pin33.setState(PinState.HIGH);
    }
  }

  public void setPneumaticV1(boolean open) {
    if (open) {
      pin29.setState(PinState.LOW);
    } else {
      pin29.setState(PinState.HIGH);
    }
  }

  public void setPneumaticV2(boolean open) {
    if (open) {
      pin23.setState(PinState.LOW);
    } else {
      pin23.setState(PinState.HIGH);
    }
  }

  public void setPneumaticV3(boolean open) {
    if (open) {
      pin21.setState(PinState.LOW);
    } else {
      pin21.setState(PinState.HIGH);
    }
  }

  public void setPneumaticV4(boolean open) {
    if (open) {
      pin19.setState(PinState.LOW);
    } else {
      pin19.setState(PinState.HIGH);
    }
  }

  public void setPneumaticV5(boolean open) {
    if (open) {
      pin24.setState(PinState.LOW);
    } else {
      pin24.setState(PinState.HIGH);
    }
  }

  public void setPneumaticV6(boolean open) {
    if (open) {
      pin26.setState(PinState.LOW);
    } else {
      pin26.setState(PinState.HIGH);
    }
  }

  public void setInteriorLight(boolean on) {
    if (on) {
      pin31.setState(PinState.LOW);
    } else {
      pin31.setState(PinState.HIGH);
    }
  }

  public void setHeadLight(boolean on) {
    if (on) {
      pin13.setState(PinState.LOW);
    } else {
      pin13.setState(PinState.HIGH);
    }
  }

  public void setFloodLight(boolean on) {
    if (on) {
      pin15.setState(PinState.LOW);
    } else {
      pin15.setState(PinState.HIGH);
    }
  }

  public void setWarningLight(boolean on) {
    if (on) {
      pin12.setState(PinState.LOW);
    } else {
      pin12.setState(PinState.HIGH);
    }
  }

  public void setOutput1Front(boolean on) {
    if (on) {
      pin16.setState(PinState.LOW);
    } else {
      pin16.setState(PinState.HIGH);
    }
  }

  public void setOutput2Front(boolean on) {
    if (on) {
      pin18.setState(PinState.LOW);
    } else {
      pin18.setState(PinState.HIGH);
    }
  }

  public void setOutput3Back(boolean on) {
    if (on) {
      pin07.setState(PinState.LOW);
    } else {
      pin07.setState(PinState.HIGH);
    }
  }

  public void setOutput4Back(boolean on) {
    if (on) {
      pin22.setState(PinState.LOW);
    } else {
      pin22.setState(PinState.HIGH);
    }
  }

  public void setDisplay(boolean on) {
    if (on) {
      pin11.setState(PinState.LOW);
    } else {
      pin11.setState(PinState.HIGH);
    }
  }

  public void setRestRelai1(boolean on) {
    if (on) {
      pin35.setState(PinState.LOW);
    } else {
      pin35.setState(PinState.HIGH);
    }
  }

  public void setRestRelai2(boolean on) {
    if (on) {
      pin37.setState(PinState.LOW);
    } else {
      pin37.setState(PinState.HIGH);
    }
  }

  public void setRestRelai3(boolean on) {
    if (on) {
      pin32.setState(PinState.LOW);
    } else {
      pin32.setState(PinState.HIGH);
    }
  }

  public void setRestRelai4(boolean on) {
    if (on) {
      pin36.setState(PinState.LOW);
    } else {
      pin36.setState(PinState.HIGH);
    }
  }

  public void setRestRelai5(boolean on) {
    if (on) {
      pin38.setState(PinState.LOW);
    } else {
      pin38.setState(PinState.HIGH);
    }
  }

  public void setRestRelai6(boolean on) {
    if (on) {
      pin40.setState(PinState.LOW);
    } else {
      pin40.setState(PinState.HIGH);
    }
  }

  public double getBatVoltage() {
    return measurement.getBatVoltage();
  }

  public double getPreasure() {
    return measurement.getPreasure();
  }

  public boolean getStatePneumatic() {
    return gpio.getState(pin33) == PinState.LOW;
  }

  public boolean getStatePneumaticValve1() {
    return gpio.getState(pin29) == PinState.LOW;
  }

  public boolean getStatePneumaticValve2() {
    return gpio.getState(pin23) == PinState.LOW;
  }

  public boolean getStatePneumaticValve3() {
    return gpio.getState(pin21) == PinState.LOW;
  }

  public boolean getStatePneumaticValve4() {
    return gpio.getState(pin19) == PinState.LOW;
  }

  public boolean getStatePneumaticValve5() {
    return gpio.getState(pin24) == PinState.LOW;
  }

  public boolean getStatePneumaticValve6() {
    return gpio.getState(pin26) == PinState.LOW;
  }

  public boolean getStateInteriorLight() {
    return gpio.getState(pin31) == PinState.LOW;
  }

  public boolean getStateHeadLight() {
    return gpio.getState(pin13) == PinState.LOW;
  }

  public boolean getStateFloodLight() {
    return gpio.getState(pin15) == PinState.LOW;
  }

  public boolean getStateWarningLight() {
    return gpio.getState(pin12) == PinState.LOW;
  }

  public boolean getStateOutput1Front() {
    return gpio.getState(pin16) == PinState.LOW;
  }

  public boolean getStateOutput2Front() {
    return gpio.getState(pin18) == PinState.LOW;
  }

  public boolean getStateOutput3Back() {
    return gpio.getState(pin07) == PinState.LOW;
  }

  public boolean getStateOutput4Back() {
    return gpio.getState(pin22) == PinState.LOW;
  }

  public boolean getStateDisplay() {
    return gpio.getState(pin11) == PinState.LOW;
  }

  public void shutdown() {
    gpio.shutdown();
    gpio.unprovisionPin(pin11, pin12, pin13, pin15, pin16, pin18, pin22, pin07, pin24, pin26, pin19, pin21,
            pin23, pin29, pin31, pin33, pin35, pin37, pin32, pin36);
  }

}
