package spells;

import java.util.Map;

import attributes.Attributes;
import main.Log;

public abstract class BuffData
{
    public static void createBuffs()
    {
        if (!Buff.getAllBuffs().isEmpty()) { Log.warn("Trying to create buffs twice") ; return ;}

        new Buff(true, 0, 1.0, Map.of(Attributes.life, new BuffPower(0.2, 0)));                                                                       
        new Buff(true, 1, 1.0, Map.of(Attributes.phyAtk, new BuffPower(0.1, 0), Attributes.phyDef, new BuffPower(-0.05, 0)));
        new Buff(true, 2, 1.0, Map.of(Attributes.phyDef, new BuffPower(0.04, 0), Attributes.magDef, new BuffPower(0.04, 0), Attributes.block, new BuffPower(0, 0.16)));
        new Buff(true, 3, 1.0, Map.of(Attributes.phyAtk, new BuffPower(-0.05, 0), Attributes.phyDef, new BuffPower(0.1, 0)));
        new Buff(true, 4, 1.0, Map.of(Attributes.life, new BuffPower(0.02, 0), Attributes.phyAtk, new BuffPower(0.02, 0), Attributes.phyDef, new BuffPower(0.02, 0)));
        new Buff(true, 5, 1.0, Map.of(Attributes.phyAtk, new BuffPower(0.1, 0), Attributes.magAtk, new BuffPower(0.1, 0), Attributes.phyDef, new BuffPower(0.1, 0), Attributes.magDef, new BuffPower(0.1, 0), Attributes.dex, new BuffPower(0.1, 0), Attributes.agi, new BuffPower(0.1, 0)));
        new Buff(true, 6, 1.0, Map.of(Attributes.block, new BuffPower(0, 0.14)));
        new Buff(true, 7, 1.0, Map.of(Attributes.life, new BuffPower(0.4, 0)));
        new Buff(true, 8, 1.0, Map.of(Attributes.magAtk, new BuffPower(0.04, 0), Attributes.magDef, new BuffPower(0.04, 0)));
        new Buff(true, 9, 1.0, Map.of(Attributes.phyAtk, new BuffPower(0.04, 0), Attributes.magAtk, new BuffPower(0.04, 0), Attributes.dex, new BuffPower(0.02, 0), Attributes.agi, new BuffPower(0.02, 0)));
        new Buff(true, 10, 1.0, Map.of(Attributes.life, new BuffPower(0.04, 0), Attributes.mp, new BuffPower(0.04, 0)));
        new Buff(true, 11, 1.0, Map.of(Attributes.phyDef, new BuffPower(0.04, 0), Attributes.magDef, new BuffPower(0.04, 0), Attributes.agi, new BuffPower(0.04, 0)));
        new Buff(true, 12, 1.0, Map.of(Attributes.phyAtk, new BuffPower(0.04, 0), Attributes.magAtk, new BuffPower(0.04, 0), Attributes.dex, new BuffPower(0.04, 0)));
        new Buff(true, 13, 1.0, Map.of(Attributes.phyDef, new BuffPower(-0.02, 0), Attributes.magDef, new BuffPower(-0.02, 0)));
        new Buff(true, 14, 1.0, Map.of(Attributes.phyAtk, new BuffPower(0.1, 0), Attributes.magAtk, new BuffPower(0.1, 0), Attributes.phyDef, new BuffPower(0.1, 0), Attributes.magDef, new BuffPower(0.1, 0), Attributes.dex, new BuffPower(0.1, 0), Attributes.agi, new BuffPower(0.1, 0), Attributes.critAtk, new BuffPower(0.1, 0), Attributes.critDef, new BuffPower(0.1, 0)));
        new Buff(true, 15, 1.0, Map.of(Attributes.blood, new BuffPower(0, 0.6)));
        new Buff(true, 16, 1.0, Map.of(Attributes.critAtk, new BuffPower(0.04, 0)));
        new Buff(true, 17, 1.0, Map.of(Attributes.phyDef, new BuffPower(0.03, 0), Attributes.agi, new BuffPower(0.05, 0)));
    }

    public static void createNerfs()
    {
        if (!Buff.getAllNerfs().isEmpty()) { Log.warn("Trying to create nerfs twice") ; return ;}

        new Buff(false, 0, 1.0, Map.of(Attributes.dex, new BuffPower(0.1, 0), Attributes.agi, new BuffPower(0.1, 0)));
        new Buff(false, 1, 1.0, Map.of(Attributes.agi, new BuffPower(0.04, 0)));
        new Buff(false, 2, 1.0, Map.of(Attributes.dex, new BuffPower(0.03, 0), Attributes.agi, new BuffPower(0.03, 0)));
    }
}
