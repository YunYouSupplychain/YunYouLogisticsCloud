package com.yunyou.modules.tms.order.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderSku;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderSkuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输订单明细信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderSkuService extends CrudService<TmTransportOrderSkuMapper, TmTransportOrderSku> {

    public String getNewLineNo(String transportNo, String orgId) {
        Integer maxLineNo = mapper.getMaxLineNo(transportNo, orgId);
        if (maxLineNo == null) {
            maxLineNo = 0;
        }
        return String.format("%04d", maxLineNo + 1);
    }

    @Override
    @Transactional
    public void save(TmTransportOrderSku entity) {
        if (StringUtils.isBlank(entity.getTransportNo())) {
            throw new TmsException("运输单号不能为空");
        }
        if (StringUtils.isBlank(entity.getSkuCode())) {
            throw new TmsException("商品不能为空");
        }
        if (entity.getQty() == null) {
            throw new TmsException("数量不能为空");
        }
        if (entity.getQty() <= 0) {
            throw new TmsException("数量必须大于零");
        }
        if (StringUtils.isBlank(entity.getLineNo())) {
            entity.setLineNo(this.getNewLineNo(entity.getTransportNo(), entity.getOrgId()));
        }
        super.save(entity);
    }

    @Transactional
    public void saveAll(List<TmTransportOrderSku> list) {
        mapper.saveAll(list);
    }
}