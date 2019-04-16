package language;

import java.util.ArrayList;
import java.util.Collections;
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
public class Dictionary {
    
    private static Dictionary instance;
    
    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }
    
    private final List<String> syntax = new ArrayList<>();
    private final List<String> commands = new ArrayList<>();
    private final List<String> params = new ArrayList<>();

    private Dictionary() {
        Command cmd;
        
        cmd = new Camera(OnOff.ON, true);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Compressor(OnOff.ON);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Extension(ExtensionOption.PRIMARY, ExtensionMode.DOWN);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Fluid(OnOff.ON);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Light(Lights.PRIMARY, OnOff.ON);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Move(0, 0);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Start();
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Stop();
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Turn(LeftRight.LEFT, 0);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Unit(LengthUnit.INCH, VeloUnit.MPS);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        cmd = new Velocity(0);
        syntax.add(cmd.getName());
        commands.add(cmd.getName());
        
        for (ExtensionMode em : ExtensionMode.values()) {
            syntax.add(em.toString().toLowerCase());
            params.add(em.toString().toLowerCase());
        }
        
        for (ExtensionOption eo : ExtensionOption.values()) {
            syntax.add(eo.toString().toLowerCase());
            params.add(eo.toString().toLowerCase());
        }
        
        for (LeftRight lr : LeftRight.values()) {
            syntax.add(lr.toString().toLowerCase());
            params.add(lr.toString().toLowerCase());
        }
        
        for (LengthUnit lu : LengthUnit.values()) {
            syntax.add(lu.toString().toLowerCase());
            params.add(lu.toString().toLowerCase());
        }
        
        for (Lights l : Lights.values()) {
            syntax.add(l.toString().toLowerCase());
            params.add(l.toString().toLowerCase());
        }
        
        for (OnOff oo : OnOff.values()) {
            syntax.add(oo.toString().toLowerCase());
            params.add(oo.toString().toLowerCase());
        }
        
        for (VeloUnit vu : VeloUnit.values()) {
            syntax.add(vu.toString().toLowerCase());
            params.add(vu.toString().toLowerCase());
        }
        
        params.add("true");
        params.add("false");
        
        Collections.sort(syntax);
        Collections.sort(commands);
        Collections.sort(params);
    }

    public List<String> getSyntax() {
        return syntax;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getParams() {
        return params;
    }

}
