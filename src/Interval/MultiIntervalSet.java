package Interval;

import Visitor.IntervalVisitor;
import Visitor.MultiIntervalSetVisitor;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import java.util.Set;

public interface MultiIntervalSet<L>{

    /**
     * 创建一个空的时间段集合
     *
     * @return MultiIntervalSet<L>, 初始化一个空的时间段集合
     */
    public static<L> MultiIntervalSet<L> empty(long begin, long end) {
        return new CommonMultiIntervalSet<>(begin, end);
    }

    /**
     * 在当前对象中插入新的时间段和标签
     * <p>
     * 在当前时间段集合中添加一个时间段，该时间段是跟据传入的标签、开始时间、终止时间决定的
     * 当欲插入时间段与当前标签下其他所属时间段产生重叠，则取消本次插入
     * 当传入的时间段在限定时间段外时，本次插入无效
     *
     * @param start long, 表示一个时间段的开始时间
     * @param end long, 表示一个时间段的终止时间
     * @param label L, 表示一个时间段的所属标签
     */
    public void insert(long start, long end, L label);

    /**
     * 获得当前对象中的标签集合
     *
     * @return Set<L>, 当前对象中包含的标签的集合
     */
    public Set<L> labels();

    /**
     * 从当前对象中移除某个标签所关联的时间段
     * <p>
     * 该方法跟据传入的标签来移除其所有关联的时间段，当移除成功时返回true；否则，返回false
     *
     * @param label L, 删除目标的标签
     * @return Boolean, 当移除成功时返回true；否则，返回false
     */
    public boolean remove(L label);

    /**
     * 从当前对象中获取与某个标签所关联的所有时间段
     * <p>
     * 该方法跟据传入的标签在当前对象中找到对应标签所关联的所有时间段，当传入的标签在当前对象中不存在时将返回空时间段集合
     * 时段段将由从小到大的顺序返回
     * example:
     * 当前对象为{ "A"=[[0,10],[20,30]],"B"=[[10,20]]}
     * 那么 intervals("A")返回的结果是 {0=[0,10],1=[20,30]}
     *
     * @param label L, 获取目标的标签
     * @return IntervalSet<Integer>, 传入标签对应的所有时间段,返回结果表达为IntervalSet<Integer>的形式
     * 其中的时间段按开始时间从小到大的次序排列。
     */
    public IntervalSet<Integer> intervals(L label);

    /**
     * 获取限定时间段的起始时间
     * @return long, 限定时间段的起始时间
     */
    public long getBegin();

    /**
     * 获取限定时间段的终止时间
     * @return long, 限定时间段的终止时间
     */
    public long getOver();

    /**
     * 设定限定时间段的起始时间
     * @param begin, long,设定起始时间
     */
    public void setBegin(long begin);

    /**
     * 设定限定时间段的终止时间
     * @param over, long,设定的终止时间
     */
    public void setOver(long over);

    /**
     * 扩展功能接口
     * @param v 扩展功能接口
     */
    public double accept(MultiIntervalSetVisitor<L> v);
}
