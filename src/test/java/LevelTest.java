import gameassets.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LevelTest {
    String object = " " +
            "\"narratives\": [\n" +
            "    \"At the market, you can buy and sell seeds in exchange for gold.\",\n" +
            "    \"The action [do buySeeds] allows you to purchase 1 unit of seed from the market.\",\n" +
            "    \"Enter [Task] [Action] to buy seeds from the market.\"\n" +
            "  ],\n" +
            "  \"feedback\": [\n" +
            "    \"You have succesfully bought seeds using the gold you have\",\n" +
            "    \"Note that a symbol '->'  will appear whenever there is a change in your assets, \"\n" +
            "  ],\n" +
            "  \"gold\": 0,\n" +
            "  \"seeds\": 1,\n" +
            "  \"seedlings\": 0,\n" +
            "  \"wheat\": 0,\n" +
            "  \"grain\": 0,\n" +
            "  \"file_path\": \"Level1.2\",\n" +
            "  \"deadline\" : 1,\n" +
            "  \"objective\": \"Buy some seeds from the market\",\n" +
            "  \"hint\": \"1.Enter [market] to check the price of a seed.\\n2.Enter [do buySeeds] to buy seeds from the market if you can afford it.\",\n" +
            "  \"modelAnswer\": \"|do buySeeds|\"" +
            "" ;

    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(object);


    public LevelTest() throws ParseException {
       Level level = new Level(json, "tester");

    }
}
