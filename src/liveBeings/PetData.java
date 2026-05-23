package liveBeings;

import java.util.List;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;

public class PetData
{
    private static final List<String> NAME ;
    private static final List<String[]> ATTRIBUTE_INCREASE_ON_LEVEL_UP ;
    private static final List<PersonalAttributes> INITIAL_PERSONAL_ATT_PER_JOB ;
    private static final List<BattleAttributes> INITIAL_BATTLE_ATT_PER_JOB ;
    private static final List<Integer> RANGE ;
    private static final List<Integer> STEP ;
    private static final List<Double> SATIATION_COUNTER_DURATION ;
    private static final List<Double> MP_COUNTER_DURATION ;
    private static final List<Double> THIRST_COUNTER_DURATION ;
    private static final List<Double> BATTLE_ACTION_COUNTER_DURATION ;
    
    static
    {
        NAME = List.of(
            "Tobby",
            "Little",
            "Melly",
            "Yall"
        ) ;

        INITIAL_PERSONAL_ATT_PER_JOB = List.of(
            new PersonalAttributes(100, 100, 1, 50, 50, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(50, 50, 1, 100, 100, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(60, 60, 1, 80, 80, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(70, 70, 1, 70, 70, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1)
        ) ;

        RANGE = List.of(
            60,
            140,
            200,
            60
        ) ;

        STEP = List.of(
            150,
            150,
            150,
            150
        ) ;

        SATIATION_COUNTER_DURATION = List.of(
            2.5,
            2.5,
            2.0,
            2.5
        ) ;

        MP_COUNTER_DURATION = List.of(
            0.625,
            0.375,
            0.625,
            0.625
        ) ;

        THIRST_COUNTER_DURATION = List.of(
            2.5,
            2.0,
            2.5,
            2.5
        ) ;

        BATTLE_ACTION_COUNTER_DURATION = List.of(
            1.0,
            1.0,
            1.0,
            0.75
        ) ;

        INITIAL_BATTLE_ATT_PER_JOB = List.of(
            new BattleAttributes(5, 2, 5, 2, 12, 2, 0.1, 0.0, 1.0, 12),
            new BattleAttributes(2, 5, 2, 5, 12, 1, 0.1, 0.0, 1.0, 10),
            new BattleAttributes(3, 3, 3, 3, 32, 3, 0.12, 0.0, 1.0, 10),
            new BattleAttributes(4, 3, 4, 3, 16, 6, 0.15, 0.0, 0.85, 10)
        ) ;

        ATTRIBUTE_INCREASE_ON_LEVEL_UP = List.of(
            new String[] {"0", "20", "10", "4", "2", "4", "2", "2", "2", "1", "1", "0.4", "0.4", "0.4", "0.4", "0.2", "0.2"},
            new String[] {"1", "40", "10", "8", "4", "4", "4", "4", "2", "1", "1", "0.5", "0.3", "0.5", "0.3", "0.2", "0.2"},
            new String[] {"2", "34", "16", "4", "4", "8", "6", "2", "2", "1", "1", "0.5", "0.4", "0.5", "0.4", "0.2", "0.2"},
            new String[] {"3", "10", "20", "2", "4", "2", "4", "2", "2", "1", "1", "0.4", "0.4", "0.4", "0.4", "0.2", "0.2"}
        ) ;
    }

    public static List<String> getName() { return NAME ;}
    public static List<String[]> getAttributeincreaseonlevelup() { return ATTRIBUTE_INCREASE_ON_LEVEL_UP ;}
    public static List<PersonalAttributes> getInitialpersonalattperjob() { return INITIAL_PERSONAL_ATT_PER_JOB ;}
    public static List<BattleAttributes> getInitialbattleattperjob() { return INITIAL_BATTLE_ATT_PER_JOB ;}
    public static List<Integer> getRange() { return RANGE ;}
    public static List<Integer> getStep() { return STEP ;}
    public static List<Double> getSatiationcounterduration() { return SATIATION_COUNTER_DURATION ;}
    public static List<Double> getMpcounterduration() { return MP_COUNTER_DURATION ;}
    public static List<Double> getThirstcounterduration() { return THIRST_COUNTER_DURATION ;}
    public static List<Double> getBattleactioncounterduration() { return BATTLE_ACTION_COUNTER_DURATION ;}

}
