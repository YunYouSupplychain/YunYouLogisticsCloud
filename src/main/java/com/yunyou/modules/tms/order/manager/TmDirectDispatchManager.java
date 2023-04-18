package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmDirectDispatch;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderSiteEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TmDirectDispatchManager extends BaseService {
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;
    @Autowired
    private TmDispatchOrderLabelManager tmDispatchOrderLabelManager;
    @Autowired
    private TmTotalManager tmTotalManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;

    /**
     * 描述：调度派车(选择运输订单直接生成派车单)
     */
    @Transactional
    public String directDispatch(TmDirectDispatch tmDirectDispatch) {
        if (StringUtils.isBlank(tmDirectDispatch.getIds())) {
            throw new TmsException("请选择记录");
        }
        if (StringUtils.isBlank(tmDirectDispatch.getCarrierCode())) {
            throw new TmsException("未指定承运商");
        }
        if (StringUtils.isBlank(tmDirectDispatch.getDispatchOutletCode())) {
            throw new TmsException("未指定派车网点");
        }
        String[] ids = tmDirectDispatch.getIds().split(",");
        String dispatchOutletCode = tmDirectDispatch.getDispatchOutletCode();
        Date dispatchTime = tmDirectDispatch.getDispatchTime() == null ? new Date() : tmDirectDispatch.getDispatchTime();
        String dispatchType = StringUtils.isBlank(tmDirectDispatch.getDispatchType()) ? "5" : tmDirectDispatch.getDispatchType();

        StringBuilder msg = new StringBuilder();
        List<TmTransportOrderEntity> entities = Lists.newArrayList();
        for (String id : ids) {
            TmTransportOrderEntity entity = tmTransportOrderManager.getEntity(id);
            if (entity != null) {
                if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
                    msg.append(MessageFormat.format("运输订单【{0}】未审核", entity.getTransportNo())).append("<br>");
                    continue;
                }
                if (TmsConstants.TRANSPORT_ORDER_STATUS_40.equals(entity.getOrderStatus())) {
                    msg.append(MessageFormat.format("运输订单【{0}】已签收", entity.getTransportNo())).append("<br>");
                    continue;
                }
                if (TmsConstants.TRANSPORT_ORDER_STATUS_50.equals(entity.getOrderStatus())) {
                    msg.append(MessageFormat.format("运输订单【{0}】已回单", entity.getTransportNo())).append("<br>");
                    continue;
                }
                if (TmsConstants.TRANSPORT_ORDER_STATUS_90.equals(entity.getOrderStatus())) {
                    msg.append(MessageFormat.format("运输订单【{0}】已取消", entity.getTransportNo())).append("<br>");
                    continue;
                }
                if (TmsConstants.TRANSPORT_ORDER_STATUS_99.equals(entity.getOrderStatus())) {
                    msg.append(MessageFormat.format("运输订单【{0}】已关闭", entity.getTransportNo())).append("<br>");
                    continue;
                }
                // 江苏豪杰 写死提货网点为【默认提货网点】
                entity.setReceiveOutletCode(TmsConstants.DEFAULT_DELIVERY_SITE);
                if (StringUtils.isBlank(entity.getOutletCode())) {
                    entity.setOutletCode(TmsConstants.DEFAULT_RECEIVE_SITE);
                }
                // 提货网点未揽收，自动揽收
                if (TmsConstants.TRANSPORT_ORDER_STATUS_10.equals(entity.getOrderStatus())) {
                    List<TmTransportOrderLabel> canReceiveLabel = tmTransportOrderManager.findCanReceiveLabel(entity.getTransportNo(), entity.getOrgId());
                    for (TmTransportOrderLabel orderLabel : canReceiveLabel) {
                        tmTransportOrderManager.receive(entity.getTransportNo(), orderLabel.getLabelNo(), entity.getReceiveOutletCode());
                    }
                }
                // 查找能调度派车使用的运输订单标签
                List<TmTransportOrderLabelEntity> orderLabels = tmTransportOrderManager.findCanDispatchLabel(entity.getTransportNo(), entity.getOrgId());
                if (CollectionUtil.isNotEmpty(orderLabels)) {
                    entity.setTmTransportOrderLabelList(orderLabels);
                    entities.add(entity);
                }
            }
        }
        if (StringUtils.isNotBlank(msg)) {
            throw new TmsException(msg.toString());
        }
        if (CollectionUtil.isEmpty(entities)) {
            throw new TmsException("运输订单已全部调度派车");
        }

        // 生成派车单头
        TmDispatchOrderEntity entity = new TmDispatchOrderEntity();
        entity.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        entity.setDispatchTime(dispatchTime);
        entity.setDispatchType(dispatchType);
        entity.setTransportType("3");
        entity.setDispatchOutletCode(dispatchOutletCode);
        entity.setDispatcher(tmDirectDispatch.getDispatcher());
        entity.setShift(tmDirectDispatch.getShift());
        entity.setPlatform(tmDirectDispatch.getPlatform());
        entity.setStartAreaId(tmDirectDispatch.getStartAreaId());
        entity.setEndAreaId(tmDirectDispatch.getEndAreaId());
        entity.setCarrierCode(tmDirectDispatch.getCarrierCode());
        entity.setCarNo(tmDirectDispatch.getCarNo());
        entity.setDriver(tmDirectDispatch.getDriver());
        entity.setDriverTel(tmDirectDispatch.getDriverTel());
        entity.setIsException(TmsConstants.NO);
        entity.setIsAppInput(TmsConstants.NO);
        entity.setDataSource(SystemAliases.TMS.getCode());
        entity.setRemarks(tmDirectDispatch.getRemarks());
        entity.setBaseOrgId(tmDirectDispatch.getBaseOrgId());
        entity.setOrgId(tmDirectDispatch.getOrgId());
        tmDispatchOrderManager.saveEntity(entity);
        entity = tmDispatchOrderManager.getEntity(entity.getId());
        // 提货网点
        List<String> rOutletCodes = entities.stream().map(TmTransportOrderEntity::getReceiveOutletCode).distinct().filter(o -> !dispatchOutletCode.equals(o)).collect(Collectors.toList());
        // 送货网点
        List<String> sOutletCodes = entities.stream().map(TmTransportOrderEntity::getOutletCode).distinct().collect(Collectors.toList());

        // 生成派车配送点
        int i = 1;
        for (String outletCode : rOutletCodes) {
            TmDispatchOrderSiteEntity orderSite = new TmDispatchOrderSiteEntity();
            orderSite.setDispatchSeq(i++);
            orderSite.setDispatchNo(entity.getDispatchNo());
            if (sOutletCodes.stream().anyMatch(outletCode::equals)) {
                orderSite.setReceiveShip(TmsConstants.RECEIVE + "," + TmsConstants.SHIP);
                sOutletCodes.remove(outletCode);
            } else {
                orderSite.setReceiveShip(TmsConstants.RECEIVE);
            }
            orderSite.setOutletCode(outletCode);
            orderSite.setBaseOrgId(entity.getBaseOrgId());
            orderSite.setOrgId(entity.getOrgId());
            tmDispatchOrderSiteManager.save(orderSite);
        }
        for (String outletCode : sOutletCodes) {
            TmDispatchOrderSiteEntity orderSite = new TmDispatchOrderSiteEntity();
            orderSite.setDispatchSeq(i++);
            orderSite.setDispatchNo(entity.getDispatchNo());
            orderSite.setReceiveShip(TmsConstants.SHIP);
            orderSite.setOutletCode(outletCode);
            orderSite.setBaseOrgId(entity.getBaseOrgId());
            orderSite.setOrgId(entity.getOrgId());
            tmDispatchOrderSiteManager.save(orderSite);
        }

        // 生成派车标签
        for (TmTransportOrderEntity o : entities) {
            for (TmTransportOrderLabel orderLabel : o.getTmTransportOrderLabelList()) {
                TmDispatchOrderLabel rDispatchLabel = new TmDispatchOrderLabel();
                rDispatchLabel.setLabelNo(orderLabel.getLabelNo());
                rDispatchLabel.setDispatchNo(entity.getDispatchNo());
                rDispatchLabel.setDispatchSiteOutletCode(o.getReceiveOutletCode());
                rDispatchLabel.setReceiveShip(TmsConstants.RECEIVE);
                rDispatchLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
                rDispatchLabel.setTransportNo(o.getTransportNo());
                rDispatchLabel.setCustomerNo(o.getCustomerNo());
                rDispatchLabel.setOwnerCode(orderLabel.getOwnerCode());
                rDispatchLabel.setSkuCode(orderLabel.getSkuCode());
                rDispatchLabel.setQty(orderLabel.getQty());
                rDispatchLabel.setWeight(orderLabel.getWeight());
                rDispatchLabel.setCubic(orderLabel.getCubic());
                rDispatchLabel.setBaseOrgId(entity.getBaseOrgId());
                rDispatchLabel.setOrgId(entity.getOrgId());
                tmDispatchOrderLabelManager.save(rDispatchLabel);

                TmDispatchOrderLabel sDispatchLabel = new TmDispatchOrderLabel();
                sDispatchLabel.setLabelNo(orderLabel.getLabelNo());
                sDispatchLabel.setDispatchNo(entity.getDispatchNo());
                sDispatchLabel.setDispatchSiteOutletCode(o.getOutletCode());
                sDispatchLabel.setReceiveShip(TmsConstants.SHIP);
                sDispatchLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
                sDispatchLabel.setTransportNo(o.getTransportNo());
                sDispatchLabel.setCustomerNo(o.getCustomerNo());
                sDispatchLabel.setOwnerCode(orderLabel.getOwnerCode());
                sDispatchLabel.setSkuCode(orderLabel.getSkuCode());
                sDispatchLabel.setQty(orderLabel.getQty());
                sDispatchLabel.setWeight(orderLabel.getWeight());
                sDispatchLabel.setCubic(orderLabel.getCubic());
                sDispatchLabel.setBaseOrgId(entity.getBaseOrgId());
                sDispatchLabel.setOrgId(entity.getOrgId());
                tmDispatchOrderLabelManager.save(sDispatchLabel);

                // 标签路由绑定派车单
                TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(o.getTransportNo(), orderLabel.getLabelNo(), o.getBaseOrgId());
                if (!TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo()) && !entity.getDispatchNo().equals(orderRoute.getPreAllocDispatchNo())) {
                    throw new TmsException("运输订单[" + o.getTransportNo() + "]标签号[" + orderLabel.getLabelNo() + "]已被其它派车单绑定");
                }
                if (TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo())) {
                    // 运输网点路由标签tm_transport_order_route绑定派车单号
                    orderRoute.setPreAllocDispatchNo(entity.getDispatchNo());
                    tmTransportOrderRouteService.save(orderRoute);
                    // 标签网点路由信息加入配送网点授权信息
                    tmAuthorityManager.addOutletAuthorityData(o.getReceiveOutletCode(), entity.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
                    tmAuthorityManager.addOutletAuthorityData(o.getOutletCode(), entity.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
                }
            }
        }
        // 统计数量、重量、体积
        tmTotalManager.totalByDispatch(entity.getDispatchNo());
        // 审核
        tmDispatchOrderManager.audit(entity.getId());
        return entity.getDispatchNo();
    }
}
