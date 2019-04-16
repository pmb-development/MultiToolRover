package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;
import language.params.Lights;
import language.params.OnOff;

/**
 *
 * @author emil
 */
public class Light extends Command {
    
    private final Lights light;
    private final OnOff activate;

    public Light(Lights light, OnOff activate) {
        this.light = light;
        this.activate = activate;
    }

    public Lights getLight() {
        return light;
    }

    public OnOff getActivate() {
        return activate;
    }

    @Override
    public String getName() {
        return "light";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, light);
        params.add(1, activate);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + light.toString().toLowerCase() + " " + activate.toString().toLowerCase();
    }

}
