package strategy;

import ProcessScheduleApp.IProcessIntervalSet;
import ProcessScheduleApp.MyProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StrategyProcessRandom implements StrategyProcess{
    /**
     * 实现“随机选择进程”的模拟策略：每次选择进程的时候，随机选择当前未结束的进程。
     * @param map Map<String, MyProcess>进程名称和进程的映射，键为进程的名称，值为进程对象本身
     * @param processIntervalSet IProcessIntervalSet<MyProcess>，当前已运行进程时间段集合
     * @return 随机选择的当前未结束的进程
     */
    @Override
    public MyProcess SelectProcess(Map<String, MyProcess> map, IProcessIntervalSet<MyProcess> processIntervalSet) {
        List<MyProcess> list = new ArrayList<>();
        for(MyProcess process : map.values()){
            if(process.getminTime() <= processIntervalSet.runTime(process) && processIntervalSet.runTime(process) <= process.getmaxTime()){
                continue;
            }
            list.add(process);
        }
        Collections.shuffle(list);
        if(!list.isEmpty()) return list.get(0);
        return null;
    }
}
