package com.github.sokobanJAVA.ecs.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.github.sokobanJAVA.ecs.components.MoveComponent;
import com.github.sokobanJAVA.ecs.components.PlayerComponent;
import com.badlogic.ashley.core.ComponentMapper;

public class InputSystem extends IteratingSystem {
    private ComponentMapper<MoveComponent> mm = ComponentMapper.getFor(MoveComponent.class);
    private Vector2 uiDir = new Vector2();

    public InputSystem() {
        super(Family.all(PlayerComponent.class, MoveComponent.class).get());
    }

    public void injectDirection(float x, float y) {
        uiDir.set(x, y);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoveComponent move = mm.get(entity);
        move.reset();
        if (!uiDir.isZero()) {
            move.direction.set(uiDir);
            uiDir.setZero();
            return;
        }

        Vector2 dir = move.direction;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            dir.set(0, 1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            dir.set(0, -1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dir.set(-1, 0);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dir.set(1, 0);
        }
    }
}
