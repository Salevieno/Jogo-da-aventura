package maps;

import java.awt.Color;

import main.Palette;

public enum GroundType
{
    wall(Palette.colors[2]),
    water(Palette.colors[20]),
    ice(Palette.colors[1]),
    lava(Palette.colors[7]),
    invisibleWall(null),
    walkingPath(Palette.colors[10]) ;

    private final Color color ;

    GroundType(Color color)
	{
        this.color = color ;
    }

    public Color getColor() { return color ;}
}