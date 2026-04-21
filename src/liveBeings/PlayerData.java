package liveBeings;

import java.util.List;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;

public abstract class PlayerData
{
    private static final List<String[]> attributeIncreaseOnLevelUp ;
    private static final List<PersonalAttributes> initialPersonalAttPerJob ;
    private static final List<BattleAttributes> initialBattleAttPerJob ;
    private static final List<Integer> range ;
    private static final List<Integer> step ;
    private static final List<Double> goldMultiplier ;
    private static final List<Double> satiationCounterDuration ;
    private static final List<Double> mpCounterDuration ;
    private static final List<Double> thirstCounterDuration ;
    private static final List<Double> battleActionCounterDuration ;

    static
    {
        initialPersonalAttPerJob = List.of(
            new PersonalAttributes(100, 100, 1, 50, 50, 1, 0, 5, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(50, 50, 1, 100, 100, 1, 0, 5, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(60, 60, 1, 80, 80, 1, 0, 5, 1, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(70, 70, 1, 70, 70, 1, 0, 5, 1.2, 100, 100, 1, 100, 100, 1),
            new PersonalAttributes(30, 30, 1, 50, 50, 1, 0, 5, 1, 100, 100, 1, 100, 100, 1)
        ) ;

        range = List.of(
            60,
            140,
            200,
            60,
            60
        ) ;

        step = List.of(
            150,
            150,
            150,
            150,
            150
        ) ;

        goldMultiplier = List.of(
            1.0,
            1.0,
            1.0,
            1.0,
            1.15
        ) ;

        satiationCounterDuration = List.of(
            5.0,
            5.0,
            5.0,
            4.25,
            5.0
        ) ;

        mpCounterDuration = List.of(
            0.25,
            0.0625,
            0.25,
            0.1875,
            0.25
        ) ;

        thirstCounterDuration = List.of(
            2.5,
            2.5,
            2.5,
            2.0,
            2.5
        ) ;

        battleActionCounterDuration = List.of(
            1.0,
            1.0,
            1.0,
            1.0,
            0.75
        ) ;

        initialBattleAttPerJob = List.of(
            new BattleAttributes(5, 2, 5, 2, 10, 2, 0.1, 0.0, 1.0, 10),
            new BattleAttributes(2, 5, 2, 5, 7, 1, 0.1, 0.0, 1.0, 10),
            new BattleAttributes(3, 3, 3, 3, 18, 3, 0.15, 0.0, 1.0, 10),
            new BattleAttributes(4, 3, 4, 3, 12, 6, 0.12, 0.0, 1.0, 10),
            new BattleAttributes(3, 2, 3, 2, 15, 8, 0.18, 0.0, 0.75, 10)
        ) ;

        attributeIncreaseOnLevelUp = List.of(
            new String[] {"0","0","20","10","2","1","2","1","1","1","1","1","0.8","0.8","0.8","0.8","0.4","0.4"},
            new String[] {"0","1","40","10","5","2","3","2","1","1","1","1","0.8","0.6","0.666666667","0.6","0.8","0.4"},
            new String[] {"0","2","34","16","3","4","5","6","1","1","1","1","0.666666667","0.4","0.8","0.4","0.4","0.4"},
            new String[] {"1","0","10","20","1","2","1","2","1","1","1","1","0.8","0.8","0.8","0.8","0.4","0.4"},
            new String[] {"1","1","10","40","3","5","3","6","1","1","1","1","0.4","0.8","0.4","0.4","0.2","0.2"},
            new String[] {"1","2","20","30","2","5","2","5","1","1","1","1","0.8","0.6","0.8","0.6","0.2","0.2"},
            new String[] {"2","0","14","14","3","3","3","3","2","1","1","1","0.4","0.4","0.4","0.4","0.4","0.6"},
            new String[] {"2","1","20","20","5","6","3","3","6","4","1","1","0.72","0.4","0.4","0.4","0.4","0.4"},
            new String[] {"2","2","20","24","6","5","3","3","4","3","1","1","0.4","0.6","0.4","0.4","0.4","0.4"},
            new String[] {"3","0","14","14","2","3","2","3","1","2","1","1","0.7","0.4","0.7","0.4","0.6","0.4"},
            new String[] {"3","1","22","20","3","3","3","5","3","4","1","1","0.8","0.8","0.8","0.4","0.4","0.4"},
            new String[] {"3","2","26","16","4","3","4","3","2","6","1","1","0.9","0.4","0.9","0.4","0.9","0.4"},
            new String[] {"4","0","10","10","2","2","2","2","1","3","1","1","0.5","0.4","0.5","0.4","0.6","0.4"},
            new String[] {"4","1","16","16","5","2","5","2","2","2","1","1","0.4","0.4","0.4","0.4","0.9","0.9"},
            new String[] {"4","2","10","10","2","4","2","2","3","6","1","1","0.5","0.4","0.5","0.4","0.4","0.4"}
        ) ;
    }

    public static List<String[]> getAttributeincreaseonlevelup() { return attributeIncreaseOnLevelUp ;}
    public static List<PersonalAttributes> getInitialpersonalattperjob() { return initialPersonalAttPerJob ;}
    public static List<BattleAttributes> getInitialbattleattperjob() { return initialBattleAttPerJob ;}
    public static List<Integer> getRange() { return range ;}
    public static List<Integer> getStep() { return step ;}
    public static List<Double> getGoldmultiplier() { return goldMultiplier ;}
    public static List<Double> getSatiationcounterduration() { return satiationCounterDuration ;}
    public static List<Double> getMpcounterduration() { return mpCounterDuration ;}
    public static List<Double> getThirstcounterduration() { return thirstCounterDuration ;}
    public static List<Double> getBattleactioncounterduration() { return battleActionCounterDuration ;}

}