import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Explain {
    JFrame frame = new JFrame("协议书");
    // 得到显示器屏幕的宽、高
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    public Explain() {
        init();
    }

    public void init() {
        //设置字体
        Font font = new Font("System", Font.PLAIN, 18);
        JLabel title = new JLabel("协议书");
        title.setBounds((int) (width * 0.11), (int) (height * 0.014), (int) (width * 0.12), (int) (height * 0.03));
        title.setFont(font);


        JTextArea msg = new JTextArea();
//        msg.setBounds((int)(width*0.01),(int)(height*0.04),(int)(width*0.23),(int)(height*0.41));
        msg.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        //取消直接从文件读入文本
//        msg.append(readFile("record/book"));
        String book = "声明：\n" +
                "        本软件的开发为学习和自行娱乐使用，使用者" +
                "不得用其做损害他人利益的事。一经使用，概" +
                "不负责！（同意即代表接受本协议！）\n" +
                "使用规则：\n" +
                "        1、同意协议\n" +
                "        2、输入【登录码】\n" +
                "        3、将要发送的文本写入上方文本框中\n" +
                "        4、设定发送的条数和间隔时间\n" +
                "        5、点击发送\n" +
                "        6、五秒内将鼠标箭头放到要发送的窗口并点击\n" +
                "        7、完成！\n" +
                "\t【祝你使用愉快】";
        msg.append(book);
        msg.setEditable(false);
        msg.setLineWrap(true);        //登录自动换行功能
        msg.setWrapStyleWord(true);            // 登录断行不断字功能

        //设置滚动条
        JScrollPane jScrollPane = new JScrollPane(msg);
        jScrollPane.setBounds((int) (width * 0.01), (int) (height * 0.04), (int) (width * 0.23), (int) (height * 0.41));
        jScrollPane.setBorder(null); // 去除边框

        final JRadioButton jr1 = new JRadioButton("同意");
        jr1.setBounds((int) (width * 0.035), (int) (height * 0.46), (int) (width * 0.1), (int) (height * 0.02));
        jr1.setFont(font);
        jr1.setOpaque(false);

        JRadioButton jr2 = new JRadioButton("不同意");
        jr2.setBounds((int) (width * 0.17), (int) (height * 0.46), (int) (width * 0.1), (int) (height * 0.02));
        jr2.setFont(font);
        jr2.setOpaque(false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(jr1);
        bg.add(jr2);

        JButton cancel = new JButton("取消");
        cancel.setBackground(Color.WHITE);
        cancel.setFont(font);
        cancel.setBounds((int) (width * 0.04), (int) (height * 0.49), (int) (width * 0.06), (int) (height * 0.04));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        JButton OK = new JButton("确定");
        OK.setBackground(Color.WHITE);
        OK.setFont(font);
        OK.setBounds((int) (width * 0.15), (int) (height * 0.49), (int) (width * 0.06), (int) (height * 0.04));
        //读取文本内容 等于0有协议 不等于0无协议
        final String count = readFile("record/count");

        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countToInt = Integer.parseInt(count);
                if (!jr1.isSelected()) {
                    JOptionPane.showMessageDialog(null, "如要使用，请同意本协议！", "提醒", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                String code = JOptionPane.showInputDialog("请输入登录码！");
                System.out.println(code);
                if (code == null || code.equals("")) {
                    System.out.println("取消事件。。");
                    return;
                }

                if (!code.equals(getCode())) {
                    JOptionPane.showMessageDialog(null, "登录码错误！", "提醒", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                countToInt++;
                write(countToInt + "");
                frame.dispose();
                new WeChatBoom();
                System.out.println("可以开始使用！");
            }
        });
        //设置背景图片
        JLabel backgroud = new JLabel();
        backgroud.setBounds(0, 0, (int) (width * 0.25), (int) (height * 0.6));
        ImageIcon imageIcon = new ImageIcon("img/backgroud1.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance((int) (width * 0.25), (int) (height * 0.6), Image.SCALE_DEFAULT));
        backgroud.setIcon(imageIcon);

        frame.setSize((int) (width * 0.25), (int) (height * 0.6));
        frame.setLayout(null);

        frame.add(title);
        frame.add(jScrollPane);
        frame.add(jr1);
        frame.add(jr2);
        frame.add(cancel);
        frame.add(OK);
        frame.add(backgroud);

        try {
            frame.setIconImage(ImageIO.read(new FileInputStream(new File("img/logo.png")))); // 设置图标
        } catch (IOException e) {
            e.printStackTrace();
        }
        //窗口居中
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * 读取文件
     *
     * @param
     */
    public static String readFile(String path) {
        int position = 0;
        String[] bufstring = new String[1024];
        String str = "";
        //打开带读取的文件
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String count = "";
        try {
            while ((line = br.readLine()) != null) {
                str += line;
            }
            br.close();//关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        //用于计算count值
        return str;
    }

    /**
     * 写入文件
     *
     * @param
     */
    public static void write(String count) {
        File file = new File("record/count");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(count.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前时间，以小时和分钟作为登录密码
     */
    public static String getCode() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");//可以方便地修改日期格式
        String currentTime = dateFormat.format(now);
        String[] split = currentTime.split(":");
        String code = split[0] + split[1];
        System.out.println("登录码为：" + code);
        return code;
    }

    public static void main(String[] args) {
        String count = readFile(
                "record/count");
        int countToInt = Integer.parseInt(count);
        if (countToInt == 0) {
            new Explain();
        } else {
            String code = JOptionPane.showInputDialog("请输入登录码！");
            System.out.println(code);
            if (code == null || code.equals("")) {
                return;
            }
            if (!getCode().equals(code)) {
                JOptionPane.showMessageDialog(null, "登录码错误！", "提醒", JOptionPane.PLAIN_MESSAGE);
                return;
            } else {
                countToInt++;
                write(countToInt + "");
                new WeChatBoom();
            }
        }
    }
}
