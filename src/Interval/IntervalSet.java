package Interval;

import Visitor.IntervalVisitor;

import java.util.Set;

public interface IntervalSet<L>{
    /**
     * 创建一个空的时间段集合
     *
     * @return IntervalSet<L>, 初始化一个空的时间段集合
     */
    public static<L> IntervalSet<L> empty(long begin, long over) {
        return new CommonIntervalSet<>(begin, over);
    }

    /**
     * 在当前对象中插入新的时间段和标签
     * <p>
     * 在当前时间段集合中添加一个时间段，该时间段是跟据传入的标签、开始时间、终止时间决定的
     * 当传入的标签已经存在所属的时间段，则本次创建的结果将会覆盖之前已有时间段
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
     * 该方法跟据传入的标签来移除其关联的时间段，当移除成功时返回true；否则，返回false
     * @param label L, 删除目标的标签
     * @return Boolean, 当移除成功时返回true；否则，返回false
     */
    public boolean remove(L label);

    /**
     * 返回某个标签对应的时间段的开始时间
     *
     * @param label L, 获取目标的标签
     * @return long, 对应标签所属时间段的起始时间
     */
    public long start(L label);

    /**
     * 返回某个标签对应的时间段的结束时间
     *
     * @param label L, 获取目标的标签
     * @return long, 对应标签所属时间段的终止时间
     */
    public long end(L label);

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
    public double accept(IntervalVisitor<L> v);
}
