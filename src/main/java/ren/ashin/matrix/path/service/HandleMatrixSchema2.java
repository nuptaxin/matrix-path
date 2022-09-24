package ren.ashin.matrix.path.service;

import java.util.List;
import java.util.Map;

import ren.ashin.matrix.path.bean.Node;
import ren.ashin.matrix.path.bean.TmpLinkNode;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

/**
 * @ClassName: HandleMatrixSchema2
 * @Description: 方案2：单个节点对递归完以后，继续递归的方式，直到找到最后一个终止节点
 * @author renzx
 * @date Apr 13, 2017
 */
public class HandleMatrixSchema2 {

    Table<Integer, Integer, Node> nodeTable = HashBasedTable.create();
    List<TmpLinkNode> tmpLinkNodeList = Lists.newLinkedList();

    public void handle(final int size, List<List<Node>> manuNodeList) {

        List<Node> nodeList = Lists.newLinkedList();

        createMatrix(nodeList, nodeTable, size);

        // 将传入的manuNodeList 转换为当前构造的matrix
        List<List<Node>> newManuNodeList = Lists.newLinkedList();
        for (List<Node> nodeLists : manuNodeList) {
            List<Node> newNodeLists = Lists.newLinkedList();
            newManuNodeList.add(newNodeLists);
            for (Node node : nodeLists) {
                Node newNode = nodeTable.get(node.getX(), node.getY());
                newNodeLists.add(newNode);
            }
        }
        manuNodeList = newManuNodeList;


        // 组一个Map，key是当前的首节点，Value为next的首末节点
        Map<Node, List<Node>> nodeNextMap = Maps.newHashMap();
        Node currentNode = null;

        List<Node> manuAllNodeList = Lists.newLinkedList();
        for (List<Node> coupleNodeList : manuNodeList) {
            if (currentNode != null) {
                nodeNextMap.put(currentNode, coupleNodeList);
            }
            currentNode = coupleNodeList.get(0);
            for (Node node : coupleNodeList) {
                manuAllNodeList.add(node);
            }
        }
        nodeNextMap.put(currentNode, null);

        for (List<Node> coupleNodes : manuNodeList) {
            TmpLinkNode tmp = new TmpLinkNode();
            tmp.setStartNode(coupleNodes.get(0));
            tmp.setEndNode(coupleNodes.get(1));
            List<Node> coupleNodeList = nodeNextMap.get(coupleNodes.get(0));
            if (coupleNodeList != null) {
                tmp.setNextStartNode(coupleNodeList.get(0));
                tmp.setNextEndNode(coupleNodeList.get(1));
            }
            List<Node> theManuAllNodeList = Lists.newLinkedList(manuAllNodeList);
            theManuAllNodeList.remove(coupleNodes.get(1));
            tmp.setDelNodeList(theManuAllNodeList);
            tmpLinkNodeList.add(tmp);

        }
        Node manuStartNode = tmpLinkNodeList.get(0).getStartNode();
        Node manuEndNode = tmpLinkNodeList.get(0).getEndNode();
        Node startNode = nodeTable.get(manuStartNode.getX(), manuStartNode.getY());
        Node endNode = nodeTable.get(manuEndNode.getX(), manuEndNode.getY());

        List<Node> delNodeList1 = Lists.newLinkedList();
        List<List<Node>> allValidPath1 = getAllPathList(nodeList, startNode, endNode, delNodeList1);
        // 打印所有的路径

        boolean hasValidPath = false;
        for (List<Node> list : allValidPath1) {
            if (list.size() == size * size) {
                hasValidPath = true;
                System.out.print("路径：");
                for (Node node : list) {
                    System.out.print("(" + node.getX() + "," + node.getY() + ")");
                }
                System.out.println("");
            }
        }
        if(!hasValidPath) {
            System.out.println("未找到有效路径");
        }
    }

    private void createMatrix(List<Node> nodeList, Table<Integer, Integer, Node> nodeTable, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Node node = new Node(i, j);
                nodeList.add(node);
                nodeTable.put(i, j, node);
            }

        }

        // 遍历所有的node，并将node的相邻节点填充
        for (Node node : nodeList) {
            // 找到四个方向的node
            Node nodeLeft = nodeTable.get(node.getX() - 1, node.getY());
            if (nodeLeft != null) {
                node.getNextNodeList().add(nodeLeft);
            }
            Node nodeRight = nodeTable.get(node.getX() + 1, node.getY());
            if (nodeRight != null) {
                node.getNextNodeList().add(nodeRight);
            }
            Node nodeUp = nodeTable.get(node.getX(), node.getY() - 1);
            if (nodeUp != null) {
                node.getNextNodeList().add(nodeUp);
            }
            Node nodeDown = nodeTable.get(node.getX(), node.getY() + 1);
            if (nodeDown != null) {
                node.getNextNodeList().add(nodeDown);
            }
        }

    }

    private List<List<Node>> getAllPathList(List<Node> nodeList, Node node, Node node2,
            List<Node> delNodes) {
        List<Node> delNodeList = Lists.newLinkedList(delNodes);
        delNodeList.add(node);

        List<List<Node>> allValidPath = Lists.newLinkedList();
        List<Node> currentPath = Lists.newLinkedList();

        findNextPath(allValidPath, currentPath, node, node2, delNodeList);
        return allValidPath;
    }

    private void findNextPath(List<List<Node>> allValidPath, List<Node> currentPath,
            Node currentNode, Node endNode, List<Node> delNodeList) {
        List<Node> theCurrentPath = Lists.newLinkedList(currentPath);
        theCurrentPath.add(currentNode);
        for (TmpLinkNode tmpNode : tmpLinkNodeList) {
            if (currentNode.equals(tmpNode.getStartNode())) {
                // 如果是第一个节点，那么把该节点的delNodeList加进去
                delNodeList.addAll(tmpNode.getDelNodeList());
            }

            if (currentNode.equals(tmpNode.getEndNode())) {
                // System.out.println(StringUtils.join(theCurrentPath.toArray(), ","));
                // 继续查找第二组节点
                if (tmpNode.getNextStartNode() == null) {
                    allValidPath.add(theCurrentPath);
                } else {
                    List<Node> theDelNodeList = Lists.newLinkedList(theCurrentPath);
                    theDelNodeList.add(currentNode);
                    theDelNodeList.remove(tmpNode.getNextEndNode());
                    findNextPath(allValidPath, theCurrentPath, tmpNode.getNextStartNode(),
                            tmpNode.getNextEndNode(), theDelNodeList);
                }
                return;
            }
        }
        List<Node> nextNodes = findNextNode(currentNode, delNodeList);
        if (nextNodes.size() == 0) {
            return;
        }
        for (Node node : nextNodes) {
            List<Node> theDelNodeList = Lists.newLinkedList(delNodeList);
            theDelNodeList.add(node);
            theDelNodeList.addAll(currentNode.getNextNodeList());
            findNextPath(allValidPath, theCurrentPath, node, endNode, theDelNodeList);
        }

    }

    private List<Node> findNextNode(Node node, List<Node> nodeList) {
        List<Node> nextNodeList = node.getNextNodeList();
        List<Node> nextValidNodeList = Lists.newLinkedList();
        for (Node node2 : nextNodeList) {
            if (!nodeList.contains(node2)) {
                nextValidNodeList.add(node2);
            }
        }
        return nextValidNodeList;
    }
}
