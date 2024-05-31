package ProcessScheduleApp;

import Interval.IntervalSet;

import java.util.Set;

public interface IProcessIntervalSet<L>{

    /**
     * 创建一个空进程序列
     *
     * @return IntervalSet<L>, 初始化一个空进程序列
     */
    public static<L> IProcessIntervalSet<L> empty() {
        return new ProcessIntervalSet<L>(0, Long.MAX_VALUE);
    }

    /**
     * 在进程序列中添加一次进程活动
     * <p>
     * 该方法跟据传入的进程与其对应的开始时间、结束时间来在进程序列中添加进程运行记录
     * 当传入的进程的时间段与该进程其他时间段重叠时，本次插入无效
     * 当传入的进程的时间段与其他进程时间段产生重叠时，本次插入无效
     *
     * @param start long,添加的进程活动开始时间
     * @param end long,添加的进程结束时间
     * @param progress L, 添加的进程
     */
    public void addProcess(long start, long end, L progress);

    /**
     * 获取当前进程序列中的进程集合
     *
     * @return Set<L>,当前进程序列中的进程集合
     */
    public Set<L> processes();

    /**
     * 获取目标进程的当前已运行时间
     *
     * @param progress L,目标进程
     * @return long, 目标进程当前已运行时间
     */
    public long runTime(L progress);

    /**
     * 获取某个进程当前已运行时间段
     * <p>
     * 该方法跟据传入的标签在当前对象中找到对应进程所关联的所有时间段，当传入的进程在当前进程序列中不存在时将返回空时间段集合
     * 时段段将由从小到大的顺序返回
     *  example:
     *      当前对象为{ "A"=[[0,10],[20,30]],"B"=[[10,20]]}
     *      那么 checkProgress("A")返回的结果是 {0=[0,10],1=[20,30]}
     * @param progress L, 目标进程
     * @return IntervalSet<Integer>,某个进程当前已运行时间段
     */
    public IntervalSet<Integer> checkProgress(L progress);

    /**
     * 设定限定时间段的终止时间
     * @param over, long,设定的终止时间
     */
    public void setOver(long over);

}
