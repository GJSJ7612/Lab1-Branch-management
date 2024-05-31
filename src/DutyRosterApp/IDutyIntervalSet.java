package DutyRosterApp;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import delegationInterface.NoBlankIntervalSet.NoBlankIntervalSet;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import java.util.Set;

public interface IDutyIntervalSet<L>{

    /**
     * 创建一个空排班表
     *
     * @return IntervalSet<L>, 初始化一个空排班表
     */
    public static<L> DutyIntervalSet<L> empty(long begin, long over) {
        return new DutyIntervalSet<L>(begin, over);
    }

    /**
     * 在当前排班表中增加新的值班人员
     * <p>
     * 该方法跟据传入的员工与其对应的开始工作时间、工作天数来在排班表上添加该员工的工作日程
     * 当传入的员工已经在排版表中存在，则本次创建的结果将会覆盖之前已有记录
     * 当传入的员工的工作时间段在限定时间段外时，本次插入无效
     * 当传入的员工的工作时间段与已安排时间段产生重叠时，本次插入无效
     *
     * @param start long, 表示员工工作的开始时间
     * @param end long,表示员工工作的结束时间
     * @param employee L, 表示员工
     * @throws IntervalConflictException 当添加的排班时间与已有时间发生冲突时抛出此异常
     */
    public void addEmployee(long start, long end, L employee) throws IntervalConflictException;

    /**
     * 获得当前排班表中已添加员工集合
     *
     * @return Set<L>, 当前排班表中已添加员工集合
     */
    public Set<L> Employees();

    /**
     * 从排版表中移除某个员工及其排班记录
     * <p>
     * 从排版表中移除某个员工及其排班记录，当移除成功时返回true；否则，返回false
     * @param employee L, 删除目标员工
     * @return Boolean, 当移除成功时返回true；否则，返回false
     */
    public boolean removeEmployee(L employee);

    /**
     * 返回某个员工的工作时间段
     * <p>
     * 该方法跟据传入的员工返回其对应在排班表上的工作时间段，起止时间由long[]给出
     * 第零号元素为该员工的开始工作时间，第一号元素为该员工的终止工作时间
     * @param employee L,目标员工
     * @return long[], 对应员工的起止工作时间
     */
    public long[] checkEmployeeStartToEnd(L employee);

    /**
     * 返回某个员工的工作天数
     *
     * @param employee L, 目标员工
     * @return int, 对应员工的工作天数
     */
    public int checkEmployeeTime(L employee);

    /**
     * 检查排班表中是否存在空白
     * <p>
     * 该方法用于保证排班表中不存在空白，若排班表中存在空白，则返回false；若排班表中不存在空白则返回true
     * @return boolean,若排班表中存在空白，则返回false；若排班表中不存在空白,则返回true
     */
    public boolean checkNoBlank();
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
     * 计算当前的空闲比率
     *
     * @return double, 空闲时间比率
     */
    public double calculateFreeTime();
}
