package Chat;

/**
 * Created by ron on 2017/4/26.
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.TitledBorder;
public class Client extends WindowAdapter implements ActionListener{
    private JFrame frame;
    private JList userList;
    private JTextArea textArea;
    private JTextField textField;
    private JTextField txt_port;
    private JTextField txt_hostIp;
    private JTextField txt_name;
    private JButton btn_start;
    private JButton btn_stop;
    private JButton btn_send;
    private JPanel northPanel;
    private JPanel southPanel;
    private JScrollPane rightScroll;
    private JSplitPane centerSplit;
    private DefaultListModel listModel;
    private boolean isConnected = false;
    private PrintWriter write = null;
    private BufferedReader read = null;
    private Socket client1 = null;

    private String receiveAll = null;
    private String whoSend = null;
    private String sendWhat = null;
    Client(){
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.blue);
        textField = new JTextField();
        txt_port = new JTextField("4396");
        txt_hostIp = new JTextField("127.0.0.1");
        txt_name = new JTextField("gzr");
        btn_start = new JButton("连接");
        btn_stop = new JButton("断开");
        btn_send = new JButton("发送");
        listModel = new DefaultListModel();
        userList = new JList(listModel);

        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 7));
        northPanel.add(new JLabel("端口"));
        northPanel.add(txt_port);
        northPanel.add(new JLabel("服务器IP"));
        northPanel.add(txt_hostIp);
        northPanel.add(new JLabel("姓名"));
        northPanel.add(txt_name);
        northPanel.add(btn_start);
        northPanel.add(btn_stop);
        northPanel.setBorder(new TitledBorder("连接信息"));

        rightScroll = new JScrollPane(textArea);
        rightScroll.setBorder(new TitledBorder("消息显示区"));
        southPanel = new JPanel(new BorderLayout());
        southPanel.add(textField, "Center");
        southPanel.add(btn_send, "East");
        southPanel.setBorder(new TitledBorder("写消息"));

        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, null,
                rightScroll);
        centerSplit.setDividerLocation(100);

        frame = new JFrame("客户机");

        frame.setLayout(new BorderLayout());
        frame.add(northPanel, "North");
        frame.add(centerSplit, "Center");
        frame.add(southPanel, "South");
        frame.setSize(600, 400);
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2,
                (screen_height - frame.getHeight()) / 2);
        frame.setVisible(true);
        frame.addWindowListener(this);

        // 写消息的文本框中按回车键时事件
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
                send();
            }
        });

        // 单击发送按钮时事件
        btn_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                send();
            }
        });

        // 单击连接按钮时事件
        btn_start.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int port;
                if (isConnected)
                {
                    JOptionPane.showMessageDialog(frame, "已处于连接上状态，不要重复连接!",
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try
                {
                    try
                    {
                        port = Integer.parseInt(txt_port.getText().trim());
                    }
                    catch (NumberFormatException e2)
                    {
                        throw new Exception("端口号不符合要求!端口为正整数!");
                    }
                    String hostIp = txt_hostIp.getText().trim();
                    String name = txt_name.getText().trim();
                    if (name.equals("") || hostIp.equals(""))
                    {
                        throw new Exception("姓名、服务器IP不能为空!");
                    }
                    boolean flag = connectServer(port, hostIp, name);
                    if (flag == false)
                    {
                        throw new Exception("与服务器连接失败!");
                    }
                    frame.setTitle(name);
                    JOptionPane.showMessageDialog(frame, "成功连接!");
                }
                catch (Exception exc)
                {
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 单击断开按钮时事件
        btn_stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (!isConnected)
                {
                    JOptionPane.showMessageDialog(frame, "已处于断开状态，不要重复断开!",
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try
                {
                    closeConnection();// 断开连接
                    JOptionPane.showMessageDialog(frame, "成功断开!");
                }
                catch (Exception exc)
                {
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 关闭窗口时事件
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                if (isConnected)
                {
                    try {
                        closeConnection();// 关闭连接
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);// 退出程序
            }
        });
    }
    //ConnectServer方法:
    public boolean connectServer(int port,String hostIp,String name) throws IOException {
        {
            client1 = new Socket(hostIp,port);
            try {
                System.out.println("客户端发送个人信息");
                try {
                    //Open输出流:
                    write = new PrintWriter(client1.getOutputStream());
                    //将客户端的name发送给服务器(myname):
                    sendOut(name);

                }catch (Exception e){
                    System.out.println("Send Error is "+e);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            System.out.println("客户端接收消息线程启用!"+client1.getLocalPort());
                            //创建读入流:
                            read = new BufferedReader(new InputStreamReader(client1.getInputStream()));
                            //分割发来的消息:
                            String[] devideReceiveAll = null;
                            String[] devideReceiveAllFirst = null;
                            if ((receiveAll = read.readLine())!=null){
                                //First time receive message and print it:
                                devideReceiveAllFirst = receiveAll.split("@");
                                whoSend = devideReceiveAllFirst[1];
                                sendWhat = devideReceiveAllFirst[2];
                                if (!whoSend.equals(name)) {
                                    System.out.println("Client "+whoSend+" said :"+sendWhat);
                                    textArea.append("Client "+whoSend+" said: "+sendWhat+"\r\n");
                                }
                                if (devideReceiveAllFirst[0].equals("NOTALL")) {
                                    //发送发消息人的昵称到srver:
                                    write.println(whoSend);
                                    write.flush();
                                }else {
                                    write.println("ALL");
                                    write.flush();
                                }

                                while ((receiveAll = read.readLine())!=null) {
                                    devideReceiveAll = receiveAll.split("@");
                                    whoSend = devideReceiveAll[1];
                                    sendWhat = devideReceiveAll[2];
                                    System.out.println(name);
                                    if (!whoSend.equals(name)){
                                        System.out.println("Client "+whoSend+" said :"+sendWhat);
                                        textArea.append("Client "+whoSend+" said: "+sendWhat+"\r\n");
                                    }
                                }
                            }
                        }catch (Exception e){
                            System.out.println("Receive Error is "+e);
                        }

                    }
                }).start();
                isConnected = true;
                textArea.append("Please input chat with who:\r\n");
                return true;
            }catch (Exception e){
                System.out.println("The Error is"+e);
                return false;
            }

        }
    }
    public void closeConnection() throws IOException {
        write.close();
        read.close();
        client1.close();
        isConnected = false;
    }

    //SendMessage两方法:
    public void send(){
        if (!isConnected)
        {
            JOptionPane.showMessageDialog(frame, "还没有连接服务器，无法发送消息！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String message = textField.getText();
        System.out.println("message is "+message);
        if (message.equals("")||message == null){
            JOptionPane.showMessageDialog(frame, "发送消息不能为空！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        sendOut(message);
        textArea.append("I said: "+message+"\r\n");
        textField.setText(null);
    }

    private void sendOut(String message) {
        write.println(message);
        write.flush();
    }
    //CloseFrame方法:
    public void windowClosing(WindowEvent e)
    {
        System.out.println("我退出了！");
        //e.getWindow().setVisible(false);
        //((Window)e.getComponent()).dispose();
        System.exit(0);
    }
    //接口的实现：
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public static void main(String args[]){
        LoginInterface loginInterface = new LoginInterface();
    }
}
