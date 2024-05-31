package CourseScheduleApp;


import Interval.IntervalSet;

import java.util.Set;

public interface ICourseIntervalSet<L> {
    /**
     * 创建一个空课表
     *
     * @return IntervalSet<L>, 初始化一个空课表
     */
    public static<L> ICourseIntervalSet<L> empty(long begin, int Cycle) {
        return new CourseIntervalSet<>(begin, begin + 69, Cycle); //一周一共7天，每天10h，共70h
    }
    /**
     * 在课表中添加课程
     * <p>
     *    该方法跟据传入的课程与其对应的开始时间来在课表中添加进程运行记录
     *    当传入的课程的时间段与该课程其他时间段重叠时，本次插入无效
     *
     * @param start long,课程的开始时间
     * @param course L,课程对象
     */
    public void addCourse(long start, L course);

    /**
     * 移除课表中某个课程
     * <p>
     * 使用该方法移除课表中的某个课程后，课表中全部与目标课程相关记录均被删除
     *
     * @param course L,目标课程
     * @return boolean, Boolean, 当移除成功时返回true；否则，返回false
     */
    public boolean removeCourse(L course);

    /**
     * 查看课表中已添加课程
     *
     * @return Set<L>, 课表中已添加课程的集合
     */
    public Set<L> courses();

    /**
     * 检查课表中的某个课程已安排时间段
     * <p>
     * 该方法跟据传入的课程在课表中找到对应课程所关联的所有时间段，当传入的课程在课表中不存在时将返回空时间段集合
     * 时段段将由从小到大的顺序返回
     * example:
     * 当前对象为{ "A"=[[0,10],[20,30]],"B"=[[10,20]]}
     * 那么 intervals("A")返回的结果是 {0=[0,10],1=[20,30]}
     * @param course L,目标课程
     * @return IntervalSet<Integer>,某个课程已安排时间段
     */
    public IntervalSet<Integer> checkCourse(L course);

    /**
     * 设定学期的开始时间
     * @param begin 学期的开始时间
     */
    public void setBegin(long begin);

    /**
     * 获取学期的开始时间
     * @return long, 学期的开始时间
     */
    public long getBegin();

    /**
     * 设定学期周数
     * @param cycle int,目标学期周数
     */
    public void setCycle(int cycle);

    /**
     * 获取学期周数
     * @return int,学期周数
     */
    public int getCycle();

}
