package StrategyTest;

import ProcessScheduleApp.IProcessIntervalSet;
import ProcessScheduleApp.MyProcess;
import ProcessScheduleApp.ProcessIntervalSet;
import org.junit.Test;
import strategy.StrategyProcess;
import strategy.StrategyProcessPriority;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PriorityTest {

    /**
     * 测试策略：
     * 1.按照进程数划分：
     *      测试0个进程的情况：预期返回null
     *      测试1个进程的情况：预期返回该进程
     *      测试3个进程的情况：预期返回距离其最大执行时间差距最小的进程
     * 2.测试运行部分进程后的情况：预期返回距离其最大执行时间差距最小的进程
     * 3.测试存在已结束进程的情况：预期返回未完成距离其最大执行时间差距最小的进程
     */
    @Test
    public void StrategyProcessPriorityTest(){
        StrategyProcess strategyProcess = new StrategyProcessPriority();
        Map<String, MyProcess> map = new HashMap<>();
        IProcessIntervalSet<MyProcess> processIntervalSet = new ProcessIntervalSet<MyProcess>(0,1000);
        //覆盖0个进程的情况
        assertNull(strategyProcess.SelectProcess(map, processIntervalSet));
        //覆盖1个进程的情况
        MyProcess first = new MyProcess(123, "first", 40, 60);
        map.put("first", first);
        assertEquals(map.get("first"), strategyProcess.SelectProcess(map,processIntervalSet));
        //覆盖3个进程的情况
        MyProcess second = new MyProcess(95, "second", 50, 70);
        MyProcess third = new MyProcess(819, "third", 30, 50);
        map.put("second", second);
        map.put("third", third);
        assertEquals(map.get("third"), strategyProcess.SelectProcess(map,processIntervalSet));
        //覆盖运行部分进程后的情况
        processIntervalSet.addProcess(0,40, second);
        assertEquals(map.get("second"), strategyProcess.SelectProcess(map,processIntervalSet));
        //覆盖存在已结束进程的情况
        processIntervalSet.addProcess(50,70, second);
        assertEquals(map.get("third"), strategyProcess.SelectProcess(map,processIntervalSet));
    }
}
