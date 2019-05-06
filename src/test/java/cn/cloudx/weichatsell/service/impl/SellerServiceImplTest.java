package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.dataobject.SellerInfo;
import cn.cloudx.weichatsell.dataobject.SellerInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author zhanghao
 * @date 2018/05/06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SellerServiceImplTest {

    @Autowired
    private SellerServiceImpl sellerService;

    @Test
    public void findSellerInfoByOpenid() {
        SellerInfo abc = sellerService.findSellerInfoByOpenid("abc");
        assertEquals("abc", abc.getOpenid());
    }
}