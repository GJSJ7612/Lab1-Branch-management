package Framework;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameWork extends JFrame{
    public FrameWork(Operation operation){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(operation.setTitle());  //设定标题
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 960);
            while(!operation.initWindow());
            // 创建一个空白的JPanel作为空白区域
            JPanel blankPanel = new JPanel();
            blankPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            blankPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Component show = operation.special(); //在空白区域添加特殊操作
            blankPanel.add(show);
            // 创建按钮区域
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
            JButton button1 = new JButton(operation.setButton1()); //设定按钮1名称
            JButton button2 = new JButton(operation.setButton2()); //设定按钮2名称
            JButton button3 = new JButton(operation.setButton3()); //设定按钮3名称
            buttonPanel.add(button1);
            buttonPanel.add(button2);
            buttonPanel.add(button3);
            for (Component component : buttonPanel.getComponents()) {
                if (component instanceof JButton) {
                    component.setPreferredSize(new Dimension(300, 45));
                    component.setFont(component.getFont().deriveFont(component.getFont().getSize() * 1.5f));
                }
            }
            //创建显示比率区域
            JLabel message = new JLabel(operation.ShowMessage());
            message.setFont(message.getFont().deriveFont(message.getFont().getSize() * 1.5f));
            blankPanel.add(message);
            // 为按钮添加动作监听器
            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button1SubWindow(operation);
                }
            });
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button2SubWindow(operation, message);
                }
            });
            button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    operation.button3(message);
                }
            });


            frame.setLayout(new BorderLayout());
            frame.add(blankPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static void button1SubWindow(Operation operation) {
        SwingUtilities.invokeLater(() -> {
            JFrame subWindow = new JFrame();
            subWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            subWindow.setSize(800, 600);
            DefaultTableModel model = new DefaultTableModel(operation.ElementData(), operation.setElementColumn());
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 760));
            //创建组件
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));
            JButton addButton = new JButton("添加");
            JButton deleteButton = new JButton("删除");
            JButton closeButton = new JButton("关闭");
            buttonPanel.add(addButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(closeButton);
            for (Component component : buttonPanel.getComponents()) {
                if (component instanceof JButton) {
                    component.setPreferredSize(new Dimension(100, 30));
                }
            }
            subWindow.add(scrollPane, BorderLayout.CENTER);
            subWindow.add(buttonPanel, BorderLayout.SOUTH);
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object[] attributes = addSubWindow(operation);
                    if(attributes != null) model.addRow(attributes);
                }
            });
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // 从模型中删除选中的行
                        String elementToDelete = (String) model.getValueAt(selectedRow, 0);
                        if(operation.controlDelete(elementToDelete)){
                            model.removeRow(selectedRow);
                            operation.DeleteElement(elementToDelete);
                        }
                        else{
                            JOptionPane.showMessageDialog(null,
                                    "Warning: 请先删除该员工的排班记录",
                                    "Warning",
                                    JOptionPane.WARNING_MESSAGE);
                        }

                    }
                }
            });

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    subWindow.dispose();
                }
            });
            subWindow.setVisible(true);
        });
    }

    private static Object[] addSubWindow(Operation operation){
        // 创建一个面板用于放置文本框
        JPanel panel = new JPanel();
        String[] attributes = operation.setElementColumn();
        JTextField[] textFields = new JTextField[attributes.length];
        panel.setLayout(new GridLayout(attributes.length, 2, 5, 5));
        for (int i = 0; i < attributes.length; i++) {
            panel.add(new JLabel(attributes[i]));
            textFields[i] = new JTextField(10);
            panel.add(textFields[i]);
        }

        // 使用JOptionPane来显示面板并获取用户输入
        String[] options = {"OK", "Cancel"};
        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                operation.setAddElementTitle(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        // 检查用户是否点击了“OK”按钮
        if (result == 0 && operation.checkElement(textFields)) {
            operation.addElement(textFields);
            Object[] data = new Object[attributes.length];
            for(int i = 0; i < attributes.length; i++){
                data[i] = textFields[i].getText();
            }
            return data;
        }
        else return null;
    }

    private static void button2SubWindow(Operation operation, JLabel message){
        SwingUtilities.invokeLater(() -> {
            // 创建一个按钮组以确保互斥性
            ButtonGroup group = new ButtonGroup();
            JRadioButton option1 = new JRadioButton(operation.setStrategy1(), true);
            JRadioButton option2 = new JRadioButton(operation.setStrategy2(), false);
            group.add(option1);
            group.add(option2);

            // 创建一个面板用于放置单选按钮
            JPanel panel = new JPanel();
            panel.add(option1);
            panel.add(option2);

            // 弹出自定义对话框
            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "选择方式",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null
            );

            // 根据用户选择执行相应操作
            if (result == JOptionPane.OK_OPTION) {
                if (option1.isSelected()) {
                    operation.strategy(1, message);
                } else if (option2.isSelected()) {
                    operation.strategy(2, message);
                }
            }
        });
    }

}
