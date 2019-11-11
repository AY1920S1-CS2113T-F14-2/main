package logic;

;


import farmio.Farmio;
import logic.commands.Command;
import logic.commands.CommandAddName;
import logic.commands.CommandCheckObjectives;
import logic.commands.CommandDayEnd;
import logic.commands.CommandDayStart;
import logic.commands.CommandGameLoad;
import logic.commands.CommandGameNew;
import logic.commands.CommandGameQuit;
import logic.commands.CommandGameSave;
import logic.commands.CommandLevelEnd;
import logic.commands.CommandLevelReset;
import logic.commands.CommandLevelStart;
import logic.commands.CommandLog;
import logic.commands.CommandMenuInGame;
import logic.commands.CommandMenuStart;
import logic.commands.CommandSetFastMode;
import logic.commands.CommandShowList;
import logic.commands.CommandTaskAddReset;
import logic.commands.CommandTaskCreate;
import logic.commands.CommandTaskDelete;
import logic.commands.CommandTaskDeleteAll;
import logic.commands.CommandTaskEdit;
import logic.commands.CommandTaskHint;
import logic.commands.CommandTaskInsert;
import logic.commands.CommandTaskRun;
import logic.usercode.tasks.Task;
import logic.usercode.tasks.IfTask;
import logic.usercode.tasks.DoTask;
import farmio.exceptions.FarmioException;
import logic.usercode.actions.Action;
import logic.usercode.conditions.Condition;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser class is responsible for parsing all user input and generating the corresponding Command.
 */

public class Parser {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Returns a Command depending on the current Stage of the game, and the user's input.
     *
     * @param userInput input String either from user or Farmio depending on the game stage
     * @param stage enum that represents the current game stage
     * @return a Command that can be executed, based on the current stage and user input
     * @throws FarmioException if an unknown game stage is passed
     */
    public static Command parse(String userInput, Farmio.Stage stage) throws FarmioException {
        userInput = userInput.toLowerCase().trim();
        if (!userInput.equals("")) {
            LOGGER.log(Level.INFO, userInput + " command entered");
        }
        if (userInput.equals("quit game") || userInput.equals("exit")) {
            return new CommandGameQuit();
        }
        if (stage != Farmio.Stage.WELCOME && stage != Farmio.Stage.MENU_START && userInput.equals("save game")) {
            return new CommandGameSave();
        }
        if (userInput.equals("load game")) {
            return new CommandGameLoad();
        }
        if (userInput.equals("new game")) {
            return new CommandGameNew();
        }
        if (userInput.equals("fastmode")) {
            return new CommandSetFastMode();
        }
        switch (stage) {
        case WELCOME:
            return parseWelcome(userInput);
        case LEVEL_START:
            return new CommandLevelStart();
        case DAY_RUNNING:
            return new CommandTaskRun();
        case CHECK_OBJECTIVES:
            return new CommandCheckObjectives();
        case DAY_START:
            return new CommandDayStart();
        case LEVEL_END:
            return new CommandLevelEnd();
        case LEVEL_FAILED:
            return new CommandLevelReset();
        case DAY_END:
            return parseDayEnd(userInput);
        case NAME_ADD:
            return new CommandAddName(userInput);
        case TASK_ADD:
            return parseTaskAdd(userInput);
        default:
            LOGGER.log(Level.INFO, "Detected invalid command at stage: "
                    + stage.toString() + " command: " + userInput);
            throw new FarmioException("Invalid Command!");
        }
    }

    /**
     * Checks user input at the welcome screen.
     *
     * @param userInput input String from user
     * @return Command for the start game menu
     * @throws FarmioException if the user input is invalid
     */
    private static Command parseWelcome(String userInput) throws FarmioException {
        if (userInput.equals("")) {
            return new CommandMenuStart();
        }
        LOGGER.log(Level.INFO, "Detected invalid command " + userInput + " at Welcome");
        throw new FarmioException("Invalid Command!");
    }

    /**
     * Used to parse the user input during the DAY_END stage. User can choose to either reset the level,
     * or proceed to the next day.
     *
     * @param userInput user input String
     * @return Command that either resets the level, or lets the user proceed to the next day
     * @throws FarmioException if user input is invalid
     */
    private static Command parseDayEnd(String userInput) throws FarmioException {
        if (userInput.length() == 0) {
            return new CommandDayEnd();
        }
        if (userInput.equals("reset")) {
            return new CommandLevelReset();
        }
        LOGGER.log(Level.SEVERE, "Detected invalid command for command: " + userInput);
        throw new FarmioException("Invalid Command!");
    }

