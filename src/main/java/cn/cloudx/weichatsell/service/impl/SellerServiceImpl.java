package cn.cloudx.weichatsell.service.impl;


import cn.cloudx.weichatsell.dataobject.SellerInfo;
import cn.cloudx.weichatsell.repository.SellerInfoRepository;
import cn.cloudx.weichatsell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SellerServiceImpl implements SellerService {


    private SellerInfoRepository repository;

    @Autowired
    public SellerServiceImpl(SellerInfoRepository repository) {
        this.repository = repository;
    }

    /**
     * 通过openid查询买家信息
     *
     * @param openid openid
     * @return SellerInfo
     */
    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
