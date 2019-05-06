package cn.cloudx.weichatsell.dataobject.mapper;

import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.Map;

/**
 * @author zhanghao
 * @date 2018/05/09
 */
@Mapper
public interface ProductCategoryMapper {
    @Insert("insert into product_category(category_name,category_type) values(#{category_name},#{category_type})")
    int insertByMap(Map<String, Object> map);


    @Insert("insert into product_category(category_name,category_type) values(#{categoryName},#{categoryType})")
    int insertByObject(ProductCategory category);


    @Select("select * from product_category where category_type =#{categoryType}")
    @Results(
            {
                    @Result(column = "category_name", property = "categoryName"),
                    @Result(column = "category_type", property = "categoryType"),
                    @Result(column = "category_id", property = "categoryId"),
            }
    )
    ProductCategory findByCategoryType(Integer categoryType);

}
