package frontend;

public class UiDummy implements Ui {
    public static String uiTestString;
    public static String input;
    public static String output;

    /**
     * creates a Ui dummy for testing.
     */
    public UiDummy() {
        uiTestString = "";
        input = "";
        output = "";
    }

    public void removeClearScreen() {
    }

    public void show(String message) {
        uiTestString += "show";
        output += message;
    }

    public void showExit() {
        uiTestString += "exit";
    }

    public void showError(String message) {
        uiTestString += "error";
    }

    public void showWarning(String message) {
        uiTestString += "warning";
    }

    public void clearScreen() {
        uiTestString += "clear";
    }

    public void showInfo(String message) {
        uiTestString += "info";
    }

    public String getInput() {
        uiTestString += "input";
        return input;
    }

    public void sleep(int delay) {
        uiTestString += "sleep";
    }

    public void showHint(String text) {
        uiTestString += "hint";
    }

    public void typeWriter(String text, boolean hasPressEnter) {
        uiTestString += "typewriter";
    }

    public void showLevelBegin() {
        uiTestString += "levelBegin";
    }
}
