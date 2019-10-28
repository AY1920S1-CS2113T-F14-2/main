package farmio;

import commands.Command;
import commands.CommandGameLoad;
import commands.CommandGameQuit;
import commands.CommandGameSave;
import commands.CommandGameNew;
import commands.CommandMenuStart;
import commands.CommandLevelEnd;
import commands.CommandLevelStart;
import commands.CommandTasksRun;
import commands.CommandCheckObjectives;
import commands.CommandDayEnd;
import commands.CommandDayStart;
import commands.CommandLevelReset;
import commands.CommandAddName;
import commands.CommandMenu;
import commands.CommandTaskDeleteAll;
import commands.CommandShowList;
import commands.CommandTaskCreate;
import commands.CommandTasksHint;
import commands.CommandTaskDelete;
import commands.CommandTaskEdit;
import commands.CommandTaskInsert;
import usercode.tasks.Task;
import usercode.tasks.IfTask;
import usercode.tasks.ForTask;
import usercode.tasks.DoTask;
import usercode.tasks.WhileTask;
import exceptions.FarmioException;
import usercode.actions.Action;
import usercode.conditions.Condition;

/**
 * Parser class is responsible for parsing all user input and generating the corresponding Command.
 */

public class Parser {

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
        switch (stage) {
        case WELCOME:
            return new CommandMenuStart();
        case LEVEL_START:
            return new CommandLevelStart();
        case MENU:
            return parseMenu(userInput);
        case RUNNING_DAY:
            return new CommandTasksRun();
        case CHECK_OBJECTIVES:
            return new CommandCheckObjectives();
        case DAY_START:
            return new CommandDayStart();
        case LEVEL_END:
            return new CommandLevelEnd();
        case LEVEL_FAILED:
            return new CommandLevelReset();
        case DAY_END:
            return new CommandDayEnd(); //TODO check if reset for dayend
        case NAME_ADD:
            return new CommandAddName(userInput);
        case TASK_ADD:
            return parseTaskAdd(userInput);
        default:
            throw new FarmioException("Invalid Command!");
        }
    }

    /**
     * Allows the user to resume the game from the menu.
     *
     * @param userInput input String from the user
     * @return Command that corresponds to the user's actions
     * @throws FarmioException if user input is invalid
     */
    private static Command parseMenu(String userInput) throws FarmioException {
        if (userInput.equals("resume game")) {
            return new CommandLevelStart();
        } else {
            throw new FarmioException("Invalid command!");
        }
    }

    /**
     * Used to parse the user input during the DAY_END stage. User can choose to either reset the level,
     * or proceed to the next day
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
            return new CommandMenu();
        }
        if (userInput.equals("deleteall") || userInput.equals("delete all")) {
            return new CommandTaskDeleteAll();
        }
        if (userInput.startsWith("delete")) {
            return parseTaskDelete(userInput);
        }
        if (userInput.startsWith("insert")) {
            return insertTask(userInput);
        }
        if (userInput.startsWith("edit")) {
            return editTask(userInput);
        }
        if (userInput.toLowerCase().equals("start")) {
            return new CommandDayStart();
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
        if (userInput.startsWith("do") || userInput.startsWith("if")
                || userInput.startsWith("for") || userInput.startsWith("while")) {
            return new CommandTaskCreate(parseTask(userInput));
        } else if (userInput.equals("hint") || userInput.equals("")) {
            return new CommandTasksHint();
        }
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
        if (userInput.matches("(delete)\\s+\\d+")) {
            int taskID = Integer.parseInt((userInput.substring(userInput.indexOf(" "))).trim());
            return new CommandTaskDelete(taskID);
        }
        throw new FarmioException("Invalid Command!");
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
     * Used to generate a DoTask from the user's input. The function first breaks the input string into the
     * taskType and the action, and validates both. If both are valid, then the corresponding DoTask is created
     * and returned
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
            throw new FarmioException("Invalid command format!");
        }
        if (!taskType.equals("do")) {
            throw new FarmioException("Invalid task type!");
        }
        if (Action.isValidAction(userAction)) {
            return new DoTask(Condition.toCondition("true"), Action.toAction(userAction));
        } else {
            throw new FarmioException("Invalid action!");
        }
    }

    /**
     * Parses Conditional Tasks. The function first breaks the input String into three parts,
     * the taskType, condition, and action.
     * If all are valid, then the corresponding Task is created and returned
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
            throw new FarmioException("Invalid command format!");
        }
        if (!taskType.equals("if")  && !taskType.equals("for") && !taskType.equals("while")) {
            throw new FarmioException("Invalid task type!");
        }
        if (!Condition.isValidCondition(condition)) {
            throw new FarmioException("Invalid Condition!");
        }
        if (!Action.isValidAction(action)) {
            throw new FarmioException("Invalid Action!");
        }
        Task task;
        switch (taskType) {
        case "if":
            task = new IfTask(Condition.toCondition(condition), Action.toAction(action));
            break;
        case "for":
            task = new ForTask(Condition.toCondition(condition), Action.toAction(action));
            break;
        case "while":
            task = new WhileTask(Condition.toCondition(condition), Action.toAction(action));
            break;
        default:
            throw new FarmioException("Error Creating Task!");
        }
        return task;
    }

    /**
     * Parses commands meant to edit any Task in the TaskList. The function uses regex to validate
     * if the input String is of the correct format, then breaks it into three parts, the keyword "edit",
     * the taskID to be edited, and the description of the Task to replace the current Task.
     * The task description is validated using the parseTask() function.
     *
     * @param userInput user input String
     * @return Command that will edit the Task in the TaskList with the specified ID when executed
     * @throws FarmioException if the user's input is of wrong format or the task description is invalid
     */
    private static Command editTask(String userInput) throws FarmioException {
        String keyword = "";
        String taskID = "";
        String taskDesc = "";
        if (userInput.matches("(edit)\\s+\\d+\\s+.+")) {
            try {
                keyword = userInput.substring(0, userInput.indexOf(" "));
                userInput = (userInput.substring(userInput.indexOf(" ") + 1)).trim();
                taskID = (userInput.substring(0, userInput.indexOf(" "))).trim();
                taskDesc = (userInput.substring(userInput.indexOf(" ") + 1)).trim();
            } catch (IndexOutOfBoundsException e) {
                throw new FarmioException("Invalid command format!");
            }
            if (!keyword.equals("edit")) {
                throw new FarmioException("Invalid Command!");
            }
            Task task = parseTask(taskDesc);
            return new CommandTaskEdit(Integer.parseInt(taskID), task);
        } else {
            throw new FarmioException("Invalid Command Format");
        }
    }

    /**
     * Parses commands meant to insert a Task at a specific position in the TaskList.
     *
     * @param userInput user input String
     * @return Command that inserts a Task at the specified position
     * @throws FarmioException if the user input is of invalid format, or the task description is invalid
     */
    private static Command insertTask(String userInput) throws FarmioException {
        String keyword = "";
        String taskID = "";
        String taskDesc = "";
        if (userInput.matches("(insert)\\s+\\d+\\s+.+")) {
            try {
                keyword = userInput.substring(0, userInput.indexOf(" "));
                userInput = (userInput.substring(userInput.indexOf(" ") + 1)).trim();
                taskID = (userInput.substring(0, userInput.indexOf(" "))).trim();
                taskDesc = (userInput.substring(userInput.indexOf(" ") + 1)).trim();
            } catch (IndexOutOfBoundsException e) {
                throw new FarmioException("Invalid command format!");
            }
            if (!keyword.equals("insert")) {
                throw new FarmioException("Invalid Command!");
            }
            Task task = parseTask(taskDesc);
            return new CommandTaskInsert(Integer.parseInt(taskID), task);
        } else {
            throw new FarmioException("Invalid Command Format");
        }
    }
}
