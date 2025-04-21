package com.github.sokobanJAVA.ecs.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.utils.ImmutableArray;
import com.github.sokobanJAVA.ecs.components.PositionComponent;
import com.github.sokobanJAVA.ecs.components.WallComponent;
import com.github.sokobanJAVA.ecs.components.BoxComponent;
import com.github.sokobanJAVA.ecs.components.BlockedComponent;
import java.util.HashMap;
import java.util.Map;

/**
 * 每帧扫描所有墙和箱子实体，维护格子映射。
 * MovementSystem 和 GoalCheckSystem 可通过此系统查询阻挡或目标状态。
 */
public class CollisionSystem extends EntitySystem {
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final PooledEngine engine;
    private final Map<Long, Entity> occupied = new HashMap<>();

    public CollisionSystem(PooledEngine engine) {
        super(5);
        this.engine = engine;
        // 设置系统优先级，高于 MovementSystem
        // setPriority(5);
    }

    @Override
    public void update(float deltaTime) {
        occupied.clear();
        // 收集所有墙、箱子和显式阻塞
        ImmutableArray<Entity> blockers = engine.getEntitiesFor(
                Family.one(WallComponent.class, BoxComponent.class, BlockedComponent.class).get());
        for (Entity e : blockers) {
            PositionComponent p = pm.get(e);
            // 将 (x,y) 转成唯一 key：格子坐标*1e4+另一个
            long key = (((long) p.position.x) << 32) | (int) p.position.y;
            occupied.put(key, e);
        }
    }

    /**
     * 判断给定格子 (x,y) 是否被阻挡。
     */
    public boolean isBlocked(float x, float y) {
        long key = (((long) x) << 32) | (int) y;
        return occupied.containsKey(key);
    }

    /**
     * 返回阻挡该格子的实体，否则返回 null。
     */
    public Entity getBlocker(float x, float y) {
        long key = (((long) x) << 32) | (int) y;
        return occupied.get(key);
    }
}
