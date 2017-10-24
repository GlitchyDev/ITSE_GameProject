package Pathfinding;

public class PathfindingNode {
    private PathfindingNode parentNode;
    private int x;
    private int y;

    private int g;
    private int h;

    public boolean openList = true;

    public PathfindingNode(PathfindingNode parentNode, int x, int y, int g, int targetX, int targetY)
    {
        this.parentNode = parentNode;
        this.x = x;
        this.y = y;
        this.g = g;
        h = (Math.abs(x-targetX) + Math.abs(y-targetY));
    }


    public PathfindingNode getParentNode()
    {
        return parentNode;
    }

    public boolean isPrimaryNode()
    {
        return parentNode == null;
    }

    public void recalculateH(int targetX, int targetY)
    {
        h = (Math.abs(x-targetX) + Math.abs(y-targetY));
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setNextNode(PathfindingNode parentNode) {
        this.parentNode = parentNode;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return g + h;
    }

    @Override
    public String toString() {
        return "Node: " + x + " " + y;
    }
}