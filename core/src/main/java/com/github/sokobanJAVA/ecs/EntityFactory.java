package com.github.sokobanJAVA.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.github.sokobanJAVA.ecs.components.PositionComponent;
import com.github.sokobanJAVA.ecs.components.SpriteComponent;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;

public class EntityFactory {
    private final PooledEngine engine;

    public EntityFactory(PooledEngine engine) {
        this.engine = engine;
    }

    public Entity createBlock(float x, float y, Texture texture) {
        Entity entity = engine.createEntity(); // 创建实体 :contentReference[oaicite:12]{index=12}
        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.position.set(x, y);
        pos.width = texture.getWidth();
        pos.height = texture.getHeight();

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = new Sprite(texture);
        sprite.sprite.setPosition(x, y);

        entity.add(pos); // 添加组件 :contentReference[oaicite:13]{index=13}
        entity.add(sprite);
        engine.addEntity(entity); // 注册到引擎 :contentReference[oaicite:14]{index=14}
        return entity;
    }
}
