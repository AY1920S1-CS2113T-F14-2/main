package logic.usercode.actions;

import farmio.exceptions.FarmioFatalException;
import frontend.Frontend;
import gameassets.Farmer;
import storage.Storage;

public class GotoMarketAction extends Action {

    public GotoMarketAction() {
        super(ActionType.gotoMarket);
    }

    @Override
    public void execute(Frontend frontend, Storage storage, Farmer farmer) throws FarmioFatalException {
        if (farmer.getLocation().equals("Market")) {
            frontend.simulate("GotoMarketSimulation", 1);
            frontend.typeWriter("You are already at the market", false);
        } else {
            farmer.changeLocation("Traveling");
            frontend.simulate("GotoMarketSimulation", 12, 1);
            farmer.changeLocation("Market");
            frontend.typeWriter("You have arrived at the market", false);
        }
        frontend.sleep(200);
    }
}

