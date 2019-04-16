package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;
import language.params.OnOff;

/**
 *
 * @author emil
 */
public class Camera extends Command {
    
    private final OnOff activate;
    private final boolean video;

    public Camera(OnOff activate, boolean video) {
        this.activate = activate;
        this.video = video;
    }

    public OnOff getActivate() {
        return activate;
    }

    public boolean isVideo() {
        return video;
    }

    @Override
    public String getName() {
        return "cam";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, activate);
        params.add(1, video);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + activate.toString().toLowerCase() + " " + video;
    }

}
