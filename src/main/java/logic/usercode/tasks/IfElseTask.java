package logic.usercode.tasks;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import farmio.exceptions.FarmioException;
import logic.usercode.actions.Action;
import logic.usercode.conditions.Condition;
import org.json.simple.JSONObject;

public class IfElseTask extends Task {

    public static final String JSON_KEY_ACTION_ELSE = "action_else";

    private Action ifAction;
    private Action elseAction;

    /**
     * Creates an if else task.
     * @param condition The condition to be considered.
     * @param ifAction The action to be executed if the condition is true.
     * @param elseAction The action to be executed if the condition is false.
     */
    public IfElseTask(Condition condition, Action ifAction, Action elseAction) {
        super(Tasktype.IF_ELSE, condition, ifAction);
        this.ifAction = ifAction;
        this.elseAction = elseAction;
    }

    @Override
    public void execute(Farmio farmio) throws FarmioException, FarmioFatalException {
        {
            if (checkCondition(farmio)) {
                ifAction.execute(farmio.getFrontend(), farmio.getStorage(), farmio.getFarmer());
            } else {
                elseAction.execute(farmio.getFrontend(), farmio.getStorage(), farmio.getFarmer());
            }
        }
    }

    @Override
    public String toString() {
        return "if " + condition.toString() + " " + ifAction.toString() + " else " + elseAction.toString();
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = super.toJson();
        object.put(JSON_KEY_ACTION_ELSE, elseAction.toString());
        return object;
    }
}