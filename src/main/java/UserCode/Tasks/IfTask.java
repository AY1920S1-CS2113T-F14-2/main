package UserCode.Tasks;

import Exceptions.FarmioFatalException;
import Farmio.Farmio;
import Exceptions.FarmioException;
import UserCode.Actions.Action;
import UserCode.Conditions.Condition;
import org.json.simple.JSONObject;

public class IfTask extends Task {

    public IfTask(Condition condition, Action action) {
        super(Type.IF, condition, action);
    }

    @Override
    public void execute(Farmio farmio) throws FarmioException, FarmioFatalException {
        {
            if (condition.check(farmio)) {
                action.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
            } else {
                farmio.getUi().show("Condition not fulfilled, not executing task!");
                farmio.getUi().sleep(1000);
            }
        }
    }

    @Override
    public String toString() {
        return "if " + super.toString();
    }
}