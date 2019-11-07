package logic.usercode.tasks;

import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import logic.usercode.actions.Action;
import logic.usercode.conditions.Condition;

public class DoTask extends Task {

    /**
     * Creates a Task of type Do.
     * @param condition The condition to be considered
     * @param action The action to be done
     */
    public DoTask(Condition condition, Action action) {
        super(Tasktype.DO, condition, action);
    }

    @Override
    public void execute(Farmio farmio) throws FarmioFatalException, FarmioException {
        {
            action.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        }
    }

    @Override
    public String toString() {
        return "do " + action.toString();
    }
}