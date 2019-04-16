package gui;

import com.Communication;
import data.LoginData;
import data.ThreadMessage;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import logging.LogBackgroundHandler;
import logging.LogOutputStreamHandler;

/**
 *
 * @author muri
 */
public final class MultiToolRover extends JFrame {

  private static final Logger LOG;
  private static final Logger LOGP;

  ComWorker raspCom;
  LoginData loginData = new LoginData("10.0.0.5", "1234", "8080");
  private SocketAddress socketAddress;
  private String response;
  private boolean isConnected = false;

  public static MultiToolRover instance;

  private static volatile boolean wPressed = false;
  private static volatile boolean aPressed = false;
  private static volatile boolean sPressed = false;
  private static volatile boolean dPressed = false;

  public int leftMotor;
  public int rightMotor;
  public int pneumaticV1;
  public int pneumaticV2;
  public int pneumaticV3;
  public int pneumaticV4;
  public int pneumaticV5;
  public int pneumaticV6;
  public int pneumatic;
  public int interiorLight;
  public int headLight;
  public int floodLight;
  public int warningLight;
  public int output1Front;
  public int output2Front;
  public int output3Back;
  public int output4Back;
  public int display;

  public static MultiToolRover getInstance() {
    if (instance == null) {
      instance = new MultiToolRover();
    }
    return instance;
  }

