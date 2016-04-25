package job;

import play.Play;
import play.jobs.Job;
import websocket.SocketServer;


/**
 * 功能描述：WEB SOCKET服务器启动任务
 * <p> 版权所有：优视科技 - 超能战队</p>
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 </p>
 *
 * @author <a href="mailto:qunying.sqy@alibaba-inc.com">宋群英</a>
 * @version 1.0.0
 * @create on: 2016-2-4
 */
public class WebSocketServerBootstrapJob extends Job{

    /**
     * WEB SOCKET端口
     */
    private static final String WEB_SOCKET_PORT = Play.configuration.getProperty("web.socket.server.port");

    @Override
    public void doJob(){
        if(WEB_SOCKET_PORT != null){
            //监听端口接入WEB SOCKET客户端连接
            int port = Integer.parseInt(WEB_SOCKET_PORT);
            SocketServer sws = new SocketServer(port);
            sws.start();
        }
    }
}
