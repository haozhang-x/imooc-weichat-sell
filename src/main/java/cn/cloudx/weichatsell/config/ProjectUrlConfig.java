package cn.cloudx.weichatsell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "project-url")
@Component
public class ProjectUrlConfig {

    /**
     * 微信公众平台授权Url
     */

    private String weChatMpAuthorize;


    /**
     * 微信开放平台授权Url
     */

    private String weChatOpenAuthorize;

    private String sell;
}
