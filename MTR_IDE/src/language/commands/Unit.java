package language.commands;

import java.util.ArrayList;
import java.util.List;
import language.Command;
import language.params.LengthUnit;
import language.params.VeloUnit;

/**
 *
 * @author emil
 */
public class Unit extends Command {
    
    private final LengthUnit lengthUnit;
    private final VeloUnit veloUnit;

    public Unit(LengthUnit lengthUnit, VeloUnit veloUnit) {
        this.lengthUnit = lengthUnit;
        this.veloUnit = veloUnit;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public VeloUnit getVeloUnit() {
        return veloUnit;
    }

    @Override
    public String getName() {
        return "unit";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(0, lengthUnit);
        params.add(1, veloUnit);
        return params;
    }

    @Override
    public String toString() {
        return getName() + " " + lengthUnit.toString().toLowerCase() + " " + veloUnit.toString().toLowerCase();
    }

}
