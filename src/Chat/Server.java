package Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by ron on 2017/4/26.
 */
public class Server {
    private static int clientNumber = 0;
    public static void main(String[] args) throws IOException {
        ServerSocket server = null;
        ArrayList<Socket> socketArrayList = new ArrayList<>();
        HashMap<String,Socket> adressAndName = new HashMap<String,Socket>();
        try {
            server = new ServerSocket(4396);
        }catch (IOException e){
            System.out.println("Error is"+e);
        }
        System.out.println("服务器建立成功！");

        //Socket signInSocket = null;
        Socket socket = null;
        while (true){

            socket = server.accept();
            socketArrayList.add(socket);
            new Thread(new TalkServerReceive(socket,clientNumber,socketArrayList,adressAndName)).start();
            //new Thread(new TalkServerSend(socket,clientNumber,socketArrayList)).start();
            clientNumber++;
            //"+server.accept().getInetAddress().getHostAddress()+"
            System.out.println("新加入了IP地址为："+socket.getInetAddress()+" 端口号为："+socket.getPort()+" 的客户\r\n共有"+socketArrayList.size()+"名客户!");
        }
    }
}
