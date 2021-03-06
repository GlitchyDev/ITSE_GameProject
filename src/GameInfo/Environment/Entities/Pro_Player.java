package GameInfo.Environment.Entities;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.*;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import GameStates.Enums.GameStateEnum;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import HardwareAdaptors.DirectionalEnum;
import HardwareAdaptors.XBoxController;

/**
 * This class aims to
 * - Be the basic Player Character, Protagonist ( Shortened to Pro )
 * - Implement Controls for the Player
 * - Implement Behaviors for the Player
 * - Animate the player
 */
public class Pro_Player extends DamageableEntityBase {
    // Controller Associations
    private Player player;
    private XBoxController controller;

    // Direction Helpers
    // Cached Direction refers to the previous state of the controller
    private DirectionalEnum cachedDirection;
    // Primary Direction refers to the last direction the player faced
    private DirectionalEnum primaryDirection;

    // State helpers
    private ProPlayerStateEnum entityState;
    private long stateStartTime;

    // Controller Movement Helpers
    private final double holdDownTime = 0.01;
    private final double moveTime = 0.2;

    // Light Emissions
    private boolean lightButtonCache = false;
    private long lightChangeTime = 0;
    private int lightLevel = -2;
    private final int maxLightLevel = 10;

    // Animation Helpers
    private ProPlayerEmotion emotion = ProPlayerEmotion.NONE;
    private long emotionStartTime = 0;
    private ProPlayerBodyState bodyState = ProPlayerBodyState.NONE;
    private String headType = "P1";
    private String bodyType = "P1";
    private String legType = "P1";

    // Death helpers
    private long deathStartTime = 0;
    private final double deathTimeAnimationLength = 10;
    private EntityType causerOfDeath = EntityType.DEBUG;



    public Pro_Player(World world, GlobalGameData globalGameData, Player player, int x, int y) {
        super(world, globalGameData, x, y);
        this.player = player;
        this.controller = player.getController();
        entityType = EntityType.PLAYER;
        currentHealth = 5;
        maxHealth = 5;
        cachedDirection = DirectionalEnum.NONE;
        primaryDirection = DirectionalEnum.SOUTH;
        entityState = ProPlayerStateEnum.IDLE;
        stateStartTime = System.currentTimeMillis();
        lightChangeTime = System.currentTimeMillis();
        PlayerSkinCreator.generateSkin(player,globalGameData);
        String[] args = player.getSkinID().split(",");
        headType = args[0];
        bodyType = args[1];
        legType = args[2];



    }

