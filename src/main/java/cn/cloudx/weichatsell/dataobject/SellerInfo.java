package cn.cloudx.weichatsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zhang
 */
@Entity
@Data
public class SellerInfo {

    @Id
    private String sellerId;
    private String username;
    private String password;
    private String openid;
    private Date createTime;
    private Date updateTime;


}
