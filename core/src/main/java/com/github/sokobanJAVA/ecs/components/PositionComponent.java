package com.github.sokobanJAVA.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public final class PositionComponent implements Component {
    public final Vector2 position = new Vector2();
    public float width, height;
}
