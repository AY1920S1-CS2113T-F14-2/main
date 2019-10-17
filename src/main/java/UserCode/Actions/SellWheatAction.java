package UserCode.Actions;

import Farmio.Farmio;
import FrontEnd.Simulation;
import FrontEnd.Ui;

public class SellWheatAction extends Action {

    public SellWheatAction(Farmio farmio) {
        super(farmio);
        this.type = ActionType.sellWheat;
    }

    @Override
    public void execute(Ui ui) {
        farmer.getWheatFarm().buySeeds(); //TODO create wheatFarm.sellWheat()
        new Simulation("SellWheat", super.farmio).animate(0, 7);
        ui.show("Selling wheat!");
    }

//    public JSONObject toJSON() {
//        JSONObject obj = super.toJSON();
//        obj.put("action", "selling_wheat");
//        return obj;
//    }
}
