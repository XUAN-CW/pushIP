import java.net.InetAddress;

/**
 * @author XUAN
 * @date 2021/2/8 - 17:02
 * @references
 * @purpose
 * @errors
 */
public class FindIP {
    public static void main(String[] args) throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        SaveAndRead saveAndRead = new SaveAndRead();
        saveAndRead.save("README.md","http://"+addr.getHostAddress()+":8080");
    }
}
