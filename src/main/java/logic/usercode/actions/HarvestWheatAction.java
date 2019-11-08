package logic.usercode.actions;

import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import gameassets.Farmer;
import storage.Storage;
import frontend.Simulation;
import frontend.Ui;
import javafx.util.Pair;

import java.util.ArrayList;

public class HarvestWheatAction extends Action {

    public HarvestWheatAction() {
        super(ActionType.harvestWheat);
    }

    @Override
    public void execute(Ui ui, Storage storage, Farmer farmer, Simulation simulation)
            throws FarmioFatalException, FarmioException {
        ArrayList<Pair<Boolean, String>> criteriaFeedbackList = new ArrayList<>();
        criteriaFeedbackList.add(new Pair<>(!farmer.hasWheat(),
                "Error! you have attempted to harvest wheat despite not having any wheat"));
        criteriaFeedbackList.add(new Pair<>(!farmer.getLocation().equals("WheatFarm"),
                "Error! you have attempted to harvest wheat despite not being at the Wheatfarm"));
        checkActionCriteria(ui, farmer, simulation, criteriaFeedbackList);
        simulation.simulate("HarvestWheatSimulation", 0, 9);
        farmer.harvestWheat();
        simulation.simulate();
        ui.sleep(700);
    }
}