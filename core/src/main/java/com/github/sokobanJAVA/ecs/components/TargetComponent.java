// core/src/main/java/com/github/sokobanJAVA/ecs/components/TargetComponent.java
package com.github.sokobanJAVA.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Marks an entity as a target location for boxes.
 * GoalCheckSystem will verify boxes are placed on these tiles.
 */
public class TargetComponent implements Component {
    // Could store identifier or color for different targets.
}