package logic.commands;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;

public class CommandTaskRun extends Command {
    /**
     * Run the tasklist and prepare to check if objectives are met.
     * @param farmio the game which stage change will be changed.
     * @throws FarmioFatalException if simulation file cannot be found.
     */
    @Override
    public void execute(Farmio farmio) throws FarmioFatalException {
        farmio.getFarmer().startDay(farmio);
        farmio.getUi().sleep(300);
        farmio.getSimulation().simulate("DayStart", 1);
        farmio.setStage(Farmio.Stage.CHECK_OBJECTIVES);
    }
}