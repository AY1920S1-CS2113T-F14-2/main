package logic.usercode.tasks;

import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import logic.usercode.actions.Action;
import logic.usercode.conditions.Condition;

public class ForTask extends Task {

    /**
     * Creates a task of type for.
     * @param condition the condition as a value.
     * @param action the action to be carried out that number of times.
     */
    public ForTask(Condition condition, Action action) {
        super(Tasktype.FOR, condition, action);
    }

    @Override
    public void execute(Farmio farmio) throws FarmioFatalException, FarmioException {
        {
            int repeatNumber = 0;
            for (int i = 0; i < repeatNumber; i++) {
                action.execute(farmio.getFrontend(), farmio.getStorage(), farmio.getFarmer());
            }
        }
    }

    @Override
    public String toString() {
        return "for " + super.toString();
    }
}