import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import gameassets.Level;
import frontend.UiDummy;
import org.junit.jupiter.api.Test;
import logic.usercode.actions.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
    private final ArrayList<String> LIST_OF_VALID_ACTIONS =
            new ArrayList<>(Arrays.asList("gotomarket", "gotowheatfarm", "buyseeds", "plantseeds",
                    "harvestwheat", "sellgrain"));
    private Action gotoMarketAction;
    private Action buySeedsAction;
    private Action gotoWheatFarmAction;
    private Action plantSeedsAction;
    private Action harvestWheatAction;
    private Action sellGrainAction;
    private Farmio farmio;
    public ActionTest() throws FarmioFatalException {
        farmio = new Farmio();
        farmio.setUi(new UiDummy());
        farmio.setLevel(new Level(farmio.getStorage().getLevel(1.1), "tester"));
        gotoMarketAction = new GotoMarketAction();
        buySeedsAction = new BuySeedsAction();
        gotoWheatFarmAction = new GotoFarmAction();
        plantSeedsAction = new PlantSeedsAction();
        harvestWheatAction = new HarvestWheatAction();
        sellGrainAction = new SellGrainAction();
    }
    @Test
    void isValidActionPositiveTest() {
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            assert Action.isValidAction(validAction);
        }
    }

    @Test
    void isValidActionNegativeTest() {
        assert !Action.isValidAction("");
        assert !Action.isValidAction("go to market");
        assert !Action.isValidAction("goto wheatfarm");
        assert !Action.isValidAction("buyseed");
        assert !Action.isValidAction(" plantseed");
        assert !Action.isValidAction("PLANTSEED");
    }

    @Test
    void toActionPositiveTest() throws FarmioException {
        assert Action.toAction("gotomarket") instanceof GotoMarketAction;
        assert Action.toAction("gotowheatfarm") instanceof GotoFarmAction;
        assert Action.toAction("buyseeds") instanceof BuySeedsAction;
        assert Action.toAction("plantseeds") instanceof PlantSeedsAction;
        assert Action.toAction("harvestwheat") instanceof HarvestWheatAction;
        assert Action.toAction("sellgrain") instanceof SellGrainAction;
    }


    @Test
    void toActionNegativeTest() throws FarmioException {
        assertFalse(Action.toAction("") instanceof GotoMarketAction);
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            if (!validAction.equals("gotomarket")) {
                assertFalse(Action.toAction(validAction) instanceof GotoMarketAction);
            }
        }
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            if (!validAction.equals("gotowheatfarm")) {
                assertFalse(Action.toAction(validAction) instanceof GotoFarmAction);
            }
        }
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            if (!validAction.equals("buyseeds")) {
                assertFalse(Action.toAction(validAction) instanceof BuySeedsAction);
            }
        }
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            if (!validAction.equals("sellgrain")) {
                assertFalse(Action.toAction(validAction) instanceof SellGrainAction);
            }
        }
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            if (!validAction.equals("plantseeds")) {
                assertFalse(Action.toAction(validAction) instanceof PlantSeedsAction);
            }
        }
        for (String validAction: LIST_OF_VALID_ACTIONS) {
            if (!validAction.equals("harvestwheat")) {
                assertFalse(Action.toAction(validAction) instanceof HarvestWheatAction);
            }
        }
    }

    @Test
    void actionSequenceOfGameTest() throws FarmioException, FarmioFatalException {
        gotoMarketAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        buySeedsAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        gotoWheatFarmAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        plantSeedsAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        farmio.getFarmer().nextDay();
        harvestWheatAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        gotoMarketAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
        sellGrainAction.execute(farmio.getUi(), farmio.getStorage(), farmio.getFarmer(), farmio.getSimulation());
    }

    @Test
    void actionToStringTest() {
        assert gotoMarketAction.toString().equals("gotoMarket");
        assert gotoWheatFarmAction.toString().equals("gotoWheatFarm");
        assert buySeedsAction.toString().equals("buySeeds");
        assert plantSeedsAction.toString().equals("plantSeeds");
        assert harvestWheatAction.toString().equals("harvestWheat");
        assert sellGrainAction.toString().equals("sellGrain");
    }
}
