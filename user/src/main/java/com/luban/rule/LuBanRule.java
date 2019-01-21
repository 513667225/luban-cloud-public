package com.luban.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Random;

/**
 * 想要咨询vip课程相关的同学加一下安其拉老师QQ：3164703201
 * 想要往期视频的同学加一下木兰老师QQ：2746251334
 * author：鲁班学院-商鞅老师
 */
public class LuBanRule extends AbstractLoadBalancerRule {
    Random rand;

    private int nowIndex = -1;

    private int lastIndex = -1;

    private int skipIndex = -1;




    public LuBanRule() {
        rand = new Random();
    }

    /**
     * 伪随机，当一个下标（微服务） 连续被调用两次， 第三次如果还是它， 那么咱们就再随机一次。
     * Randomly choose from all living servers
     */
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

            int index = rand.nextInt(serverCount);
            System.out.println("当前下标:"+index);
            if (index==skipIndex){
                System.out.println("跳过");
                index =rand.nextInt(serverCount);
                lastIndex = -1;
                System.out.println("跳过之后的下标:"+index);
            }
            //1 1   0
            skipIndex = -1;
            nowIndex = index;
            if(nowIndex==lastIndex){
                System.out.println("需要跳过的下标："+nowIndex);
                skipIndex = nowIndex;
            }

            server = upList.get(index);

            lastIndex = nowIndex;

            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub

    }
}
