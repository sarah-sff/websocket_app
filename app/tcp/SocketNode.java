package tcp;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.java_websocket.WebSocket;
import play.Logger;
import play.Play;
import play.cache.Cache;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;

/**
 * 功能描述：TCP SOCKET客户端节点
 * <p> 版权所有：优视科技 - 超能战队</p>
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 </p>
 *
 * @author <a href="mailto:qunying.sqy@alibaba-inc.com">宋群英</a>
 * @version 1.0.0
 * @create on: 2016-2-4
 */
public class SocketNode extends Thread{

    /**
     * JCS缓存区名称
     */
    private static final String CACHE_REGION = Play.configuration.getProperty("jcs.region", "JVM_CACHE");

    /**
     * JCS缓存键值
     */
    private static final String CACHE_KEY = Play.configuration.getProperty("jcs.region.key", "SOCKET_CONNECTION");

    private Socket socket;
    private CacheAccess cacheAccess;

    public SocketNode(Socket socket){
        this.socket=socket;
        cacheAccess = JCS.getInstance(CACHE_REGION);

    }

    @Override
    public void run() {
        String mission = null;
        DataInputStream is = null;

        try {
            is = new DataInputStream(socket.getInputStream());
            while(true){
                if((mission= is.readUTF()) != null){
                    Logger.debug("接收到更新任务信息为：%s", mission);

                    Collection<WebSocket> socketList = (Collection<WebSocket>) cacheAccess.get(CACHE_KEY);
                    if(socketList != null && socketList.size() != 0){
                        // 遍历所有客户端连接进行消息广播
                        for(WebSocket webSocket : socketList){
                            webSocket.send(mission);
                        }
                    }
                }
            }
        } catch(SocketException e){
            Logger.info("应用[%s]已经中断连接", socket.getRemoteSocketAddress());
        } catch (IOException e) {
            Logger.error(e, "应用[%s]发送消息格式不匹配，消息内容：%s", socket.getRemoteSocketAddress(), mission);
        } finally {
            try {
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
