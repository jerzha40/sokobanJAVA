// core/src/main/java/com/github/sokobanJAVA/ecs/components/BlockedComponent.java
package com.github.sokobanJAVA.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Marks an entity or tile as blocked, preventing movement through it.
 * Useful for dynamic obstacles or temporary blocks.
 */
public class BlockedComponent implements Component {
    // Could include duration or conditions for unblocking.
}