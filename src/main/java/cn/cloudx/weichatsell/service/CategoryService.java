package cn.cloudx.weichatsell.service;


import cn.cloudx.weichatsell.dataobject.ProductCategory;

import java.util.List;

/**
 * 类目
 *
 * @author zhanghao
 * @date 2018/04/19
 */
public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
