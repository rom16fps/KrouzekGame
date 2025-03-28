package com.KrouzekGame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import javax.swing.*;
import java.util.ArrayList;

public class Main extends ApplicationAdapter {

    int stavHry = 0;
    int vybranySkin = 0;

    Image startTlacitko;
    Image skin1;
    Image skin2;
    Image chooseSkinButton;
    Image pozadi;
    Texture text;
    Image postavicka;
    Image healthBar;
    Image healthBarBackground;
    int hpHrace = 100;
    int damageHrace = 50;
    Rectangle hitboxHrace;
    ArrayList<Prekazka> prekazky = new ArrayList<Prekazka>();
    String vybranyCustomSkin;

    Table uiTable;

    Stage stage;
    Stage menuStage;
    Stage endStage;

    Image winScreen;
    Image loseScreen;

    ArrayList<NPC> npccka = new ArrayList<NPC>();
    Float otoceniHrace;
    ArrayList<Strela> strely;
    boolean byloNPCckoHitnuto = false;
    int npcTimer = 0;
    Sound zvuk;

    JFileChooser fileChooser = new JFileChooser();

    @Override
    public void create () {
        uiTable = new Table();

        zvuk = Gdx.audio.newSound(Gdx.files.internal("gunshot-short.mp3"));

        text = new Texture("badlogic.jpg");
        strely = new ArrayList<>();
        postavicka = new Image(text);
        postavicka.setSize(200, 200);

        healthBar = new Image(new Texture("bullet.jpg"));
        healthBarBackground = new Image(new Texture("brick.jpg"));

        healthBar.setSize(100,20);
        healthBarBackground.setSize(100,20);
       /* prekazky.add(new Prekazka(1000, 200, 50,50));
        prekazky.add(new Prekazka(500, 600, 50,50));
        prekazky.add(new Prekazka(500, 200, 50,50));*/
        hitboxHrace = new Rectangle(postavicka.getX(), postavicka.getY(), postavicka.getWidth(), postavicka.getHeight());

        stage = new Stage();
        stage.setViewport(new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.getViewport().setCamera(new OrthographicCamera());
        stage.getCamera().position.x = postavicka.getX()+postavicka.getWidth()/2;
        stage.getCamera().position.y = postavicka.getY()+postavicka.getHeight()/2;
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().apply();


        stage.addActor(postavicka);
        npccka.add(new NPC(1000,500, this, 100));
        npccka.add(new NPC(0,0, this, 300));
        npccka.add(new NPC(0,500, this, 50));

        for(Prekazka p : prekazky){
            stage.addActor(p);
        }

        for(NPC n : npccka){
            stage.addActor(n);
            stage.addActor(n.healthBarBackground);
            stage.addActor(n.healthBar);
        }

        pozadi = new Image(new Texture("bg.jpg"));
        pozadi.setScale(2);
        pozadi.setPosition(-1000, -500);
        startTlacitko = new Image(new Texture("start_button.jpg"));
        skin1 = new Image(new Texture("skin1.jpg"));
        skin2 = new Image(new Texture("skin2.jpg"));
        Image chooseSkinButton = new Image(new Texture("skinChoose.jpg"));

        uiTable.add(startTlacitko).colspan(2).pad(20);
        uiTable.row();
        uiTable.add(skin1).pad(20);
        uiTable.add(skin2).pad(20);
        uiTable.row();
        uiTable.add(chooseSkinButton).pad(20).colspan(2);

        uiTable.pack();

        menuStage = new Stage();
        menuStage.setViewport(new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        menuStage.getViewport().setCamera(new OrthographicCamera());
        menuStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuStage.getViewport().apply();

        menuStage.getCamera().position.x = uiTable.getWidth()/2;
        menuStage.getCamera().position.y = uiTable.getHeight()/2;

        chooseSkinButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                fileChooser.showOpenDialog(null);
                vybranyCustomSkin = fileChooser.getSelectedFile().getAbsolutePath();
                postavicka.setDrawable(new TextureRegionDrawable(new Texture(Gdx.files.absolute(vybranyCustomSkin))));
            }
        });

        startTlacitko.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("START BUTTON STISKNUTO");
                stavHry = 1;

                if(vybranySkin == 1){
                    postavicka.setDrawable(new TextureRegionDrawable(new Texture("skin1.jpg")));
                }

