package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;

/**
 *
 * @author emil
 */
public class Stop extends Command {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public List<Object> getParams() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return getName();
    }

}
