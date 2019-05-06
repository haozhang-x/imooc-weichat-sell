package cn.cloudx.weichatsell.service;


import cn.cloudx.weichatsell.dataobject.ProductInfo;
import cn.cloudx.weichatsell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/20
 */
public interface ProductService {
    /**
     * 查找一个
     *
     * @param productId productId
     * @return ProductInfo
     */
    ProductInfo findOne(String productId);

    /**
     * 查询所有在架的商品列表
     *
     * @return List<ProductInfo>
     */
    List<ProductInfo> findUpAll();


    /**
     * 查找所有的
     *
     * @param pageable pageable
     * @return List<ProductInfo>
     */
    Page<ProductInfo> findAll(Pageable pageable);

    /**
     * 保存方法
     *
     * @param productInfo productInfo
     * @return ProductInf
     */

    ProductInfo save(ProductInfo productInfo);


    /**
     * 加库存
     */

    void increaseStock(List<CartDTO> cartDTOList);

    /**
     * 减库存
     */

    void decreaseStock(List<CartDTO> cartDTOList);

    /**
     * 上架
     */
    ProductInfo onSale(String productId);


    /**
     * 下架
     *
     * @param productId productId
     */

    ProductInfo offSale(String productId);


}
