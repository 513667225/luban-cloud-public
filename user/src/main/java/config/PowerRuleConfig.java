package config;

import com.luban.rule.LuBanRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 想要咨询vip课程相关的同学加一下安其拉老师QQ：3164703201
 * 想要往期视频的同学加一下木兰老师QQ：2746251334
 * author：鲁班学院-商鞅老师
 */
@Configuration
public class PowerRuleConfig {
    @Bean
    public IRule iRule(){

        return  new LuBanRule();
    }
}
