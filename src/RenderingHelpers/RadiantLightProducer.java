package RenderingHelpers;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.World;

public class RadiantLightProducer {

    //private static ArrayList<Position> blocksLookedAt = new ArrayList<>();

    public static void produceLight(World world, int x, int y, int initLightAmount)
    {
        if(initLightAmount > 0) {
            BlockBase b = world.getBlockFromCords(x, y);
            if (b.getCurrentLightLevel() < initLightAmount) {
                b.setCurrentlyLit(true);
                b.setCurrentLightLevel(initLightAmount);
                expandLightMap(world, x, y, initLightAmount);
            }
        }
    }

    private static void expandLightMap(World world, int x, int y, int initLightAmount) {
        int expandNorth = -1;
        int expandSouth = -1;
        int expandEast = -1;
        int expandWest = -1;

        BlockBase b = world.getBlockFromCords(x, y + 1);
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {
            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(world, x, y+1, initLightAmount)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandNorth = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x, y - 1));
        }


        b = world.getBlockFromCords(x, y - 1);
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {

            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(world, x, y-1, initLightAmount)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandSouth = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x, y - 1));
        }

        b = world.getBlockFromCords(x + 1, y );
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {

            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(world, x+1, y, initLightAmount)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandEast = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x + 1, y ));
        }
        b = world.getBlockFromCords(x - 1, y);
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {

            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(world, x-1, y, initLightAmount)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandWest = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x + 1, y ));
        }

        if(expandNorth != -1)
        {
            expandLightMap(world,x,y+1,expandNorth);
        }
        if(expandSouth != -1)
        {
            expandLightMap(world,x,y-1,expandSouth);
        }
        if(expandEast != -1)
        {
            expandLightMap(world,x+1,y,expandEast);
        }
        if(expandWest != -1)
        {
            expandLightMap(world,x-1,y,expandWest);
        }


    }



    private static boolean hasBetterLight(World world, int x, int y, int light)
    {

        BlockBase b = world.getBlockFromCords(x,y);

        return b.getCurrentLightLevel() < BlockTypeEnum.caculateLightCost(b.getBlockType(), light);

    }

    public static double determineDarkness(int lightLevel)
    {
        //return 0.0;
        switch(lightLevel)
        {
            case 15:
                return 1 - 1.0;
            case 14:
                return 1 - 1.0;
            case 13:
                return 1 - 1.0;
            case 11:
                return 1 - 1.0;
            case 10:
                return 1 - 1.0;
            case 9:
                return 1 - 0.9;
            case 8:
                return 1 - 0.85;
            case 7:
                return 1 - 0.7;
            case 6:
                return 1 - 0.6;
            case 5:
                return 1 - 0.5;
            case 4:
                return 1 - 0.4;
            case 3:
                return 1 - 0.3;
            case 2:
                return 1 - 0.2;
            case 1:
                return 1 - 0.1;
            case 0:
                return 1 - 0.026;
        }
            if (lightLevel > 10) {
                return 0.0;
            } else {
                return 1.0 - ((double) lightLevel * 0.1);
            }
    }


    //******************************************************************************************
    //******************************************************************************************

    public static void produceLight(int x, int y, int initLightAmount, BlockBase[][] blocks)
    {
        if(initLightAmount > 0) {
            BlockBase b = blocks[x][y];
            if (b.getCurrentLightLevel() < initLightAmount) {
                b.setCurrentlyLit(true);
                b.setCurrentLightLevel(initLightAmount);
                expandLightMap(x, y, initLightAmount, blocks);
            }
        }
    }

    private static void expandLightMap(int x, int y, int initLightAmount, BlockBase[][] blocks) {
        int expandNorth = -1;
        int expandSouth = -1;
        int expandEast = -1;
        int expandWest = -1;

        BlockBase b = blocks[x][y+1];
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {
            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(x, y+1, initLightAmount,blocks)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandNorth = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x, y - 1));
        }


        b = blocks[x][y-1];
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {

            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(x, y-1, initLightAmount,blocks)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandSouth = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x, y - 1));
        }

        b = blocks[x+1][y];
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {

            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(x+1, y, initLightAmount,blocks)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandEast = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x + 1, y ));
        }
        b = blocks[x-1][y];
        if(BlockTypeEnum.caculateLightCost(b.getBlockType(),initLightAmount) > 0) {

            if (BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount) >= 0) {
                if (hasBetterLight(x-1, y, initLightAmount,blocks)) {
                    b.setCurrentlyLit(true);
                    b.setCurrentLightLevel(BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount));
                    if(!BlockTypeEnum.isOpaque(b.getBlockType())) {
                        expandWest = BlockTypeEnum.caculateLightCost(b.getBlockType(), initLightAmount);
                    }
                }
            }
            //blocksLookedAt.add(new Position(x + 1, y ));
        }

        if(expandNorth != -1)
        {
            expandLightMap(x,y+1,expandNorth,blocks);
        }
        if(expandSouth != -1)
        {
            expandLightMap(x,y-1,expandSouth,blocks);
        }
        if(expandEast != -1)
        {
            expandLightMap(x+1,y,expandEast,blocks);
        }
        if(expandWest != -1)
        {
            expandLightMap(x-1,y,expandWest,blocks);
        }


    }



    private static boolean hasBetterLight(int x, int y, int light, BlockBase[][] blocks)
    {
        BlockBase b = blocks[x][y];

        return b.getCurrentLightLevel() < BlockTypeEnum.caculateLightCost(b.getBlockType(), light);

    }


}

