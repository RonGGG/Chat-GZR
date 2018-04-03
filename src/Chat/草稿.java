package Chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ron on 2017/4/30.
 */
public class 草稿 {
    public static void main(String[] args){
//        Socket s1 = new Socket();
//        Socket s2 = new Socket();
//        HashMap<Socket,String> ad = new HashMap<Socket,String>();
//        ad.put(s1,"1");
//        ad.put(s2,"2");
//        System.out.println(ad.get(s1));
//        System.out.println(ad.get(s2));


        String s = "abc@de";
        String[] de = s.split("@");
        String s1 = de[0];
        String s2 = de[1];
        int k=0;
        int i=0;
        while (k!=2){
            if (i==1) continue;
            System.out.println(s1);
            System.out.println(s2);
            k++;
            i++;
        }
    }
}
