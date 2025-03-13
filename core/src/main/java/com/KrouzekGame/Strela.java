package com.KrouzekGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;



public class Strela extends Image {

    boolean odHrace;
    Float angle;
    Rectangle hitbox;
    Strela(float x, float y , float angle, boolean odHrace){
        super(new Texture("bullet.jpg"));
        setSize(20,20);

        setPosition(x, y);
        hitbox = new Rectangle(x,y, getWidth(), getHeight());
        this.angle = angle;
        this.odHrace = odHrace;

        hitbox.setPosition(getX(), getY());
        setOrigin(getWidth()/2, getHeight()/2);
    }

    @Override
    public void act(float delta) {

        hitbox.setPosition(getX(), getY());

        float posunX = (float) (15*Math.cos(Math.toRadians(angle)));
        float posunY = (float) (15*Math.sin(Math.toRadians(angle)));
        moveBy(posunX,posunY);

    }
}
