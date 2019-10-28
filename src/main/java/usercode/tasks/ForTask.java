package usercode.tasks;

import exceptions.FarmioException;
import exceptions.FarmioFatalException;
import farmio.Farmio;
import usercode.actions.Action;
import usercode.conditions.Condition;

public class ForTask extends Task {

    /**
     * Creates a task of type for
     * @param condition the condition as a value
     * @param action the action to be carried out that number of times
     */
    public ForTask(Condition condition, Action action) {
        super(taskType.FOR, condition, action);
    }

    @Override
    public void execute(Farmio farmio) throws FarmioFatalException, FarmioException { //if got error during the loop we use FarmioException to break out
        {
            int repeatNumber = 0;
            for (int i = 0; i < repeatNumber; i ++) {
                action.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
            }
        }
    }

    @Override
    public String toString() {
        return "for " + super.toString();
    }
}