package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.WmRepCombineEntity;
import com.yunyou.modules.wms.report.entity.extend.WmRepCombineExportEntity;
import com.yunyou.modules.wms.report.service.BanQinWmRepCombineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 进出存合并报表Controller
 *
 * @author WMJ
 * @version 2020-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmRepCombine")
public class BanQinWmRepCombineController extends BaseController {
    @Autowired
    private BanQinWmRepCombineService wmRepCombineService;

    @ModelAttribute
    public WmRepCombineEntity get(@RequestParam(required = false) String id) {
        return new WmRepCombineEntity();
    }

    @RequiresPermissions("report:banQinWmRepCombine:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmRepCombineList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmRepCombine:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WmRepCombineEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WmRepCombineEntity> page = wmRepCombineService.findPage(new Page<WmRepCombineEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepCombine:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WmRepCombineEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "进出存合并数据记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<WmRepCombineEntity> page = wmRepCombineService.findPage(new Page<WmRepCombineEntity>(request, response, -1), entity);
            new ExportExcel("", WmRepCombineExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出进出存合并数据记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}