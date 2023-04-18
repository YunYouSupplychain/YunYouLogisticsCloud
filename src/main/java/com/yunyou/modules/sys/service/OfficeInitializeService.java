package com.yunyou.modules.sys.service;

import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.wms.basicdata.entity.*;
import com.yunyou.modules.wms.basicdata.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 机构创建时初始化
 *
 * @author liujianhua
 */
@Service
@Transactional
public class OfficeInitializeService implements OfficeInitialize{
    @Autowired
    private BanQinCdWhAreaService cdWhAreaService;
    @Autowired
    private BanQinCdWhZoneService cdWhZoneService;
    @Autowired
    private BanQinCdWhLocService cdWhLocService;
    @Autowired
    private BanQinCdWhLotHeaderService cdWhLotHeaderService;
    @Autowired
    private BanQinCdWhLotDetailService cdWhLotDetailService;
    @Autowired
    private BanQinCdWhQcItemHeaderService cdWhQcItemHeaderService;
    @Autowired
    private BanQinCdWhQcItemDetailService cdWhQcItemDetailService;
    @Autowired
    private BanQinCdWhPackageService cdWhPackageService;
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;
    @Autowired
    private BanQinCdRuleQcHeaderService cdRuleQcHeaderService;
    @Autowired
    private BanQinCdRulePaHeaderService cdRulePaHeaderService;
    @Autowired
    private BanQinCdRulePaDetailService cdRulePaDetailService;
    @Autowired
    private BanQinCdRuleRotationHeaderService cdRuleRotationHeaderService;
    @Autowired
    private BanQinCdRuleRotationDetailService cdRuleRotationDetailService;
    @Autowired
    private BanQinCdRuleAllocHeaderService cdRuleAllocHeaderService;
    @Autowired
    private BanQinCdRuleAllocDetailService cdRuleAllocDetailService;

    @Autowired
    private TmTransportObjService tmTransportObjService;

    /**
     * 机构创建时初始化数据
     *
     * @param orgId 机构ID
     */
    @Override
    public void init(String orgId) {
        // 仓储系统
        initOrgForWms(orgId);
        // 运输系统
        initOrgForTms(orgId);
    }

