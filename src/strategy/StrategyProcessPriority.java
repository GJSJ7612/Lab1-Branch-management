package strategy;

import ProcessScheduleApp.IProcessIntervalSet;
import ProcessScheduleApp.MyProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StrategyProcessPriority implements StrategyProcess{
    /**
     * 实现“最短进程优先”的模拟策略：每次选择进程的时候，优先选择距离其最大执行时间差距最小的进程。
     * @param map Map<String, MyProcess>进程名称和进程的映射，键为进程的名称，值为进程对象本身
     * @param processIntervalSet IProcessIntervalSet<MyProcess>，当前已运行进程时间段集合
     * @return MyProcess，距离其最大执行时间差距最小的进程
     */
    @Override
    public MyProcess SelectProcess(Map<String, MyProcess> map, IProcessIntervalSet<MyProcess> processIntervalSet) {
        MyProcess ret = null;
        long min = Long.MAX_VALUE;
        for(MyProcess process : map.values()) {
            if (process.getminTime() <= processIntervalSet.runTime(process) && processIntervalSet.runTime(process) <= process.getmaxTime()) {
                continue;
            }
            if ((process.getmaxTime() - processIntervalSet.runTime(process)) < min) {
                min = process.getmaxTime() - processIntervalSet.runTime(process);
                ret = process;
            }
        }
        return ret;
    }
}
