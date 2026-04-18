package NPC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Log;

public class NPCMenu
{
	private String speech ;
    private Map<String, Integer> optionDestination ;
	private List<String> options ;
	private final List<Integer> destination ;

    protected NPCMenu(List<Integer> destination, String speech, List<String> options)
    {
        if (speech == null) { Log.error("Trying to create NPCMenu with null speech") ;}
        if (destination.size() != options.size()) { Log.error("Trying to create NPCMenu with destination size = " + destination.size() + " and options size = " + options.size()) ;}

        this.speech = speech ;
        this.options = options ;
        this.destination = destination ;
        this.optionDestination = new HashMap<>() ;
        for (int i = 0 ; i <= destination.size() - 1 ; i += 1)
        {
            this.optionDestination.put(options.get(i), destination.get(i)) ;
        }
    }

    protected void updateOptionDestination()
    {
        for (int i = 0 ; i <= destination.size() - 1 ; i += 1)
        {
            this.optionDestination.put(options.get(i), destination.get(i)) ;
        }
    }

    public String getSpeech() { return speech ;}

    public List<String> getOptions() { return options ;}

    public List<Integer> getDestination() { return destination ;}    

    protected void setSpeech(String speech) { this.speech = speech ;}

    protected void setOptionDestination(Map<String, Integer> optionDestination) { this.optionDestination = optionDestination ;}

    protected void setOptions(List<String> options) { this.options = options ;}

    public Map<String, Integer> getOptionDestination() { return optionDestination ;}
}