    /**
     * 初始化仓储系统
     *
     * @param orgId 机构ID
     */
    public void initOrgForWms(String orgId) {
        // 区域
        BanQinCdWhArea cdWhArea = cdWhAreaService.getByCode("AREA", orgId);
        if (cdWhArea == null) {
            cdWhArea = new BanQinCdWhArea();
            cdWhArea.setAreaCode("AREA");
            cdWhArea.setAreaName("AREA");
            cdWhArea.setRemarks("系统默认");
            cdWhArea.setOrgId(orgId);
            cdWhAreaService.save(cdWhArea);
        }

        // 库区
        BanQinCdWhZone cdWhZone = cdWhZoneService.getByCode("ZONE", orgId);
        if (cdWhZone == null) {
            cdWhZone = new BanQinCdWhZone();
            cdWhZone.setZoneCode("ZONE");
            cdWhZone.setZoneName("ZONE");
            cdWhZone.setType("1");
            cdWhZone.setAreaCode("AREA");
            cdWhZone.setAreaName("AREA");
            cdWhZone.setRemarks("系统默认");
            cdWhZone.setOrgId(orgId);
            cdWhZoneService.save(cdWhZone);
        }

        // 库位
        // 库位 STAGE
        BanQinCdWhLoc stage = cdWhLocService.getByCode("STAGE", orgId);
        if (stage == null) {
            stage = new BanQinCdWhLoc();
            stage.setLocCode("STAGE");
            stage.setLocUseType("ST");
            stage.setCategory("1");
            stage.setZoneCode("ZONE");
            stage.setZoneName("ZONE");
            stage.setPaSeq("STAGE");
            stage.setPkSeq("STAGE");
            stage.setStatus("00");
            stage.setMaxMixLot(0L);
            stage.setMaxMixSku(0L);
            stage.setIsEnable("Y");
            stage.setIsLoseId("N");
            stage.setIsMixLot("Y");
            stage.setIsMixSku("Y");
            stage.setOrgId(orgId);
            cdWhLocService.save(stage);
        }
        // 库位 CROSSDOCK
        BanQinCdWhLoc crossDock = cdWhLocService.getByCode("CROSSDOCK", orgId);
        if (crossDock == null) {
            crossDock = new BanQinCdWhLoc();
            crossDock.setLocCode("CROSSDOCK");
            crossDock.setLocUseType("CD");
            crossDock.setCategory("1");
            crossDock.setZoneCode("ZONE");
            crossDock.setZoneName("ZONE");
            crossDock.setPaSeq("CROSSDOCK");
            crossDock.setPkSeq("CROSSDOCK");
            crossDock.setStatus("00");
            crossDock.setMaxMixLot(0L);
            crossDock.setMaxMixSku(0L);
            crossDock.setIsEnable("Y");
            crossDock.setIsLoseId("N");
            crossDock.setIsMixLot("Y");
            crossDock.setIsMixSku("Y");
            crossDock.setOrgId(orgId);
            cdWhLocService.save(crossDock);
        }
        // 库位 SORTATION
        BanQinCdWhLoc sortation = cdWhLocService.getByCode("SORTATION", orgId);
        if (sortation == null) {
            sortation = new BanQinCdWhLoc();
            sortation.setLocCode("SORTATION");
            sortation.setLocUseType("SS");
            sortation.setCategory("1");
            sortation.setZoneCode("ZONE");
            sortation.setZoneName("ZONE");
            sortation.setPaSeq("SORTATION");
            sortation.setPkSeq("SORTATION");
            sortation.setStatus("00");
            sortation.setMaxMixLot(0L);
            sortation.setMaxMixSku(0L);
            sortation.setIsEnable("Y");
            sortation.setIsLoseId("N");
            sortation.setIsMixLot("Y");
            sortation.setIsMixSku("Y");
            sortation.setOrgId(orgId);
            cdWhLocService.save(sortation);
        }
        // 库位 WORKBENCH
        BanQinCdWhLoc workbench = cdWhLocService.getByCode("WORKBENCH", orgId);
        if (workbench == null) {
            workbench = new BanQinCdWhLoc();
            workbench.setLocCode("WORKBENCH");
            workbench.setLocUseType("KT");
            workbench.setCategory("1");
            workbench.setZoneCode("ZONE");
            workbench.setZoneName("ZONE");
            workbench.setPaSeq("WORKBENCH");
            workbench.setPkSeq("WORKBENCH");
            workbench.setStatus("00");
            workbench.setMaxMixLot(0L);
            workbench.setMaxMixSku(0L);
            workbench.setIsEnable("Y");
            workbench.setIsLoseId("N");
            workbench.setIsMixLot("Y");
            workbench.setIsMixSku("Y");
            workbench.setOrgId(orgId);
            cdWhLocService.save(workbench);
        }

        // 批次属性
        BanQinCdWhLotHeader cdWhLotHeader = cdWhLotHeaderService.getByCode("01", orgId);
        if (cdWhLotHeader == null) {
            cdWhLotHeader = new BanQinCdWhLotHeader();
            cdWhLotHeader.setLotCode("01");
            cdWhLotHeader.setLotName("标准批次");
            cdWhLotHeader.setOrgId(orgId);
            cdWhLotHeader.setCdWhLotDetailList(cdWhLotDetailService.initialList());
            cdWhLotHeaderService.save(cdWhLotHeader);
        }

        // 质检项
        BanQinCdWhQcItemHeader cdWhQcItemHeader = cdWhQcItemHeaderService.getByCode("01", orgId);
        if (cdWhQcItemHeader == null) {
            cdWhQcItemHeader = new BanQinCdWhQcItemHeader();
            cdWhQcItemHeader.setItemGroupCode("01");
            cdWhQcItemHeader.setItemGroupName("标准质检项");
            cdWhQcItemHeader.setOrgId(orgId);
            cdWhQcItemHeaderService.save(cdWhQcItemHeader);
            BanQinCdWhQcItemDetail cdWhQcItemDetail = new BanQinCdWhQcItemDetail();
            cdWhQcItemDetail.setHeaderId(cdWhQcItemHeader.getId());
            cdWhQcItemDetail.setItemGroupCode(cdWhQcItemHeader.getItemGroupCode());
            cdWhQcItemDetail.setQcItem("目视");
            cdWhQcItemDetail.setQcRef("VISUAL");
            cdWhQcItemDetail.setOrgId(orgId);
            cdWhQcItemDetailService.save(cdWhQcItemDetail);
        }

        // 包装
        BanQinCdWhPackage cdWhPackage = cdWhPackageService.findByPackCode("01", orgId);
        if (cdWhPackage == null) {
            cdWhPackage = new BanQinCdWhPackage();
            cdWhPackage.setCdpaCode("01");
            cdWhPackage.setCdpaType("M_P_G");
            cdWhPackage.setCdpaFormat("1/1/1");
            cdWhPackage.setCdpaFormatEn("1/1/1");
            cdWhPackage.setCdpaDesc("默认包装规格");
            cdWhPackage.setCdpaIsUse("0");
            cdWhPackage.setOrgId(orgId);
            cdWhPackage.setPackageDetailList(cdWhPackageRelationService.initialList());
            cdWhPackageService.save(cdWhPackage);
        }

        // 质检规则
        BanQinCdRuleQcHeader cdRuleQcHeader = cdRuleQcHeaderService.getByRuleCode("01", orgId);
        if (cdRuleQcHeader == null) {
            cdRuleQcHeader = new BanQinCdRuleQcHeader();
            cdRuleQcHeader.setRuleCode("01");
            cdRuleQcHeader.setRuleName("标准质检");
            cdRuleQcHeader.setOrgId(orgId);
            cdRuleQcHeaderService.save(cdRuleQcHeader);
        }

        // 上架规则
        BanQinCdRulePaHeader cdRulePaHeader = cdRulePaHeaderService.getByRuleCode("01", orgId);
        if (cdRulePaHeader == null) {
            cdRulePaHeader = new BanQinCdRulePaHeader();
            cdRulePaHeader.setRuleCode("01");
            cdRulePaHeader.setRuleName("标准上架");
            cdRulePaHeader.setOrgId(orgId);
            cdRulePaHeaderService.save(cdRulePaHeader);
            BanQinCdRulePaDetail cdRulePaDetail = new BanQinCdRulePaDetail();
            cdRulePaDetail.setHeaderId(cdRulePaHeader.getId());
            cdRulePaDetail.setRuleCode(cdRulePaHeader.getRuleCode());
            cdRulePaDetail.setLineNo("01");
            cdRulePaDetail.setMainCode("B01");
            cdRulePaDetail.setIsEnable("Y");
            cdRulePaDetail.setToZone("ZONE");
            cdRulePaDetail.setIsPackageRestrict("N");
            cdRulePaDetail.setIsLessCs("N");
            cdRulePaDetail.setIsMoreCsLessPl("N");
            cdRulePaDetail.setIsMorePl("N");
            cdRulePaDetail.setIsAsnTypeRestrict("N");
            cdRulePaDetail.setIsLotAttRestrict("N");
            cdRulePaDetail.setIsUseTypeRestrict("Y");
            cdRulePaDetail.setIncludeUseType("RS,CS,EA,PC");
            cdRulePaDetail.setIsSpaceRestrict("N");
            cdRulePaDetail.setIsPlRestrict("N");
            cdRulePaDetail.setIsCubicRestrict("N");
            cdRulePaDetail.setIsWeightRestrict("N");
            cdRulePaDetail.setIsCategoryRestrict("N");
            cdRulePaDetail.setIsAbcRestrict("N");
            cdRulePaDetail.setOrgId(orgId);
            cdRulePaDetailService.save(cdRulePaDetail);
        }

        // 库存周转规则
        BanQinCdRuleRotationHeader cdRuleRotationHeader = cdRuleRotationHeaderService.findByRuleCode("01", orgId);
        if (cdRuleRotationHeader == null) {
            cdRuleRotationHeader = new BanQinCdRuleRotationHeader();
            cdRuleRotationHeader.setRuleCode("01");
            cdRuleRotationHeader.setRuleName("标准周转");
            cdRuleRotationHeader.setRotationType("ROTATION");
            cdRuleRotationHeader.setLotCode("01");
            cdRuleRotationHeader.setOrgId(orgId);
            cdRuleRotationHeaderService.save(cdRuleRotationHeader);
            BanQinCdRuleRotationDetail cdRuleRotationDetail = new BanQinCdRuleRotationDetail();
            cdRuleRotationDetail.setHeaderId(cdRuleRotationHeader.getId());
            cdRuleRotationDetail.setRuleCode(cdRuleRotationHeader.getRuleCode());
            cdRuleRotationDetail.setLineNo("01");
            cdRuleRotationDetail.setLotAtt("LOT_ATT01");
            cdRuleRotationDetail.setOrderBy("A");
            cdRuleRotationDetail.setOrgId(orgId);
            cdRuleRotationDetailService.save(cdRuleRotationDetail);
        }

        // 分配规则
        BanQinCdRuleAllocHeader cdRuleAllocHeader = cdRuleAllocHeaderService.getByRuleCode("01", orgId);
        if (cdRuleAllocHeader == null) {
            cdRuleAllocHeader = new BanQinCdRuleAllocHeader();
            cdRuleAllocHeader.setRuleCode("01");
            cdRuleAllocHeader.setRuleName("标准分配");
            cdRuleAllocHeader.setOrgId(orgId);
            cdRuleAllocHeaderService.save(cdRuleAllocHeader);
            BanQinCdRuleAllocDetail cdRuleAllocDetail = new BanQinCdRuleAllocDetail();
            cdRuleAllocDetail.setHeaderId(cdRuleAllocHeader.getId());
            cdRuleAllocDetail.setRuleCode(cdRuleAllocHeader.getRuleCode());
            cdRuleAllocDetail.setLineNo("01");
            cdRuleAllocDetail.setUom("EA");
            cdRuleAllocDetail.setIsClearFirst("Y");
            cdRuleAllocDetail.setOrgId(orgId);
            cdRuleAllocDetailService.save(cdRuleAllocDetail);
        }
    }