                if(vybranySkin == 2){
                    postavicka.setDrawable(new TextureRegionDrawable(new Texture("skin2.jpg")));
                }
            }
        });
        skin1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                vybranySkin = 1;
            }
        });
        skin2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                vybranySkin = 2;
            }
        });

        menuStage.addActor(pozadi);
        menuStage.addActor(uiTable);

        menuStage.addActor(healthBar);
        menuStage.addActor(healthBarBackground);

        endStage = new Stage();
        loseScreen = new Image(new Texture("losescreen.png"));
        winScreen = new Image(new Texture("winscreen.png"));
        endStage.setViewport(new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        endStage.getViewport().setCamera(new OrthographicCamera());
        endStage.getCamera().position.x = winScreen.getWidth()/2;
        endStage.getCamera().position.y = winScreen.getHeight()/2;
        endStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        endStage.getViewport().apply();
        endStage.addActor(winScreen);
        endStage.addActor(loseScreen);

        postavicka.setOrigin(postavicka.getWidth()/2, postavicka.getHeight()/2);
    }

    @Override
    public void render () {
        ScreenUtils.clear(1, 1, 1, 0);

        if(stavHry == 0){
            Gdx.input.setInputProcessor(menuStage);

            menuStage.act();
            menuStage.draw();
            menuStage.getViewport().apply();
        }
        else if (stavHry == 1){
            Gdx.input.setInputProcessor(stage);
            for (NPC n : npccka){
                n.hracX = postavicka.getX()+postavicka.getWidth()/2;
                n.hracY = postavicka.getY()+postavicka.getHeight()/2;
            }

            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                postavicka.moveBy(0,10);
                stage.getCamera().translate(0, 10,0 );
                hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                if(!muzeHracProjit()){
                    postavicka.moveBy(0,-10);
                    stage.getCamera().translate(0, -10,0 );
                    hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                postavicka.moveBy(0,-10);
                stage.getCamera().translate(0, -10,0 );
                hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                if(!muzeHracProjit()){
                    postavicka.moveBy(0,10);
                    stage.getCamera().translate(0, 10,0 );
                    hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                postavicka.moveBy(-10,0);
                stage.getCamera().translate(-10, 0,0 );
                hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                if(!muzeHracProjit()){
                    postavicka.moveBy(10,0);
                    stage.getCamera().translate(10, 0,0 );
                    hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                postavicka.moveBy(10,0);
                stage.getCamera().translate(10, 0,0 );
                hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                if(!muzeHracProjit()){
                    postavicka.moveBy(-10,0);
                    stage.getCamera().translate(-10, 0,0 );
                    hitboxHrace.setPosition(postavicka.getX(), postavicka.getY());
                }
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                spawniStrelu(true, postavicka.getX(), postavicka.getY(),otoceniHrace);
            }

            healthBar.setPosition(postavicka.getX()+100, postavicka.getY()+250);
            healthBarBackground.setPosition(postavicka.getX()+100, postavicka.getY()+250);

            otocHraceZaMysi();
            zkontrolujKolize();

            stage.act();
            stage.draw();
            stage.getViewport().apply();

            if (npccka.isEmpty()){
                stavHry = 2;
            }
        }else if (stavHry == 2){
            loseScreen.setVisible(false);
            Gdx.input.setInputProcessor(endStage);

            endStage.act();
            endStage.draw();
        }

    }

    @Override
    public void dispose () {

    }

    void spawniStrelu(boolean odHrace, float x, float y, float rotation){
        Strela strela = new Strela(x, y, rotation, odHrace);
        stage.addActor(strela);
        strely.add(strela);
        zvuk.play();
    }

    void zkontrolujKolize(){
        ArrayList<Strela> strelyKeSmazani = new ArrayList<>();

        for(Strela s : strely){
            for(Prekazka p : prekazky){
                if(s.hitbox.overlaps(p.hitbox)){
                    System.out.println("HIT");
                    s.remove();
                    strelyKeSmazani.add(s);
                }
            }

            for(NPC n : npccka){
                if(s.hitbox.overlaps(n.hitbox)&&s.odHrace){
                    System.out.println("HIT");
                    s.remove();
                    strelyKeSmazani.add(s);
                    n.hp -= damageHrace;
                }
            }

            if(s.hitbox.overlaps(hitboxHrace)&&!s.odHrace){
                s.remove();
                strelyKeSmazani.add(s);
            }
        }

        strely.removeAll(strelyKeSmazani);
        strelyKeSmazani = null;
    }

    boolean muzeHracProjit(){

        for(Prekazka p : prekazky){
            if(hitboxHrace.overlaps(p.hitbox)){
                return false;
            }
        }

        return true;
    }

    void otocHraceZaMysi(){
        float mysX = Gdx.input.getX()+stage.getCamera().position.x-Gdx.graphics.getWidth()/2;
        float mysY = Gdx.graphics.getHeight()-Gdx.input.getY()+stage.getCamera().position.y-Gdx.graphics.getHeight()/2;

        float hracX = postavicka.getX() + postavicka.getOriginX();
        float hracY = postavicka.getY() + postavicka.getOriginY();

        float angle =  180+(float)Math.toDegrees((float) Math.atan2(hracY-mysY, hracX-mysX));
        otoceniHrace = angle;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height);
        menuStage.getViewport().update(width, height);
        endStage.getViewport().update(width, height);
    }
}
