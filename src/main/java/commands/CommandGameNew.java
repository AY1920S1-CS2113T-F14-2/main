package commands;

import exceptions.FarmioFatalException;
import farmio.Farmer;
import farmio.Farmio;
import farmio.Storage;
import frontend.Ui;

public class CommandGameNew extends Command {
    /**
     * Creates a new game
     * @param farmio The game to be reinitialised as a new game
     * @throws FarmioFatalException if simulation file is not found
     */
    @Override
    public void execute(Farmio farmio) throws FarmioFatalException {
        Ui ui = farmio.getUi();
        Storage storage = farmio.getStorage();
        farmio.setFarmer(new Farmer());
        farmio.getSimulation().simulate("GameNew", 0 , true);
        ui.typeWriter("New Game Created!", false);
        ui.typeWriter("Enter your name:", false);
        farmio.setStage(Farmio.Stage.NAME_ADD);
    }
}