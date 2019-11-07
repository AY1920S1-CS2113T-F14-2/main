package logic.usercode.tasks;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import farmio.exceptions.FarmioException;
import logic.usercode.actions.Action;
import logic.usercode.conditions.Condition;

public class WhileTask extends Task {

    /**
     * Creates a Task of type while.
     * @param condition The condition to be considerd.
     * @param action The action to be executed while the condition is true.
     */
    public WhileTask(Condition condition, Action action) {
        super(Tasktype.WHILE, condition, action);
    }

    @Override
    public void execute(Farmio farmio) throws FarmioException, FarmioFatalException {
        while (checkCondition(farmio)) {
            action.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        }
    }
}