    /**
     * Used to parse the user's input during the TASK_ADD stage. Facilitates creating, editing and deleting of tasks,
     * as well as opening in=game menu, or seeing the list of actions or conditions
     *
     * @param userInput user input String
     * @return Command that corresponds to the user's input
     * @throws FarmioException if user input is invalid
     */
    private static Command parseTaskAdd(String userInput) throws FarmioException {
        if (userInput.equals("menu")) {
            return new CommandMenuInGame();
        }
        if (userInput.equals("deleteall") || userInput.equals("delete all")) {
            return new CommandTaskDeleteAll();
        }
        if (userInput.startsWith("delete")) {
            return parseTaskDelete(userInput);
        }
        if (userInput.startsWith("insert")) {
            return parseTaskInsert(userInput);
        }
        if (userInput.startsWith("edit")) {
            return parseTaskEdit(userInput);
        }
        if (userInput.toLowerCase().equals("start")) {
            return new CommandDayStart();
        }
        if (userInput.startsWith("log")) {
            return parseTaskLog(userInput);
        }
        if (userInput.equals("conditions") || userInput.equals("condition")) {
            return new CommandShowList("ConditionList");
        }
        if (userInput.equals("actions") || userInput.equals("action")) {
            return new CommandShowList("ActionList");
        }
        if (userInput.equals("market")) {
            return new CommandShowList("MarketList");
        }
        if (userInput.equals("task commands") || userInput.equals("task command")) {
            return new CommandShowList("TaskCommands");
        }
        if (userInput.startsWith("do") || userInput.startsWith("if")) {
            return new CommandTaskCreate(parseTask(userInput));
        } else if (userInput.equals("hint")) {
            return new CommandTaskHint();
        } else if (userInput.equals("")) {
            return new CommandTaskAddReset();
        }
        LOGGER.log(Level.INFO, "Detected invalid command for command: " + userInput);
        throw new FarmioException("Invalid command!");
    }

    /**
     * Used to parse the user's command if it is determined to be a delete task command.
     *
     * @param userInput user input String
     * @return Command that deletes the specified task when executed
     * @throws FarmioException if user input is invalid
     */
    private static Command parseTaskDelete(String userInput) throws FarmioException {
        Matcher matcher = Pattern.compile("^delete\\s+(?<index>\\d+)$").matcher(userInput);
        if (matcher.find()) {
            int taskID = 0;
            try {
                taskID = Integer.parseInt(matcher.group("index"));
            } catch (NumberFormatException e) {
                throw new FarmioException("Your number is too large!");
            }
            return new CommandTaskDelete(taskID);
        }
        LOGGER.log(Level.INFO, "Detected invalid command for command: " + userInput);
        throw new FarmioException("Invalid argument.");
    }

    /**
     * Used to parse the user's command if it is determined to be a log command.
     * @param userInput user input String
     * @return Command that displays a list of the logs
     * @throws FarmioException if the user input is invalid
     */
    private static Command parseTaskLog(String userInput) throws FarmioException {
        Matcher matcher = Pattern.compile("^log\\s+(?<index>\\d+)$").matcher(userInput);
        if (matcher.find()) {
            int pageNumber = 0;
            try {
                pageNumber = Integer.parseInt(matcher.group("index"));
            } catch (NumberFormatException e) {
                throw new FarmioException("Your number is too big!");
            }
            return new CommandLog(pageNumber);
        }
        LOGGER.log(Level.INFO, "Detected invalid command for command: " + userInput);
        try {
            if (!userInput.substring(0, userInput.indexOf(" ")).equals("log")) {
                throw new FarmioException("Invalid command!");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new FarmioException("Invalid command!");
        }
        throw new FarmioException("Invalid Log Page! Please enter log PAGE_NUMBER");
    }

    /**
     * Determines if the user is creating a DoTask or a ConditionalTask, and calls the corresponding function.
     * to further parse the user input
     *
     * @param userInput user input String
     * @return Task generated from the user's input
     * @throws FarmioException if there is an error in generating a Task from the user's input
     */
    private static Task parseTask(String userInput) throws FarmioException {
        if (userInput.startsWith("do")) {
            return parseDoTask(userInput);
        } else {
            return parseConditionalTask(userInput);
        }
    }

    /**
     * Used to generate a DoTask from the user's input.
     *
     * @param userInput user input String
     * @return Task corresponding to the user input
     * @throws FarmioException if user input is of incorrect format, or the taskType or action is invalid
     */
    private static Task parseDoTask(String userInput) throws FarmioException {
        String taskType = "";
        String userAction = "";
        try {
            taskType = userInput.substring(0, userInput.indexOf(" "));
            userAction = (userInput.substring(userInput.indexOf(" "))).trim();
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.WARNING, e.toString());
            throw new FarmioException("Invalid command format!");
        }
        if (!taskType.equals("do")) {
            LOGGER.log(Level.INFO, "Detected invalid task type for command: " + userInput);
            throw new FarmioException("Invalid task type!");
        }
        if (Action.isValidAction(userAction)) {
            return new DoTask(Condition.toCondition("true"), Action.toAction(userAction));
        } else {
            LOGGER.log(Level.INFO, "Detected invalid action for command: " + userInput);
            throw new FarmioException("Invalid action!");
        }
    }

