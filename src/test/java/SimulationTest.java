import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import gameassets.Level;
import frontend.UiDummy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulationTest {
    Farmio farmio;

    public SimulationTest() throws FarmioFatalException{
        farmio = new Farmio();
        farmio.setUi(new UiDummy());
        farmio.setLevel(new Level(farmio.getStorage().getLevel(1.1), "tester"));
    }
    @Test
    void singleFullScreenSimulationTest() throws FarmioFatalException {
        farmio.getSimulation().simulate();
        assertEquals("sleepclearshow", UiDummy.uiTestString);
    }
    @Test
    void multiFullScreenSimulationTest() throws FarmioFatalException {
        UiDummy.uiTestString = "";
        farmio.getSimulation().simulate("Test", 0, 3, true);
        assertEquals("sleepclearshow".repeat(4), UiDummy.uiTestString);
    }
    @Test
    void singleSimulationTest() throws FarmioFatalException {
        UiDummy.uiTestString = "";
        farmio.getSimulation().simulate("Test",0);
        assertEquals("sleepclearshow", UiDummy.uiTestString);
    }
    @Test
    void multiFrameSimulationTest() throws FarmioFatalException {
        UiDummy.uiTestString = "";
        farmio.getSimulation().simulate("Test", 0, 3, true);
        assertEquals("sleepclearshow".repeat(4), UiDummy.uiTestString);
    }
    @Test
    void multiFrameReverseSimulationTest() throws FarmioFatalException {
        UiDummy.uiTestString = "";
        farmio.getSimulation().simulate("Test", 3, 0, true);
        assertEquals("sleepclearshow".repeat(4), UiDummy.uiTestString);
    }

    @Test
    void narrativeSimulationContinueTest() throws FarmioFatalException {
        UiDummy.input = "";
        double levelId = 1.1;
        while (levelId != 0) {
            UiDummy.uiTestString = "";
            farmio.setLevel(new Level(farmio.getStorage().getLevel(levelId), "tester"));
            Level level = farmio.getLevel();
            farmio.getSimulation().showNarrative();
            assert ("inputsleepclearshowtypewriter".repeat(level.getNarratives().size())
                    + "levelBegin").equals(UiDummy.uiTestString);
            levelId = farmio.getFarmer().nextLevel();
        }
    }

    @Test
    void narrativeSimulationSkipTest() throws FarmioFatalException {
        UiDummy.input = "skip";
        double levelId = 1.1;
        while (levelId != 0) {
            UiDummy.uiTestString = "";
            farmio.setLevel(new Level(farmio.getStorage().getLevel(levelId), "tester"));
            farmio.getSimulation().showNarrative();
            assert ("inputsleepclearshowtypewriterlevelBegin").equals(UiDummy.uiTestString);
            levelId = farmio.getFarmer().nextLevel();
        }
    }
}
