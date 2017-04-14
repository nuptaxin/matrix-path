package ren.ashin.matrix.path.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import ren.ashin.matrix.path.bean.Node;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

/**
 * @ClassName: HandleMatrixSchema2
 * @Description: 方案2：单个节点对递归完以后，继续递归的方式，直到找到最后一个终止节点
 * @author renzx
 * @date Apr 13, 2017
 */
public class HandleMatrixSchema2 {

    Table<Integer, Integer, Node> nodeTable = HashBasedTable.create();
    int i = 0;

    public void handle(final int size, List<List<Node>> manuNodeList) {

        List<Node> nodeList = Lists.newLinkedList();

        createMatrix(nodeList, nodeTable, size);

        List<Node> manuAllNodeList = Lists.newLinkedList();
        for (List<Node> coupleNodeList : manuNodeList) {
            for (Node node : coupleNodeList) {
                manuAllNodeList.add(node);
            }

        }
        for (List<Node> coupleNodes : manuNodeList) {
            Node manuStartNode = coupleNodes.get(0);
            Node manuEndNode = coupleNodes.get(1);
            Node startNode = nodeTable.get(manuStartNode.getX(), manuStartNode.getY());
            Node endNode = nodeTable.get(manuEndNode.getX(), manuEndNode.getY());

            List<Node> delNodeList1 = Lists.newLinkedList();
            for (Node node : manuAllNodeList) {
                if (!node.equals(manuStartNode) && !node.equals(manuEndNode)) {
                    Node invalidNode = nodeTable.get(node.getX(), node.getY());
                    delNodeList1.add(invalidNode);
                }
            }
            List<List<Node>> allValidPath1 =
                    getAllPathList(nodeList, startNode, endNode, delNodeList1);
            // 打印所有的路径

            for (List<Node> list : allValidPath1) {
                if (list.size() == size * size) {
                    System.out.print("路径：");
                    for (Node node : list) {
                        System.out.print("(" + node.getX() + "," + node.getY() + ")");
                    }
                    System.out.println("");
                }
            }
            break;
        }


        // 查找所有加和为size*size的组合
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
        int i = 0;

        fixedThreadPool.shutdown();
        try {
            fixedThreadPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        List<Node> delNodeList = Lists.newLinkedList();
        delNodeList.add(node);
        delNodeList.addAll(delNodes);

        List<List<Node>> allValidPath = Lists.newLinkedList();
        List<Node> currentPath = Lists.newLinkedList();

        findNextPath(allValidPath, currentPath, node, node2, delNodeList);
        return allValidPath;
    }

    private void findNextPath(List<List<Node>> allValidPath, List<Node> currentPath,
            Node currentNode, Node endNode, List<Node> delNodeList) {
        List<Node> theCurrentPath = Lists.newLinkedList(currentPath);
        theCurrentPath.add(currentNode);
        if (currentNode.equals(nodeTable.get(7, 0))) {
            // System.out.println(StringUtils.join(theCurrentPath.toArray(), ","));
            // 继续查找第二组节点
            List<Node> theDelNodeList = Lists.newLinkedList(theCurrentPath);
            theDelNodeList.add(nodeTable.get(1, 1));
            theDelNodeList.add(nodeTable.get(2, 4));
            theDelNodeList.add(nodeTable.get(6, 0));
            theDelNodeList.add(nodeTable.get(3, 2));
            theDelNodeList.add(nodeTable.get(5, 5));
            findNextPath(allValidPath, theCurrentPath, nodeTable.get(1, 1), nodeTable.get(2, 5),
                    theDelNodeList);
            // System.out.println("继续查找第二组节点结束"+i++);
            return;
        }
        if (currentNode.equals(nodeTable.get(2, 5))) {
            // 继续查找第三组节点
            List<Node> theDelNodeList = Lists.newLinkedList(theCurrentPath);
            theDelNodeList.add(nodeTable.get(2, 4));
            theDelNodeList.add(nodeTable.get(3, 2));
            theDelNodeList.add(nodeTable.get(5, 5));
            findNextPath(allValidPath, theCurrentPath, nodeTable.get(2, 4), nodeTable.get(6, 0),
                    theDelNodeList);
            // System.out.println("继续查找第3组节点结束");
            return;
        }
        if (currentNode.equals(nodeTable.get(6, 0))) {
            // 继续查找第四组节点
            List<Node> theDelNodeList = Lists.newLinkedList(theCurrentPath);
            theDelNodeList.add(nodeTable.get(3, 2));
            findNextPath(allValidPath, theCurrentPath, nodeTable.get(3, 2), nodeTable.get(5, 5),
                    theDelNodeList);
            // System.out.println("继续查找第4组节点结束");
            return;
        }
        if (currentNode.equals(nodeTable.get(5, 5))) {
            // 结束遍历
            allValidPath.add(theCurrentPath);
            // System.out.println("找到路径");
            return;
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
