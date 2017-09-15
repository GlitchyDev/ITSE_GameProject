package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;

import java.util.ArrayList;

/**
 * This class is intended to
 * - Allow the generation of Certain block structures and entities within a chunk
 * - Maintain behaviors of blocks inside a structure ( EX. Make stuff indestructible inside a house )
 * - Make Charlies life so much fucking easier


 */
public abstract class StructureBase {
    protected World world;
    protected GlobalGameData globalGameData;

    protected ArrayList<BlockBase> affectedBlocks;
    protected ArrayList<EntityBase> affectedEntities;

    protected int structureWidth = 0;
    protected int structureHeight = 0;
    // The X and Y describe the Upper Lefthand corner of the Structure
    // Upper Left represents +x +y, so keep this in mind!
    protected int structureX = 0;
    protected int structureY = 0;

    public StructureBase(World world, GlobalGameData globalGameData, int structureX, int structureY){
        this.world = world;
        this.globalGameData = globalGameData;
        this.structureX = structureX;
        this.structureY = structureY;
        affectedBlocks = new ArrayList<>();
        affectedEntities = new ArrayList<>();
    }

    /**
     * This method will
     * A. If this Structure is overlapping a previously existing structure
     * B. Will Trigger buildStructure, an abstract method overriden to add Blocks and Entities to the Structure
     * C. Return if all of that went well!
     * @return If the Structure Successfully Built!
     */
    public boolean attemptBuildStructure()
    {
        ArrayList<StructureBase> structureBases = world.getChunkFromCordXY(structureX,structureY).getStructures();
        // This checks if any previous structures already added to world ( THIS STRUCT SHOULDN"T BE INCLUDED YET )

        boolean passed = true;
        //System.out.println("Checking Structure " + structureX + " " + structureY);
        for(StructureBase struct: structureBases)
        {
            //System.out.println("Does Structure " + struct.getStructureX() + " " + struct.getStructureY() + " " + " overlap?");
            if(doesOverlap(struct)) {
                //System.out.println("It does! ");
                passed = false;
           }
        }

        if(passed) {
            //System.out.println("No overlaps! So we build!");
            buildStructure();
        }
        return passed;


    }

    public boolean doesOverlap(StructureBase base)
    {
        //System.out.println(structureX + ">" + (base.getStructureX() + base.structureWidth) + " " + (structureX + structureWidth) + ">" + base.getStructureX());
        if (structureX > base.getStructureX() + base.structureWidth || structureX + structureWidth < base.getStructureX())
            return false;
        //System.out.println(structureY + ">" + (base.getStructureY() + base.structureHeight) + " " + (structureY + structureHeight) + ">" + base.getStructureY());
        if (structureY > base.getStructureY() + base.structureHeight || structureY + structureHeight < base.getStructureY())
            return false;
        //System.out.println("OVERLAP DETECTED!");
        return true;
    }

    /**
     * This method will create all Blocks and Entities that reside within that structure
     */
    public abstract void buildStructure();

    public abstract StructureEventCollisionOverrideEnum checkCollision(int x, int y);
    public abstract void enterEvent(int x, int y);
    public abstract void exitEvent(int x, int y);


    public int getStructureWidth()
    {
        return structureWidth;
    }
    public int getStructureHeight()
    {
        return structureHeight;
    }
    public int getStructureX()
    {
        return structureX;
    }
    public int getStructureY()
    {
        return structureY;
    }
}
