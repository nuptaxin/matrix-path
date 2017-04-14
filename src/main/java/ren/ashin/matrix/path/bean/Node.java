package ren.ashin.matrix.path.bean;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @ClassName: Node
 * @Description: TODO
 * @author renzx
 * @date Apr 13, 2017
 */
public class Node {
    private int x;
    private int y;
    
    private List<Node> nextNodeList = Lists.newLinkedList();

    public Node(int i, int j) {
        x = i;
        y = j;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Node> getNextNodeList() {
        return nextNodeList;
    }

    public void setNextNodeList(List<Node> nextNodeList) {
        this.nextNodeList = nextNodeList;
    }

    @Override
    public String toString() {
        return "Node [x=" + x + ", y=" + y + "]";
    }

}
