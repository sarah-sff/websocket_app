package job;

import play.Play;
import play.jobs.Job;
import tcp.SocketNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 功能描述：TCP SOCKET服务器启动任务
 * <p> 版权所有：优视科技 - 超能战队</p>
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 </p>
 *
 * @author <a href="mailto:qunying.sqy@alibaba-inc.com">宋群英</a>
 * @version 1.0.0
 * @create on: 2016-2-4
 */
public class TcpSocketServerBootstrapJob extends Job{

    /**
     * TCP SOCKET端口
     */
    private static final String TCP_PORT = Play.configuration.getProperty("tcp.socket.server.port");

    @Override
    public void doJob() throws IOException {
        if(TCP_PORT != null) {
            //监听端口接入TCP SOCKET连接
            ServerSocket sever = new ServerSocket(Integer.valueOf(TCP_PORT));
            while (true) {
                Socket socket = sever.accept();
                SocketNode node = new SocketNode(socket);
                node.start();
            }
        }
    }
}
