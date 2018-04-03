package Chat;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class TalkServerReceive implements Runnable{
    private Socket s =null;
    private int numOfClient;
    private HashMap<String,Socket> adressAndName;

    private BufferedReader read = null;
    private PrintWriter write = null;
    private ArrayList<Socket> arraySocketList;
    TalkServerReceive(Socket sR, int num, ArrayList<Socket> arrayList, HashMap<String, Socket> adAndName){
        this.s = sR;
        arraySocketList = arrayList;
        numOfClient = num;
        adressAndName = adAndName;
    }
    @Override
    public void run() {
        try {
            System.out.println("服务器接受/发送信息线程启用！");
            read = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            //接收客户name信息:
            String fromAdressName = read.readLine();

            //得到IP地址和客户端内核端口:
            String fromAdress = s.getInetAddress().toString();
            String fromAdressPort = Integer.toString(s.getPort());

            //加入到字典中:
            adressAndName.put(fromAdressName,this.s);
            System.out.println("将此客户："+adressAndName.get(fromAdressName).getPort()+" 加入到服务器字典中");

            //监听客户要发送给谁:
            String sendToWho = read.readLine();
            //打印这些信息:
            System.out.println("IP为 "+fromAdress+"端口是 "+fromAdressPort+"的 :"+fromAdressName+"对"+sendToWho+"说");

            //发送的消息内容:
            String message = null;
            while (fromAdress!=null) {
                message = read.readLine();
                System.out.println(fromAdressName+" Send to "+sendToWho+":"+message);
                //防止上一句中打印的错误发生(System.out.println(fromAdressName+" Send to "+sendToWho+":"+message);)
                if (!message.equals(sendToWho)) {
                    if (sendToWho.equals("ALL")){
                        int k=0;
                        while (k<arraySocketList.size()){
                            write = new PrintWriter(arraySocketList.get(k).getOutputStream());
                            write.println("ALL"+"@"+fromAdressName+"@"+message);
                            write.flush();
                            k++;
                        }
                    }else {

                        write = new PrintWriter(adressAndName.get(sendToWho).getOutputStream());

                        write.println("NOTALL"+"@"+fromAdressName+"@"+message);
                        write.flush();
                    }
                }
            }
            read.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

