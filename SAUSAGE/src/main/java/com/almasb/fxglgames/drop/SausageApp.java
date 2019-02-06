/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */
// importere packagen fxglgames, der indeholder

package com.almasb.fxglgames.drop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

// NOTE: Her importeres FXGL med en række metoder, der bruges i spilled
import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Dette spil er baseret på AlmasB turtorial om Drop.
 *
 * Spilleren kan med tasterne w,s,d gå fra side til side og fange pølser
 * Der er ingen win/lose conditions
 *

 *
 * @author Rachid Moutiq baseret på Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */

/**
 * Gør at vores SausageApp nedarver GameApllication med syntaxen "extends".
 * GameApplication er en class, der er givet i GameApplication.class som er lavet op forhånd
 * Den indeholder en lang rækker metoder.
 */
public class SausageApp extends GameApplication {

    //variablerne med datatypen enum sættes under navnet droptype
    public enum DropType {
        SAUSAGE, HEAD
    }
//skaber entitien bucket
    private Entity bucket;

//Her implementeres metoden GameSettings som var del af GameApplication-klassen.
    @Override
    protected void initSettings(GameSettings settings) {
        //setTitle gør at man kan vælge en titel på sit spil
        settings.setTitle("SAUSAGE");
        //setVersion gør at versionen bliver vist
        settings.setVersion("1.0");
        //Settings.setWidth definerer bredden på den skærm som spillet popper op i
        settings.setWidth(480);
        //setHeight definere højden på spillet.
        settings.setHeight(800);
    }

    //Her implementeres metoden initInput som var del af GameApplication-klassen.
    @Override
    protected void initInput() {
        //Her defineres hvordan vores spand bevæger sig. Man kan også ændre det så man bruger andre knapper til at styre spanden
        // der her er et hoved.
        onKey(KeyCode.A, "Move Left", () -> bucket.translateX(-200 * tpf()));
        onKey(KeyCode.D, "Move Right", () -> bucket.translateX(200 * tpf()));
    }
    //Her implementeres metoden initGame igen fra gameApplication
    @Override
    protected void initGame() {
        //Den fortæller spillet at den skal lave en "bucket"
        bucket = spawnBucket();
// Her skabes der en "Droplet hvert sekund"
        run(() -> spawnDroplet(), Duration.seconds(1));
// Baggrundsmusikken loades, her er musikken fra drop udskiftet med en svedig basgang, der loopes
        loopBGM("bgm.mp3");
    }
//Metoden initPhysics implementeres
    @Override
    protected void initPhysics() {
        //metoden gør her at når  "Head" kolliderer med "Sausage", så forsvinder Sausage
        onCollisionBegin(DropType.HEAD, DropType.SAUSAGE, (bucket, droplet) -> {
            droplet.removeFromWorld();
//Lyden "drop.wav afspilles her efter.
            play("drop.wav");
        });
    }
//implementere onUpdate metoden, der bruges til at lave entiteter i spillet
    @Override
    protected void onUpdate(double tpf) {
        getGameWorld().getEntitiesByType(DropType.SAUSAGE ).forEach( droplet -> droplet.translateY(150 * tpf));
    }
//laver entitien Bucket, der får beskrevet hvor den skal spawnes
    private Entity spawnBucket() {
        return entityBuilder()
                .type(DropType.HEAD )
                .at(getAppWidth() / 2, getAppHeight() - 200)
                .viewWithBBox("bucket.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
    //laver entitien Droplet, der får beskrevet hvor den skal spawnes
    private Entity spawnDroplet() {
        return entityBuilder()
                .type(DropType.SAUSAGE )
                .at(FXGLMath.random(getAppWidth() - 64), 0)
                .viewWithBBox("droplet.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
//starter spillet
    public static void main(String[] args) {
        launch(args);
    }
}