    /**
     * Creates a Conditional Task from the user input.
     *
     * @param userInput user input String
     * @return Task corresponding to the user input
     * @throws FarmioException if user input is of wrong format, or either the tasktype, action or condition is invalid
     */
    private static Task parseConditionalTask(String userInput) throws FarmioException {
        String taskType = "";
        String condition = "";
        String action = "";
        try {
            taskType = (userInput.substring(0, userInput.indexOf(" "))).trim();
            condition = (userInput.substring(userInput.indexOf(" ") + 1, userInput.indexOf("do"))).trim();
            action = userInput.substring(userInput.lastIndexOf(" ") + 1);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.WARNING, e.toString());
            throw new FarmioException("Invalid command format!");
        }
        if (!taskType.equals("if")) {
            LOGGER.log(Level.INFO, "Detected invalid task type for command: " + userInput);
            throw new FarmioException("Invalid task type!");
        }
        if (!Condition.isValidCondition(condition)) {
            LOGGER.log(Level.INFO, "Detected invalid condition for command: " + userInput);
            throw new FarmioException("Invalid Condition!");
        }
        if (!Action.isValidAction(action)) {
            LOGGER.log(Level.INFO, "Detected invalid action for command: " + userInput);
            throw new FarmioException("Invalid Action!");
        }
        return new IfTask(Condition.toCondition(condition), Action.toAction(action));
    }

    /**
     * Parses logic.commands meant to edit any Task in the TaskList.
     * Edit logic.commands must be of the form 'edit [TaskID] [taskType] [Condition] do [Action].
     *
     * @param userInput user input String
     * @return Command that will edit the Task in the TaskList with the specified ID when executed
     * @throws FarmioException if the user's input is of wrong format or the task description is invalid
     */
    private static Command parseTaskEdit(String userInput) throws FarmioException {
        Matcher matcher = Pattern.compile("^(?<key>edit)\\s+(?<index>-?\\d+)\\s(?<cmd>.+)$").matcher(userInput);
        if (matcher.find()) {
            int taskID = 0;
            try {
                taskID = Integer.parseInt(matcher.group("index"));
            } catch (NumberFormatException e) {
                throw new FarmioException("Your number is too large");
            }
            Task task = parseTask(matcher.group("cmd"));
            return new CommandTaskEdit(taskID, task);
        }
        LOGGER.log(Level.SEVERE, "Detected invalid command for command: " + userInput);
        throw new FarmioException("Invalid Command");
    }

    /**
     * Parses logic.commands meant to insert a Task at a specific position in the TaskList.
     *
     * @param userInput user input String
     * @return Command that inserts a Task at the specified position
     * @throws FarmioException if the user input is of invalid format, or the task description is invalid
     */
    private static Command parseTaskInsert(String userInput) throws FarmioException {
        Matcher matcher = Pattern.compile("^(?<key>insert)\\s+(?<id>-?\\d+)\\s+(?<task>.+)$").matcher(userInput);
        if (matcher.find()) {
            int taskID = 0;
            try {
                taskID = Integer.parseInt(matcher.group("id"));
            } catch (NumberFormatException e) {
                throw new FarmioException("Your number is too large!");
            }
            Task task = parseTask(matcher.group("task"));
            return new CommandTaskInsert(taskID, task);
        }
        LOGGER.log(Level.SEVERE, "Detected invalid command for command: " + userInput);
        throw new FarmioException("Invalid Command");
    }
}
