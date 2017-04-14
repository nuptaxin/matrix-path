package ren.ashin.matrix.path;

import java.util.List;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;

import ren.ashin.matrix.path.bean.Node;
import ren.ashin.matrix.path.service.HandleMatrix;
import ren.ashin.matrix.path.util.MainConfig;

import com.google.common.collect.Lists;

/**
 * Hello world!
 *
 */
public class MatrixPath {
    /**
     * @Fields LOG : 日志
     */
    private static final Logger LOG = Logger.getLogger(MatrixPath.class);
    public static MainConfig mfg = null;

    public static void main(String[] args) {

        PropertyConfigurator.configure("conf/log4j-matrixPath.properties");
        LOG.info("程序开始运行时间：" + DateTime.now());
        mfg = ConfigFactory.create(MainConfig.class);


        // 分析获取到的配置项
        final int size = mfg.size();
        String nodes = mfg.nodes();
        LOG.info("当前获取到的矩阵size为：" + size);
        LOG.info("当前获取到的矩阵nodes为：" + nodes);
        List<List<Node>> manuNodeList = Lists.newLinkedList();
        try {
            String[] nodeCouples = StringUtils.split(nodes, "|");
            for (String nodeCouple : nodeCouples) {
                List<Node> coupleNodeList = Lists.newLinkedList();
                manuNodeList.add(coupleNodeList);
                String[] nodeSingles = StringUtils.substringsBetween(nodeCouple, "(", ")");
                for (String nodeValue : nodeSingles) {
                    String x = StringUtils.substringBefore(nodeValue, ",");
                    String y = StringUtils.substringAfter(nodeValue, ",");
                    Node node = new Node(Integer.valueOf(x.trim()), Integer.valueOf(y.trim()));
                    coupleNodeList.add(node);
                }
            }
        } catch (Exception e) {
            LOG.error("解析配置文件异常");
            System.exit(0);
        }

        HandleMatrix handleMatrix = new HandleMatrix();
        handleMatrix.handle(size, manuNodeList);
        LOG.info("程序结束运行时间：" + DateTime.now());
    }
}
