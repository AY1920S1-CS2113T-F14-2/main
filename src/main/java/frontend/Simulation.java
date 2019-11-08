package frontend;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import gameassets.Farmer;
import gameassets.Level;
import storage.Storage;

public class Simulation {
    private static final int SLEEP_TIME = 300;
    private Farmio farmio;
    private Storage storage;
    private Ui ui;
    private Farmer farmer;
    private static String lastPath;
    private static int lastFrameId;
    private static boolean hadFullscreen;

    /**
     * Creates a Simulation for the game, farmio.
     * @param farmio the game that is being simulated.
     */
    public Simulation(Farmio farmio) {
        this.farmio = farmio;
        storage = farmio.getStorage();
        ui = farmio.getUi();
        farmer = farmio.getFarmer();
        lastPath = "Welcome";
        lastFrameId = 1;
        hadFullscreen = true;
    }

    /**
     * Shows the ascii art of a file as a simulation.
     * @param framePath The directory of where the file is found.
     * @param frameId The frame number to be shown.
     * @param isFullscreen if the ascii image is to be shown as the full console or within the frame section.
     * @throws FarmioFatalException if the file cannot be found.
     */
    public void simulate(String framePath, int frameId, boolean isFullscreen) throws FarmioFatalException {
        lastPath = framePath;
        lastFrameId = frameId;
        hadFullscreen = isFullscreen;
        refresh();
        if (isFullscreen) {
            ui.show(GameConsole.blankConsole(storage.loadFrame(framePath, frameId, GameConsole.FULL_CONSOLE_WIDTH,
                    GameConsole.FULL_CONSOLE_HEIGHT)));
        } else {
            ui.show(GameConsole.fullconsole(storage.loadFrame(framePath, frameId, GameConsole.FRAME_SECTION_WIDTH,
                    GameConsole.FRAME_SECTION_HEIGHT), farmer, farmio.getLevel().getGoals(),
                    farmio.getLevel().getObjective()));
        }
    }

    /**
     * Simulates from startFrame to endFrame like an animation.
     * @param framePath The directory of where the files are found.
     * @param startFrame The starting frame number.
     * @param endFrame The ending frame number.
     * @param isFullscreen if the ascii image is to be shown as the full console or within the frame section.
     * @throws FarmioFatalException if any file cannot be found.
     */
    public void simulate(String framePath, int startFrame, int endFrame, boolean isFullscreen)
            throws FarmioFatalException {
        if (startFrame <= endFrame) {
            for (int i = startFrame; i <= endFrame; i++) {
                simulate(framePath, i, isFullscreen);
            }
        } else {
            for (int i = startFrame; i >= endFrame; i--) {
                simulate(framePath, i, isFullscreen);
            }
        }
    }

    /**
     * Simulates a file without full screen, within the frame section.
     * @param framePath the directory of where the file is found.
     * @param frameId the frame numer to be shown.
     * @throws FarmioFatalException if the file cannot be found.
     */
    public void simulate(String framePath, int frameId) throws FarmioFatalException {
        simulate(framePath, frameId, false);
    }

    /**
     * Simulates the last file simulated.
     * @throws FarmioFatalException if the file cannot be found.
     */
    public void simulate() throws FarmioFatalException {
        simulate(lastPath, lastFrameId, hadFullscreen);
    }

    /**
     * Simulates a file without full screen from start frame to end frame.
     * @param framePath the directory of where the files are found.
     * @param startFrame the starting frame number.
     * @param endFrame the ending frame number.
     * @throws FarmioFatalException if any file cannot be found.
     */
    public void simulate(String framePath, int startFrame, int endFrame) throws FarmioFatalException {
        simulate(framePath, startFrame, endFrame, false);
    }

    /**
     * Updates storage, ui and farmer, clears the screen and gives a short delay.
     */
    private void refresh() {
        storage = farmio.getStorage();
        ui = farmio.getUi();
        farmer = farmio.getFarmer();
        ui.sleep(SLEEP_TIME);
        ui.clearScreen();
    }

    /**
     * Prints the Narrative of a given level with a simulation instance.
     * @throws FarmioFatalException if simulation file is not found
     */
    public void showNarrative() throws FarmioFatalException {
        ui = farmio.getUi();
        storage = farmio.getStorage();
        farmer = farmio.getFarmer();
        Level level = farmio.getLevel();
        int frameId = 0;
        int lastFrameId = level.getNarratives().size() - 1;
        for (String narrative: level.getNarratives()) {
            String userInput;
            userInput = ui.getInput();
            while (!userInput.equals("") && !userInput.toLowerCase().equals("skip")) {
                simulate();
                ui.showWarning("Invalid Command for story mode!");
                ui.show("Story segment only accepts [skip] to skip the story or pressing [ENTER] to continue with the "
                        + "narrative.\nIf you wish to use other logic.commands, enter [skip] followed by entering the "
                        + "command of your choice.");
                userInput = ui.getInput();
            }
            if (userInput.toLowerCase().equals("skip") || frameId == lastFrameId) {
                break;
            }
            simulate(level.getPath(), frameId++);
            ui.typeWriter(narrative, true);
        }
        simulate(level.getPath(), lastFrameId);
        ui.typeWriter(level.getNarratives().get(lastFrameId), false);
        ui.showLevelBegin();
    }
}