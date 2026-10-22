package main;

import org.json.simple.JSONObject;

import attributes.PersonalAttributes;

public class PlayerPreview
{
    private final String name ;
    private final int job ;
    private final int level ;
    private final PersonalAttributes personalAttributes ;

    private PlayerPreview(String name, int job, int level, PersonalAttributes personalAttributes)
    {
        this.name = name ;
        this.job = job ;
        this.level = level ;
        this.personalAttributes = personalAttributes ;
    }

    public static PlayerPreview fromJson(JSONObject jsonData)
    {
        return new PlayerPreview(
            (String) jsonData.get("name"),
            ((Number) jsonData.get("job")).intValue(),
            ((Number) jsonData.get("level")).intValue(),
            PersonalAttributes.fromJson((JSONObject) jsonData.get("PA"))
        ) ;
    }

    public String getName() { return name ;}
    public int getJob() { return job ;}
    public int getLevel() { return level ;}
    public PersonalAttributes getPA() { return personalAttributes ;}
}
