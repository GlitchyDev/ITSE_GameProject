package GameInfo.Environment.Entities.Pathfinding;

public class PathfindingNode {
    private PathfindingNode parentNode;
    private int x;
    private int y;

    private int g;
    private int h;
    private int f;

    public PathfindingNode(PathfindingNode parentNode, int x, int y, int g, int targetX, int targetY)
    {
        this.parentNode = parentNode;
        this.x = x;
        this.y = y;
        this.g = g;
        h = 10*(Math.abs(x-targetX) + Math.abs(y-targetY));
        f = g + h;
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

        h = 14*(Math.abs(x-targetX) + Math.abs(y-targetY));
        f = g + h;
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
        return f;
    }
}