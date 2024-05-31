package Framework;

import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import javax.swing.*;
import java.awt.*;

public interface Operation {
    /**
     * 在框架空白区域添加个性化方法
     * @return Component,返回个性化组件
     */
    Component special();

    /**
     * 设定主窗口标题
     * @return String,标题名称
     */
    String setTitle();

    /**
     * 设定按钮一名称
     * @return String,按钮一名称
     */
    String setButton1();

    /**
     * 设定按钮二名称
     * @return String,按钮二名称
     */
    String setButton2();

    /**
     * 设定按钮三名称
     * @return String,按钮三名称
     */
    String setButton3();

    /**
     * 设定添加对象窗口标题
     * @return String,对象窗口标题
     */
    String setAddElementTitle();

    /**
     * 展示个性化信息
     * @return String, 个性化信息
     */
    String ShowMessage();

    /**
     * 初始化系统
     * @return boolean,当初始化成功返回true，否则返回false
     */
    boolean initWindow();

    /**
     * 检查添加的元素是否合规
     * @param textFields 通过窗口输入的元素信息
     * @return boolean,当添加元素合规时返回true,否则返回false
     */
    boolean checkElement(JTextField[] textFields);

    /**
     * 添加元素到内部
     * @param textFields 输入的元素信息
     */
    void addElement(JTextField[] textFields);

    /**
     * 将内部信息输出到外部
     * @return object[][],规整后的内部数据
     */
    Object[][] ElementData();

    /**
     * 设定元素表格列名
     * @return 元素表格列名
     */
    String[] setElementColumn();

    /**
     * 检查该元素是否满足可移除条件
     * @param Element String,待删除对象名称
     * @return boolean,如果该对象可以删除，则返回true；否则，返回false
     */
    boolean controlDelete(String Element);

    /**
     * 删除元素
     * @param Element String,待删除对象名称
     */
    void DeleteElement(String Element);

    /**
     * 设定选项1名称
     * @return String选项1名称
     */
    String setStrategy1();

    /**
     * 设定选项2名称
     * @return String选项2名称
     */
    String setStrategy2();

    /**
     * 选择不同的执行策略
     * @param state 策略标志符
     * @param message 个性化消息组件（用作更改）
     */
    void strategy(int state, JLabel message);

    /**
     * 设定按钮3触发后的执行逻辑
     * @param message 个性化消息组件（用作更改）
     */
    void button3(JLabel message);
}
