package windows;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import UI.GameButton;
import UI.GameTextButton;
import animations.MessageAnimation;
import graphics.Align;
import items.Item;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Log;
import main.Palette;
import main.Path;
import shared.SharedImages;
import utilities.Util;

public class ShopBag
{
    private final Point topLeftPos ;
    private List<Item> itemsForSale ;
    private List<Point> slotsCenter ;
    private List<Point> textsLeftCenter ;
    private int qtdItemsOnDisplay ;
    private int totalPrice ;
    private Color priceColor ;
    private final Point coinPos ;
    private final Point pricePos ;
    private final GameButton buyButton ;
    private final GameButton cancelButton ;
    
    private static final Dimension MARGIN = new Dimension(16, 44) ;
    private static final int SPACING_Y = 24 ;
    private static final int QTD_ITEMS_ON_WINDOW = 7 ;
    private static final Image SHOP_BAG_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "ShopBag.png") ;
    private static final Dimension BAG_SIZE = Util.getSize(SHOP_BAG_IMAGE) ;
    
    public ShopBag(Point topLeftPos)
    {
        this.topLeftPos = topLeftPos ;
        this.slotsCenter = new ArrayList<>() ;
        this.textsLeftCenter = new ArrayList<>() ;
        this.itemsForSale = new ArrayList<>() ;
        
        for (int i = 0 ; i <= QTD_ITEMS_ON_WINDOW - 1 ; i += 1)
        {
            Point slotCenter = Util.translate(topLeftPos, MARGIN.width, MARGIN.height + SPACING_Y * i) ;
            this.slotsCenter.add(slotCenter) ;
            this.textsLeftCenter.add(Util.translate(slotCenter, 14, 0)) ;
        }

        this.qtdItemsOnDisplay = 0 ;
        this.totalPrice = 0 ;
        this.coinPos = Util.translate(topLeftPos, 10, SHOP_BAG_IMAGE.getHeight(null) - 14) ;
        this.pricePos = Util.translate(coinPos, SharedImages.getCoinImg().getWidth(null) / 2 + 4, 0) ;
        this.priceColor = Palette.colors[12] ;

        this.buyButton = new GameTextButton(Util.translate(topLeftPos, 80, BAG_SIZE.height), Align.center, "Buy", "Buy", () -> { buyItems() ;}) ;
        this.cancelButton = new GameTextButton(Util.translate(topLeftPos, 180, BAG_SIZE.height), Align.center, "Cancel", "Cancel", () -> { cancelItems() ;}) ;
        this.buyButton.deactivate() ;
        this.cancelButton.deactivate() ;
    }

    protected void open()
    {
        buyButton.activate() ;
        cancelButton.activate() ;
    }

    public void addItem(Item item)
    {
        if (item == null) { Log.warn("Trying to add null item to shop bag") ; return ;}
        if (QTD_ITEMS_ON_WINDOW <= itemsForSale.size())
        {
            MessageAnimation.start(Util.translate(topLeftPos, BAG_SIZE.width / 2, -30), "Sacola cheia", Palette.colors[0]) ;
            return ;
        }

        itemsForSale.add(item) ;
        qtdItemsOnDisplay = Math.min(itemsForSale.size(), QTD_ITEMS_ON_WINDOW) ;
        totalPrice += item.getPrice() ;
        if (totalPrice <= Game.getPlayer().getBag().getGold())
        {
            buyButton.activate() ;
            priceColor = Palette.colors[12] ;
        }
        else
        {
            buyButton.deactivate() ;
            priceColor = Palette.colors[6] ;
        }
    }

    private void buyItems()
    {
        if (itemsForSale.isEmpty()) { return ;}

        BagWindow bag = Game.getPlayer().getBag() ;
		itemsForSale.forEach(item -> bag.add(item, 1));
		bag.removeGold(totalPrice) ;
        MessageAnimation.start(Util.translate(topLeftPos, BAG_SIZE.width / 2, -30), "Itens comprados", Palette.colors[0]) ;
        clear() ;
    }

    private void cancelItems()
    {
        clear() ;
    }

    private void clear()
    {
        itemsForSale.clear() ;
        qtdItemsOnDisplay = 0 ;
        totalPrice = 0 ;
        priceColor = Palette.colors[12] ;
        buyButton.activate() ;
    }

    protected void display()
    {
        GamePanel.getDP().drawImage(SHOP_BAG_IMAGE, topLeftPos, Align.topLeft) ;

        for (int i = 0 ; i <= qtdItemsOnDisplay - 1 ; i += 1)
        {
            Item item = itemsForSale.get(i) ;
            item.displayInSlot(slotsCenter.get(i)) ;
            GamePanel.getDP().drawText(textsLeftCenter.get(i), Align.centerLeft, item.getName(), Palette.colors[0]);
        }

        GamePanel.getDP().drawImage(SharedImages.getCoinImg(), coinPos, Align.center) ;
        GamePanel.getDP().drawText(pricePos, Align.centerLeft, String.valueOf(totalPrice), priceColor);
        buyButton.display(true, GamePanel.getMousePos()) ;
        cancelButton.display(true, GamePanel.getMousePos()) ;
    }
}
