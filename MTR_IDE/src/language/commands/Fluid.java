package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;
import language.params.OnOff;

/**
 *
 * @author emil
 */
public class Fluid extends Command {
    
    private final OnOff mode;

    public Fluid(OnOff mode) {
        this.mode = mode;
    }

    public OnOff getMode() {
        return mode;
    }

    @Override
    public String getName() {
        return "fluid";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, mode);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + mode.toString().toLowerCase();
    }

}
