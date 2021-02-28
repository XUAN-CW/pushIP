import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

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

        Set<String> ipList = new HashSet<>();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                // 排除虚拟接口和没有启动运行的接口
                if (netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
//                        if (ip != null && (ip instanceof Inet4Address || ip instanceof Inet6Address)) {
//                            ipList.add(ip.getHostAddress());
//                        }
                        if (ip != null && (ip instanceof Inet4Address)) {
                            ipList.add(ip.getHostAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ipList.remove("127.0.0.1");
        if(ipList.size() > 0) {
            String tomcat="";
            for (String ip:ipList) {
                tomcat+="http://"+ip+":8080"+"\n";
            }
            saveAndRead.save("README.md","# tomcat\n"+tomcat);
        }


    }
}
