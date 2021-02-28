/**
 * @author XUAN
 * @date 2021/2/28 - 19:55
 * @references [Java获取本机ip地址,并扫描本机所在局域网的电脑设备](https://www.cnblogs.com/mxh-java/p/13187115.html)
 * @purpose
 * @errors
 */
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 类注解
 *
 * @author 尘世间迷茫的小书童
 * @date 2020年06月23日 17:31
 */
public class Demo {

    public static void main(String[] args) {
        Set<String> set = getIpAddress();
        if(set.size() > 0) {
            set.forEach(ip -> {
                System.out.println("本机ip: " + ip);
            });
        }
        set.remove("127.0.0.1");
        scannerNetwork(set);
        System.out.println("扫描完毕...");
        System.exit(0);
    }

    /**
     * 获取本机的IP地址（包括ipv4和ipv6） <br>
     * 包含回环地址127.0.0.1和0:0:0:0:0:0:0:1
     */
    private static Set<String> getIpAddress() {
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
        return ipList;
    }


    /**
     * 根据本机ip扫描局域网设备
     * @param set
     */
    private static void scannerNetwork(Set<String> set) {
        try {
            set.forEach(address -> {
                // 设置IP地址网段
                String ips = getNetworkSegment(address);

                System.out.println("开始扫描 " + ips + "网段...");

                String ip;
                InetAddress addip = null;
                // 遍历IP地址
                for (int i = 1; i < 255; i++) {
                    ip = ips + i;
                    try {
                        addip = InetAddress.getByName(ip);
                    } catch (UnknownHostException e) {
                        System.out.println("找不到主机: " + ip);
                    }
                    // 获取登录过的设备
                    if (!ip.equals(addip.getHostName())) {
                        try {
                            // 检查设备是否在线，其中1000ms指定的是超时时间
                            boolean status = InetAddress.getByName(addip.getHostName()).isReachable(1000); // 当返回值是true时，说明host是可用的，false则不可。
                            System.out.println("IP地址为:" + ip + "\t\t设备名称为: " + addip.getHostName() + "\t\t是否可用: "
                                    + (status ? "可用" : "不可用"));
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception uhe) {
            System.err.println("Unable to find: " + uhe.getLocalizedMessage());
        }
    }


    /**
     * 根据ip获取网段
     * @param ip
     * @return
     */
    private static String getNetworkSegment(String ip) {
        int startIndex = ip.lastIndexOf(".");
        return ip.substring(0, startIndex+1);
    }

}