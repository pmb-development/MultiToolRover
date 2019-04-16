package data;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
 *
 * @author muri
 */
public class LoginData {
  

  private static boolean isGenerated = false;
  private final String raspIP;
  private final String password;
  private final int port;
  
  public boolean isGenerated () {
    return isGenerated;
  }
  
  public LoginData(String raspIP, String password, String port) {

    if (!raspIP.isEmpty() && InetAddressValidator.getInstance().isValidInet4Address(raspIP)) {
      this.raspIP = raspIP;
    } else {
      throw new IllegalArgumentException("invalid IP-Address");
    }

    this.password = password;

    if (!port.isEmpty()) {
      int intPort = Integer.parseInt(port);
      if (intPort > 0 || intPort < 10000) {
        this.port = intPort;
      } else {
        throw new IllegalArgumentException("invalid Port");
      }
    } else {
      throw new IllegalArgumentException("invalid Port");
    }
    isGenerated = true;
  }

  public String getRaspIP() {
    return raspIP;
  }

  public String getPassword() {
    return password;
  }

  public int getPort() {
    return port;
  }

  
}
