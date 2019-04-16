package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;

/**
 *
 * @author emil
 */
public class Move extends Command {
    
    private final double length;
    private final double direction;

    public Move(double length, double direction) {
        this.length = length;
        this.direction = direction;
    }

    public double getLength() {
        return length;
    }

    public double getDirection() {
        return direction;
    }

    @Override
    public String getName() {
        return "move";
    }
    
    // move length direction
    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, length);
        params.add(1, direction);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + length + " " + direction;
    }
    
    
    
}
