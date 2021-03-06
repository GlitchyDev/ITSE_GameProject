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

    public DamageableEntityBase(World world, GlobalGameData globalGameData, int x, int y)
    {
        super(world,globalGameData,x,y);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    public boolean isDead() {
        return isDead;
    }

    /**
     * Deal damage to the Entity, no causer specified
     * @param damageType The type of damage Dealt
     * @param damageAmount The amount of Damage Dealt
     * @return If the damage "Succeeded"
     */
    public abstract boolean takeDamage(DamageType damageType, int damageAmount);

    /**
     * Heal the Entity, no causer specified
     * @param damageType The type of Healing Dealt
     * @param healingAmount The amount of Healing Dealt
     * @return If the Healing "Succeeded"
     */
    public abstract boolean takeHealing(DamageType damageType, int healingAmount);

    /**
     * Deal damage to the Entity, no causer specified
     * @param damageType The type of damage Dealt
     * @param damageAmount The amount of Damage Dealt
     * @return If the damage "Succeeded"
     */
    public abstract boolean takeDamage(EntityBase causer, DamageType damageType, int damageAmount);
}
