package cn.cloudx.weichatsell.repository;

import cn.cloudx.weichatsell.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/20
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    /**
     * 通过商品的状态来查询商品信息
     *
     * @param productStatus 商品状态
     * @return List<ProductInfo>
     */
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
