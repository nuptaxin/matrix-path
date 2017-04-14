package ren.ashin.matrix.path.bean;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @ClassName: TmpLinkNode
 * @Description: TODO
 * @author renzx
 * @date Apr 14, 2017
 */
public class TmpLinkNode {
    private Node startNode;
    private Node endNode;
    private Node nextStartNode;
    private Node nextEndNode;
    private List<Node> delNodeList = Lists.newArrayList();
    public Node getStartNode() {
        return startNode;
    }
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }
    public Node getEndNode() {
        return endNode;
    }
    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }
    public Node getNextStartNode() {
        return nextStartNode;
    }
    public void setNextStartNode(Node nextStartNode) {
        this.nextStartNode = nextStartNode;
    }
    public Node getNextEndNode() {
        return nextEndNode;
    }
    public void setNextEndNode(Node nextEndNode) {
        this.nextEndNode = nextEndNode;
    }
    public List<Node> getDelNodeList() {
        return delNodeList;
    }
    public void setDelNodeList(List<Node> delNodeList) {
        this.delNodeList = delNodeList;
    }
    
}
