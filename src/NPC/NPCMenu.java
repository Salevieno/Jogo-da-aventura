package NPC;

import java.util.List;

import main.Log;

public class NPCMenu
{
	private String speech ;
	private List<String> options ;

    protected NPCMenu(String speech, List<String> options)
    {
        if (speech == null) { Log.error("Trying to create NPCMenu with null speech") ;}

        this.speech = speech ;
        this.options = options ;
    }

    public String getSpeech() { return speech ;}

    public List<String> getOptions() { return options ;}

    protected void setSpeech(String speech) { this.speech = speech ;}

    protected void setOptionsText(List<String> options)
    {
        if (options == null || this.options == null) { Log.warn("Opções nulas ao tentar setar opções de menu de NPC. Recebeu " + options + " e possuia " + this.options) ; return ;}
        if (options.size() != this.options.size()) { Log.warn("Qtd. de opções inválida ao tentar setar opções de menu de NPC. Recebeu " + options.size() + " e esperava " + this.options.size()) ; return ;}
    
        for (int i = 0 ; i <= options.size() - 1 ; i += 1)
        {
            this.options.set(i, options.get(i)) ;
        }
    }
}
