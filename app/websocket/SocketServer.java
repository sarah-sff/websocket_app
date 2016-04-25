package websocket;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import play.Logger;
import play.Play;

import java.net.InetSocketAddress;

/**
 * 功能描述：WEB SOCKET服务器类
 * <p> 版权所有：优视科技 - 超能战队</p>
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 </p>
 *
 * @author <a href="mailto:qunying.sqy@alibaba-inc.com">宋群英</a>
 * @version 1.0.0
 * @create on: 2016-2-4
 */
public class SocketServer extends WebSocketServer{

    /**
     * JCS缓存区名称
     */
    private static final String CACHE_REGION = Play.configuration.getProperty("jcs.region", "JVM_CACHE");

    /**
     * JCS缓存键值
     */
    private static final String CACHE_KEY = Play.configuration.getProperty("jcs.region.key", "SOCKET_CONNECTION");

    private CacheAccess cacheAccess;

    public SocketServer(int port){
        super(new InetSocketAddress(port));
        cacheAccess = JCS.getInstance(CACHE_REGION);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        Logger.debug("[SocketServer.onOpen]开启连接：%s", webSocket.getRemoteSocketAddress());

        cacheAccess.put(CACHE_KEY, this.connections());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        Logger.debug("[SocketServer.onClose]关闭连接：%s", webSocket.getRemoteSocketAddress());

        cacheAccess.put(CACHE_KEY, this.connections());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        Logger.error("[SocketServer.onError]通信出错", e);
    }
}
