package delegationInterface.Period;

public interface Period {
    /**
     * 获取设定的重复次数
     * @return 设定的重复次数
     */
    public int getTimes();

    /**
     * 设定重复次数
     * @param times, int, 设定的重复次数
     */
    public void setTimes(int times);
}
