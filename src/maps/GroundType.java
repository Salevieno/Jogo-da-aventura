package maps;

import java.awt.Color;

import main.Game;

public enum GroundType
{
    wall(Game.palette[2]),
    water(Game.palette[20]),
    ice(Game.palette[1]),
    lava(Game.palette[7]),
    invisibleWall(null),
    walkingPath(Game.palette[10]) ;

    private final Color color ;

    GroundType(Color color)
	{
        this.color = color ;
    }

    public Color getColor() { return color ;}
}