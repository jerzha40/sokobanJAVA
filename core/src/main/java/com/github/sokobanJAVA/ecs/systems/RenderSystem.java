package com.github.sokobanJAVA.ecs.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.github.sokobanJAVA.ecs.components.PositionComponent;
import com.github.sokobanJAVA.ecs.components.SpriteComponent;
import com.github.sokobanJAVA.ecs.components.SizeComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class RenderSystem extends IteratingSystem {
    private final SpriteBatch batch;
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SizeComponent> sm = ComponentMapper.getFor(SizeComponent.class);
    private final ComponentMapper<SpriteComponent> spm = ComponentMapper.getFor(SpriteComponent.class);

    public RenderSystem(SpriteBatch batch) {
        super(Family.all(PositionComponent.class, SizeComponent.class, SpriteComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        SizeComponent size = sm.get(entity);
        SpriteComponent spriteC = spm.get(entity);
        Sprite sprite = spriteC.sprite;

        sprite.setSize(size.width, size.height); // 根据组件调整大小 :contentReference[oaicite:4]{index=4}
        sprite.setPosition(pos.position.x, pos.position.y);
        batch.begin();
        sprite.draw(batch); // SpriteBatch 渲染 :contentReference[oaicite:5]{index=5}
        batch.end();
    }
}
