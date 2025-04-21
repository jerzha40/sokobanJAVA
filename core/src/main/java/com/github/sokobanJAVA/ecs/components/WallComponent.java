// core/src/main/java/com/github/sokobanJAVA/ecs/components/WallComponent.java
package com.github.sokobanJAVA.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Marks an entity as an immovable wall.
 * Used by collision and movement systems to block movement.
 */
public class WallComponent implements Component {
    // No data needed; presence of this component is sufficient.
}