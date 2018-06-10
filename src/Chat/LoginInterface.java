package Chat;

/**
 * Created by ron on 2017/4/26.
 */
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.*;
public class LoginInterface extends WindowAdapter implements ActionListener{
    private Frame f1;
    private Label l1,l2,l3,l4;
    private TextField tf1,tf2;
    private Button b1,b2;
    private JLabel imgLabel;
    private ImageIcon imageIcon;
    String userAccount = null;
    String password = null;

    LoginInterface()
    {
        f1=new Frame("MyQQ");
        f1.setSize(400,300);
        f1.setLayout(null);
        l1=new Label("账号：");
        l1.setBounds(20, 120, 50, 20);
        f1.add(l1);
        tf1=new TextField(25);
        tf1.setBounds(100, 120, 180, 20);
        f1.add(tf1);
        l2=new Label("密码：");
        l2.setBounds(20, 170, 50, 20);
        f1.add(l2);

        URL url = this.getClass().getResource("maozhuxi.jpg");
        imageIcon = new ImageIcon(url);
        imgLabel = new JLabel(imageIcon);
        imgLabel.setBounds((400-imageIcon.getIconWidth())/2,30,imageIcon.getIconWidth(),imageIcon.getIconHeight());

        f1.add(imgLabel);

        tf2=new TextField(25);
        tf2.setBounds(100, 170, 180, 20);
        tf2.setEchoChar('*');
        tf2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (tf1.getText().equals("")||tf2.getText().equals("")){
                    JOptionPane.showMessageDialog(f1,"用户名或密码不能为空！","错误",JOptionPane.CLOSED_OPTION);
                }else {
                    if (userAccount==null||password==null){
                        JOptionPane.showMessageDialog(f1,"请注册！","错误",JOptionPane.CLOSED_OPTION);
                    }else {
                        if(userAccount.equals(tf1.getText())&&password.equals(tf2.getText()))
                        {
                            f1.setVisible(false);
                            new Client();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(f1, "账号密码错误！", "错误",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
        });
        f1.add(tf2);
        b1=new Button("登陆");
        b1.setBounds(130,220,60,35);
        b2=new Button("注册");
        b2.setBounds(210,220,60,35);
        f1.add(b1);
        f1.add(b2);
        f1.setBackground(Color.white);
        f1.setVisible(true);
        f1.addWindowListener(this);
        b1.addActionListener(this);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userAccount = tf1.getText();
                password = tf2.getText();
                JOptionPane.showMessageDialog(f1,"注册成功！","提示",JOptionPane.CANCEL_OPTION);
            }
        });
    }
    public void windowClosing(WindowEvent e)
    {
        System.out.println("我退出了！");
        //e.getWindow().setVisible(false);
        //((Window)e.getComponent()).dispose();
        System.exit(0);
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==b1)
        {
            if (tf1.getText().equals("")||tf2.getText().equals("")){
                JOptionPane.showMessageDialog(f1,"用户名或密码不能为空！","错误",JOptionPane.CLOSED_OPTION);
            }else {
                if (userAccount==null||password==null){
                    JOptionPane.showMessageDialog(f1,"请注册！","错误",JOptionPane.CLOSED_OPTION);
                }else {
                    if(userAccount.equals(tf1.getText())&&password.equals(tf2.getText()))
                    {
                        f1.setVisible(false);
                        new Client();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(f1, "账号密码错误！", "错误",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        }
    }

}
