package strategy;

import Interval.MultiIntervalSet;
import ProcessScheduleApp.IProcessIntervalSet;
import ProcessScheduleApp.MyProcess;

import java.util.Map;

public interface StrategyProcess {
     /**
      * 从未结束的进程中选择一个进程
      *
      * @param map Map<String, MyProcess>进程名称和进程的映射，键为进程的名称，值为进程对象本身
      * @param processIntervalSet IProcessIntervalSet<MyProcess>，当前已运行进程时间段集合
      * @return MyProcess，进程对象
      */
     MyProcess SelectProcess(Map<String, MyProcess> map, IProcessIntervalSet<MyProcess> processIntervalSet);
}
