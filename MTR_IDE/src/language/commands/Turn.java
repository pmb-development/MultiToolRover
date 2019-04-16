package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;
import language.params.LeftRight;

/**
 *
 * @author emil
 */
public class Turn extends Command {
    
    private final LeftRight direction;
    private final int degrees;

    public Turn(LeftRight direction, int degrees) {
        this.direction = direction;
        this.degrees = degrees;
    }

    @Override
    public String getName() {
        return "turn";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, direction);
        params.add(1, degrees);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + direction.toString().toLowerCase() + " " + degrees;
    }

}
