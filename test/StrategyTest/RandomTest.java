package StrategyTest;

import ProcessScheduleApp.IProcessIntervalSet;
import ProcessScheduleApp.MyProcess;
import ProcessScheduleApp.ProcessIntervalSet;
import org.junit.Test;
import strategy.StrategyProcess;
import strategy.StrategyProcessPriority;
import strategy.StrategyProcessRandom;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RandomTest {
    /**
     * 测试策略：
     * 按照进程数划分：
     *      测试0个进程：预期返回null
     *      测试1个进程：预期返回该进程
     * （由于随机性，较难测试2个以上的进程）
     */
    @Test
    public void StrategyProcessRandomTest(){
        StrategyProcess strategyProcess = new StrategyProcessRandom();
        Map<String, MyProcess> map = new HashMap<>();
        IProcessIntervalSet<MyProcess> processIntervalSet = new ProcessIntervalSet<MyProcess>(0,1000);
        //覆盖0个进程的情况
        assertNull(strategyProcess.SelectProcess(map, processIntervalSet));
        //覆盖1个进程的情况
        MyProcess first = new MyProcess(123, "first", 40, 60);
        map.put("first", first);
        assertEquals(map.get("first"), strategyProcess.SelectProcess(map,processIntervalSet));
    }
}
