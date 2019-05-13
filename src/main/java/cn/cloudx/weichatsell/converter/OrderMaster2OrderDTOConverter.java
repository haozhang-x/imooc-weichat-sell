package cn.cloudx.weichatsell.converter;

import cn.cloudx.weichatsell.dataobject.OrderMaster;
import cn.cloudx.weichatsell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;


public class OrderMaster2OrderDTOConverter {

    public static OrderDTO covert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(OrderMaster2OrderDTOConverter::covert).collect(Collectors.toList());
    }
}
