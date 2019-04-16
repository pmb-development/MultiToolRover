package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;

/**
 *
 * @author emil
 */
public class Velocity extends Command {
    
    private final double velocity;

    public Velocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    @Override
    public String getName() {
        return "velo";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, velocity);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + velocity;
    }

}
