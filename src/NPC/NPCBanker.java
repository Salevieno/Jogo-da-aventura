package NPC;

import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;
import windows.BankAction;
import windows.BankWindow;

public class NPCBanker extends NPC
{

    public NPCBanker(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.banker, name, pos, menus);
        window = new BankWindow() ;
    }
    
	public void act(Player player, Pet pet, String action)
	{
		if (action == null) { return ;}		
		if (!actionIsForward(action)) { return ;}
		if (currentMenuID != 0) { return ;}

		switch (selOption)
		{
			case 0: ((BankWindow) window).setMode(BankAction.deposit) ; player.switchOpenClose(window) ; return ;
			case 1: ((BankWindow) window).setMode(BankAction.withdraw) ; player.switchOpenClose(window) ; return ;
			case 2: ((BankWindow) window).setMode(BankAction.investmentLowRisk) ; player.switchOpenClose(window) ; return ;
			case 3: ((BankWindow) window).setMode(BankAction.investmentHighRisk) ; player.switchOpenClose(window) ; return ;
			default: return ;
		}
    }
}
