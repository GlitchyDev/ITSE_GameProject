package GameInfo.Environment.Entities.AbstractClasses;

import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;

/**
 * This class aims to
 * - Create Entities with a common "Take Damage" method, so dealing damage can be done globally
 */
public abstract class DamageableEntityBase extends EntityBase {
    protected int maxHealth = 0;
    protected int currentHealth = 0;
    protected boolean isDead = false;


    public DamageableEntityBase(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
    }
    /**
     * Deal damage to the Entity, no causer specified
     * @param damageType The type of damage Dealt
     * @param damageAmount The amount of Damage Dealt
     */
    public abstract void takeDamage(DamageType damageType, int damageAmount);
    /**
     * Deal damage to the Entity, no causer specified
     * @param damageType The type of damage Dealt
     * @param damageAmount The amount of Damage Dealt
     */
    public abstract void takeDamage(EntityBase causer, DamageType damageType, int damageAmount);
}
