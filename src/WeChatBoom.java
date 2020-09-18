import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WeChatBoom {

    //声明机器人
    Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    //获取系统剪贴板
    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
     * 初始化窗口
     */
    public void init() {
        // 得到显示器屏幕的宽、高
        final int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        final int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        //设置字体
        Font font = new Font("System", Font.PLAIN, 17);
        Font font1 = new Font("System", Font.PLAIN, 18);


        JFrame frame = new JFrame("微信轰炸机");
        final JTextArea msg = new JTextArea();//要发送的消息
        msg.setFont(font);
        msg.setOpaque(false);
        msg.setLineWrap(true);        //激活自动换行功能
        msg.setWrapStyleWord(true);            // 激活断行不断字功能

        JButton send = new JButton("发送");
        send.setBackground(Color.WHITE);
        send.setFont(font1);
        JButton cln = new JButton("清空");
        cln.setBackground(Color.WHITE);
        cln.setFont(font1);
        JLabel jL_times = new JLabel("发送条数");
        jL_times.setFont(font);
        final JTextField times = new JTextField();

        times.setFont(font);
        JLabel jL_delay = new JLabel("间隔时间/毫秒");
        jL_delay.setFont(font);
        jL_delay.setBounds((int) (width * 0.13), (int) (height * 0.23), (int) (width * 0.10), (int) (height * 0.033));

        final JTextField delay = new JTextField();
        delay.setFont(font);
        delay.setBounds((int) (width * 0.22), (int) (height * 0.23), (int) (width * 0.05), (int) (height * 0.03));

        msg.setBounds((int) (width * 0.008), (int) (height * 0.01), (int) (width * 0.234) + 70, (int) (height * 0.19));

        jL_times.setBounds((int) (width * 0.013), (int) (height * 0.23), (int) (width * 0.07), (int) (height * 0.033));
        times.setBounds((int) (width * 0.07), (int) (height * 0.23), (int) (width * 0.05), (int) (height * 0.03));
        cln.setBounds((int) (width * 0.019), (int) (height * 0.28), (int) (width * 0.1), (int) (height * 0.05));

        //设置背景图片
        final JLabel backgroud = new JLabel();
        backgroud.setBounds(0, 0, (int) (width * 0.25) + 70, (int) (height * 0.4));
        ImageIcon imageIcon = new ImageIcon("img/backgroud1.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance((int) (width * 0.25) + 70, (int) (height * 0.4), Image.SCALE_DEFAULT));
        backgroud.setIcon(imageIcon);


        /**
         * 清空内容
         */
        cln.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msg.setText("");
                times.setText("");
                delay.setText("");
            }
        });

        send.setBounds((int) (width * 0.17), (int) (height * 0.28), (int) (width * 0.1), (int) (height * 0.05));

        /**
         * 点击发送逻辑：
         * 1、获取msg文本域内容
         * 2、复制文本内容
         * 3、粘贴到剪切板，延迟4秒钟，打开qq/微信内容输入框，四秒钟后粘贴
         * 4、发送，3秒发送一次
         */
        final String regex = "^[0-9]+$";
        /**
         * 文本输入“更换皮肤，可进行换肤”
         */

        send.addActionListener(new ActionListener() {
            int bgImg = 1;//用于控制壁纸的更换
            /**
             * 背景音乐的控制
             */
            boolean musicflag = false;
            URL soundFile;

            {
                try {
                    soundFile = new URL("file:music/bgMusic.wav");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            AudioClip sound = Applet.newAudioClip(soundFile);

            @Override
            public void actionPerformed(ActionEvent e) {
                if (null == msg.getText() || "".equals(msg.getText())) {
                    JOptionPane.showMessageDialog(null, "发送内容不能为空！", "提醒", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                if ("更换皮肤".equals(msg.getText().trim())) {
                    String[] bgpath = {"img/backgroud1.png", "img/backgroud2.jpg", "img/backgroud3.png", "img/backgroud4.png", "img/backgroud3.jpg",};
                    ImageIcon imageIcon = new ImageIcon(bgpath[bgImg]);
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance((int) (width * 0.25) + 70, (int) (height * 0.4), Image.SCALE_DEFAULT));
                    backgroud.setIcon(imageIcon);
                    bgImg++;
                    if (bgImg == 5) {
                        bgImg = 0;
                    }
                    return;
                }
                if ("播放音乐".equals(msg.getText().trim())) {
                    musicflag = !musicflag;
                    System.out.println("musicflag:" + musicflag);
                    if (musicflag) {
                        sound.loop();
                        return;
                    } else {
                        sound.stop();
                        return;
                    }
                }


                /**
                 * 延迟时间和发送次数只能为正数
                 */
                if (!times.getText().matches(regex) || !delay.getText().matches(regex)) {
                    JOptionPane.showMessageDialog(null, "延迟时间和发送次数只能为正数！", "提醒", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                //设置反应时间：即运行程序时打开微信/QQ将鼠标放到发送文字区的时间
                robot.delay(5000);

                // 封装文本内容
                Transferable trans = new StringSelection(msg.getText());
                // 把文本内容设置到系统剪贴板
                clip.setContents(trans, null);

                /**
                 * 循环发送
                 */
                for (int i = 0; i < Integer.parseInt(times.getText()); i++) {
                    // 以下两行按下了ctrl+v，完成粘贴功能
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_CONTROL);// 释放ctrl按键，像ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放，不然会出问题。crtl如果按住没有释放，在按其他字母按键是，敲出来的回事ctrl的快捷键。
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.delay(Integer.parseInt(delay.getText()));// 设置延迟秒数再发送，不然会一次性全发布出去，因为电脑的处理速度很快，每次粘贴发送的速度几乎是一瞬间，所以给人的感觉就是一次性发送了全部。这个时间可以自己改，想几秒发送一条都可以
                    robot.keyPress(KeyEvent.VK_ENTER);// 回车

                }
            }
        });

        frame.setSize((int) (width * 0.25) + 70, (int) (height * 0.4));
        frame.setLayout(null);

        frame.add(msg);
        frame.add(jL_times);
        frame.add(times);
        frame.add(cln);
        frame.add(send);
        frame.add(jL_delay);
        frame.add(delay);
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

    public WeChatBoom() {
        init();
    }

}