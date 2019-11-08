package logic.commands;

import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import logic.usercode.tasks.Task;

public class CommandTaskInsert extends CommandChangeTask {
    private Task task;
    private int taskID;

    public CommandTaskInsert(int taskID, Task task) {
        this.taskID = taskID;
        this.task = task;
    }

    /**
     * Inserts a Task at the specified position.
     *
     * @param farmio the game which contains the TaskList.
     * @throws FarmioException if the TaskID is invalid.
     * @throws FarmioFatalException if the Simlation file cannot be found.
     */
    @Override
    public void execute(Farmio farmio) throws FarmioException, FarmioFatalException {
        farmio.getFarmer().insertTask(taskID, task);
        super.saveTaskandResetScreen(farmio);
        farmio.getUi().showInfo("You have added a new task: " + task.toString() + " at position " + taskID);
    }
}
