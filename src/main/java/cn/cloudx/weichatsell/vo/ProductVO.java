package cn.cloudx.weichatsell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品包含类目
 *
 * @author zhanghao
 * @date 2018/04/20
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 2607521253037619947L;
    /**
     *
     */
    @JsonProperty("name")
    private String categoryName;
    /**
     *
     * */
    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
