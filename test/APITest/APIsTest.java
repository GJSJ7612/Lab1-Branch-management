package APITest;

import APIs.API;
import APIs.APIs;
import Interval.CommonMultiIntervalSet;
import Interval.MultiIntervalSet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class APIsTest {
    /**
     * 测试策略：
     * <p>
     * 1.按照不同标签重叠时间段划分：预期正常计算相似度
     * 2.按照同一标签下时间段重叠情况划分：预期正常计算相似度
     *      测试重叠发生在s1第一个区间的开始部分和s2第二个区间的结束部分：预期正常计算相似度
     *      测试重叠发生在s1第一个区间的开始部分和s2第二个区间的结束部分：预期正常计算相似度
     *      测试重叠发生在两个区间的中间部分：预期正常计算相似度
     * 3.特殊情况：
     *      测试s1与s2相等且为满的情况：预期相似度为1
     *      测试s1或s2为null的情况：预期抛出NullPointerException异常
     *      测试s1与s2完全不相等的情况：预期相似度为0
     *      测试s1与s2为空的情况：预期相似度为0
     *      测试案例：预期正常计算相似度
     */
    @Test
    public void APITest(){
        MultiIntervalSet<String> s1 = new CommonMultiIntervalSet<>(0,9);
        MultiIntervalSet<String> s2 = new CommonMultiIntervalSet<>(0,9);
        //覆盖s1与s2为空的情况
        APIs<String> api = new API<String>();
        assertEquals(0, api.Similarity(s1,s2), 0.0001);
        //覆盖s1与s2相等的情况
        s1.insert(0,9, "A");
        assertEquals(1, api.Similarity(s1,s1), 0.0001);
        //覆盖s1和s2完全不相等的情况
        s1.remove("A");
        s1.insert(2,4, "A");
        assertEquals(0, api.Similarity(s1,s2), 0.0001);
        //覆盖s1和s2为null的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> api.Similarity(null, s1));
        assertEquals("MultiIntervalSet can't be null", exception1.getMessage());
        Exception exception2 = assertThrows(NullPointerException.class, () -> api.Similarity(s1, null));
        assertEquals("MultiIntervalSet can't be null", exception2.getMessage());
        Exception exception3 = assertThrows(NullPointerException.class, () -> api.Similarity(null, null));
        assertEquals("MultiIntervalSet can't be null", exception3.getMessage());
        //覆盖s1第一个区间的开始部分和s2第二个区间的结束部分的情况
        s2.insert(1,3,"A");
        assertEquals(0.2, api.Similarity(s1,s2), 0.0001);
        //覆盖s1第一个区间的开始部分和s2第二个区间的结束部分的情况
        s2.insert(4,6,"A");
        assertEquals(0.3, api.Similarity(s1,s2), 0.0001);
        //覆盖重叠发生在两个区间的中间部分
        s1.remove("A");
        s2.remove("A");
        s1.insert(2,8,"A");
        s2.insert(4,6,"A");
        assertEquals(0.3, api.Similarity(s1,s2), 0.0001);
        //覆盖案例
        MultiIntervalSet<String> s3 = new CommonMultiIntervalSet<>(0,34);
        MultiIntervalSet<String> s4 = new CommonMultiIntervalSet<>(0,34);
        s3.insert(0,4,"A");
        s3.insert(10,19,"B");
        s3.insert(20,24,"A");
        s3.insert(25,39,"B");
        s4.insert(0,4,"C");
        s4.insert(10,19,"B");
        s4.insert(20,34,"A");
        assertEquals(0.42857, api.Similarity(s3,s4), 0.0001);
    }
}