  private MultiToolRover() {
    initComponents();
    setLocationRelativeTo(null);
    setTitle("MultiToolRover");
    setMinimumSize(new Dimension(800, 500));
    setJButtons(false);
    setjSliderSpeed(100, 50, 25);
    jSliderSpeed.setValue(0);
    jTextBat.setFocusable(false);
    jTextBat.setText("00.0V");
    jTextPreasure.setFocusable(false);
    jTextPreasure.setText("0.0bar");
    addLogFileHandler(true);

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

      @Override
      public boolean dispatchKeyEvent(KeyEvent ke) {
        synchronized (MultiToolRover.class) {
          switch (ke.getID()) {
            case KeyEvent.KEY_PRESSED:
              if (ke.getKeyCode() == KeyEvent.VK_W) {
                wPressed = true;
              }
              if (ke.getKeyCode() == KeyEvent.VK_A) {
                aPressed = true;
              }
              if (ke.getKeyCode() == KeyEvent.VK_S) {
                sPressed = true;
              }
              if (ke.getKeyCode() == KeyEvent.VK_D) {
                dPressed = true;
              }
              break;

            case KeyEvent.KEY_RELEASED:
              if (ke.getKeyCode() == KeyEvent.VK_W) {
                wPressed = false;
              }
              if (ke.getKeyCode() == KeyEvent.VK_A) {
                aPressed = false;
              }
              if (ke.getKeyCode() == KeyEvent.VK_S) {
                sPressed = false;
              }
              if (ke.getKeyCode() == KeyEvent.VK_D) {
                dPressed = false;
              }
              break;
          }
          return false;
        }
      }
    });

  }

  public synchronized void getPins() {
    pneumaticV1 = jV1.isSelected() ? 1 : 0;
    pneumaticV2 = jV2.isSelected() ? 1 : 0;
    pneumaticV3 = jV3.isSelected() ? 1 : 0;
    pneumaticV4 = jV4.isSelected() ? 1 : 0;
    pneumaticV5 = jV5.isSelected() ? 1 : 0;
    pneumaticV6 = jV6.isSelected() ? 1 : 0;
    pneumatic = jPneumaticON.isSelected() ? 1 : 0;
    interiorLight = jInteriorLight.isSelected() ? 1 : 0;
    headLight = jHeadLight.isSelected() ? 1 : 0;
    floodLight = jFloodLight.isSelected() ? 1 : 0;
    warningLight = jWarningLight.isSelected() ? 1 : 0;
    output1Front = jOutputFront1.isSelected() ? 1 : 0;
    output2Front = jOutputFront2.isSelected() ? 1 : 0;
    output3Back = jOutputBack1.isSelected() ? 1 : 0;
    output4Back = jOutputBack2.isSelected() ? 1 : 0;
    display = jDisplay.isSelected() ? 1 : 0;
  }

  public synchronized void getMotorSpeed() {
    double speed = (jSliderSpeed.getValue() / 2);

    // Variante 1
    if (wPressed && !aPressed && !sPressed && !dPressed) {
      leftMotor = (int) ((speed * 1) + 100);
      rightMotor = (int) ((speed * 1) + 100);

      // Variante 2
    } else if (!wPressed && !aPressed && sPressed && !dPressed) {
      leftMotor = (int) (100 - (speed * 1));
      rightMotor = (int) (100 - (speed * 1));

      // Variante 3
    } else if (!wPressed && aPressed && !sPressed && !dPressed) {
      leftMotor = (int) ((speed * 0) + 100);
      rightMotor = (int) ((speed * 1) + 100);

      // Variante 4
    } else if (!wPressed && !aPressed && !sPressed && dPressed) {
      leftMotor = (int) ((speed * 1) + 100);
      rightMotor = (int) ((speed * 0) + 100);

      // Variante 5
    } else if (wPressed && aPressed && !sPressed && !dPressed) {
      leftMotor = (int) ((speed * 0.5) + 100);
      rightMotor = (int) ((speed * 1) + 100);

      // Variante 6
    } else if (!wPressed && aPressed && sPressed && !dPressed) {
      leftMotor = (int) (100 - (speed * 0.5));
      rightMotor = (int) (100 - (speed * 1));

      // Variante 7
    } else if (wPressed && !aPressed && !sPressed && dPressed) {
      leftMotor = (int) ((speed * 1) + 100);
      rightMotor = (int) ((speed * 0.5) + 100);

      // Variante 8
    } else if (!wPressed && !aPressed && sPressed && dPressed) {
      leftMotor = (int) (100 - (speed * 1));
      rightMotor = (int) (100 - (speed * 0.5));

      // Variante 9
    } else if (wPressed && !aPressed && sPressed && !dPressed) {
      leftMotor = (int) ((speed * 1) + 100);
      rightMotor = (int) (100 - (speed * 1));

      // Variante 10
    } else if (!wPressed && aPressed && !sPressed && dPressed) {
      leftMotor = (int) (100 - (speed * 1));
      rightMotor = (int) ((speed * 1) + 100);

    } else {
      leftMotor = 100;
      rightMotor = 100;
    }
  }

  public synchronized String getRequest() {
    getPins();
    getMotorSpeed();
    String leftMotorAsString = (String.format("%03d", leftMotor));
    String rightMotorAsString = (String.format("%03d", rightMotor));

    String pneumaticString = ("P-" + pneumatic + ";" + pneumaticV1 + ";" + pneumaticV2
            + ";" + pneumaticV3 + ";" + pneumaticV4 + ";" + pneumaticV5 + ";" + pneumaticV6 + ";");
    String lightString = ("L-" + interiorLight + ";" + headLight + ";" + floodLight + ";" + warningLight + ";");
    String outputString = ("O-" + output1Front + ";" + output2Front + ";" + output3Back + ";" + output4Back + ";");
    String displayString = ("D-" + display + ";");
    String motorString = ("M-" + leftMotorAsString + ";" + rightMotorAsString + ";");

    return ("START|" + pneumaticString + "|" + lightString + "|" + outputString + "|" + displayString + "|" + motorString + "|STOP");
  }

  public void setJButtons(boolean set) {
    jSpeedMode.setEnabled(set);
    jWarningLight.setEnabled(set);
    jFloodLight.setEnabled(set);
    jHeadLight.setEnabled(set);
    jInteriorLight.setEnabled(set);
    jPneumaticON.setEnabled(set);
    jV1.setEnabled(set);
    jV2.setEnabled(set);
    jV3.setEnabled(set);
    jV4.setEnabled(set);
    jV5.setEnabled(set);
    jV6.setEnabled(set);
    jOutputFront1.setEnabled(set);
    jOutputFront2.setEnabled(set);
    jOutputBack1.setEnabled(set);
    jOutputBack2.setEnabled(set);
    jDisplay.setEnabled(set);
    jSliderSpeed.setFocusable(set);
    jSliderSpeed.setEnabled(set);
    jSliderSpeed.setValue(0);
    jSpeedMode.setFocusable(set);
    jTextBat.setFocusable(false);
    jTextBat.setEnabled(set);
    jTextPreasure.setFocusable(false);
    jTextPreasure.setEnabled(set);
  }

  public void resetJButtons() {
    jTextBat.setText("00.0V");
    jTextPreasure.setText("0.0bar");
    jSpeedMode.setSelected(false);
    jWarningLight.setSelected(false);
    jFloodLight.setSelected(false);
    jHeadLight.setSelected(false);
    jInteriorLight.setSelected(false);
    jPneumaticON.setSelected(false);
    jV1.setSelected(false);
    jV2.setSelected(false);
    jV3.setSelected(false);
    jV4.setSelected(false);
    jV5.setSelected(false);
    jV6.setSelected(false);
    jOutputFront1.setSelected(false);
    jOutputFront2.setSelected(false);
    jOutputBack1.setSelected(false);
    jOutputBack2.setSelected(false);
    jDisplay.setSelected(false);
    jSliderSpeed.setFocusable(false);
    jSliderSpeed.setEnabled(false);
    jSliderSpeed.setValue(0);
    jSpeedMode.setFocusable(false);
  }

  public void setjSliderSpeed(int max, int tickMajorSpacing, int tickMinorSpacing) {
    jSliderSpeed.setMaximum(max);
    jSliderSpeed.setMajorTickSpacing(tickMajorSpacing);
    jSliderSpeed.setMinorTickSpacing(tickMinorSpacing);
    jSliderSpeed.setPaintTicks(true);
    jSliderSpeed.setPaintLabels(true);
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
          folder = new File(home + File.separator + "Documents" + File.separator + "MTR_Files");
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

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanJFrame = new javax.swing.JPanel();
    jPanHeadBar = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    jButConnect = new javax.swing.JToggleButton();
    jPanel3 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    jTextBat = new javax.swing.JTextField();
    jPanel4 = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    jPanCenter = new javax.swing.JPanel();
    jPanVideo = new javax.swing.JPanel();
    FrontCam = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    BackCam = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jPanButtons = new javax.swing.JPanel();
    jPanLPAM = new javax.swing.JPanel();
    Light = new javax.swing.JPanel();
    jWarningLight = new javax.swing.JCheckBox();
    jInteriorLight = new javax.swing.JCheckBox();
    jHeadLight = new javax.swing.JCheckBox();
    jFloodLight = new javax.swing.JCheckBox();
    Pneumatic = new javax.swing.JPanel();
    jV3 = new javax.swing.JCheckBox();
    jV2 = new javax.swing.JCheckBox();
    jV4 = new javax.swing.JCheckBox();
    jV5 = new javax.swing.JCheckBox();
    jV6 = new javax.swing.JCheckBox();
    jV1 = new javax.swing.JCheckBox();
    jPneumaticON = new javax.swing.JToggleButton();
    jLabelPreasure = new javax.swing.JLabel();
    jTextPreasure = new javax.swing.JTextField();
    Output = new javax.swing.JPanel();
    jOutputFront2 = new javax.swing.JCheckBox();
    jOutputBack1 = new javax.swing.JCheckBox();
    jOutputBack2 = new javax.swing.JCheckBox();
    jOutputFront1 = new javax.swing.JCheckBox();
    Motor = new javax.swing.JPanel();
    jLabelSpeed = new javax.swing.JLabel();
    jSliderSpeed = new javax.swing.JSlider();
    jSpeedMode = new javax.swing.JCheckBox();
    jPanD = new javax.swing.JPanel();
    jDisplay = new javax.swing.JCheckBox();
    jPanStateBar = new javax.swing.JPanel();
    jLabStatus = new javax.swing.JLabel();
    jLabStateText = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(new java.awt.GridLayout(1, 0));

    jPanJFrame.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0, true));
    jPanJFrame.setLayout(new java.awt.BorderLayout());

    jPanHeadBar.setBackground(new java.awt.Color(236, 236, 236));
    jPanHeadBar.setLayout(new java.awt.BorderLayout());

    jPanel1.setBackground(new java.awt.Color(236, 236, 236));
    jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 35));
    jPanel1.setLayout(new java.awt.GridBagLayout());

    jButConnect.setText("Verbinden");
    jButConnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButConnectActionPerformed(evt);
      }
    });
    jPanel1.add(jButConnect, new java.awt.GridBagConstraints());

    jPanHeadBar.add(jPanel1, java.awt.BorderLayout.WEST);

    jPanel3.setBackground(new java.awt.Color(236, 236, 236));
    jPanel3.setLayout(new java.awt.GridBagLayout());

    jLabel3.setText("Batterie Spannung:");
    jPanel3.add(jLabel3, new java.awt.GridBagConstraints());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 45;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    jPanel3.add(jTextBat, gridBagConstraints);

    jPanHeadBar.add(jPanel3, java.awt.BorderLayout.EAST);

    jPanel4.setBackground(new java.awt.Color(236, 236, 236));
    jPanel4.setLayout(new java.awt.BorderLayout());

    jLabel4.setBackground(new java.awt.Color(236, 236, 236));
    jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 26)); // NOI18N
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("MultiToolRover");
    jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jPanel4.add(jLabel4, java.awt.BorderLayout.CENTER);

    jPanHeadBar.add(jPanel4, java.awt.BorderLayout.CENTER);

    jPanJFrame.add(jPanHeadBar, java.awt.BorderLayout.NORTH);

    jPanCenter.setBackground(new java.awt.Color(250, 250, 250));
    jPanCenter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(142, 142, 142)));
    jPanCenter.setLayout(new java.awt.GridBagLayout());

    jPanVideo.setBackground(new java.awt.Color(228, 228, 228));
    jPanVideo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 211, 211)));
    jPanVideo.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

    FrontCam.setBackground(new java.awt.Color(228, 228, 228));
    FrontCam.setBorder(javax.swing.BorderFactory.createTitledBorder("Front Kamera"));
    FrontCam.setLayout(new java.awt.GridLayout(1, 0));

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Camera-icon.png"))); // NOI18N
    FrontCam.add(jLabel5);

    jPanVideo.add(FrontCam);

    BackCam.setBackground(new java.awt.Color(228, 228, 228));
    BackCam.setBorder(javax.swing.BorderFactory.createTitledBorder("Heck Kamera"));
    BackCam.setLayout(new java.awt.GridLayout(1, 0));

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Camera-icon.png"))); // NOI18N
    BackCam.add(jLabel2);

    jPanVideo.add(BackCam);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.weighty = 0.1;
    jPanCenter.add(jPanVideo, gridBagConstraints);

    jPanButtons.setBackground(new java.awt.Color(236, 236, 236));
    jPanButtons.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 211, 211)));
    jPanButtons.setLayout(new java.awt.GridBagLayout());

    jPanLPAM.setBackground(new java.awt.Color(236, 236, 236));
    jPanLPAM.setLayout(new java.awt.GridLayout(2, 2, 10, 10));

    Light.setBackground(new java.awt.Color(236, 236, 236));
    Light.setBorder(javax.swing.BorderFactory.createTitledBorder("Licht Steuerung"));
    Light.setLayout(new java.awt.GridBagLayout());

    jWarningLight.setText("Warnblinklicht");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    Light.add(jWarningLight, gridBagConstraints);

    jInteriorLight.setText("Wartungslicht");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    Light.add(jInteriorLight, gridBagConstraints);

    jHeadLight.setText("Hauptscheinwerfer");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    Light.add(jHeadLight, gridBagConstraints);

    jFloodLight.setText("Scheinwerfer links/rechts");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    Light.add(jFloodLight, gridBagConstraints);

    jPanLPAM.add(Light);

    Pneumatic.setBackground(new java.awt.Color(236, 236, 236));
    Pneumatic.setBorder(javax.swing.BorderFactory.createTitledBorder("Pneumatik Steuerung"));
    Pneumatic.setLayout(new java.awt.GridBagLayout());

    jV3.setText("V3");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    Pneumatic.add(jV3, gridBagConstraints);

    jV2.setText("V2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    Pneumatic.add(jV2, gridBagConstraints);

    jV4.setText("V4");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    Pneumatic.add(jV4, gridBagConstraints);

    jV5.setText("V5");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    Pneumatic.add(jV5, gridBagConstraints);

    jV6.setText("V6");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    Pneumatic.add(jV6, gridBagConstraints);

    jV1.setText("V1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    Pneumatic.add(jV1, gridBagConstraints);

    jPneumaticON.setText("Kompressor EIN");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    Pneumatic.add(jPneumaticON, gridBagConstraints);

    jLabelPreasure.setText("Luftdruck: ");
    Pneumatic.add(jLabelPreasure, new java.awt.GridBagConstraints());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.ipadx = 45;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    Pneumatic.add(jTextPreasure, gridBagConstraints);

    jPanLPAM.add(Pneumatic);

    Output.setBackground(new java.awt.Color(236, 236, 236));
    Output.setBorder(javax.swing.BorderFactory.createTitledBorder("Ausgänge Steuerung"));
    Output.setLayout(new java.awt.GridBagLayout());

    jOutputFront2.setText("Vorne 2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    Output.add(jOutputFront2, gridBagConstraints);

    jOutputBack1.setText("Hinten 1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    Output.add(jOutputBack1, gridBagConstraints);

    jOutputBack2.setText("Hinten 2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    Output.add(jOutputBack2, gridBagConstraints);

    jOutputFront1.setText("Vorne 1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    Output.add(jOutputFront1, gridBagConstraints);

    jPanLPAM.add(Output);

    Motor.setBackground(new java.awt.Color(236, 236, 236));
    Motor.setBorder(javax.swing.BorderFactory.createTitledBorder("Motoren Steuerung"));
    Motor.setLayout(new java.awt.GridBagLayout());

    jLabelSpeed.setText("Geschwindigkeit");
    Motor.add(jLabelSpeed, new java.awt.GridBagConstraints());

    jSliderSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jSliderSpeedStateChanged(evt);
      }
    });
    Motor.add(jSliderSpeed, new java.awt.GridBagConstraints());

    jSpeedMode.setText("SpeedMode");
    jSpeedMode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jSpeedModeActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    Motor.add(jSpeedMode, gridBagConstraints);

    jPanLPAM.add(Motor);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    jPanButtons.add(jPanLPAM, gridBagConstraints);

    jPanD.setBackground(new java.awt.Color(236, 236, 236));
    jPanD.setBorder(javax.swing.BorderFactory.createTitledBorder("Sonstige Steuerung"));
    jPanD.setLayout(new java.awt.GridBagLayout());

    jDisplay.setText("Display");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    jPanD.add(jDisplay, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.weighty = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    jPanButtons.add(jPanD, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.1;
    jPanCenter.add(jPanButtons, gridBagConstraints);

    jPanJFrame.add(jPanCenter, java.awt.BorderLayout.CENTER);

    jPanStateBar.setBackground(new java.awt.Color(228, 228, 228));
    jPanStateBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jPanStateBar.setLayout(new java.awt.GridBagLayout());

    jLabStatus.setText("Status:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
    jPanStateBar.add(jLabStatus, gridBagConstraints);

    jLabStateText.setText("Aufgaben");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 5.0;
    jPanStateBar.add(jLabStateText, gridBagConstraints);

    jPanJFrame.add(jPanStateBar, java.awt.BorderLayout.PAGE_END);

    getContentPane().add(jPanJFrame);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButConnectActionPerformed
    try {
      Login login = new Login(this, true);

      if (loginData.isGenerated()) {
        login.setLogin(loginData);
      }

      if (isConnected) {
        int choosen = JOptionPane.showConfirmDialog(this, "Sie sind bereits Verbunden. Wollen Sie die Verbindung Trennen?", "Verbindung", JOptionPane.INFORMATION_MESSAGE);
        if (choosen == JOptionPane.YES_OPTION) {
          LOG.warning("MTR-> CommunicationThread Interrupt");
          raspCom.cancel(true);
        } else if (choosen == JOptionPane.NO_OPTION || choosen == JOptionPane.CANCEL_OPTION) {
          return;
        }
      }

      if (!isConnected) {
        login.setVisible(true);
      }

      if (login.isPressedConnect()) {
        loginData = login.getLoginData();
        String hostname = loginData.getRaspIP();
        String password = loginData.getPassword();
        int port = loginData.getPort();

        raspCom = new ComWorker();
        raspCom.initCommunication(hostname, port, password);
        raspCom.execute();

        login.setPressedConnect(false);

      } else if (raspCom.getConnection()) {
        raspCom.cancel(true);
        LOG.info("MTR-> CommunicationThread Interrupt");
      }
      jButConnect.setSelected(false);
      jButConnect.setText("Verbinden");
    } catch (Exception ex) {
      LOG.warning("MTR-> Verbindungsaufbau fehlgeschlagen: " + ex.getMessage());
    }
  }//GEN-LAST:event_jButConnectActionPerformed

  private void jSpeedModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSpeedModeActionPerformed
    if (jSpeedMode.isSelected()) {
      setjSliderSpeed(200, 100, 50);
    } else {
      setjSliderSpeed(100, 50, 25);
    }
  }//GEN-LAST:event_jSpeedModeActionPerformed

  private void jSliderSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderSpeedStateChanged

  }//GEN-LAST:event_jSliderSpeedStateChanged

  // SwingWorker
  private class ComWorker extends Communication {

    private Cursor lastCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private boolean lastConnection = false;
    private final MultiToolRover mtr = MultiToolRover.getInstance();

    @Override
    protected void done() {
      setJButtons(false);
      resetJButtons();
      isConnected = false;
      jLabStateText.setText("Nicht Verbunden");
      LOG.warning("CWORK-> SwingWorker wurde beendet");
      super.done();
    }

    @Override
    protected void process(List<ThreadMessage> chunks) {
      for (ThreadMessage tm : chunks) {

        // change cursor
        if (tm.getCursor() != lastCursor) {
          lastCursor = tm.getCursor();
          setCursor(tm.getCursor());
        }

        isConnected = tm.isConnected();
        if (lastConnection != isConnected) {
          if (isConnected) {
            setJButtons(true);
            lastConnection = isConnected;
          } else {
            setJButtons(false);
            resetJButtons();
            lastConnection = isConnected;
          }
        }

        jLabStateText.setText(tm.getMessage());

        if (tm.isConnected()) {
          jButConnect.setText("Verbunden");
          jButConnect.setSelected(true);
        } else if (!tm.isConnected()) {
          jButConnect.setSelected(false);
          jButConnect.setText("Verbinden");
        }

        if (!tm.getjOptionPane().isEmpty()) {
          JOptionPane.showMessageDialog(mtr, tm.getjOptionPane(), "Verbindungsaufbau", JOptionPane.ERROR_MESSAGE);
          LOG.warning("CWORK-> JOptionPane bei Verbindungsaufbau erzeugt");
        }
        jTextPreasure.setText(tm.getPreasure() + " bar");
        jTextBat.setText(tm.getVoltage() + " V");

      }
    }

  }

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    LOGP.addHandler(new LogBackgroundHandler(new LogOutputStreamHandler(System.out)));
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if (System.getProperty("os.name").contains("Mac OS X")) {
          System.setProperty("apple.awt.fileDialogForDirectories", "true");
          System.setProperty("apple.laf.useScreenMenuBar", "true");
          System.setProperty("com.apple.mjr.application.apple.menu.about.name", "MultiToolRover");
          if ("MAC OS X".equals(info.getName())) {
            javax.swing.UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        } else if (System.getProperty("os.name").startsWith("Windows")) {
          if ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel".equals(info.getName())) {
            javax.swing.UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        } else {
          if ("Nimbus".equals(info.getName())) {
            javax.swing.UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(MultiToolRover.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MultiToolRover.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MultiToolRover.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MultiToolRover.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        LOG.info("MTR-> Start of MultiToolRover");
        MultiToolRover.getInstance().setVisible(true);
      }
    });
  }

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

    LOG = Logger.getLogger(MultiToolRover.class.getName());
    LOGP = Logger.getParentLogger();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel BackCam;
  private javax.swing.JPanel FrontCam;
  private javax.swing.JPanel Light;
  private javax.swing.JPanel Motor;
  private javax.swing.JPanel Output;
  private javax.swing.JPanel Pneumatic;
  private javax.swing.JToggleButton jButConnect;
  private javax.swing.JCheckBox jDisplay;
  private javax.swing.JCheckBox jFloodLight;
  private javax.swing.JCheckBox jHeadLight;
  private javax.swing.JCheckBox jInteriorLight;
  private javax.swing.JLabel jLabStateText;
  private javax.swing.JLabel jLabStatus;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabelPreasure;
  private javax.swing.JLabel jLabelSpeed;
  private javax.swing.JCheckBox jOutputBack1;
  private javax.swing.JCheckBox jOutputBack2;
  private javax.swing.JCheckBox jOutputFront1;
  private javax.swing.JCheckBox jOutputFront2;
  private javax.swing.JPanel jPanButtons;
  private javax.swing.JPanel jPanCenter;
  private javax.swing.JPanel jPanD;
  private javax.swing.JPanel jPanHeadBar;
  private javax.swing.JPanel jPanJFrame;
  private javax.swing.JPanel jPanLPAM;
  private javax.swing.JPanel jPanStateBar;
  private javax.swing.JPanel jPanVideo;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JToggleButton jPneumaticON;
  private javax.swing.JSlider jSliderSpeed;
  private javax.swing.JCheckBox jSpeedMode;
  private javax.swing.JTextField jTextBat;
  private javax.swing.JTextField jTextPreasure;
  private javax.swing.JCheckBox jV1;
  private javax.swing.JCheckBox jV2;
  private javax.swing.JCheckBox jV3;
  private javax.swing.JCheckBox jV4;
  private javax.swing.JCheckBox jV5;
  private javax.swing.JCheckBox jV6;
  private javax.swing.JCheckBox jWarningLight;
  // End of variables declaration//GEN-END:variables

}
