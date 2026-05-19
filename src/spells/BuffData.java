package spells;

import java.util.Map;

import attributes.Attributes;

public abstract class BuffData
{
    public static void createBuffs()
    {
        new Buff(true, 0, Map.of(Attributes.life, new BuffPower(0.2, 0, 1)));                                                                       
        new Buff(true, 1, Map.of(Attributes.phyAtk, new BuffPower(0.1, 0, 1), Attributes.phyDef, new BuffPower(-0.05, 0, 1)));
        new Buff(true, 2, Map.of(Attributes.phyDef, new BuffPower(0.04, 0, 1), Attributes.magDef, new BuffPower(0.04, 0, 1), Attributes.block, new BuffPower(0, 0.16, 1)));
        new Buff(true, 3, Map.of(Attributes.phyAtk, new BuffPower(-0.05, 0, 1), Attributes.phyDef, new BuffPower(0.1, 0, 1)));
        new Buff(true, 4, Map.of(Attributes.life, new BuffPower(0.02, 0, 1), Attributes.phyAtk, new BuffPower(0.02, 0, 1), Attributes.phyDef, new BuffPower(0.02, 0, 1)));
        new Buff(true, 5, Map.of(Attributes.phyAtk, new BuffPower(0.1, 0, 1), Attributes.magAtk, new BuffPower(0.1, 0, 1), Attributes.phyDef, new BuffPower(0.1, 0, 1), Attributes.magDef, new BuffPower(0.1, 0, 1), Attributes.dex, new BuffPower(0.1, 0, 1), Attributes.agi, new BuffPower(0.1, 0, 1)));
        new Buff(true, 6, Map.of(Attributes.block, new BuffPower(0, 0.14, 1)));
        new Buff(true, 7, Map.of(Attributes.life, new BuffPower(0.4, 0, 1)));
        new Buff(true, 8, Map.of(Attributes.magAtk, new BuffPower(0.04, 0, 1), Attributes.magDef, new BuffPower(0.04, 0, 1)));
        new Buff(true, 9, Map.of(Attributes.phyAtk, new BuffPower(0.04, 0, 1), Attributes.magAtk, new BuffPower(0.04, 0, 1), Attributes.dex, new BuffPower(0.02, 0, 1), Attributes.agi, new BuffPower(0.02, 0, 1)));
        new Buff(true, 10, Map.of(Attributes.life, new BuffPower(0.04, 0, 1), Attributes.mp, new BuffPower(0.04, 0, 1)));
        new Buff(true, 11, Map.of(Attributes.phyDef, new BuffPower(0.04, 0, 1), Attributes.magDef, new BuffPower(0.04, 0, 1), Attributes.agi, new BuffPower(0.04, 0, 1)));
        new Buff(true, 12, Map.of(Attributes.phyAtk, new BuffPower(0.04, 0, 1), Attributes.magAtk, new BuffPower(0.04, 0, 1), Attributes.dex, new BuffPower(0.04, 0, 1)));
        new Buff(true, 13, Map.of(Attributes.phyDef, new BuffPower(-0.02, 0, 1), Attributes.magDef, new BuffPower(-0.02, 0, 1)));
        new Buff(true, 14, Map.of(Attributes.phyAtk, new BuffPower(0.1, 0, 1), Attributes.magAtk, new BuffPower(0.1, 0, 1), Attributes.phyDef, new BuffPower(0.1, 0, 1), Attributes.magDef, new BuffPower(0.1, 0, 1), Attributes.dex, new BuffPower(0.1, 0, 1), Attributes.agi, new BuffPower(0.1, 0, 1), Attributes.critAtk, new BuffPower(0.1, 0, 1), Attributes.critDef, new BuffPower(0.1, 0, 1)));
        new Buff(true, 15, Map.of(Attributes.blood, new BuffPower(0, 0.6, 1)));
        new Buff(true, 16, Map.of(Attributes.critAtk, new BuffPower(0.04, 0, 1)));
        new Buff(true, 17, Map.of(Attributes.phyDef, new BuffPower(0.03, 0, 1), Attributes.agi, new BuffPower(0.05, 0, 1)));
    }

    public static void createNerfs()
    {
        new Buff(false, 0, Map.of(Attributes.dex, new BuffPower(0.1, 0, 1), Attributes.agi, new BuffPower(0.1, 0, 1)));
        new Buff(false, 1, Map.of(Attributes.agi, new BuffPower(0.04, 0, 1)));
        new Buff(false, 2, Map.of(Attributes.dex, new BuffPower(0.03, 0, 1), Attributes.agi, new BuffPower(0.03, 0, 1)));
    }
}
