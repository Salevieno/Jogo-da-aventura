package NPC;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.OptionBox;
import main.Log;

public class NPCMenu
{
	private String speech ;
    private Map<String, Integer> optionDestination ;
	private List<OptionBox> options ;
	private final List<Integer> destination ;

    protected NPCMenu(Point pos, List<Integer> destination, String speech, List<String> optionsText)
    {
        if (speech == null) { Log.error("Trying to create NPCMenu with null speech") ;}
        if (destination.size() != optionsText.size()) { Log.error("Trying to create NPCMenu with destination size = " + destination.size() + " and options size = " + optionsText.size()) ;}

        this.speech = speech ;
        this.options = new ArrayList<>() ;
        optionsText.forEach(opText -> this.options.add(new OptionBox(pos, NPC.stdfont, opText)));
        this.destination = destination ;
        this.optionDestination = new HashMap<>() ;
        for (int i = 0 ; i <= destination.size() - 1 ; i += 1)
        {
            this.optionDestination.put(optionsText.get(i), destination.get(i)) ;
        }
    }

    protected void updateOptionDestination()
    {
        for (int i = 0 ; i <= destination.size() - 1 ; i += 1)
        {
            this.optionDestination.put(options.get(i).getText(), destination.get(i)) ;
        }
    }

    public String getSpeech() { return speech ;}

    public List<OptionBox> getOptions() { return options ;}

    public List<Integer> getDestination() { return destination ;}    

    protected void setSpeech(String speech) { this.speech = speech ;}

    protected void setOptionDestination(Map<String, Integer> optionDestination) { this.optionDestination = optionDestination ;}

    protected void setOptionsText(List<String> options)
    {
        if (options.size() != this.options.size()) { Log.warn("Qtd. de opções inválida ao tentar setar opções de menu de NPC. Recebeu " + options.size() + " e esperava " + this.options.size()) ; return ;}
    
        for (int i = 0 ; i <= options.size() ; i += 1)
        {
            this.options.get(i).setText(options.get(i)) ;
        }
    }

    public Map<String, Integer> getOptionDestination() { return optionDestination ;}
}
