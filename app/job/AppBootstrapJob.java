package job;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import java.io.IOException;
import java.util.Properties;

/**
 * 功能描述：应用启动任务
 * <p> 版权所有：优视科技 - 超能战队</p>
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 </p>
 *
 * @author <a href="mailto:qunying.sqy@alibaba-inc.com">宋群英</a>
 * @version 1.0.0
 * @create on: 2016-2-4
 */
@OnApplicationStart
public class AppBootstrapJob extends Job{

    @Override
    public void doJob() throws IOException {

        //初始化JCS内存缓存配置
        Properties props = new Properties();
        props.put("jcs.default.cacheattributes", "org.apache.commons.jcs.engine.CompositeCacheAttributes");
        props.put("jcs.default.cacheattributes.MemoryCacheName", "org.apache.commons.jcs.engine.memory.lru.LRUMemoryCache");
        props.put("jcs.default.cacheattributes.MaxObjects", "100000");

        CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
        ccm.configure(props);

        //启动WEB SOCKET监听应用的消息
        WebSocketServerBootstrapJob wssb = new WebSocketServerBootstrapJob();
        wssb.now();

        //启动TCP SOCKET接入客户端连接
        TcpSocketServerBootstrapJob tssb = new TcpSocketServerBootstrapJob();
        tssb.now();
    }
}