    /**
     * 初始化运输系统
     *
     * @param orgId 机构ID
     */
    public void initOrgForTms(String orgId) {
        Office orgCenter = UserUtils.getOrgCenter(orgId);
        if (orgCenter == null || StringUtils.isBlank(orgCenter.getId())) {
            return;
        }
        // 组织中心ID
        String baseOrgId = orgCenter.getId();

        // 网点
        // 默认提货网点
        TmTransportObj dds = tmTransportObjService.getByCode(TmsConstants.DEFAULT_DELIVERY_SITE, baseOrgId);
        if (dds == null) {
            dds = new TmTransportObj();
            dds.setTransportObjCode(TmsConstants.DEFAULT_DELIVERY_SITE);
            dds.setTransportObjName("默认提货网点");
            dds.setTransportObjType(CustomerType.OUTLET.getCode());
            dds.setOrgId(baseOrgId);
            tmTransportObjService.save(dds);
        }
        // 默认送货网点
        TmTransportObj drs = tmTransportObjService.getByCode(TmsConstants.DEFAULT_RECEIVE_SITE, baseOrgId);
        if (drs == null) {
            drs = new TmTransportObj();
            drs.setTransportObjCode(TmsConstants.DEFAULT_RECEIVE_SITE);
            drs.setTransportObjName("默认送货网点");
            drs.setTransportObjType(CustomerType.OUTLET.getCode());
            drs.setOrgId(baseOrgId);
            tmTransportObjService.save(drs);
        }

    }
}
