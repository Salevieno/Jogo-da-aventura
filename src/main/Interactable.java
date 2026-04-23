package main;

import java.awt.Image;
import java.awt.Point;

import components.Hitbox;

public interface Interactable
{
    Point getPos() ;
    Image getImage() ;
    Hitbox getHitbox() ;
}
