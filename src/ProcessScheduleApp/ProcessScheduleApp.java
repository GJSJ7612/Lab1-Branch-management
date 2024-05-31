package ProcessScheduleApp;

import Framework.FrameWork;
import Framework.Operation;
import strategy.StrategyProcess;
import strategy.StrategyProcessPriority;
import strategy.StrategyProcessRandom;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProcessScheduleApp implements Operation {
    private final Map<String, MyProcess> map = new HashMap<>();
    private final IProcessIntervalSet<MyProcess> processIntervalSet = IProcessIntervalSet.empty();
    private ProcessDiagram processDiagram;
    private long currentTime = 0;
    private long over;
    @Override
    public Component special() {
        this.processDiagram = new ProcessDiagram();
        return processDiagram;
    }

    @Override
    public String setTitle() {
        return "进程调度管理系统";
    }

    @Override
    public String setButton1() {
        return "进程管理";
    }

    @Override
    public String setButton2() {
        return "进程模拟";
    }

    @Override
    public String setButton3() {
        return "添加空白";
    }

    @Override
    public String setAddElementTitle() {
        return "添加进程";
    }

    @Override
    public String ShowMessage() {
        return "已运行时间为：" + (currentTime);
    }

    @Override
    public boolean initWindow() {
        return true;
    }

    @Override
    public boolean checkElement(JTextField[] textFields) {
        if (!textFields[0].getText().matches("[0-9]{1,5}")) {
            JOptionPane.showMessageDialog(null,
                    "Warning: 进程号不合规!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(textFields[1].getText().isBlank()){
            JOptionPane.showMessageDialog(null,
                    "Warning: 进程名称不能为空!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!textFields[2].getText().matches("\\d*")){
            JOptionPane.showMessageDialog(null,
                    "Warning: 进程最短运行时间应为自然数!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!textFields[3].getText().matches("\\d*")){
            JOptionPane.showMessageDialog(null,
                    "Warning: 进程最长运行时间应为自然数!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(Long.parseLong(textFields[3].getText()) < Long.parseLong(textFields[2].getText())){
            JOptionPane.showMessageDialog(null,
                    "Warning: 进程最长运行时间应大于等于最短运行时间!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public void addElement(JTextField[] textFields) {
        MyProcess process = new MyProcess(Integer.parseInt(textFields[0].getText()),textFields[1].getText(), Long.parseLong(textFields[2].getText()), Long.parseLong(textFields[3].getText()));
        map.put(textFields[1].getText(), process);
    }

    @Override
    public Object[][] ElementData() {
        Object[][] data =  new Object[map.size()][4];
        int index = 0;
        for(String name : map.keySet()){
            data[index][0] = map.get(name).getPID();
            data[index][1] = name;
            data[index][2] = map.get(name).getminTime();
            data[index++][3] = map.get(name).getmaxTime();
        }
        return data;
    }

    @Override
    public String[] setElementColumn() {
        return new String[]{"进程号", "进程名称","进程最短运行时间","进程最大运行时间"};
    }

    @Override
    public boolean controlDelete(String Element) {
        return !processIntervalSet.processes().contains(map.get(Element));
    }

    @Override
    public void DeleteElement(String Element) {
        map.remove(Element);
    }

    @Override
    public String setStrategy1() {
        return "随机选择进程";
    }

    @Override
    public String setStrategy2() {
        return "最短进程优先";
    }

    @Override
    public void strategy(int state, JLabel message) {
        MyProcess process = null;
        if(state == 1){
            StrategyProcess strategyProcess = new StrategyProcessRandom();
            process = strategyProcess.SelectProcess(map, processIntervalSet);
        }
        else if(state == 2){
            StrategyProcess strategyProcess = new StrategyProcessPriority();
            process = strategyProcess.SelectProcess(map, processIntervalSet);
        }
        if(process != null){
            JPanel panel = new JPanel();
            String text = "当前所选进程为：" + process.getname() + "  已运行时间为：" + processIntervalSet.runTime(process)+
                    "  最多运行时间为：" + (process.getmaxTime() - processIntervalSet.runTime(process)) + "  最少运行时间为：" + (process.getminTime() - processIntervalSet.runTime(process)) +
                    "  请输入运行进程的时间：";
            JLabel jLabel = new JLabel();
            jLabel.setText(text);
            JTextField textField = new JTextField(10);
            panel.add(new JLabel(text));
            panel.add(new JLabel());
            panel.add(textField);
            String[] options = {"OK"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "输入进程运行时间",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
            // 检查用户是否点击了“OK”按钮
            if (result == 0) {
                if(!textField.getText().matches("\\d*") || Long.parseLong(textField.getText()) <= 0){
                    JOptionPane.showMessageDialog(null,
                            "Warning: 运行时间应大于0!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }else if(Long.parseLong(textField.getText()) > (process.getmaxTime() - processIntervalSet.runTime(process))){
                    JOptionPane.showMessageDialog(null,
                            "Warning: 运行时间应小于等于剩余可执行时间!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                else{
                    long time = Long.parseLong(textField.getText())-1;
                    processIntervalSet.addProcess(currentTime, currentTime+time, process);
                    this.over = currentTime+time;
                    processDiagram.addProcess(currentTime, currentTime+time, process.getname(), over);
                    currentTime += time+1;
                    message.setText("已运行时间为：" + (currentTime));
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null,
                    "Warning: 所有进程都已结束",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void button3(JLabel message) {
        JPanel panel = new JPanel();
        String text = "添加空白时间：";
        JLabel jLabel = new JLabel();
        jLabel.setText(text);
        JTextField textField = new JTextField(10);
        panel.add(new JLabel(text));
        panel.add(new JLabel());
        panel.add(textField);
        String[] options = {"OK"};
        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                "输入空白时间",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        // 检查用户是否点击了“OK”按钮
        if (result == 0) {
            if(!textField.getText().matches("\\d*") || Long.parseLong(textField.getText()) < 0) {
                JOptionPane.showMessageDialog(null,
                        "Warning: 运行时间应大于等于0!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            currentTime += Long.parseLong(textField.getText());
            this.over = currentTime-1;
            processDiagram.addProcess(over);
        }
        message.setText("已运行时间为：" + (currentTime));
    }

    public static void main(String[] args) {
        FrameWork fw = new FrameWork(new ProcessScheduleApp());
    }
}
