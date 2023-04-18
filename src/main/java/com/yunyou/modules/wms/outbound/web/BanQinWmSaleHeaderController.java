package com.yunyou.modules.wms.outbound.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleHeader;
import com.yunyou.modules.wms.outbound.service.BanQinWmSaleHeaderService;

/**
 * 销售单Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmSaleHeader")
public class BanQinWmSaleHeaderController extends BaseController {

    @Autowired
    private BanQinWmSaleHeaderService banQinWmSaleHeaderService;

    @ModelAttribute
    public BanQinWmSaleEntity get(@RequestParam(required = false) String id) {
        BanQinWmSaleEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmSaleHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmSaleEntity();
        }
        return entity;
    }

    /**
     * 销售单列表页面
     */
    @RequiresPermissions("outbound:banQinWmSaleHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmSaleHeaderList";
    }

    /**
     * 销售单列表数据
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSaleHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSaleEntity banQinWmSaleEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmSaleEntity> page = banQinWmSaleHeaderService.findPage(new Page<BanQinWmSaleEntity>(request, response), banQinWmSaleEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑销售单表单页面
     */
    @RequiresPermissions(value = {"outbound:banQinWmSaleHeader:view", "outbound:banQinWmSaleHeader:add", "outbound:banQinWmSaleHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmSaleEntity banQinWmSaleEntity, Model model) {
        model.addAttribute("banQinWmSaleEntity", banQinWmSaleEntity);
        if (StringUtils.isBlank(banQinWmSaleEntity.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/wms/outbound/banQinWmSaleHeaderForm";
    }

    /**
     * 保存销售单
     */
    @RequiresPermissions(value = {"outbound:banQinWmSaleHeader:add", "outbound:banQinWmSaleHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinWmSaleEntity banQinWmSaleEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmSaleEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
            return j;
        }
        //新增或编辑表单保存
        ResultMessage msg = banQinWmSaleHeaderService.saveSaleHeader(banQinWmSaleEntity);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("entity", msg.getData());
        j.setBody(map);
        return j;
    }

    /**
     * 批量删除销售单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSaleHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
//        banQinWmSaleHeaderService.removeBySaleNo(banQinWmSaleHeaderService.get(id));
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSaleHeader:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSaleHeader banQinWmSaleHeader, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "销售单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmSaleHeader> page = banQinWmSaleHeaderService.findPage(new Page<BanQinWmSaleHeader>(request, response, -1), banQinWmSaleHeader);
            new ExportExcel("销售单", BanQinWmSaleHeader.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出销售单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("outbound:banQinWmSaleHeader:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmSaleHeader> list = ei.getDataList(BanQinWmSaleHeader.class);
            for (BanQinWmSaleHeader banQinWmSaleHeader : list) {
                try {
                    banQinWmSaleHeaderService.save(banQinWmSaleHeader);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条销售单记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条销售单记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入销售单失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSaleHeader/?repage";
    }

    /**
     * 下载导入销售单数据模板
     */
    @RequiresPermissions("outbound:banQinWmSaleHeader:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "销售单数据导入模板.xlsx";
            List<BanQinWmSaleHeader> list = Lists.newArrayList();
            new ExportExcel("销售单数据", BanQinWmSaleHeader.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSaleHeader/?repage";
    }

    /**
     * 审核
     */
    @ResponseBody
    @RequestMapping(value = "audit")
    public AjaxJson audit(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
//        ResultMessage msg = banQinWmSaleHeaderService.audit(ids.split(","));
//        j.setSuccess(msg.isSuccess());
//        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 取消审核
     */
    @ResponseBody
    @RequestMapping(value = "cancelAudit")
    public AjaxJson cancelAudit(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
//        ResultMessage msg = banQinWmSaleHeaderService.cancelAuditPo(ids.split(","));
//        j.setSuccess(msg.isSuccess());
//        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 销售单生成SO单页面
     */
    @RequestMapping(value = "createSoForm")
    public String createAsnForm(BanQinWmSaleEntity banQinWmSaleEntity, Model model) {
        model.addAttribute("banQinWmSaleEntity", banQinWmSaleEntity);
        return "modules/wms/outbound/banQinWmSaleCreateSoForm";
    }

    /**
     * 生成SO单页面数据
     */
    @RequestMapping(value = "createAsnData")
    @ResponseBody
    public Map<String, Object> createAsnData(BanQinWmSaleEntity banQinWmSaleEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page page = new Page<BanQinWmSaleEntity>(request, response);
//        page.setList(banQinWmPoDetailService.findEntityByPoId(banQinWmPoEntity.getId().split(","), banQinWmPoEntity.getOrgId()));
        return getBootstrapData(page);
    }

    /**
     * 生成So
     */
    @ResponseBody
    @RequestMapping(value = "createSo")
    public AjaxJson createAsn(@RequestBody BanQinWmSaleEntity entity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
//        ResultMessage msg = inboundPoService.createAsn(entity.getOwnerCode(), entity.getSupplierCode(), entity.getWmPoDetailEntitys(), entity.getOrgId());
//        j.setSuccess(msg.isSuccess());
//        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 关闭订单
     */
    @ResponseBody
    @RequestMapping(value = "closeOrder")
    public AjaxJson closeOrder(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
//        ResultMessage msg = inboundPoService.closePo(ids.split(","));
//        j.setSuccess(msg.isSuccess());
//        j.setMsg(msg.getMessage());
        return j;
    }

    /**
     * 取消订单
     */
    @ResponseBody
    @RequestMapping(value = "cancelOrder")
    public AjaxJson cancelOrder(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
//        ResultMessage msg = inboundPoService.cancelPo(ids.split(","));
//        j.setSuccess(msg.isSuccess());
//        j.setMsg(msg.getMessage());
        return j;
    }

}