package cn.cloudx.weichatsell.dataobject.mapper;

import cn.cloudx.weichatsell.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanghao
 * @date 2018/05/09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ProductCategoryMapperTest {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Test
    public void insertByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("category_type", 1099);
        map.put("category_name", "我最不爱");
        int result = productCategoryMapper.insertByMap(map);
        Assert.assertEquals(1, result);

    }

    @Test
    public void insetByObject() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryType(10);
        productCategory.setCategoryName("师兄最不爱");
        productCategoryMapper.insertByObject(productCategory);
    }

    @Test
    public void findByCategoryType() {
        ProductCategory category = productCategoryMapper.findByCategoryType(10);
        Assert.assertNotNull(category);
    }
}