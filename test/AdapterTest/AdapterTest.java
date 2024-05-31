package AdapterTest;

import Adaptor.Adaptor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import Adaptor.AdaptorImpl;

public class AdapterTest {
    /**
     * 测试策略：
     * 按照是否符合转化格式划分：
     *       测试符合转化格式：预期正确转化
     *       测试不符合转化格式：预期返回-1
     */
    @Test
    public void translatorStringToLongTest(){
        Adaptor adapter = new AdaptorImpl();
        //覆盖符合转化格式的情况
        assertEquals(1714492800000L, adapter.translatorStringToLong("2024-05-01"));
        //覆盖不符合转化规格的情况
        assertEquals(-1, adapter.translatorStringToLong("hgdrf"));
    }

    /**
     * 测试是否正确转化
     */
    @Test
    public void translatorLongToStringTest(){
        Adaptor adapter = new AdaptorImpl();
        //覆盖符合转化格式的情况
        assertEquals("2024-05-01", adapter.translatorLongToString(1714492800000L));
    }
}
