package com.github.sokobanJAVA.ecs.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.github.sokobanJAVA.ecs.components.PositionComponent;
import com.github.sokobanJAVA.ecs.components.SpriteComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.ashley.core.ComponentMapper;

public class RenderSystem extends IteratingSystem {
    private final SpriteBatch batch;
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    public RenderSystem(SpriteBatch batch) {
        super(Family.all(PositionComponent.class, SpriteComponent.class).get()); // 关注位置与纹理组件
                                                                                 // :contentReference[oaicite:16]{index=16}
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        SpriteComponent sp = sm.get(entity);
        sp.sprite.setPosition(pos.position.x, pos.position.y);
        batch.begin();
        sp.sprite.draw(batch); // 绘制纹理 :contentReference[oaicite:17]{index=17}
        batch.end();
    }
}