    @Override
    public void tickEntity() {
        if(!isDead) {

            // Do controller Logic
            buttonInput();
            switch (entityState) {
                case IDLE:
                    break;
                case ATTEMPTING_MOVE:
                    boolean moveSucceded = true;
                    if (System.currentTimeMillis() > stateStartTime + holdDownTime * 1000) {
                        switch (cachedDirection) {
                            case NORTH:
                                moveSucceded = advancedMoveRelative(0, 1, true, true, true, true);
                                break;
                            case SOUTH:
                                moveSucceded = advancedMoveRelative(0, -1, true, true, true, true);
                                break;
                            case EAST:
                                moveSucceded = advancedMoveRelative(-1, 0, true, true, true, true);
                                break;
                            case WEST:
                                moveSucceded = advancedMoveRelative(1, 0, true, true, true, true);
                                break;
                        }

                        if (moveSucceded) {
                            entityState = ProPlayerStateEnum.MOVING;
                            stateStartTime = System.currentTimeMillis();
                        } else {
                            entityState = ProPlayerStateEnum.IDLE;
                            stateStartTime = System.currentTimeMillis();
                        }
                    }
                    break;
                case MOVING:
                    if (System.currentTimeMillis() > stateStartTime + moveTime * 1000) {
                        entityState = ProPlayerStateEnum.IDLE;
                        stateStartTime = System.currentTimeMillis();
                    }
                    break;
                case DEAD:
                    break;

            }
            // Adjust Bodystates
            switch (bodyState) {
                case NONE:
                    lightLevel = 0;
                    break;
                case LIGHT_ON:
                    if ((System.currentTimeMillis() - lightChangeTime) / 500.0 <= 1) {
                        lightLevel = (int) (((System.currentTimeMillis() - lightChangeTime) / 500.0) * maxLightLevel);
                    } else {
                        lightLevel = maxLightLevel;
                        switch ((int) (Math.random() * 100)) {
                            case 0:
                                lightLevel -= 3;
                                break;
                            case 1:
                                lightLevel -= 2;
                                break;
                            case 2:
                                lightLevel -= 1;
                                break;
                        }
                    }

                    break;
                case LIGHT_OFF:
                    if ((System.currentTimeMillis() - lightChangeTime) / 1000.0 >= 0.3) {
                        bodyState = ProPlayerBodyState.LIGHT_AWAY;
                        lightChangeTime = System.currentTimeMillis();
                    } else {
                        lightLevel = maxLightLevel - (int) (((System.currentTimeMillis() - lightChangeTime) / 200.0) * maxLightLevel);

                    }
                    break;
                case LIGHT_AWAY:
                    if ((System.currentTimeMillis() - lightChangeTime) / 1000.0 >= 0.1) {
                        bodyState = ProPlayerBodyState.NONE;
                        lightChangeTime = System.currentTimeMillis();
                    }
                    break;
            }
            // Do Emotion Logic
            int random = (int) (Math.random() * 300);
            if(emotion == ProPlayerEmotion.NONE) {
                switch (random) {
                    case 0:
                        break;
                    case 1:
                        emotion = ProPlayerEmotion.BLINK_1;
                        emotionStartTime = System.currentTimeMillis();
                        break;
                }
            }
            else
            {
                double duration = (System.currentTimeMillis() - emotionStartTime)/1000.0;
                switch(emotion)
                {
                    case BLINK_1:
                        if(duration > 0.1)
                        {
                            emotion = ProPlayerEmotion.BLINK_2;
                            emotionStartTime = System.currentTimeMillis();
                        }
                        break;
                    case BLINK_2:
                        if(duration > 0.1)
                        {
                            emotion = ProPlayerEmotion.BLINK_3;
                            emotionStartTime = System.currentTimeMillis();
                        }
                        break;
                    case BLINK_3:
                        if(duration > 0.1)
                        {
                            emotion = ProPlayerEmotion.NONE;
                            emotionStartTime = System.currentTimeMillis();
                        }
                        break;
                    case EYES_CLOSED:
                }
            }
        }
        else
        {
            double duration = (System.currentTimeMillis() - deathStartTime)/1000.0;
            if(causerOfDeath == EntityType.HAUNTED_SKULL)
            {
                if(duration > deathTimeAnimationLength/2.0) {
                    if(world.getChunkFromCordXY(x, y).getAllEntities().contains(this)) {
                        world.getChunkFromCordXY(x, y).removeEntity(this);
                        Haunted_Skull_Entity playerSkull = new Haunted_Skull_Entity(world, globalGameData, x, y, true);
                        playerSkull.setCurrentDirection(primaryDirection);
                        world.getChunkFromCordXY(x, y).addEntity(playerSkull);
                    }
                    if (duration > deathTimeAnimationLength)
                    {
                        globalGameData.switchGameState(GameStateEnum.TitleScreen);
                    }
                }

            }
            else {
                if (duration > deathTimeAnimationLength) {
                    world.getChunkFromCordXY(x, y).removeEntity(this);
                    Haunted_Skull_Entity playerSkull = new Haunted_Skull_Entity(world, globalGameData, x, y, true);
                    playerSkull.setCurrentDirection(primaryDirection);
                    world.getChunkFromCordXY(x, y).addEntity(playerSkull);
                    globalGameData.switchGameState(GameStateEnum.TitleScreen);
                }
            }
            // Dead Player Logic
        }
        RadiantLightProducer.produceLight(world,x,y,lightLevel);
        RadiantLightProducer.produceLight(world,x,y,4);


    }


