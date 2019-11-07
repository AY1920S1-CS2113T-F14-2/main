package logic.usercode.actions;

import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import gameassets.Farmer;
import storage.Storage;
import frontend.Simulation;
import frontend.Ui;
import javafx.util.Pair;

import java.util.ArrayList;

public class SellGrainAction extends Action {

    public SellGrainAction() {
        super(ActionType.sellGrain);
    }

    @Override
    public void execute(Ui ui, Storage storage, Farmer farmer, Simulation simulation)
            throws FarmioFatalException, FarmioException {
        ArrayList<Pair<Boolean, String>> criteriaFeedbackList = new ArrayList<>();
        criteriaFeedbackList.add(new Pair<>(!farmer.getWheatFarm().hasGrain(),
                "Error! you have attempted to sell grain despite not having any grain"));
        criteriaFeedbackList.add(new Pair<>(!farmer.getLocation().equals("Market"),
                "Error! you have attempted to sell grain despite not being at the market"));
        checkActionCriteria(ui, farmer, simulation, criteriaFeedbackList);
        simulation.simulate("SellWheatSimulation", 0, 9);
        ui.typeWriter("Selling grain!", false);
        ui.sleep(700);
        farmer.earnGold(farmer.getWheatFarm().sell());
        simulation.simulate();
        ui.sleep(700);
    }
}