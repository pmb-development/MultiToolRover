package language;

import java.util.List;
import language.commands.Camera;
import language.commands.Compressor;
import language.commands.Extension;
import language.commands.Fluid;
import language.commands.Light;
import language.commands.Move;
import language.commands.Start;
import language.commands.Stop;
import language.commands.Turn;
import language.commands.Unit;
import language.commands.Velocity;
import exceptions.WrongSyntaxException;
import language.params.ExtensionMode;
import language.params.ExtensionOption;
import language.params.LeftRight;
import language.params.LengthUnit;
import language.params.Lights;
import language.params.OnOff;
import language.params.VeloUnit;

/**
 *
 * @author emil
 */
public abstract class Command {
    
    public static Command commandFactory(String code) throws WrongSyntaxException {
        String[] parts = code.split(" ");
        
        switch(parts[0]) {
            case "cam": return new Camera(parseOnOff(parts[1]), Boolean.parseBoolean(parts[2]));
            case "comp": return new Compressor(parseOnOff(parts[1]));
            case "ext": return new Extension(parseExtensionOption(parts[1]), parseExtensionMode(parts[2]));
            case "fluid": return new Fluid(parseOnOff(parts[1]));
            case "light": return new Light(parseLights(parts[1]), parseOnOff(parts[2]));
            case "move": return new Move(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
            case "start": return new Start();
            case "stop": return new Stop();
            case "turn": return new Turn(parseLeftRight(parts[1]), Integer.parseInt(parts[2]));
            case "unit": return new Unit(parseLengthUnit(parts[1]), parseVeloUnit(parts[2]));
            case "velo": return new Velocity(Double.parseDouble(parts[1]));
            default: throw new WrongSyntaxException();
        }
    }
    
    private static ExtensionMode parseExtensionMode(String s) throws WrongSyntaxException {
        switch(s) {
            case "up": return ExtensionMode.UP;
            case "down": return ExtensionMode.DOWN;
            case "fold": return ExtensionMode.FOLD;
            case "unfold": return ExtensionMode.UNFOLD;
            default: throw new WrongSyntaxException();
        }
    }
    
    private static ExtensionOption parseExtensionOption(String s) throws WrongSyntaxException {
        switch(s) {
            case "primary": return ExtensionOption.PRIMARY;
            case "front": return ExtensionOption.PRIMARY;
            case "secondary": return ExtensionOption.SECONDARY;
            case "rear": return ExtensionOption.SECONDARY;
            default: throw new WrongSyntaxException();
        }
    }
    
    private static LeftRight parseLeftRight(String s) throws WrongSyntaxException {
        switch(s) {
            case "left": return LeftRight.LEFT;
            case "right": return LeftRight.RIGHT;
            default: throw new WrongSyntaxException();
        }
    }
    
    private static LengthUnit parseLengthUnit(String s) throws WrongSyntaxException {
        switch(s) {
            case "mm": return LengthUnit.MM;
            case "cm": return LengthUnit.CM;
            case "m": return LengthUnit.M;
            case "km": return LengthUnit.KM;
            case "inch": return LengthUnit.INCH;
            case "foot": return LengthUnit.FOOT;
            case "yard": return LengthUnit.YARD;
            case "mile": return LengthUnit.MILE;
            default: throw new WrongSyntaxException();
        }
    }
    
    private static Lights parseLights(String s) throws WrongSyntaxException {
        switch(s) {
            case "primary": return Lights.PRIMARY;
            case "main": return Lights.PRIMARY;
            case "secondary": return Lights.SECONDARY;
            case "lr": return Lights.SECONDARY;
            case "inside": return Lights.INSIDE;
            case "working": return Lights.INSIDE;
            default: throw new WrongSyntaxException();
        }
    }
    
    private static OnOff parseOnOff(String s) throws WrongSyntaxException {
        switch(s) {
            case "on": return OnOff.ON;
            case "1": return OnOff.ON;
            case "activate": return OnOff.ON;
            case "true": return OnOff.ON;
            case "off": return OnOff.OFF;
            case "0": return OnOff.OFF;
            case "deactivate": return OnOff.OFF;
            case "false": return OnOff.OFF;
            default: throw new WrongSyntaxException();
        }
    }
    
    private static VeloUnit parseVeloUnit(String s) throws WrongSyntaxException {
        switch(s) {
            case "mps": return VeloUnit.MPS;
            case "kmh": return VeloUnit.KMH;
            case "fps": return VeloUnit.FPS;
            case "mph": return VeloUnit.MPH;
            case "mih": return VeloUnit.MPH;
            default: throw new WrongSyntaxException();
        }
    }
    
    public abstract String getName();
    public abstract List<Object> getParams();

}
