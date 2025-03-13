package com.KrouzekGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class NPC
    extends Image {
    Rectangle hitbox;

    int maximalniHp;
    int hp;

    Image healthBar;
    Image healthBarBackground;

    float hracX;
    float hracY;

    float speed = 300;
    float shootingTimer = 0;
    enum Stavy {AFK, PRONASLEDOVANI, STRILENI, UTEK}
    Stavy aktualniStav = Stavy.AFK;

    Main main;

    public NPC(int x, int y, Main main, int maximalniHp){
        super(new Texture("enemy.jpg"));
        setPosition(x, y);
        this.main = main;
        this.maximalniHp = maximalniHp;
        hp = maximalniHp;
        hitbox = new Rectangle(x, y, getWidth(), getHeight());

        healthBar = new Image(new Texture("bullet.jpg"));
        healthBarBackground = new Image(new Texture("brick.jpg"));

        healthBar.setSize(100,20);
        healthBarBackground.setSize(100,20);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        vyhodnotChovani(delta);
        hitbox.setPosition(getX(), getY());

        if (hp <= 0){
            remove();
            main.npccka.remove(this);

            healthBar.remove();
            healthBarBackground.remove();
        }

        healthBar.setPosition(getX()+100, getY()+250);
        healthBarBackground.setPosition(getX()+100, getY()+250);

        healthBar.setWidth(hp*100/maximalniHp);
    }

    public void vyhodnotChovani(float delta){
        float vzdalenost = getPlayerDistance();

        if(aktualniStav == Stavy.AFK){
            if(vzdalenost<1000){
                aktualniStav = Stavy.PRONASLEDOVANI;
            }
        } else if (aktualniStav == Stavy.PRONASLEDOVANI) {
            Vector2 pohyb = posunKeHraci(delta);
            moveBy(pohyb.x, pohyb.y);

            if(vzdalenost>1000){
                aktualniStav = Stavy.AFK;
            }

            if(vzdalenost<500){
                aktualniStav = Stavy.STRILENI;
            }
        } else if (aktualniStav == Stavy.STRILENI) {
            shootingTimer += delta;
            if(shootingTimer>0.5){
                shootingTimer = 0;
                main.spawniStrelu(false, getX()+getWidth()/2, getY()+getHeight()/2, 180+(float) Math.toDegrees(natoceniNaHrace()));
            }

            if(vzdalenost>500){
                aktualniStav = Stavy.PRONASLEDOVANI;
            }
        }
    }

    public Vector2 posunKeHraci(float delta){
        float rozdilX = getX()-hracX;
        float rozdilY = getY()-hracY;

        float uhel = (float) Math.atan2(rozdilY, rozdilX);
        float posunX = -(float) (Math.cos(uhel)*speed)*delta;
        float posunY = -(float) (Math.sin(uhel)*speed)*delta;

        Vector2 posun = new Vector2(posunX, posunY);
        return posun;
    }

    public float getPlayerDistance(){
        float rozdilX = getX()-hracX;
        float rozdilY = getY()-hracY;

        float vzdalenost = (float) Math.sqrt(rozdilX*rozdilX + rozdilY*rozdilY);
        return vzdalenost;
    }

    public float natoceniNaHrace(){
        float rozdilX = getX()+getWidth()/2-hracX;
        float rozdilY = getY()+getHeight()/2-hracY;

        float uhel = (float) Math.atan2(rozdilY, rozdilX);
        return uhel;
    }
}
