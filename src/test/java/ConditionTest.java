import farmio.exceptions.FarmioException;
import org.junit.jupiter.api.Test;
import logic.usercode.conditions.BooleanCondition;
import logic.usercode.conditions.BooleanConditionType;
import logic.usercode.conditions.Condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConditionTest {

    public ConditionTest() {}

    @Test
    public void isBooleanConditionTest() {
        assert Condition.isValidCondition("hasseeds");
        assert Condition.isValidCondition("hasgrain");
        assert Condition.isValidCondition("haswheat");
        assert !Condition.isValidCondition("");
        assert !Condition.isValidCondition(" ");
        assert !Condition.isValidCondition("irsntr");
    }

    @Test
    public void isValueConditionTest() {
        assert Condition.isValidCondition("gold greater than or equals 100");
        assert Condition.isValidCondition("gold greaterthan 100");
        assert Condition.isValidCondition("gold lessthanorequals 20");
        assert Condition.isValidCondition("gold less than 40");
        assert !Condition.isValidCondition("gold greater than ");
        assert !Condition.isValidCondition("gold");
        assert !Condition.isValidCondition("gold greater than -10");
    }

    @Test
    public void toBooleanCondition() throws FarmioException {
        BooleanCondition hasGrainCondition = new BooleanCondition(BooleanConditionType.hasGrain);
        BooleanCondition hasSeedsCondition = new BooleanCondition(BooleanConditionType.hasSeeds);
        BooleanCondition hasWheatCondition = new BooleanCondition(BooleanConditionType.hasWheat);
        assertEquals(Condition.toCondition("hasgrain").toString(), hasGrainCondition.toString());
        assertEquals(Condition.toCondition("hasseeds").toString(), hasSeedsCondition.toString());
        assertEquals(Condition.toCondition("haswheat").toString(), hasWheatCondition.toString());
    }

}