    /**
     * Controls when a valid "Input" has been given, by calculating how long a button needs to be held down for before we consider it
     * valid input
     */
    public void buttonInput()
    {
        controller.poll();

        // MOVEMENT
        DirectionalEnum direction = controller.getDirectionalPad();
        switch(entityState)
        {
            case IDLE:
                if(direction != DirectionalEnum.NONE && cachedDirection == DirectionalEnum.NONE && !DirectionalEnum.isDiagnal(direction))
                {

                    entityState = ProPlayerStateEnum.ATTEMPTING_MOVE;
                    stateStartTime = System.currentTimeMillis();
                    cachedDirection = direction;
                    primaryDirection = direction;
                }
                else
                {
                    if(direction != cachedDirection)
                    {
                        cachedDirection = direction;
                        if(direction != DirectionalEnum.NONE)
                        {
                            primaryDirection = direction;
                        }

                    }
                }
                break;
            case ATTEMPTING_MOVE:
                if(cachedDirection != direction)
                {
                    entityState = ProPlayerStateEnum.IDLE;
                    stateStartTime = System.currentTimeMillis();
                    cachedDirection = direction;
                }
                break;
            case MOVING:
                break;
            case DEAD: break;

        }
        // Lighting
        if(lightButtonCache != controller.getRightShoulder())
        {

            if(controller.getRightShoulder() && (bodyState == ProPlayerBodyState.NONE || bodyState == ProPlayerBodyState.LIGHT_ON))
            {
                if(bodyState == ProPlayerBodyState.LIGHT_ON)
                {
                    bodyState = ProPlayerBodyState.LIGHT_OFF;
                    lightChangeTime = System.currentTimeMillis();
                    globalGameData.playSound("flashLightOff",false);
                    globalGameData.stopSound("elecHumExtended");
                }
                else {
                    if (bodyState == ProPlayerBodyState.NONE) {
                        bodyState = ProPlayerBodyState.LIGHT_ON;
                        lightChangeTime = System.currentTimeMillis();
                        globalGameData.playSound("elecHumExtended",true,0.3);
                        globalGameData.playSound("flashLightOn",false);
                    }
                }
            }
            lightButtonCache = controller.getRightShoulder();
        }

        if(controller.getLeftShoulder())
        {
            world.setBlockFromCords(x,y+1,new WallFloorBlock(globalGameData,BlockTypeEnum.TEST_WALL));
            world.setBlockFromCords(x,y-1,new WallFloorBlock(globalGameData,BlockTypeEnum.TEST_WALL));
            world.setBlockFromCords(x+1,y,new WallFloorBlock(globalGameData,BlockTypeEnum.TEST_WALL));
            world.setBlockFromCords(x-1,y,new WallFloorBlock(globalGameData,BlockTypeEnum.TEST_WALL));
        }


    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            double xOffset = 0;
            double yOffset = 0;

            String direction = "Front";
            if (entityState == ProPlayerStateEnum.MOVING) {
                switch (primaryDirection) {
                    case NORTH:
                        yOffset = World.getScaledUpSquareSize() - ((System.currentTimeMillis() - stateStartTime) / 1000.0) / moveTime * (World.getScaledUpSquareSize());
                        break;
                    case SOUTH:
                        yOffset = -World.getScaledUpSquareSize() + ((System.currentTimeMillis() - stateStartTime) / 1000.0) / moveTime * (World.getScaledUpSquareSize());
                        break;
                    case EAST:
                        xOffset = -World.getScaledUpSquareSize() + ((System.currentTimeMillis() - stateStartTime) / 1000.0) / moveTime * World.getScaledUpSquareSize();
                        break;
                    case WEST:
                        xOffset = World.getScaledUpSquareSize() - ((System.currentTimeMillis() - stateStartTime) / 1000.0) / moveTime * World.getScaledUpSquareSize();
                        break;
                }
            }
            switch (primaryDirection) {
                case NORTH:
                    direction = "Back";
                    break;
                case SOUTH:
                    direction = "Front";
                    break;
                case EAST:
                    direction = "Right";
                    break;
                case WEST:
                    direction = "Left";
                    break;
            }
            String head = headType + "_" + direction + "_Head";
            switch(emotion)
            {
                case EVIL:
                    head += "_Evil";
                    break;
                case REALLY:
                    head += "_Really";
                    break;
                case BLINK_1:
                    head += "_Really";
                    break;
                case BLINK_2:
                    head += "_Eyes_Closed";
                    break;
                case BLINK_3:
                    head += "_Really";
                    break;

                case EYES_CLOSED:
                    head += "_Eyes_Closed";
                    break;
                case ALSI_Close_Eyes_1:
                    head += "_Really";
                    break;
                case ALSI_Close_Eyes_2:
                    head += "_Eyes_Closed";
                    break;
                case ALSI_TURN_LEFT:
                    head += "_Look_Left";
                    break;
                case ALSI_TURN_RIGHT:
                    head += "_Look_Right";
                    break;
                case ALSI_EVIL:
                    head += "_Evil";
                    break;

            }
            String body = bodyType + "_" + direction + "_Body";
            switch (bodyState) {
                case LIGHT_ON:
                    double timePassed = ((System.currentTimeMillis() - lightChangeTime) / 1000.0);
                    if (timePassed < 0.03) {
                        body += "_Light_Away";
                    } else {
                        if (timePassed < 0.05) {
                            body += "_Light_Off";
                        } else {
                            body += "_Light_On";
                        }
                    }
                    break;
                case LIGHT_OFF:
                    body += "_Light_Off";
                    break;
                case LIGHT_AWAY:
                    body += "_Light_Away";
                    break;
                case NONE:
                    body += "";
                    break;
            }
            String leg = legType + "_" + direction + "_";
            if (entityState == ProPlayerStateEnum.ATTEMPTING_MOVE || entityState == ProPlayerStateEnum.MOVING) {
                switch ((int) ((System.currentTimeMillis() / 100) % 4)) {
                    case 0:
                        leg += "Legs_1";
                        break;
                    case 1:
                        leg += "Legs_2";
                        break;
                    case 2:
                        leg += "Legs_1";
                        break;
                    case 3:
                        leg += "Legs_3";
                        break;
                }
            } else {
                leg += "Legs_1";
            }

            Image sprite = globalGameData.getSprite(player.getUuid().toString() + "|" + head + "|" + body + "|" + leg);

            gc.setGlobalAlpha(1.0);
            drawSpriteAtXY(sprite, gc, x, y + 1, 1.5 + xOffset, (World.getScaledUpSquareSize() - 70 - World.getScaledUpSquareSize() / 2) + yOffset,true);
            //TextRenderHelper.drawCenteredText((int)x,(int)y+10,player.getDisplayName(),gc,globalGameData);
            if(!isDead) {
                TextRenderHelper.drawViewportCenteredText("[" + currentHealth + "]", gc, globalGameData, x, y, xOffset + World.getScaledUpSquareSize() / 2, yOffset - 20);
            }
            else
            {
                TextRenderHelper.drawViewportCenteredText("[" + "DEAD" + "]", gc, globalGameData, x, y, xOffset + World.getScaledUpSquareSize() / 2, yOffset - 20);

            }


        }

    }


    @Override
    public boolean takeDamage(DamageType damageType, int damageAmount) {
        return true;
    }

    @Override
    public boolean takeHealing(DamageType damageType, int healingAmount) {
        currentHealth += healingAmount;
        return false;
    }

    @Override
    public boolean takeDamage(EntityBase attacker, DamageType damageType, int damageAmount) {
        currentHealth -= damageAmount;
        if(currentHealth <= 0)
        {
            isDead = true;
            deathStartTime = System.currentTimeMillis();
            entityState = ProPlayerStateEnum.DEAD;
            causerOfDeath = attacker.getEntityType();
        }
        return true;
    }

    public ProPlayerBodyState getBodyState() {
        return bodyState;
    }
    public long getDeathStartTime() {
        return deathStartTime;
    }
    public EntityType getCauserOfDeath() { return causerOfDeath;}
    public double getDeathTimeAnimationLength() { return deathStartTime;}
    public DirectionalEnum getPrimaryDirection() {
        return primaryDirection;
    }
    public void setPrimaryDirection(DirectionalEnum direction)
    {
        this.primaryDirection = direction;
    }
    public int getHealth()
    {
        return currentHealth;
    }
    public void setEmotion(ProPlayerEmotion emotion)
    {
        this.emotion = emotion;
    }


}
