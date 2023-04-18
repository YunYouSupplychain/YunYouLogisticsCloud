package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.order.entity.TmDeliverLabel;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.entity.extend.TmDeliverEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmDeliverMapper;
import com.yunyou.modules.tms.order.service.TmDeliverLabelService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：网点发货处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmDeliverManager extends CrudService<TmDeliverMapper, TmDeliverEntity> {
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDeliverLabelService tmDeliverLabelService;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;

    @Override
    public Page<TmDeliverEntity> findPage(Page<TmDeliverEntity> page, TmDeliverEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        /*qEntity.setShipOutletCodes(this.getShipOutletCondition(qEntity.getOrgId()));*/
        qEntity.setPage(page);
        List<TmDeliverEntity> list = mapper.findPage(qEntity);
        for (TmDeliverEntity entity : list) {
            entity.setShipCity(areaService.getFullName(entity.getShipCityId()));
            entity.setConsigneeCity(areaService.getFullName(entity.getConsigneeCityId()));
        }
        page.setList(list);
        return page;
    }

    public List<TmDeliverLabel> findList(TmDeliverLabel qEntity) {
        return tmDeliverLabelService.findList(qEntity);
    }

    @Transactional
    public void save(TmDeliverLabel entity) {
        tmDeliverLabelService.save(entity);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(entity);
    }

    @Transactional
    public void deliverByLabel(String dispatchNo, String transportNo, String labelNo, String deliveryOutletCode, String destinationOutletCode) {
        TmTransportOrderHeader transportOrder = tmTransportOrderHeaderService.getByNo(transportNo);
        TmDispatchOrderHeader dispatchOrder = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        // 获取标签运输网点路由信息
        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, transportOrder.getBaseOrgId());
        // 绑定派车单
        orderRoute.setDispatchNo(dispatchNo);
        // 指定当前网点
        orderRoute.setNowOutletCode(StringUtils.isBlank(deliveryOutletCode) ? orderRoute.getNowOutletCode() : deliveryOutletCode);
        // 指定送往下一站网点
        orderRoute.setNextOutletCode(StringUtils.isBlank(destinationOutletCode) ? orderRoute.getNextOutletCode() : destinationOutletCode);
        tmTransportOrderRouteService.save(orderRoute);
        // 网点发货后此时标签路由授权信息重新初始化生成
        tmAuthorityManager.genAuthorityData(orderRoute);

        // 插入Tm_Deliver_Label
        TmDeliverLabel deliverLabel = new TmDeliverLabel();
        deliverLabel.setDeliverOutletCode(orderRoute.getNowOutletCode());
        deliverLabel.setLabelNo(orderRoute.getLabelNo());
        deliverLabel.setTransportNo(orderRoute.getTransportNo());
        deliverLabel.setCustomerNo(transportOrder.getCustomerNo());
        deliverLabel.setDispatchNo(orderRoute.getDispatchNo());
        deliverLabel.setOwnerCode(orderRoute.getOwnerCode());
        deliverLabel.setSkuCode(orderRoute.getSkuCode());
        deliverLabel.setCarrierCode(dispatchOrder.getCarrierCode());
        deliverLabel.setOrgId(dispatchOrder.getOrgId());
        deliverLabel.setBaseOrgId(transportOrder.getBaseOrgId());
        this.save(deliverLabel);
    }

    private List<String> getShipOutletCondition(String opOrgId) {
        List<String> shipOutletCodes = Lists.newArrayList();
        Office office = officeService.get(opOrgId);
        if (office != null) {
            if (OfficeType.OUTLET.getValue().equals(office.getType()) || OfficeType.WAREHOUSE.getValue().equals(office.getType())) {
                // 如果当前操作的机构是“网点”，那么找出对应的业务对象编码作为网点筛选条件
                Office organizationCenter = UserUtils.getOrgCenter(opOrgId);
                if (StringUtils.isNotBlank(organizationCenter.getId())) {
                    List<TmTransportObj> list = tmTransportObjService.findOutletMatchObjByOrgId(opOrgId, organizationCenter.getId());
                    if (CollectionUtil.isNotEmpty(list)) {
                        shipOutletCodes = list.stream().map(TmTransportObj::getTransportObjCode).collect(Collectors.toList());
                    }
                }
                if (CollectionUtil.isEmpty(shipOutletCodes)) {
                    // 如果无对应业务对象，采用一个不存在的作为网点筛选条件过滤
                    shipOutletCodes = Lists.newArrayList("*");
                }
            } else if (OfficeType.CARRIER.getValue().equals(office.getType())) {
                shipOutletCodes = Lists.newArrayList("*");
                // 如果当前操作的机构是“网点”，采用一个不存在的作为网点筛选条件过滤
            }
        }
        return shipOutletCodes;
    }

    /**
     * 描述：强制删除
     */
    @Transactional
    public void forceRemove(String transportNo, String orgId, String baseOrgId) {
        TmDeliverLabel qEntity = new TmDeliverLabel();
        qEntity.setTransportNo(transportNo);
        qEntity.setBaseOrgId(baseOrgId);
        List<TmDeliverLabel> deliverLabels = tmDeliverLabelService.findList(qEntity);
        for (TmDeliverLabel deliverLabel : deliverLabels) {
            tmDeliverLabelService.delete(deliverLabel);
            // 删除授权数据信息
            tmAuthorityManager.remove(TmAuthorityTable.TM_DELIVER_LABEL.getValue(), deliverLabel.getId());
        }
    }
}
