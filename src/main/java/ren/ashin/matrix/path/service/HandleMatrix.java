package ren.ashin.matrix.path.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import ren.ashin.matrix.path.bean.Node;

/**
 * @ClassName: HandleMatrix
 * @Description: TODO
 * @author renzx
 * @date Apr 13, 2017
 */
public class HandleMatrix {

    public void handle(final int size, List<List<Node>> manuNodeList) {

        List<Node> nodeList = Lists.newLinkedList();
        Table<Integer, Integer, Node> nodeTable = HashBasedTable.create();

        createMatrix(nodeList, nodeTable, size);

        List<Node> manuAllNodeList = Lists.newLinkedList();
        for (List<Node> coupleNodeList : manuNodeList) {
            for (Node node : coupleNodeList) {
                manuAllNodeList.add(node);
            }

        }
        Multimap<Integer, List<Node>> multimap1 = ArrayListMultimap.create();
        Multimap<Integer, List<Node>> multimap2 = ArrayListMultimap.create();
        Multimap<Integer, List<Node>> multimap3 = ArrayListMultimap.create();
        Multimap<Integer, List<Node>> multimap4 = ArrayListMultimap.create();

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
            for (List<Node> validNodeList : allValidPath1) {

                // System.out.print("路径：");
                // for (Node node : validNodeList) {
                // System.out.print("(" + node.getX() + "," + node.getY() + ")");
                // }
                // System.out.println("");
                if (multimap1.size() == 0) {
                    multimap1.put(validNodeList.size(), validNodeList);
                } else if (multimap1.size() > 0) {
                    multimap2.put(validNodeList.size(), validNodeList);
                }
                else if (multimap2.size() > 0) {
                    multimap3.put(validNodeList.size(), validNodeList);
                }
                else if (multimap3.size() > 0) {
                    multimap4.put(validNodeList.size(), validNodeList);
                }

            }
            System.out.println("路径个数：" + allValidPath1.size() + ";路径长度种类："
                    + multimap1.keySet().size() + ",分别为:"
                    + StringUtils.join(multimap1.keySet().toArray(), ","));
        }

        // 查找所有加和为size*size的组合
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
        int i = 0;
        for (Integer key1 : multimap1.keySet()) {
            for (Integer key2 : multimap2.keySet()) {
                for (Integer key3 : multimap3.keySet()) {
                    for (Integer key4 : multimap4.keySet()) {
                        if (key1 + key2 + key3 + key4 == size * size) {
                            System.out.println("找到方案" + i++ + "：key1:" + key1 + ",key2:" + key2
                                    + ",key3:" + key3 + ",key4:" + key4);
                            // 验证方案的可行性
                            final List<List<Node>> node1 = (List<List<Node>>) multimap1.get(key1);
                            final List<List<Node>> node2 = (List<List<Node>>) multimap2.get(key2);
                            final List<List<Node>> node3 = (List<List<Node>>) multimap3.get(key3);
                            final List<List<Node>> node4 = (List<List<Node>>) multimap4.get(key4);
                            final int j = i - 1;
                            fixedThreadPool.execute(new Runnable() {
                                public void run() {
                                    System.out.println("方案" + j + "执行中");
                                    for (List<Node> list1 : node1) {
                                        for2: for (List<Node> list2 : node2) {
                                            for3: for (List<Node> list3 : node3) {
                                                for4: for (List<Node> list4 : node4) {
                                                    Set<Node> checkSet = Sets.newHashSet();
                                                    checkSet.addAll(list1);
                                                    checkSet.addAll(list2);
                                                    if (checkSet.size() < list1.size()
                                                            + list2.size()) {
                                                        continue for2;
                                                    }
                                                    checkSet.addAll(list3);
                                                    if (checkSet.size() < list1.size()
                                                            + list2.size() + list3.size()) {
                                                        continue for3;
                                                    }
                                                    checkSet.addAll(list4);
                                                    if (checkSet.size() < list1.size()
                                                            + list2.size() + list3.size()
                                                            + list4.size()) {
                                                        continue for4;
                                                    }
                                                    // System.out.println("set1:" +
                                                    // DateTime.now().toString());
                                                    if (checkSet.size() == size * size) {
                                                        System.out.println("找到最终解决方案：");
                                                        System.out.print("list1：");
                                                        for (Node node : list1) {
                                                            System.out.print("(" + node.getX()
                                                                    + "," + node.getY() + ")");
                                                        }
                                                        System.out.println("");
                                                        System.out.print("list2：");
                                                        for (Node node : list2) {
                                                            System.out.print("(" + node.getX()
                                                                    + "," + node.getY() + ")");
                                                        }
                                                        System.out.println("");
                                                        System.out.print("list3：");
                                                        for (Node node : list3) {
                                                            System.out.print("(" + node.getX()
                                                                    + "," + node.getY() + ")");
                                                        }
                                                        System.out.println("");
                                                        System.out.print("list4：");
                                                        for (Node node : list4) {
                                                            System.out.print("(" + node.getX()
                                                                    + "," + node.getY() + ")");
                                                        }
                                                        System.out.println("");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }

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
        if (currentNode.equals(endNode)) {
            allValidPath.add(theCurrentPath);
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
