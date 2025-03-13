package com.KrouzekGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Prekazka
    extends Image {

    Rectangle hitbox;

    Prekazka(int x, int y, int sirka, int vyska){
        super(new Texture("brick.jpg"));
        hitbox = new Rectangle(x, y, sirka, vyska);

        setPosition(x, y);
        setSize(sirka, vyska);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        hitbox.setPosition(getX(), getY());
    }
}
