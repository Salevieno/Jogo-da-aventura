package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Path
{
    public static String CSV = "./csv/" ;
    public static String DADOS = "./dados/" ;
    public static String IMAGES = "./images/" ;
    public static String MUSIC = "./music/" ;
    public static String TEXT_BR = "./Texto-PT-br.json" ;
    public static String BATTLE_IMG = "Battle/" ;
    public static String BUILDINGS_IMG = "Buildings/" ;
    public static String COLLECTABLES_IMG = "Collect/" ;
    public static String ELEMENTS_IMG = "Elements/" ;
    public static String EQUIPS_IMG = "Equips/" ;
    public static String GAME_IMG = "Game/" ;
    public static String LIVE_BEINGS_IMG = "LiveBeings/" ;
    public static String MAPS_IMG = "Maps/" ;
    public static String MAP_ELEMENTS_IMG = "MapElements/" ;
    public static String NPC_IMG = "NPCs/" ;
    public static String OPENING_IMG = "Opening/" ;
    public static String PLAYER_IMG = "Player/" ;
    public static String PET_IMG = "Pet/" ;
    public static String SIDEBAR_IMG = "Sidebar/" ;
    public static String SKY_IMG = "Sky/" ;
    public static String SPELLS_IMG = "Spells/" ;
    public static String STATUS_IMG = "Status/" ;
    public static String UI_IMG = "UI/" ;
    public static String WINDOWS_IMG = "Windows/" ;

    static
    {
        try (FileInputStream fis = new FileInputStream("paths.properties"))
        {
            Properties props = new Properties() ;
            props.load(fis) ;
            CSV = props.getProperty("CSV", CSV) ;
            DADOS = props.getProperty("DADOS", DADOS) ;
            IMAGES = props.getProperty("IMAGES", IMAGES) ;
            MUSIC = props.getProperty("MUSIC", MUSIC) ;
            TEXT_BR = props.getProperty("TEXT_BR", TEXT_BR) ;
            BATTLE_IMG = props.getProperty("BATTLE_IMG", BATTLE_IMG) ;
            BUILDINGS_IMG = props.getProperty("BUILDINGS_IMG", BUILDINGS_IMG) ;
            COLLECTABLES_IMG = props.getProperty("COLLECT_IMG", COLLECTABLES_IMG) ;
            GAME_IMG = props.getProperty("GAME_IMG", GAME_IMG) ;
            ELEMENTS_IMG = props.getProperty("ELEMENTS_IMG", ELEMENTS_IMG) ;
            EQUIPS_IMG = props.getProperty("EQUIPS_IMG", EQUIPS_IMG) ;
            LIVE_BEINGS_IMG = props.getProperty("LIVE_BEINGS_IMG", LIVE_BEINGS_IMG) ;
            MAPS_IMG = props.getProperty("MAPS_IMG", MAPS_IMG) ;
            MAP_ELEMENTS_IMG = props.getProperty("MAP_ELEMENTS_IMG", MAP_ELEMENTS_IMG) ;
            NPC_IMG = props.getProperty("NPC_IMG", NPC_IMG) ;
            OPENING_IMG = props.getProperty("OPENING_IMG", OPENING_IMG) ;            
            PLAYER_IMG = props.getProperty("PLAYER_IMG", PLAYER_IMG) ;
            PET_IMG = props.getProperty("PET_IMG", PET_IMG) ;
            SIDEBAR_IMG = props.getProperty("SIDEBAR_IMG", SIDEBAR_IMG) ;
            SKY_IMG = props.getProperty("SKY_IMG", SKY_IMG) ;
            SPELLS_IMG = props.getProperty("SPELLS_IMG", SPELLS_IMG) ;
            STATUS_IMG = props.getProperty("STATUS_IMG", STATUS_IMG) ;
            UI_IMG = props.getProperty("UI_IMG", UI_IMG) ;
            WINDOWS_IMG = props.getProperty("WINDOWS_IMG", WINDOWS_IMG) ;
        }
        catch (IOException e)
        {
            System.out.println("Erro ao carregar paths: " + e.getMessage()) ;
            e.printStackTrace() ;
        }
    }
}
