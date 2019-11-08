package logic.commands;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import frontend.AsciiColours;
import frontend.Frontend;
import gameassets.Farmer;

public class CommandDayStart extends Command {
    /**
     * Shows and sets the start of a new day.
     * @param farmio The game where its stage is set to RUNNING_DAY.
     * @throws FarmioFatalException if simulation file is missing.
     */
    @Override
    public void execute(Farmio farmio) throws FarmioFatalException {
        //todo clearLog Details
        Farmer.getLogTaskList().deleteAll();//deletes everything in the task list

        Frontend frontend = farmio.getFrontend();
        farmio.getFrontend().simulate("DayStart", 1, 5);
        frontend.show(AsciiColours.MAGENTA + AsciiColours.UNDERLINE + "Day Started!" + AsciiColours.SANE);
        frontend.sleep(300);
        farmio.setStage(Farmio.Stage.RUNNING_DAY);
    }
}