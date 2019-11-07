import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import gameassets.Level;
import frontend.UiDummy;
import org.junit.jupiter.api.Test;

public class GameConsoleTest {
    private Farmio farmio;
    private final String frame0Test = "[0m.____________________________________________________________________"
            + "___________________________________.|   [34m[1mLevel: 1.1[0m  |[31m[1mObjective: [0m[1mTravel to the"
            + " Market[0m                        |[35m[1mDay: 1  [0m | [32m[1mLocation: WheatFarm [0m||____________"
            + "___|_______________________________________________________|_________|_____________________||----[31m"
            + "[1m<GOALS>[0m----|[1m[30m                Farmer name's Adventure                [0m|------------[36m"
            + "[1m<CODE>[0m-------------||[1mLocation:Market[0m|[47m[30mvalidation for GameConsole Test,            "
            + "           [0m|                               ||               |[47m[30mdo not change this file to "
            + "anything!                   [0m|                               ||               |[47m[30m7/11/2019"
            + " 3:19                                         [0m|                               ||               "
            + "|[47m[30m                                                       [0m|                               "
            + "||               |[47m[30m                                                       [0m|               "
            + "                ||               |[47m[30m                                                       "
            + "[0m|                               ||               |[47m[30m                                     "
            + "                  [0m|                               ||---[33m[1m<ASSETS>[0m----|[47m[30m          "
            + "                                             [0m|                               ||Gold: 10       "
            + "|[47m[30m                                                       [0m|                             "
            + "  ||               |[47m[30m                                                       [0m|            "
            + "                   ||               |[47m[30m                                                       "
            + "[0m|                               ||               |[47m[30m                                        "
            + "               [0m|                               ||               |[47m[30m                         "
            + "                              [0m|                               ||               |[47m[30m         "
            + "                                              [0m|                               ||               "
            + "|[47m[30m                                                       [0m|                               "
            + "||               |[47m[30m                                                       [0m|             "
            + "                  ||               |[47m[30m                                                       "
            + "[0m|                               ||               |[47m[30m                                     "
            + "                  [0m|                               ||_______________|____________________________"
            + "___________________________|_______________________________||[44m[1m[4m [exit] to exit      [menu] "
            + "for full instruction list or settings          [hint] for hint on <CODE>   [0m|[0m";
    GameConsoleTest() throws FarmioFatalException {
        farmio = new Farmio();
        farmio.setUi(new UiDummy());
        farmio.setLevel(new Level(farmio.getStorage().getLevel(1.1), "tester"));
    }

    @Test
    void formatTest() throws FarmioFatalException {
        UiDummy.output = "";
        farmio.getSimulation().simulate("Test", 0);
        String test = UiDummy.output.replaceAll("\\p{C}", "");
        assert test.equals(frame0Test);
    }
}
