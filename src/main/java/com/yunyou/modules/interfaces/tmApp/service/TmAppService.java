package com.yunyou.modules.interfaces.tmApp.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.tmApp.entity.*;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.service.TmVehicleService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderHeader;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderSku;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.*;
import com.yunyou.modules.tms.order.service.TmDispatchOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * app后台Service
 *
 * @author zyf
 * @version 2020-06-30
 */
@Service
public class TmAppService {
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmVehicleService tmVehicleService;
    @Autowired
    private TmDemandPlanManager tmDemandPlanManager;
    @Autowired
    private TmVehicleSafetyCheckManager tmVehicleSafetyCheckManager;
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;
    @Autowired
    private TmDispatchTankInfoManager tmDispatchTankInfoManager;
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmRepairOrderManager tmRepairOrderManager;
    @Autowired
    private TmExceptionHandleBillManager tmExceptionHandleBillManager;

    /**
     * 需求下单-生成需求订单
     */
    public ResultMessage createCustomerOrder(TmAppCreateCustomerOrderRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmDemandPlanEntity planHeaderEntity = new TmDemandPlanEntity();
            planHeaderEntity.setCreateBy(new User(request.getOperator()));
            planHeaderEntity.setCreateDate(new Date());
            planHeaderEntity.setUpdateBy(new User(request.getOperator()));
            planHeaderEntity.setUpdateDate(new Date());
            planHeaderEntity.setOrgId(request.getOrgId());
            planHeaderEntity.setOrderTime(request.getOrderTime());
            planHeaderEntity.setOwnerCode(request.getOwnerCode());
            planHeaderEntity.setArrivalTime(request.getArriveTime());
            planHeaderEntity.setBaseOrgId(request.getBaseOrgId());
            planHeaderEntity.setStatus("00");
            msg.setData(tmDemandPlanManager.saveEntity(planHeaderEntity));
            if (msg.isSuccess() && CollectionUtil.isNotEmpty(request.getSkuList())) {
                planHeaderEntity = (TmDemandPlanEntity)msg.getData();
                List<TmDemandPlanDetailEntity> detailList = Lists.newArrayList();
                for (TmAppCreateCustomerOrderSkuListRequest skuDetail : request.getSkuList()) {
                    if (skuDetail.getQty() == null || skuDetail.getQty() == 0) {
                        continue;
                    }
                    TmDemandPlanDetailEntity detailEntity = new TmDemandPlanDetailEntity();
                    detailEntity.setId(IdGen.uuid());
                    detailEntity.setCreateBy(new User(request.getOperator()));
                    detailEntity.setCreateDate(new Date());
                    detailEntity.setUpdateBy(new User(request.getOperator()));
                    detailEntity.setUpdateDate(new Date());
                    detailEntity.setOrgId(request.getOrgId());
                    detailEntity.setPlanOrderNo(planHeaderEntity.getPlanOrderNo());
                    detailEntity.setOwnerCode(request.getOwnerCode());
                    detailEntity.setSkuCode(skuDetail.getSkuCode());
                    detailEntity.setQty(skuDetail.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(skuDetail.getQty()));
                    detailEntity.setBaseOrgId(request.getBaseOrgId());
                    detailEntity.setIsNewRecord(true);
                    detailList.add(detailEntity);
                }
                tmDemandPlanManager.saveDetailList(detailList);
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 删除需求订单
     */
    public ResultMessage deleteCustomerOrder(TmAppDeleteCustomerOrderRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmDemandPlanEntity condition = new TmDemandPlanEntity();
            condition.setPlanOrderNo(request.getPlanOrderNo());
            condition.setOrgId(request.getOrgId());
            List<TmDemandPlanEntity> tmDemandPlanList = tmDemandPlanManager.findHeaderList(condition);
            if (CollectionUtil.isNotEmpty(tmDemandPlanList)) {
                if ("10".equals(tmDemandPlanList.get(0).getStatus())) {
                    msg.setSuccess(false);
                    msg.setMessage("已生成订单，无法删除！");
                    return msg;
                } else {
                    tmDemandPlanManager.removeEntity(tmDemandPlanManager.getEntity(tmDemandPlanList.get(0).getId()));
                }
            } else {
                msg.setSuccess(false);
                msg.setMessage("数据不存在，请刷新！");
                return msg;
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setSuccess(true);
        msg.setMessage("操作成功！");
        return msg;
    }

    /**
     * 保存出车安全检查表
     */
    public ResultMessage saveVehicleSafetyCheck(TmAppCreateVehicleSafetyCheckRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmVehicleSafetyCheckEntity saveEntity = new TmVehicleSafetyCheckEntity();
            if (StringUtils.isNotBlank(request.getId())) {
                saveEntity = tmVehicleSafetyCheckManager.getEntity(request.getId());
            } else {
                saveEntity.setCreateBy(new User(request.getOperator()));
                saveEntity.setCreateDate(new Date());
            }
            BeanUtils.copyProperties(request, saveEntity);
            saveEntity.setUpdateBy(new User(request.getOperator()));
            saveEntity.setUpdateDate(new Date());
            saveEntity.setCheckDate(new Date());
            msg.setData(tmVehicleSafetyCheckManager.saveEntity(saveEntity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 发车
     */
    public ResultMessage depart(TmAppDepartRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            tmDispatchOrderManager.depart(request.getDispatchId());
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 派车单关闭
     */
    public ResultMessage finish(TmAppDispatchOrderFinishRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            tmDispatchOrderManager.close(request.getDispatchId(), new User(request.getOperator()));
            TmVehicleSafetyCheckEntity tmVehicleSafetyCheck = tmVehicleSafetyCheckManager.getVehicleSafetyCheckIntraday(request.getCarNo(), request.getBaseOrgId());
            if (tmVehicleSafetyCheck != null) {
                tmVehicleSafetyCheck.setUpdateBy(new User(request.getOperator()));
                tmVehicleSafetyCheck.setUpdateDate(new Date());
                tmVehicleSafetyCheck.setReturnOdometerNumber(BigDecimal.valueOf(request.getOdometerNumber()));
                tmVehicleSafetyCheckManager.saveEntity(tmVehicleSafetyCheck);
            }
            TmVehicle tmVehicle = tmVehicleService.getByNo(request.getCarNo(), request.getBaseOrgId());
            if (tmVehicle != null) {
                tmVehicle.setMileage(request.getOdometerNumber());
                tmVehicle.setUpdateBy(new User(request.getOperator()));
                tmVehicle.setUpdateDate(new Date());
                tmVehicleService.save(tmVehicle);
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 网点交接 - 运输订单
     */
    public ResultMessage handoverConfirm(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            User user = new User(request.getOperator());
            tmHandoverOrderManager.handoverConfirmByTransportOrder(request.getDispatchNo(), request.getTransportNo(), request.getOutletCode(), request.getConfirmPerson(), request.getRemarks(), request.getRsFlag(), request.getOrgId(), user);
            TmHandoverOrderHeader handoverOrder = tmHandoverOrderManager.getHandoverByDispatchAndOutlet(request.getDispatchNo(), request.getOutletCode(), request.getOrgId());
            // 保存交接商品信息
            List<TmHandoverOrderSku> skuList = Lists.newArrayList();
            if (CollectionUtil.isNotEmpty(request.getSkuList())) {
                for (TmAppHandoverConfirmSkuListRequest skuRequest : request.getSkuList()) {
                    TmHandoverOrderSku handoverOrderSku = tmHandoverOrderManager.getHandoverSku(handoverOrder.getHandoverNo(), request.getTransportNo(), skuRequest.getOwnerCode(), skuRequest.getSkuCode(), request.getRsFlag(), handoverOrder.getOrgId());
                    handoverOrderSku.setActualQty(new BigDecimal(skuRequest.getActualQty()));
                    handoverOrderSku.setUnloadingTime(new BigDecimal(skuRequest.getUnloadingTime()));
                    skuList.add(handoverOrderSku);
                }
                tmHandoverOrderManager.batchSaveSku(skuList, user);
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 揽收 - 运输订单
     */
    public ResultMessage receiveConfirm(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            User user = new User(request.getOperator());
            tmHandoverOrderManager.appReceiveConfirmByTransportOrder(request.getDispatchNo(), request.getTransportNo(), request.getConfirmPerson(), request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), user);
            TmHandoverOrderHeader handoverOrder = tmHandoverOrderManager.getHandoverByDispatchAndOutlet(request.getDispatchNo(), request.getConfirmPerson(), request.getOrgId());
            // 保存交接商品信息
            List<TmHandoverOrderSku> skuList = Lists.newArrayList();
            if (CollectionUtil.isNotEmpty(request.getSkuList())) {
                for (TmAppHandoverConfirmSkuListRequest skuRequest : request.getSkuList()) {
                    TmHandoverOrderSku handoverOrderSku = tmHandoverOrderManager.getHandoverSku(handoverOrder.getHandoverNo(), request.getTransportNo(), skuRequest.getOwnerCode(), skuRequest.getSkuCode(), TmsConstants.RECEIVE, handoverOrder.getOrgId());
                    handoverOrderSku.setActualQty(new BigDecimal(skuRequest.getActualQty()));
                    handoverOrderSku.setUnloadingTime(new BigDecimal(skuRequest.getUnloadingTime()));
                    skuList.add(handoverOrderSku);
                }
                tmHandoverOrderManager.batchSaveSku(skuList, user);
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 签收 - 运输订单
     */
    public ResultMessage signConfirm(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            User user = new User(request.getOperator());
            tmHandoverOrderManager.appSignConfirmByTransportOrder(request.getDispatchNo(), request.getTransportNo(), request.getOutletCode(), request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), user);
            TmHandoverOrderHeader handoverOrder = tmHandoverOrderManager.getHandoverByDispatchAndOutlet(request.getDispatchNo(), request.getOutletCode(), request.getOrgId());
            List<TmHandoverOrderSku> skuList = Lists.newArrayList();
            if (CollectionUtil.isNotEmpty(request.getSkuList())) {
                for (TmAppHandoverConfirmSkuListRequest skuRequest : request.getSkuList()) {
                    TmHandoverOrderSku handoverOrderSku = tmHandoverOrderManager.getHandoverSku(handoverOrder.getHandoverNo(), request.getTransportNo(), skuRequest.getOwnerCode(), skuRequest.getSkuCode(), TmsConstants.SHIP, handoverOrder.getOrgId());
                    handoverOrderSku.setActualQty(new BigDecimal(skuRequest.getActualQty()));
                    handoverOrderSku.setUnloadingTime(new BigDecimal(skuRequest.getUnloadingTime()));
                    skuList.add(handoverOrderSku);
                }
                tmHandoverOrderManager.batchSaveSku(skuList, user);
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 网点交接 - 标签
     */
    public ResultMessage handoverConfirmLabel(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
//        msg = tmHandoverOrderAction.handoverConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
//                request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), request.getRsFlag(), new User(request.getOperator()));
        try {
            tmHandoverOrderManager.handoverConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
                    request.getConfirmPerson(), request.getRemarks(), request.getRsFlag(), request.getOrgId(), new User(request.getOperator()));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");

        return msg;
    }

    /**
     * 揽收 - 标签
     */
    public ResultMessage receiveConfirmLabel(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            tmHandoverOrderManager.appReceiveConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
                    request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), new User(request.getOperator()));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 签收 - 标签
     */
    public ResultMessage signConfirmLabel(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            tmHandoverOrderManager.appSignConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
                    request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), new User(request.getOperator()));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 附件保存
     */
    public ResultMessage saveTmAttachement(TmAppAttachementRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            if (TmsConstants.IMP_UPLOAD_TYPE_HANDOVER.equals(request.getOrderType())) {
                // 交接单图片保存
                tmHandoverOrderManager.saveAppImgDetail(request.getDispatchNo(), request.getOutletCode(), request.getOrderType(), request.getLabelNo(), request.getOrgId(),
                        request.getFileName(), request.getImgFilePath(), request.getImgUrl(), new User(request.getOperator()));
            } else if (TmsConstants.IMP_UPLOAD_TYPE_EXCEPTION.equals(request.getOrderType())) {
                // 异常单图片保存
                tmExceptionHandleBillManager.saveImgDetail(request.getOrderNo(), request.getOrgId(), request.getFileName(), request.getImgFilePath(),
                        request.getImgUrl(), new User(request.getOperator()));
            } else {
                msg.setSuccess(false);
                msg.setMessage("未知的图片类型！");
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 保存派车单现金费用信息
     */
    public ResultMessage saveDispatchOrderCashAmount(TmAppDispatchOrderSaveRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmDispatchOrderEntity con = new TmDispatchOrderEntity();
            con.setDispatchNo(request.getDispatchNo());
            con.setOrgId(request.getOrgId());
            List<TmDispatchOrderEntity> dispatchOrderList = tmDispatchOrderManager.findEntityList(con);
            if (CollectionUtil.isNotEmpty(dispatchOrderList)) {
                TmDispatchOrderEntity dispatchOrder = dispatchOrderList.get(0);
                dispatchOrder.setCashAmount(request.getCashAmount());
                dispatchOrder.setRemarks(request.getRemarks());
                dispatchOrder.setUpdateBy(new User(request.getOperator()));
                dispatchOrder.setUpdateDate(new Date());
                tmDispatchOrderManager.saveEntity(dispatchOrder);
                msg.setData(tmDispatchOrderManager.getEntity(dispatchOrder.getId()));
            } else {
                msg.setSuccess(false);
                msg.setMessage("派车单不存在！");
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 保存维修工单信息
     */
    public ResultMessage saveRepairOrder(TmAppSaveRepairOrderRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmRepairOrderEntity saveEntity = new TmRepairOrderEntity();
            BeanUtils.copyProperties(request, saveEntity);
            saveEntity.setCreateBy(new User(request.getOperator()));
            saveEntity.setCreateDate(new Date());
            saveEntity.setUpdateBy(new User(request.getOperator()));
            saveEntity.setUpdateDate(new Date());
            msg.setData(tmRepairOrderManager.get(tmRepairOrderManager.saveForUnRepair(saveEntity)));
        } catch (TmsException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 维修完成确认
     */
    public ResultMessage finishRepairOrder(TmAppSaveRepairOrderRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmRepairOrderEntity saveEntity = tmRepairOrderManager.getEntity(request.getId());
            saveEntity.setStatus("99");
            saveEntity.setUpdateBy(new User(request.getOperator()));
            saveEntity.setUpdateDate(new Date());
            msg.setData(tmRepairOrderManager.saveForUnRepair(saveEntity));
        } catch (TmsException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 保存派车装罐信息
     */
    public ResultMessage saveDispatchTankInfo(TmAppCreateDispatchTankInfoRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmDispatchTankInfoEntity saveEntity = new TmDispatchTankInfoEntity();
            if (StringUtils.isNotBlank(request.getId())) {
                saveEntity = tmDispatchTankInfoManager.getEntity(request.getId());
            } else {
                saveEntity.setCreateBy(new User(request.getOperator()));
                saveEntity.setCreateDate(new Date());
            }
            BeanUtils.copyProperties(request, saveEntity);
            saveEntity.setUpdateBy(new User(request.getOperator()));
            saveEntity.setUpdateDate(new Date());
            msg.setData(tmDispatchTankInfoManager.saveEntity(saveEntity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 异常上报
     */
    public ResultMessage exceptionConfirm(TmAppExceptionConfirmRequest request) {
        ResultMessage msg = new ResultMessage();
        try {
            TmTransportOrderHeader transportOrderHeader = tmTransportOrderHeaderService.getByNo(request.getTransportNo());
            TmDispatchOrderHeader dispatchOrderHeader = tmDispatchOrderHeaderService.getByNo(request.getDispatchNo());
            TmExceptionHandleBillEntity saveEntity = new TmExceptionHandleBillEntity();
            BeanUtils.copyProperties(request, saveEntity);
            saveEntity.setHappenTime(request.getHappenDate());
            saveEntity.setCustomerNo(transportOrderHeader.getCustomerNo());
            saveEntity.setConsigneeCode(transportOrderHeader.getConsigneeCode());
            saveEntity.setConsigneeName(transportOrderHeader.getConsignee());
            saveEntity.setConsigneePhone(transportOrderHeader.getConsigneeTel());
            saveEntity.setCustomerCode(transportOrderHeader.getCustomerCode());
            saveEntity.setCarNo(dispatchOrderHeader.getCarNo());
            saveEntity.setDriver(dispatchOrderHeader.getDriver());
            saveEntity.setCreateBy(new User(request.getOperator()));
            saveEntity.setCreateDate(new Date());
            saveEntity.setUpdateBy(new User(request.getOperator()));
            saveEntity.setUpdateDate(new Date());
            msg.setData(tmExceptionHandleBillManager.saveEntity(saveEntity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}