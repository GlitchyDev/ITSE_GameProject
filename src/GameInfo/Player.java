package GameInfo;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import sample.XBoxController;

import java.util.UUID;

/**
 * Created by Robert on 8/26/2017.
 *
 * The Purpose of this Class
 * - Represents a Controller connected to a computer AKA a person
 * - Holds reference to the players Entity, UUID, and Controller
 */
public class Player {
    private XBoxController controller;
    private UUID uuid;
    private EntityBase playerCharacter;

    public Player(XBoxController controller,EntityBase playerCharacter)
    {
        this.controller = controller;
        uuid = UUID.randomUUID();
        this.playerCharacter = playerCharacter;
    }

    public XBoxController getController() {
        return controller;
    }

    public UUID getUuid() {
        return uuid;
    }

    public EntityBase getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(EntityBase playerCharacter) {
        this.playerCharacter = playerCharacter;
    }
}
