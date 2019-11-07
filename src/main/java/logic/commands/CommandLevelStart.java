package logic.commands;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import storage.Storage;
import gameassets.Farmer;
import gameassets.Level;

public class CommandLevelStart extends Command {
    /**
     * Starts the level.
     * @param farmio the game where level is extracted from for narratives.
     * @throws FarmioFatalException if simulation file cannot be found.
     */
    @Override
    public void execute(Farmio farmio) throws FarmioFatalException {
        Storage storage = farmio.getStorage();
        Farmer farmer = farmio.getFarmer();
        storage.storeFarmer(farmer);
        Level level = new Level(storage.getLevel(farmer.getLevel()),farmer.getName());
        farmio.setLevel(level);
        farmio.getSimulation().showNarrative();
        farmio.setStage(Farmio.Stage.TASK_ADD);
    }
}
