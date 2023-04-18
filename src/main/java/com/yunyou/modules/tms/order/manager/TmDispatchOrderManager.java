package com.yunyou.modules.tms.order.manager;

import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.g7.G7Client;
import com.yunyou.modules.interfaces.gps.g7.constant.ApiPathList;
import com.yunyou.modules.interfaces.gps.g7.entity.VehicleTracksByGpsNoResponse;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.common.map.geo.Point;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.mapper.TmDispatchOrderMapper;
import com.yunyou.modules.tms.order.service.TmDispatchOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderLabelService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderSiteService;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：派车单业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchOrderManager extends CrudService<TmDispatchOrderMapper, TmDispatchOrderHeader> {
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDispatchOrderSiteService tmDispatchOrderSiteService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmDeliverManager tmDeliverManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private AreaService areaService;

    public TmDispatchOrderEntity getEntity(String id) {
        TmDispatchOrderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setTmDispatchOrderSiteList(tmDispatchOrderSiteManager.findList(new TmDispatchOrderSiteEntity(entity.getDispatchNo(), entity.getOrgId())));
        }
        return entity;
    }

    public List<TmDispatchOrderEntity> findEntityList(TmDispatchOrderEntity entity) {
        dataRuleFilter(entity);
        return mapper.findOrderList(entity);
    }

    public List<TmDispatchOrderLabelEntity> findList(TmDispatchOrderLabelEntity qEntity) {
        return mapper.findLabelList(qEntity);
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchOrderEntity> findPage(Page page, TmDispatchOrderEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchOrderLabelEntity> findPage(Page page, TmDispatchOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findLabelPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderEntity> findTransportPage(Page page, TmDispatchOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        List<TmTransportOrderEntity> list = mapper.findTransportPage(qEntity);
        for (TmTransportOrderEntity o : list) {
            o.setShipCity(areaService.getFullName(o.getShipCityId()));
            o.setConsigneeCity(areaService.getFullName(o.getConsigneeCityId()));
        }
        page.setList(list);
        return page;
    }

    public Page<TmCarrierFreight> findCarrierFreight(Page<TmCarrierFreight> page, TmCarrierFreight qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findCarrierFreight(qEntity));
        return page;
    }

    @Transactional
    public void saveEntity(TmDispatchOrderEntity entity) {
        if (entity.getDispatchTime() == null) {
            throw new TmsException("派车时间不能为空");
        }
        if (StringUtils.isBlank(entity.getDispatchType())) {
            throw new TmsException("派车单类型不能为空");
        }
        if (StringUtils.isBlank(entity.getCarrierCode())) {
            throw new TmsException("承运商不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(entity.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        if (StringUtils.isBlank(entity.getDispatchNo())) {
            entity.setDispatchNo(noService.getDocumentNo(GenNoType.TM_DISPATCH_NO.name()));
        }
        if (StringUtils.isBlank(entity.getDataSource())) {
            entity.setDataSource(SystemAliases.TMS.getCode());
        }
        if (StringUtils.isBlank(entity.getFeedBackStatus())) {
            entity.setFeedBackStatus(TmsConstants.DISPATCH_FEEDBACK_STATUS_00);
        }
        tmDispatchOrderHeaderService.save(entity);
        if (TmsConstants.DISPATCH_ORDER_STATUS_00.equals(entity.getDispatchStatus())) {
            tmAuthorityManager.genDispatchAuthorityData(entity.getDispatchNo(), TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), entity.getId());
        }
    }

    @Transactional
    public void removeEntity(TmDispatchOrderEntity entity) {
        if (TmsConstants.DS_01.equals(entity.getDataSource())) {
            throw new TmsException(MessageFormat.format("派车单【{0}】来源于调度计划，请至调度计划操作", entity.getDispatchNo()));
        }
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(entity.getDispatchStatus())) {
            throw new TmsException(MessageFormat.format("派车单【{0}】不是新建状态，无法操作", entity.getDispatchNo()));
        }
        tmDispatchOrderSiteManager.remove(tmDispatchOrderSiteService.findList(new TmDispatchOrderSite(entity.getDispatchNo(), entity.getOrgId())));
        tmDispatchOrderHeaderService.delete(new TmDispatchOrderHeader(entity.getId()));
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), entity.getId());
    }

    @Transactional
    public void audit(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]不是新建状态，无法操作");
        }
        // 校验配载标签中所有的提货标签是否存在对应的收货标签
        List<TmDispatchOrderLabelEntity> orderLabels = this.findList(new TmDispatchOrderLabelEntity(orderHeader.getDispatchNo(), orderHeader.getOrgId()));
        if (CollectionUtil.isEmpty(orderLabels)) {
            throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]未配载订单，无法操作");
        }
        List<TmDispatchOrderLabel> rOrderLabels = orderLabels.stream().filter(o -> TmsConstants.RECEIVE.equals(o.getReceiveShip())).collect(Collectors.toList());
        Map<String, List<TmDispatchOrderLabel>> map = orderLabels.stream().collect(Collectors.groupingBy(TmDispatchOrderLabel::getLabelNo));
        for (TmDispatchOrderLabel orderLabel : rOrderLabels) {
            if (map.get(orderLabel.getLabelNo()).size() == 1) {
                throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]标签[" + orderLabel.getLabelNo() + "]仅配载提货点未配载送货点，无法操作");
            }
        }
        orderHeader.setDispatchStatus(TmsConstants.TRANSPORT_ORDER_STATUS_10);
        tmDispatchOrderHeaderService.save(orderHeader);

        // 生成交接单
        tmHandoverOrderManager.createByDispatch(orderHeader.getDispatchNo());
    }

    @Transactional
    public void cancelAudit(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (!TmsConstants.DISPATCH_ORDER_STATUS_10.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException(MessageFormat.format("派车单[{0}]不是审核状态，无法操作", orderHeader.getDispatchNo()));
        }
        orderHeader.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        tmDispatchOrderHeaderService.save(orderHeader);

        // 删除交接单
        tmHandoverOrderManager.removeByDispatch(orderHeader.getDispatchNo(), orderHeader.getOrgId());
    }

    @Transactional
    public void depart(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (orderHeader == null) {
            throw new TmsException("无效的订单");
        }
        if (!TmsConstants.DISPATCH_ORDER_STATUS_10.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException(MessageFormat.format("派车单[{0}]不是审核状态，无法操作", orderHeader.getDispatchNo()));
        }

        // TMS参数：派车单发车时派车网点自动发货(Y:是 N:否)
        final String DEPART_OUTLET_AUTO_SHIP = SysControlParamsUtils.getValue(SysParamConstants.DEPART_OUTLET_AUTO_SHIP, orderHeader.getOrgId());
        if (TmsConstants.YES.equals(DEPART_OUTLET_AUTO_SHIP)) {
            TmDeliverEntity qEntity = new TmDeliverEntity();
            qEntity.setPreAllocDispatchNo(orderHeader.getDispatchNo());
            qEntity.setNowOutletCode(orderHeader.getDispatchOutletCode());
            qEntity.setOrgId(orderHeader.getOrgId());
            qEntity.setBaseOrgId(orderHeader.getBaseOrgId());
            List<TmDeliverEntity> list = tmDeliverManager.findPage(new Page<>(), qEntity).getList();
            for (TmDeliverEntity entity : list) {
                tmDeliverManager.deliverByLabel(entity.getDispatchNo(), entity.getTransportNo(), entity.getLabelNo(), entity.getNowOutletCode(), entity.getNextOutletCode());
                tmTransportOrderTrackService.saveTrackNode(entity.getTransportNo(), entity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_SHIP, UserUtils.getUser());
            }
        } else {
            // 校验派车网点是否已发货
            TmDispatchOrderLabel qEntity = new TmDispatchOrderLabel();
            qEntity.setDispatchNo(orderHeader.getDispatchNo());
            qEntity.setDispatchSiteOutletCode(orderHeader.getDispatchOutletCode());
            qEntity.setReceiveShip(TmsConstants.RECEIVE);
            qEntity.setOrgId(orderHeader.getOrgId());
            List<TmDispatchOrderLabel> orderLabels = tmDispatchOrderLabelService.findList(qEntity);
            for (TmDispatchOrderLabel orderLabel : orderLabels) {
                TmDeliverLabel tmDeliverLabel = new TmDeliverLabel();
                tmDeliverLabel.setDispatchNo(orderHeader.getDispatchNo());
                tmDeliverLabel.setDeliverOutletCode(orderHeader.getDispatchOutletCode());
                tmDeliverLabel.setLabelNo(orderLabel.getLabelNo());
                List<TmDeliverLabel> deliverRecord = tmDeliverManager.findList(tmDeliverLabel);
                if (CollectionUtil.isEmpty(deliverRecord)) {
                    throw new TmsException(MessageFormat.format("标签[{1}]在网点[{0}]未发货", orderHeader.getDispatchOutletCode(), orderLabel.getLabelNo()));
                }
            }
        }
        orderHeader.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_20);
        orderHeader.setDepartureTime(new Date());
        tmDispatchOrderHeaderService.save(orderHeader);
    }

    /**
     * 关闭（app回场）（不校验交接单状态）
     */
    @Transactional
    public void close(String id, User user) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (orderHeader == null) {
            return;
        }
        orderHeader.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_99);
        orderHeader.setUpdateBy(user);
        tmDispatchOrderHeaderService.save(orderHeader);
    }

    @Transactional
    public void copy(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);

        TmDispatchOrderEntity entity = new TmDispatchOrderEntity();
        BeanUtils.copyProperties(orderHeader, entity);
        entity.setId("");
        entity.setRecVer(0);
        entity.setDispatchNo(noService.getDocumentNo(GenNoType.TM_DISPATCH_NO.name()));
        entity.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        entity.setDepartureTime(null);
        entity.setIsAppInput(TmsConstants.NO);
        entity.setIsException(TmsConstants.NO);
        entity.setTotalQty(null);
        entity.setTotalWeight(null);
        entity.setTotalCubic(null);
        entity.setTotalAmount(null);
        this.saveEntity(entity);

        List<TmDispatchOrderSite> orderSiteList = tmDispatchOrderSiteService.findList(new TmDispatchOrderSite(orderHeader.getDispatchNo(), orderHeader.getOrgId()));
        for (TmDispatchOrderSite orderSite : orderSiteList) {
            orderSite.setId("");
            orderSite.setRecVer(0);
            orderSite.setDispatchNo(entity.getDispatchNo());
            tmDispatchOrderSiteManager.save(orderSite);
        }
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchOrderEntity> transportCheckDispatchPage(Page page, TmDispatchOrderEntity qEntity) {
        TmDispatchOrderLabelEntity labelEntity = new TmDispatchOrderLabelEntity();
        labelEntity.setTransportNo(qEntity.getTransportNo());
        labelEntity.setBaseOrgId(qEntity.getBaseOrgId());

        List<TmDispatchOrderLabelEntity> labelEntityList = this.findList(labelEntity);
        if (CollectionUtil.isNotEmpty(labelEntityList)) {
            List<String> dispatchNoList = labelEntityList.stream().map(TmDispatchOrderLabel::getDispatchNo).distinct().collect(Collectors.toList());
            qEntity.setDispatchNoList(dispatchNoList);

            dataRuleFilter(qEntity);
            String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), qEntity.getOrgId());
            qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
            qEntity.setPage(page);
            page.setList(mapper.findPage(qEntity));
            return page;
        } else {
            return page;
        }
    }

    /**
     * 描述：强制删除
     */
    @Transactional
    public void forceRemove(String dispatchNo, String orgId, String baseOrgId) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        if (orderHeader != null) {
            if (TmsConstants.DISPATCH_ORDER_STATUS_99.equals(orderHeader.getDispatchStatus())) {
                throw new TmsException("该调度计划对应的派车单已经关闭，无法撤回");
            }
            // 校验是否全部交接
            TmHandoverOrderHeader qEntity = new TmHandoverOrderHeader();
            qEntity.setDispatchNo(dispatchNo);
            qEntity.setBaseOrgId(baseOrgId);
            qEntity.setOrgId(orgId);
            List<TmHandoverOrderHeader> handoverOrders = tmHandoverOrderManager.findList(qEntity);
            if (handoverOrders.stream().allMatch(o -> TmsConstants.HANDOVER_ORDER_STATUS_20.equals(o.getStatus()))) {
                throw new TmsException("该调度计划对应的派车单已经全部交接，无法撤回");
            }
            // 删除派车单
            tmDispatchOrderHeaderService.delete(new TmDispatchOrderHeader(orderHeader.getId()));
            // 删除授权数据信息
            tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), orderHeader.getId());
            // 删除关联交接单
            tmHandoverOrderManager.forceRemove(dispatchNo, orgId, baseOrgId);
            // 删除派车标签
            List<TmDispatchOrderLabel> orderLabels = tmDispatchOrderLabelService.findList(new TmDispatchOrderLabel(dispatchNo, orgId));
            for (TmDispatchOrderLabel orderLabel : orderLabels) {
                tmDispatchOrderLabelService.delete(orderLabel);
                // 删除授权数据信息
                tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_LABEL.getValue(), orderLabel.getId());
            }
            // 删除派车配送点
            List<TmDispatchOrderSite> orderSites = tmDispatchOrderSiteService.findList(new TmDispatchOrderSite(dispatchNo, orgId));
            for (TmDispatchOrderSite orderSite : orderSites) {
                tmDispatchOrderSiteService.delete(orderSite);
                // 删除授权数据信息
                tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_SITE.getValue(), orderSite.getId());
            }
        }
    }

    public Map<String, List<Point>> findVehicleTracks(String transportNo, String baseOrgId, String orgId) {
        Map<String, List<Point>> rsMap = Maps.newHashMap();

        /*List<TmDispatchVehicleEntity> entities = mapper.findDispatchVehicles(transportNo, baseOrgId, orgId);
        for (TmDispatchVehicleEntity entity : entities) {
            Map<String, String> queries = Maps.newHashMap();
            queries.put("gpsno", entity.getEquipmentNo());
            queries.put("from", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, entity.getDispatchTime()));
            queries.put("to", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, "99".equals(entity.getDispatchStatus()) ? entity.getLastTravelTime() : new Date()));
            queries.put("map", "baidu");
            VehicleTracksByGpsNoResponse[] tracks = G7Utils.send(ApiPathList.GET_VEHICLE_TRACKS_BY_GPS_NO, queries);
            if (tracks != null) {
                List<MapPoint> points = Lists.newArrayList();
                for (VehicleTracksByGpsNoResponse track : tracks) {
                    points.add(new MapPoint(track.getLng(), track.getLat()));
                }
                rsMap.put(entity.getVehicleNo(), points);
            }
        }*/

        Map<String, String> queries = Maps.newHashMap();
        queries.put("gpsno", "90320098");
        queries.put("from", "2021-06-25 00:00:00");
        queries.put("to", "2021-06-30 00:00:00");
        queries.put("map", "baidu");
        try {
            Response response = G7Client.get(ApiPathList.GET_VEHICLE_TRACKS_BY_GPS_NO, queries);
            String body = response.getBody();
            JSONObject res = JSONObject.parseObject(body);
            if (res.getInteger("code") == 0 && res.getInteger("sub_code") == 0) {
                List<Point> points = Lists.newArrayList();
                List<VehicleTracksByGpsNoResponse> tracks = res.getJSONArray("data").toJavaList(VehicleTracksByGpsNoResponse.class);
                for (VehicleTracksByGpsNoResponse track : tracks) {
                    points.add(new Point(track.getLng(), track.getLat()));
                }
                rsMap.put("90320098", points);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsMap;
    }

    public List<TmNoReturnVehicleInfo> findNoReturnVehicle() {
        return mapper.findNoReturnVehicle();
    }
}