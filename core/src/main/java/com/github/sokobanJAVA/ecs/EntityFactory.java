// core/src/main/java/com/github/sokobanJAVA/ecs/EntityFactory.java
package com.github.sokobanJAVA.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.sokobanJAVA.ecs.components.*;

public class EntityFactory {
    private final PooledEngine engine;

    public EntityFactory(PooledEngine engine) {
        this.engine = engine;
    }

    public Entity createBlock(float x, float y, Texture texture) {
        Entity entity = engine.createEntity();
        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.position.set(x, y);
        pos.width = texture.getWidth();
        pos.height = texture.getHeight();

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = new Sprite(texture);
        sprite.sprite.setPosition(x, y);

        entity.add(pos);
        entity.add(sprite);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createRectangle(float x, float y, Texture texture, float width, float height) {
        Entity entity = engine.createEntity();
        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.position.set(x, y);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        size.width = width;
        size.height = height;

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = new Sprite(texture);
        sprite.sprite.setSize(width, height);

        entity.add(pos);
        entity.add(size);
        entity.add(sprite);
        engine.addEntity(entity);
        return entity;
    }

    /**
     * Creates the player entity at given world coordinates.
     * Adds Position, Size, Sprite, Move, and Player components.
     */
    public Entity createPlayer(float x, float y, Texture texture, float width, float height) {
        Entity entity = engine.createEntity();

        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.position.set(x, y);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        size.width = width;
        size.height = height;

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = new Sprite(texture);
        sprite.sprite.setSize(width, height);
        sprite.sprite.setPosition(x, y);

        MoveComponent move = engine.createComponent(MoveComponent.class);
        move.direction.set(0, 0);

        PlayerComponent player = engine.createComponent(PlayerComponent.class);

        entity.add(pos);
        entity.add(size);
        entity.add(sprite);
        entity.add(move);
        entity.add(player);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createBox(float x, float y, Texture texture, float width, float height) {
        Entity entity = engine.createEntity();

        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.position.set(x, y);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        size.width = width;
        size.height = height;

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = new Sprite(texture);
        sprite.sprite.setSize(width, height);
        sprite.sprite.setPosition(x, y);

        BoxComponent box = engine.createComponent(BoxComponent.class);

        entity.add(pos);
        entity.add(size);
        entity.add(sprite);
        entity.add(box);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createWall(float x, float y, Texture texture, float width, float height) {
        Entity entity = engine.createEntity();

        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.position.set(x, y);

        SizeComponent size = engine.createComponent(SizeComponent.class);
        size.width = width;
        size.height = height;

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = new Sprite(texture);
        sprite.sprite.setSize(width, height);
        sprite.sprite.setPosition(x, y);

        WallComponent wall = engine.createComponent(WallComponent.class);

        entity.add(pos);
        entity.add(size);
        entity.add(sprite);
        entity.add(wall);
        engine.addEntity(entity);
        return entity;
    }
}
