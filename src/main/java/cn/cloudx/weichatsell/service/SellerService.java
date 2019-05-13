package cn.cloudx.weichatsell.service;


import cn.cloudx.weichatsell.dataobject.SellerInfo;


public interface SellerService {

    /**
     * 通过openid查询买家信息
     *
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
