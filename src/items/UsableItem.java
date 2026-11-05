package items;

import liveBeings.LiveBeing;

public interface UsableItem
{
    public boolean canUse(LiveBeing target) ;
    public void use(LiveBeing target) ;
}
