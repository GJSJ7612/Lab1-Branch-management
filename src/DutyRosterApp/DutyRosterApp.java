package DutyRosterApp;

import Adaptor.Adaptor;
import Adaptor.AdaptorImpl;
import Framework.FrameWork;
import Framework.Operation;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

public class DutyRosterApp implements Operation {
    private final Map<String, Employee> map = new HashMap<>();
    private IDutyIntervalSet<Employee> dutyIntervalSet;
    private final Adaptor adaptor = new AdaptorImpl();
    private DefaultTableModel model;
    private long begin;
    private long over;

    @Override
    public Component special() {
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1250, 700));
        return scrollPane;
    }

    @Override
    public String setTitle() {
        return "值班表管理系统";
    }

    @Override
    public String setButton1() {
        return "管理员工";
    }

    @Override
    public String setButton2() {
        return "排班";
    }

    @Override
    public String setButton3() {
        return "删除排班记录";
    }

    @Override
    public String setAddElementTitle() {
        return "添加员工";
    }

    @Override
    public String ShowMessage() {
        return "排班开始时间：" + adaptor.translatorLongToString(dutyIntervalSet.getBegin()) + "        "
                + "排班结束时间：" + adaptor.translatorLongToString(dutyIntervalSet.getOver()) + "        "
                + "空白时间比率：" + dutyIntervalSet.calculateFreeTime();
    }

    @Override
    public boolean initWindow() {
        JTextField[] textFields;
        while (true) {
            // 创建一个面板用于放置文本框
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2, 5, 5));

            // 创建标签和文本框
            String[] attributes = {"排班开始时间：yyyy-mm-dd  ", "排班结束时间：yyyy-mm-dd  "};
            textFields = new JTextField[3];

            for (int i = 0; i < attributes.length; i++) {
                panel.add(new JLabel(attributes[i]));
                textFields[i] = new JTextField(10);
                panel.add(textFields[i]);
            }

            // 使用JOptionPane来显示面板并获取用户输入
            String[] options = {"OK", "导入文件", "Cancel"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "设定排班期限",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            // 检查用户是否点击了“OK”按钮
            if (result == 0) {
                if (!textFields[0].getText().matches("^\\d{4}-\\d{2}-\\d{2}") || !textFields[1].getText().matches("^\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(null,
                            "Warning: 排班时间要符合格式yyyy-MM-dd",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }else if(adaptor.translatorStringToLong(textFields[0].getText()) > adaptor.translatorStringToLong(textFields[1].getText())){
                    JOptionPane.showMessageDialog(null,
                            "Warning: 排班结束时间要大于等于排班开始时间",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                else{
                    begin = adaptor.translatorStringToLong(textFields[0].getText());
                    over = adaptor.translatorStringToLong(textFields[1].getText());
                    this.dutyIntervalSet = IDutyIntervalSet.empty(begin,over);
                    break;
                }
            } else if (result == 1) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
                int file = fileChooser.showOpenDialog(panel);
                if (file == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (fileRead(selectedFile)) break;
                }
            } else exit(0);
        }
        int delta = (int) ((over - begin) / 86400000 + 1);
        String[] column = new String[]{"日期", "姓名", "职务", "手机号"};
        Object[][] data = new Object[delta][4];
        int index = 0;
        for (Employee employee : dutyIntervalSet.Employees()) {
            long start = dutyIntervalSet.checkEmployeeStartToEnd(employee)[0];
            long end = dutyIntervalSet.checkEmployeeStartToEnd(employee)[1];
            for (long i = start; i <= end; i += 86400000) {
                data[index][0] = adaptor.translatorLongToString(i);
                data[index][1] = employee.getName();
                data[index][2] = employee.getOffice();
                data[index++][3] = employee.getPhoneNumber();
            }
        }
        this.model = new DefaultTableModel(data, column);
        for (long i = begin; i <= over; i += 86400000) {
            model.setValueAt(adaptor.translatorLongToString(i), (int) ((i - begin) / 86400000), 0);
        }
        return true;
    }

    @Override
    public boolean checkElement(JTextField[] textFields) {
        if (textFields[0].getText().isBlank()) {
            JOptionPane.showMessageDialog(null,
                    "Warning: 姓名不能为空!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(textFields[1].getText().isBlank()){
            JOptionPane.showMessageDialog(null,
                    "Warning: 职务不能为空!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!textFields[2].getText().matches("[0-9]{11}")){
            JOptionPane.showMessageDialog(null,
                    "Warning: 手机号应该为11位数字!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public void addElement(JTextField[] textFields){
        Employee employee = new Employee(textFields[0].getText(), textFields[1].getText(), textFields[2].getText());
        map.put(textFields[0].getText(), employee);
    }

    @Override
    public Object[][] ElementData() {
        Object[][] data =  new Object[map.size()][3];
        int index = 0;
        for(String name : map.keySet()){
            data[index][0] = name;
            data[index][1] = map.get(name).getOffice();
            data[index++][2] = map.get(name).getPhoneNumber();
        }
        return data;
    }

    @Override
    public String[] setElementColumn() {
        return new String[]{"姓名","职务","手机号"};
    }

    @Override
    public boolean controlDelete(String Element) {
        return dutyIntervalSet.checkEmployeeStartToEnd(map.get(Element)) == null;
    }

    @Override
    public void DeleteElement(String Element) {
        map.remove(Element);
    }

    @Override
    public String setStrategy1() {
        return "手动排班";
    }

    @Override
    public String setStrategy2() {
        return "自动排班";
    }

    @Override
    public void strategy(int state, JLabel message){
        if(state == 1){
            while(!dutyIntervalSet.checkNoBlank()){
                JPanel panel = new JPanel();
                // 创建标签和文本框
                String[] attributes = {"工作开始日期：yyyy-mm-dd  ", "工作结束日期：yyyy-mm-dd  ", "员工姓名"};
                JTextField[] textFields = new JTextField[3];

                for (int i = 0; i < attributes.length; i++) {
                    panel.add(new JLabel(attributes[i]));
                    textFields[i] = new JTextField(10);
                    panel.add(textFields[i]);
                }

                // 使用JOptionPane来显示面板并获取用户输入
                String[] options = {"OK"};
                int result = JOptionPane.showOptionDialog(
                        null,
                        panel,
                        "排班",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);

                // 检查用户是否点击了“OK”按钮
                if (result == 0) {
                    if(!textFields[0].getText().matches("^\\d{4}-\\d{2}-\\d{2}") || !textFields[1].getText().matches("^\\d{4}-\\d{2}-\\d{2}")){
                        JOptionPane.showMessageDialog(null,
                                "Warning: 排班时间要符合格式yyyy-MM-dd",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }else if(adaptor.translatorStringToLong(textFields[0].getText()) < dutyIntervalSet.getBegin() || adaptor.translatorStringToLong(textFields[1].getText()) > dutyIntervalSet.getOver()) {
                        JOptionPane.showMessageDialog(null,
                                "Warning: 排班时间在排班开始时间以内",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }else if(!map.containsKey(textFields[2].getText())){
                        JOptionPane.showMessageDialog(null,
                                "Warning: “" + textFields[2].getText() + "”不存在",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        long start = adaptor.translatorStringToLong(textFields[0].getText());
                        long end = adaptor.translatorStringToLong(textFields[1].getText());
                        try {
                            if(!dutyIntervalSet.Employees().contains(map.get(textFields[2].getText()))){
                                dutyIntervalSet.addEmployee(start, end, map.get(textFields[2].getText()));
                                for(long i = start; i <= end; i+= 86400000){
                                    model.setValueAt(map.get(textFields[2].getText()).getName(),(int)((i- dutyIntervalSet.getBegin())/86400000),1);
                                    model.setValueAt(map.get(textFields[2].getText()).getOffice(),(int)((i- dutyIntervalSet.getBegin())/86400000),2);
                                    model.setValueAt(map.get(textFields[2].getText()).getPhoneNumber(),(int)((i- dutyIntervalSet.getBegin())/86400000),3);
                                }
                            }
                            else{
                                for (int i = model.getRowCount() - 1; i >= 0; i--) {
                                    if(model.getValueAt(i, 1) != null && model.getValueAt(i, 1).equals(textFields[2].getText())){
                                        model.setValueAt(null, i, 1);
                                        model.setValueAt(null, i, 2);
                                        model.setValueAt(null, i, 3);
                                    }
                                }
                                dutyIntervalSet.addEmployee(start, end, map.get(textFields[2].getText()));
                                for(long i = start; i <= end; i+= 86400000){
                                    model.setValueAt(map.get(textFields[2].getText()).getName(),(int)((i- dutyIntervalSet.getBegin())/86400000),1);
                                    model.setValueAt(map.get(textFields[2].getText()).getOffice(),(int)((i- dutyIntervalSet.getBegin())/86400000),2);
                                    model.setValueAt(map.get(textFields[2].getText()).getPhoneNumber(),(int)((i- dutyIntervalSet.getBegin())/86400000),3);
                                }
                            }
                        }catch (IntervalConflictException e){
                            JOptionPane.showMessageDialog(null,
                                    "Warning: “排班记录发生重叠",
                                    "Warning",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        message.setText("排班开始时间：" + adaptor.translatorLongToString(dutyIntervalSet.getBegin()) + "        "
                                + "排班结束时间：" + adaptor.translatorLongToString(dutyIntervalSet.getOver()) + "        "
                                + "空白时间比率：" + dutyIntervalSet.calculateFreeTime());
                    }
                }
            }
        }
        else if(state == 2){
            Set<Employee> e = dutyIntervalSet.Employees();
            for(Employee employee : e){
                dutyIntervalSet.removeEmployee(employee);
                for (int i = model.getRowCount() - 1; i >= 0; i--) {
                    if(model.getValueAt(i, 1) != null && model.getValueAt(i, 1).equals(employee.getName())){
                        model.setValueAt(null, i, 1);
                        model.setValueAt(null, i, 2);
                        model.setValueAt(null, i, 3);
                    }
                }
            }
            int delta = (int)((over - begin)/86400000+1); int n = map.size();
            Random random = new Random();
            int remainingSum = delta;
            int[] days;
            if (n <= remainingSum){
                days = new int[n];
            }
            else days = new int[remainingSum];
            for (int i = 0; i < days.length-1; i++) {
                int max = remainingSum - (days.length-i);
                int num = random.nextInt(max) + 1;
                days[i] = num;
                remainingSum -= num;
            }
            days[days.length-1] = remainingSum;
            List<Employee> employees = new ArrayList<>(map.values());
            Collections.shuffle(employees);
            long start = begin;
            for(int i = 0; i < days.length; i++){
                try {
                    long end = start + days[i]*86400000L;
                    dutyIntervalSet.addEmployee(start, end-86400000, employees.get(i));
                    for(long j = start; j <= end-1; j+= 86400000){
                        model.setValueAt(employees.get(i).getName(),(int)((j- dutyIntervalSet.getBegin())/86400000),1);
                        model.setValueAt(employees.get(i).getOffice(),(int)((j- dutyIntervalSet.getBegin())/86400000),2);
                        model.setValueAt(employees.get(i).getPhoneNumber(),(int)((j- dutyIntervalSet.getBegin())/86400000),3);
                    }
                    start = end;
                }catch (IntervalConflictException ignored){
                }
            }
            message.setText("排班开始时间：" + adaptor.translatorLongToString(dutyIntervalSet.getBegin()) + "        "
                    + "排班结束时间：" + adaptor.translatorLongToString(dutyIntervalSet.getOver()) + "        "
                    + "空白时间比率：" + dutyIntervalSet.calculateFreeTime());
        }
    }

    @Override
    public void button3(JLabel message) {
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(10);
        panel.add(new JLabel("请输入想要删除的排班记录对应的员工姓名："));
        panel.add(textField);

        // 使用JOptionPane来显示面板并获取用户输入
        String[] options = {"OK"};
        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                "删除排班记录",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        // 检查用户是否点击了“OK”按钮
        if (result == 0) {
            if(!map.containsKey(textField.getText())){
                JOptionPane.showMessageDialog(null,
                        "Warning: “" + textField.getText() + "”不存在",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else if(!dutyIntervalSet.Employees().contains(map.get(textField.getText()))){
                JOptionPane.showMessageDialog(null,
                        "Warning: 该员工不存在排班记录",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            dutyIntervalSet.removeEmployee(map.get(textField.getText()));
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                if(model.getValueAt(i, 1) != null && model.getValueAt(i, 1).equals(textField.getText())){
                    model.setValueAt(null, i, 1);
                    model.setValueAt(null, i, 2);
                    model.setValueAt(null, i, 3);
                }
            }
            message.setText("排班开始时间：" + adaptor.translatorLongToString(dutyIntervalSet.getBegin()) + "        "
                    + "排班结束时间：" + adaptor.translatorLongToString(dutyIntervalSet.getOver()) + "        "
                    + "空白时间比率：" + dutyIntervalSet.calculateFreeTime());
        }
    }


    private boolean fileRead(File file){
        Pattern employee = Pattern.compile("([a-zA-Z]+)\\{([a-zA-Z ]*),([0-9]{3}-[0-9]{4}-[0-9]{4})}");
        Pattern period = Pattern.compile("Period\\{([0-9]{4}-[0-9]{2}-[0-9]{2}),([0-9]{4}-[0-9]{2}-[0-9]{2})}");
        Pattern roster = Pattern.compile("([a-zA-Z]+)\\{([0-9]{4}-[0-9]{2}-[0-9]{2}),([0-9]{4}-[0-9]{2}-[0-9]{2})}");
        Pattern others = Pattern.compile("Employee\\{|Roster\\{|^}");
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcherEmployee = employee.matcher(line.trim());
                Matcher matcherPeriod = period.matcher(line.trim());
                Matcher matcherRoster = roster.matcher(line.trim());
                Matcher matcherOthers = others.matcher(line.trim());
                if(matcherEmployee.matches()){
                    StringBuilder PhoneNumber = new StringBuilder();
                    for (int i = 0; i < matcherEmployee.group(3).length(); i++) {
                        if (matcherEmployee.group(3).charAt(i) != '-') {
                            PhoneNumber.append(matcherEmployee.group(3).charAt(i));
                        }
                    }
                    Employee e = new Employee(matcherEmployee.group(1), matcherEmployee.group(2), PhoneNumber.toString());
                    map.put(matcherEmployee.group(1), e);
                }
                else if(matcherPeriod.matches()){
                    begin = adaptor.translatorStringToLong(matcherPeriod.group(1));
                    over = adaptor.translatorStringToLong(matcherPeriod.group(2));
                    this.dutyIntervalSet = IDutyIntervalSet.empty(begin, over);
                }
                else if(matcherRoster.matches()){
                    dutyIntervalSet.addEmployee(Math.max(adaptor.translatorStringToLong(matcherRoster.group(2)), begin), Math.min(adaptor.translatorStringToLong(matcherRoster.group(3)),over), map.get(matcherRoster.group(1)));
                }
                else if(matcherOthers.matches()) ;
                else{
                    JOptionPane.showMessageDialog(null,
                            "Warning: 文件内容存在不合规，请重新检查",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        } catch (IOException ignored) {
        } catch (IntervalConflictException e) {
            JOptionPane.showMessageDialog(null,
                    "Warning: 排班时间发生冲突，请重新检查文件内容",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public static void main(String[] args){
        FrameWork fw = new FrameWork(new DutyRosterApp());
    }
}
