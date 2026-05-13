package NPC;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.GameTextButton;
import main.Log;

public class NPCMenu
{
	private String speech ;
    private Map<String, Integer> optionDestination ;
	private List<GameTextButton> options ;
	private final List<Integer> destination ;

    protected NPCMenu(Point pos, List<Integer> destination, String speech, List<GameTextButton> options)
    {
        if (speech == null) { Log.error("Trying to create NPCMenu with null speech") ;}
        if (destination.size() != options.size()) { Log.error("Trying to create NPCMenu with destination size = " + destination.size() + " and options size = " + options.size()) ;}

        this.speech = speech ;
        this.options = options ;
        this.destination = destination ;
        this.optionDestination = new HashMap<>() ;
        for (int i = 0 ; i <= destination.size() - 1 ; i += 1)
        {
            this.optionDestination.put(options.get(i).getText(), destination.get(i)) ;
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

    public List<GameTextButton> getOptions() { return options ;}

    protected void setSpeech(String speech) { this.speech = speech ;}

    protected void setOptionsText(List<String> options)
    {
        if (options.size() != this.options.size()) { Log.warn("Qtd. de opções inválida ao tentar setar opções de menu de NPC. Recebeu " + options.size() + " e esperava " + this.options.size()) ; return ;}
    
        for (int i = 0 ; i <= options.size() ; i += 1)
        {
            this.options.get(i).setText(options.get(i)) ;
        }
    }
}
