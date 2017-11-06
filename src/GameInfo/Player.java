package GameInfo;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import HardwareAdaptors.XBoxController;

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
    private String displayName;
    private UUID uuid;
    private EntityBase playerCharacter;
    private String skinID;

    public Player(XBoxController controller,EntityBase playerCharacter)
    {
        this.controller = controller;
        uuid = UUID.randomUUID();
        displayName = uuid.toString().substring(0,3);
        this.playerCharacter = playerCharacter;
        generateSkinID();
    }

    public void generateSkinID()
    {
        switch((int)(Math.random() * 20))
        {
            case 0:
                skinID = "P1,Blue,P1";
                break;
            case 1:
                skinID = "P1,Green,P1";
                break;
            case 2:
                skinID = "P1,Red,P1";
                break;
            default:
                skinID = "P1,P1,P1";
                break;

        }
    }
    public Player(XBoxController controller,EntityBase playerCharacter, UUID uuid, String displayName)
    {
        this.controller = controller;
        this.uuid = uuid;
        this.displayName = displayName;
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

    public String getSkinID()
    {
        return skinID;
    }

    public String getDisplayName() {
        return displayName;
    }
}
