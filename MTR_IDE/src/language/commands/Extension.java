package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;
import language.params.ExtensionMode;
import language.params.ExtensionOption;

/**
 *
 * @author emil
 */
public class Extension extends Command {
    
    private final ExtensionMode mode;
    private final ExtensionOption option;

    public Extension(ExtensionOption option, ExtensionMode mode) {
        this.mode = mode;
        this.option = option;
    }
    
    public ExtensionMode getMode() {
        return mode;
    }
    
    public ExtensionOption getOption() {
        return option;
    }

    @Override
    public String getName() {
        return "ext";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, option);
        params.add(1, mode);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + option.toString().toLowerCase() + " " + mode.toString().toLowerCase();
    }

}
