package cn.cloudx.weichatsell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 2019/05/06 zhanghao
 */
@Component
public class WeChatOpenConfig {
    private WeChatAccountConfig config;


    @Autowired
    public WeChatOpenConfig(WeChatAccountConfig accountConfig) {
        this.config = accountConfig;
    }


    @Bean
    public WxMpService wxOpenService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxOpenConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxOpenConfigStorage() {
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(config.getOpenAppId());
        wxMpConfigStorage.setSecret(config.getOpenAppSecret());
        return wxMpConfigStorage;
    }
}
