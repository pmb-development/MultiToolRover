package Pins;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.*;
import logging.Logger;

/**
 *
 * @author muri
 */
public class AnalogMeasurementThread extends Thread {

  private static final Logger LOG = Logger.getLogger(AnalogMeasurementThread.class.getName());

  private final GpioController gpio = GpioFactory.getInstance();
  private static AtomicReference<Double> batVoltage = new AtomicReference<>();
  private static AtomicReference<Double> airPreasure = new AtomicReference<>();

  public double getBatVoltage() {
    if (batVoltage.get().isNaN() || batVoltage.get() < 0) {
      return 0;
    }
    return batVoltage.get();
  }

  public double getPreasure() {
    if (airPreasure.get().isNaN() || airPreasure.get() < 0) {
      return 0;
    }
    return airPreasure.get();
  }

  @Override
  public void run() {

    DecimalFormat voltageFormat = new DecimalFormat("##.#");
    DecimalFormat airPreasureFormat = new DecimalFormat("#.#");

    try {
      final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);

      GpioPinAnalogInput measurement[] = {
        gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, "AirPreasure"), // Druckluft
        gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, "BatterieVoltage"), // Spannung Bat.
        gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, "MyAnalogInput-A2"),
        gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, "MyAnalogInput-A3"),};

      gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);

      gpioProvider.setEventThreshold(500, ADS1115Pin.ALL);

      gpioProvider.setMonitorInterval(100);

      GpioPinListenerAnalog batVoltageListener = new GpioPinListenerAnalog() {
        @Override
        public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {

          double value = event.getValue();

          double voltage = (gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * ((value) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE)) * 6;

          LOG.fine("MEASURMENT-> BatterieSpannung: " + voltage);
          batVoltage.set(voltage);
        }
      };

      GpioPinListenerAnalog preasureListener = new GpioPinListenerAnalog() {
        @Override
        public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {

          double value = event.getValue();

          double voltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * ((value) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);

          LOG.fine("MEASURMENT-> LuftDruck-Spannung: " + voltage);
          airPreasure.set(voltage);
        }
      };

      measurement[0].addListener(preasureListener);
      measurement[1].addListener(batVoltageListener);

      Thread.sleep(Long.MAX_VALUE);

    } catch (I2CFactory.UnsupportedBusNumberException | IOException | InterruptedException ex) {
      LOG.warning("MEASURMENT-> Problem bei der Messung: " + ex.getMessage());
    }

  }
}
