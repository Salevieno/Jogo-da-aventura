package liveBeings;

import java.util.List;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;

public class PetData
{
    private static final List<String> name ;
    private static final List<String[]> attributeIncreaseOnLevelUp ;
    private static final List<PersonalAttributes> initialPersonalAttPerJob ;
    private static final List<BattleAttributes> initialBattleAttPerJob ;
    private static final List<Integer> range ;
    private static final List<Integer> step ;
    private static final List<Double> satiationCounterDuration ;
    private static final List<Double> mpCounterDuration ;
    private static final List<Double> thirstCounterDuration ;
    private static final List<Double> battleActionCounterDuration ;
    
    static
    {
        name = List.of(
            "Tobby",
            "Little",
            "Melly",
            "Yall"
        ) ;

        initialPersonalAttPerJob = List.of(
            new PersonalAttributes(100, 100, 1, 50, 50, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(50, 50, 1, 100, 100, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(60, 60, 1, 80, 80, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(70, 70, 1, 70, 70, 1, 0, 50, 1, 100, 100, 1, 100, 100, 1)
        ) ;

        range = List.of(
            60,
            140,
            200,
            60
        ) ;

        step = List.of(
            150,
            150,
            150,
            150
        ) ;

        satiationCounterDuration = List.of(
            2.5,
            2.5,
            2.0,
            2.5
        ) ;

        mpCounterDuration = List.of(
            0.625,
            0.375,
            0.625,
            0.625
        ) ;

        thirstCounterDuration = List.of(
            2.5,
            2.0,
            2.5,
            2.5
        ) ;

        battleActionCounterDuration = List.of(
            1.0,
            1.0,
            1.0,
            0.75
        ) ;

        initialBattleAttPerJob = List.of(
            new BattleAttributes(5, 2, 5, 2, 12, 2, 0.1, 0.0, 1.0, 12),
            new BattleAttributes(2, 5, 2, 5, 12, 1, 0.1, 0.0, 1.0, 10),
            new BattleAttributes(3, 3, 3, 3, 32, 3, 0.12, 0.0, 1.0, 10),
            new BattleAttributes(4, 3, 4, 3, 16, 6, 0.15, 0.0, 0.85, 10)
        ) ;

        attributeIncreaseOnLevelUp = List.of(
            new String[] {"0", "20", "10", "4", "2", "4", "2", "2", "2", "1", "1", "0.4", "0.4", "0.4", "0.4", "0.2", "0.2"},
            new String[] {"1", "40", "10", "8", "4", "4", "4", "4", "2", "1", "1", "0.5", "0.3", "0.5", "0.3", "0.2", "0.2"},
            new String[] {"2", "34", "16", "4", "4", "8", "6", "2", "2", "1", "1", "0.5", "0.4", "0.5", "0.4", "0.2", "0.2"},
            new String[] {"3", "10", "20", "2", "4", "2", "4", "2", "2", "1", "1", "0.4", "0.4", "0.4", "0.4", "0.2", "0.2"}
        ) ;
    }

    public static List<String> getName() { return name ;}
    public static List<String[]> getAttributeincreaseonlevelup() { return attributeIncreaseOnLevelUp ;}
    public static List<PersonalAttributes> getInitialpersonalattperjob() { return initialPersonalAttPerJob ;}
    public static List<BattleAttributes> getInitialbattleattperjob() { return initialBattleAttPerJob ;}
    public static List<Integer> getRange() { return range ;}
    public static List<Integer> getStep() { return step ;}
    public static List<Double> getSatiationcounterduration() { return satiationCounterDuration ;}
    public static List<Double> getMpcounterduration() { return mpCounterDuration ;}
    public static List<Double> getThirstcounterduration() { return thirstCounterDuration ;}
    public static List<Double> getBattleactioncounterduration() { return battleActionCounterDuration ;}

}
