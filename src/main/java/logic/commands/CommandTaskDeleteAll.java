package logic.commands;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;

public class CommandTaskDeleteAll extends CommandChangeTask {

    /**
     * Delete all tasks in the tasklist.
     * @param farmio the game that contains the tasklist to be cleared.
     * @throws FarmioFatalException if the simulation file cannot be found.
     */
    @Override
    public void execute(Farmio farmio) throws FarmioFatalException {
        farmio.getFarmer().getTasks().deleteAll();
        super.saveTaskandResetScreen(farmio);
        farmio.getFrontend().showInfo("You have deleted all tasks!");
    }
}
