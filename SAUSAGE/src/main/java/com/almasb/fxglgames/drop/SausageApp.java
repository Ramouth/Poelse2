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

//Starter
public class SausageApp extends GameApplication {

    /**
     *
     */
    public enum DropType {
        SAUSAGE, HEAD
    }

    private Entity bucket;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("SAUSAGE");
        settings.setVersion("1.0");
        settings.setWidth(480);
        settings.setHeight(800);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.A, "Move Left", () -> bucket.translateX(-200 * tpf()));
        onKey(KeyCode.D, "Move Right", () -> bucket.translateX(200 * tpf()));
    }

    @Override
    protected void initGame() {
        bucket = spawnBucket();

        run(() -> spawnDroplet(), Duration.seconds(1));

        loopBGM("bgm.mp3");
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(DropType.HEAD, DropType.SAUSAGE, (bucket, droplet) -> {
            droplet.removeFromWorld();

            play("drop.wav");
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        getGameWorld().getEntitiesByType(DropType.SAUSAGE ).forEach( droplet -> droplet.translateY(150 * tpf));
    }

    private Entity spawnBucket() {
        return entityBuilder()
                .type(DropType.HEAD )
                .at(getAppWidth() / 2, getAppHeight() - 200)
                .viewWithBBox("bucket.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private Entity spawnDroplet() {
        return entityBuilder()
                .type(DropType.SAUSAGE )
                .at(FXGLMath.random(getAppWidth() - 64), 0)
                .viewWithBBox("droplet.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
