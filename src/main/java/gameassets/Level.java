package gameassets;

import farmio.Farmio;
import gameassets.Farmer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class Level {
    private ArrayList<String> narratives;
    private String filePath;
    private String objective;
    private String hint;
    private int endSeeds;
    private int endSeedlings;
    private int endWheat;
    private int endGrain;
    private int endGold;
    private int deadline;
    public String modelAnswer;
    public ArrayList<String> successfulFeedback;

    private boolean detailedFeedbackProvided = true;

    /**
     * Intitalises variables based off values obtain form the JSON File.
     * @param object JSON Object to be parsed.
     * @param name player's name
     */
    public Level(JSONObject object, String name) {
        JSONArray array = (JSONArray) object.get("narratives");
        narratives = new ArrayList<>();
        for (Object i : array) {
            String line = (String) i;
            line = line.replace("+", name);
            narratives.add(line);
        }

        JSONArray feedbackarray = (JSONArray) object.get("feedback");
        successfulFeedback = new ArrayList<>();
        for (Object i : feedbackarray) {
            successfulFeedback.add((String) i);
        }

        filePath = (String) object.get("file_path");
        endGold = Math.toIntExact((Long) object.get("gold"));
        endSeeds = Math.toIntExact((Long) object.get("seeds"));
        endSeedlings = Math.toIntExact((Long) object.get("seedlings"));
        endWheat = Math.toIntExact((Long) object.get("wheat"));
        endGrain = Math.toIntExact((Long) object.get("grain"));
        deadline = Math.toIntExact((Long) object.get("deadline"));
        objective = (String) object.get("objective");
        hint = ((String) object.get("hint")).replace("+", name);
        modelAnswer = (String) object.get("modelAnswer");

    }

    /**
     * Get the narrative of the level .
     * @return the list of narrative
     */
    public ArrayList<String> getNarratives() {
        return narratives;
    }

    /**
     * Get hint for completing the level.
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * Get path for simulation of the level's narrative.
     * @return the file path
     */
    public String getPath() {
        return filePath;
    }

    public enum ObjectiveResult {
        NOT_DONE,
        DONE,
        FAILED,
        INVALID
    }

    private boolean checkDeadlineExceeded(int currentDay) {
        return deadline < currentDay;
    }

    private boolean allDone(Farmer farmer) {
        if (farmer.getLevel() == 1.1) {
            return farmer.getLocation().equals("Market");
        }
        int seeds = farmer.wheatFarm.getSeeds();
        int seedlings = farmer.wheatFarm.getSeedlings();
        int wheat = farmer.wheatFarm.getWheat();
        int grain = farmer.wheatFarm.getGrain();
        int gold = farmer.getGold();
        return (seeds >= endSeeds) && (wheat >= endWheat) && (grain >= endGrain) && (seedlings >= endSeedlings)
                && (gold >= endGold);
    }

    /**
     * Checks on the state of the level.
     * @param farmio Farmio Object
     * @return enum current state of the level
     */
    public ObjectiveResult checkAnswer(Farmio farmio) {
        if (farmio.getFarmer().isHasfailedCurrentTask()) {
            farmio.getFarmer().resetTaskFailed();
            return ObjectiveResult.INVALID;
        }
        Farmer farmer = farmio.getFarmer();
        ObjectiveResult levelState;
        int day = farmer.getDay();
        if (checkDeadlineExceeded(day)) {
            levelState = ObjectiveResult.FAILED;
        } else {
            if (allDone(farmer)) {
                levelState = ObjectiveResult.DONE;
            } else if (checkDeadlineExceeded(day + 1)) {
                levelState = ObjectiveResult.FAILED;
            } else {
                levelState = ObjectiveResult.NOT_DONE;
            }
        }
        return levelState;
    }

    /**
     * Checks for incomplete objectives and returns feedback on objectives.
     * @param farmer Farmer object
     * @return Feedback on incomplete objectives
     */
//    private List<String> checkIncompleteObjectives(Farmer farmer) {
//        List<String> output = new ArrayList<String>();
//        int seeds = farmer.wheatFarm.getSeeds();
//        int wheat = farmer.wheatFarm.getWheat();
//        int grain = farmer.wheatFarm.getGrain();
//        int gold = farmer.getGold();
//        double levelNumber = farmer.getLevel();
//        if (levelNumber == 1.1) {
//            return output;
//        }
//
//        if (levelNumber == 1.2) {
//            if (seeds != endSeeds) {
//
//                int balancedWheatSeed = endSeeds - seeds;
//
//                output.add(" Seeds left :" + balancedWheatSeed);
//            } else {
//                output.add(" Seeds Completed");
//            }
//        }
//
//        if (levelNumber == 1.5) {
//            if (grain != endGrain) {
//                int balancedWheatRipe =   grain - endGrain;
//                output.add(" | Grain left :" + balancedWheatRipe);
//            } else {
//                output.add(" | Grain completed");
//            }
//        }
//
//        if (levelNumber == 1.6 || levelNumber == 1.5) {
//            if (gold != endGold) {
//
//                int balancedGold = endGold - gold;
//                output.add(" Gold required :" + balancedGold);
//
//            } else {
//                output.add(" gold Completed");
//            }
//        }
//
//
//        if (levelNumber == 1.4) {
//            if (grain != endGrain) {
//                int balancedWheatRipe = endGrain - grain;
//                output.add(" | Grain left :" + balancedWheatRipe);
//            } else {
//                output.add(" | Grain completed");
//            }
//        }
//
//        return output;
//    }

    /**
     * Splits string by | to List of Strings.
     * @param modelAnswer String to be split
     * @return List of Strings
     */
    public List<String> convertStringToList(String modelAnswer) {

        //todo build a much more comprehensive parser for level
        String[] taskItems = modelAnswer.split("|");
        List<String> modelTaskList = new ArrayList<String>(Arrays.asList(taskItems));
        return  modelTaskList;
    }

    /**
     * Converts numbered format for tasks to a  standard format Strings.
     * @param taskList  List of strings to be converted
     * @return newly formatted List of Strings
     */
    public List<String> convertTaskListFormat(List<String> taskList) {

        List<String> splitTaskList = new ArrayList<String>();
        for (String taskListItems: taskList) {
            String removedNumbering = taskListItems.substring(taskListItems.indexOf(".") + 1);
            removedNumbering.trim();// removed the numbering

            //separate based on actions - todo check how its divided
            //String[] splitString = taskListItems.split("\\s+");
            String[] splitString = taskListItems.split("(?<!\\G\\w)\\s"); //splits on every second space
            //should be after every 3 spacesh

            splitTaskList.add(splitString[0]);
            if (splitString[1] != null && !splitString[1].isEmpty()) {
                splitTaskList.add(splitString[1]);
            }

        }
        return splitTaskList;
    }

    /**
     * Returns the feedback based off the different permutations of tasks when the level failes.
     * @param farmio farmio current state
     * @param levelNumber the level the game is currently running
     * @return feedback on failed tasks
     */
    public List<String> getPermutationFeedback(Farmio farmio,double levelNumber) {
        List<String> output = new ArrayList<String>();
        //todo convert to some sort of metric for future iterations
        List<String> userTaskList = farmio.getFarmer().tasks.toStringArray();
        List<String> modelTaskList = convertStringToList(modelAnswer);
        List<String> modifieduserTaskList = convertTaskListFormat(userTaskList);

        if (levelNumber == 1.5) {
            output.add("for this level you need to check for presence of grain by using the if command before");
            output.add("using the do command to sell your grain");

        }
        return output;
    }

    /**
     * Feedback on failed objectives.
     * @param farmio farmio
     * @return feedback
     */
    public List<String> getDetailedFeedback(Farmio farmio) {
        List<String> output = new ArrayList<String>();
        double levelNumber = farmio.getFarmer().getLevel(); // unsure if this is needed rn
        output.add(" The objective of this level was to " + objective);
        output.add("\nUnfortunately you were unable to complete within the allocated time of " + deadline + " days");

        output.add("\nYour actions ");
        output.add(farmio.getFarmer().tasks.toString());

        output.addAll(getPermutationFeedback(farmio, levelNumber));

        return output;
    }

    /**
     *  Returns feedback for the succesful completion of a level.
     * @return List of succesfull feedbackS
     */
    public List<String> getSuccessfulFeedback() {
        List<String> output = new ArrayList<String>();
        for (String x: successfulFeedback) {
            output.add(x);
        }
        return output;
    }

    /**
     * Returns different feedback based on the levelState.
     * @param farmio farmio object
     * @param currentLevelState current state of the level
     * @return feedback
     */

    public List<String> getFeedback(Farmio farmio, ObjectiveResult currentLevelState) {
        Farmer farmer = farmio.getFarmer();

        List<String> output = new ArrayList<String>();
        if (currentLevelState == ObjectiveResult.DONE) {
            output.addAll(getSuccessfulFeedback());
            output.add("well done you have completed the level - all tasks has been completed succesfully");
            return output;
        } else if (currentLevelState == ObjectiveResult.NOT_DONE) { //day completed but tasks not achieved succesfult
            String feedback = "tasks have yet to be completed";
            output.add(feedback);
            if (detailedFeedbackProvided) {
//                output.add("detailed feedback : -- \n");
//                output.addAll(checkIncompleteObjectives(farmer));
            }
            output.add("Press [ENTER] to continue the game or enter [reset] to restart the level");
            return output;
        } else if (currentLevelState == ObjectiveResult.FAILED) {
            String feedback = "Oh no! The objectives were not met by the deadline! Level failed ! \n";
            output.add(feedback);
            if (detailedFeedbackProvided) { //todo -redo this code
                output.addAll(getDetailedFeedback(farmio));
            }
            return output;
        } else if (currentLevelState == ObjectiveResult.INVALID) {
            output.add("Oh no! There has been an error during code execution!");
            return  output;
        }
        return output;
    }

    /**
     * Get the list of goals to be completed.
     * @return the list of goals
     */
    public Map<String,Integer> getGoals() {
        Map<String,Integer> goals = new HashMap<String,Integer>();
        goals.put("Gold", endGold);
        goals.put("Seeds", endSeeds);
        goals.put("Seedlings", endSeedlings);
        goals.put("Wheat", endWheat);
        goals.put("Grain", endGrain);
        return goals;
    }

    /**
     * Get the main objective of the level.
     * @return the objective of the level
     */
    public String getObjective() {
        return objective;
    }

}