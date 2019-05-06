package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.config.WeChatAccountConfig;
import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.service.PushMessage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhanghao
 * @date 2018/05/07
 */
@Service
@Slf4j
public class PushMessageImpl implements PushMessage {


    private WxMpService wxMpService;

    private WeChatAccountConfig accountConfig;

    @Autowired
    public PushMessageImpl(WxMpService wxMpService, WeChatAccountConfig accountConfig) {
        this.wxMpService = wxMpService;
        this.accountConfig = accountConfig;
    }



    /**
     * 订单状态变更
     *
     * @param orderDTO orderDTO
     */
    @Override
    public void status(OrderDTO orderDTO) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(accountConfig.getTemplateMap().get("orderStatus"));
        templateMessage.setToUser(orderDTO.getBuyerOpenid());

        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first","亲，记得收货。"),
                new WxMpTemplateData("keyword1","微信点餐"),
                new WxMpTemplateData("keyword2","10010"),
                new WxMpTemplateData("keyword3",orderDTO.getOrderId()),
                new WxMpTemplateData("keyword4",orderDTO.getOrderStatusEnum().getMsg()),
                new WxMpTemplateData("keyword5","￥"+orderDTO.getOrderAmount()),
                new WxMpTemplateData("remark","欢迎再次光临！")

        );


        templateMessage.setData(data);

        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error("【模板消息】发送失败={}",e.getMessage());
        }


    }
}
