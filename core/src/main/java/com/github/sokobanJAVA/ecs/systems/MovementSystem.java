package com.github.sokobanJAVA.ecs.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.github.sokobanJAVA.ecs.components.MoveComponent;
import com.github.sokobanJAVA.ecs.components.PositionComponent;
import com.github.sokobanJAVA.ecs.components.BoxComponent;
import com.github.sokobanJAVA.ecs.components.WallComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Engine;

public class MovementSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MoveComponent> mm = ComponentMapper.getFor(MoveComponent.class);
    private ComponentMapper<BoxComponent> bm = ComponentMapper.getFor(BoxComponent.class);
    private ComponentMapper<WallComponent> wm = ComponentMapper.getFor(WallComponent.class);

    private Engine engine;
    private float tileSize;

    public MovementSystem(Engine engine, float tileSize) {
        super(Family.all(PositionComponent.class, MoveComponent.class).get());
        this.engine = engine;
        this.tileSize = tileSize;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoveComponent move = mm.get(entity);
        Vector2 dir = move.direction;
        if (dir.isZero())
            return; // 没有移动意图

        PositionComponent pos = pm.get(entity);
        float newX = pos.position.x + dir.x * tileSize;
        float newY = pos.position.y + dir.y * tileSize;

        // 检查目标格
        Entity blocker = findBlockingEntity(newX, newY);
        if (blocker == null) {
            // 空地，直接移动
            pos.position.x = newX;
            pos.position.y = newY;
        } else if (bm.has(blocker)) {
            // 是箱子，尝试推动
            if (tryPush(blocker, dir)) {
                pos.position.x = newX;
                pos.position.y = newY;
            }
        }
        // 移动完成后重置方向
        move.reset();
    }

    /** 查找某个格子是否有墙或箱子 */
    private Entity findBlockingEntity(float x, float y) {
        for (Entity e : engine.getEntitiesFor(Family.one(WallComponent.class, BoxComponent.class).get())) {
            PositionComponent p = pm.get(e);
            if (p.position.x == x && p.position.y == y)
                return e;
        }
        return null;
    }

    /** 递归尝试推动箱子 */
    private boolean tryPush(Entity box, Vector2 dir) {
        PositionComponent bpos = pm.get(box);
        float bx = bpos.position.x + dir.x * tileSize;
        float by = bpos.position.y + dir.y * tileSize;
        // 前面要空置或可继续推动
        Entity next = findBlockingEntity(bx, by);
        if (next == null) {
            // 推动箱子
            bpos.position.x = bx;
            bpos.position.y = by;
            return true;
        }
        return false;
    }
}
