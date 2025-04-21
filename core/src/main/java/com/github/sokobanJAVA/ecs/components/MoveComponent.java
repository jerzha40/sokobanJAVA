// core/src/main/java/com/github/sokobanJAVA/ecs/components/MoveComponent.java
package com.github.sokobanJAVA.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Stores desired movement direction or velocity.
 * dx, dy typically -1, 0 or +1 indicating tile-based movement direction.
 */
public class MoveComponent implements Component {
    public Vector2 direction = new Vector2(0, 0);

    /**
     * Reset movement intent to zero.
     */
    public void reset() {
        direction.set(0, 0);
    }
}