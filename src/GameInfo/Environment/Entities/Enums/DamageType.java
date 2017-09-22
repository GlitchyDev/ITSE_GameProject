package GameInfo.Environment.Entities.Enums;

/**
 * This specifies exactly what kind of damage is being delt, and will handle logic of identifying what damage attributes each have
 * ( EX: Projectile and Stabbing would count as "Armor Piercing"
 */
public enum DamageType {
    DEBUG,
    BLUNT,
    PROJECTILE,
    STABBING,
    FIRE,
    EXPLOSIVE,
    SMITE
}
