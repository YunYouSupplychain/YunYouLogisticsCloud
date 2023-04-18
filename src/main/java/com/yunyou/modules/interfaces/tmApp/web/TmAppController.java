package com.yunyou.modules.interfaces.tmApp.web;

import com.google.common.collect.Maps;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.tmApp.entity.*;
import com.yunyou.modules.interfaces.tmApp.service.TmAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;

/**
 * app后台Controller
 *  注：app端涉及到数据修改的操作
 *
 * @author zyf
 * @version 2020-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/interface/tmApp")
public class TmAppController extends BaseController {

    @Autowired
    private TmAppService tmAppService;

    /**
     * 需求下单-生成需求订单
     */
    @ResponseBody
    @RequestMapping(value = "/appCustomerOrder/createOrder", method = RequestMethod.POST)
    public AjaxJson createCustomerOrder(@RequestBody TmAppCreateCustomerOrderRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.createCustomerOrder(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除需求订单
     */
    @ResponseBody
    @RequestMapping(value = "/appCustomerOrder/delete", method = RequestMethod.POST)
    public AjaxJson deleteCustomerOrder(@RequestBody TmAppDeleteCustomerOrderRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = tmAppService.deleteCustomerOrder(request);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 保存出车安全检查表
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/saveVehicleSafetyCheck", method = RequestMethod.POST)
    public AjaxJson saveVehicleSafetyCheck(@RequestBody TmAppCreateVehicleSafetyCheckRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.saveVehicleSafetyCheck(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 发车
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/depart", method = RequestMethod.POST)
    public AjaxJson depart(@RequestBody TmAppDepartRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.depart(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 派车单关闭
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/finish", method = RequestMethod.POST)
    public AjaxJson finish(@RequestBody TmAppDispatchOrderFinishRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.finish(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 网点交接 - 运输订单
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/handoverConfirm", method = RequestMethod.POST)
    public AjaxJson handoverConfirm(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.handoverConfirm(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 揽收 - 运输订单
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/receiveConfirm", method = RequestMethod.POST)
    public AjaxJson receiveConfirm(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.receiveConfirm(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 签收 - 运输订单
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/signConfirm", method = RequestMethod.POST)
    public AjaxJson signConfirm(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.signConfirm(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 网点交接 - 标签
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/handoverConfirmLabel", method = RequestMethod.POST)
    public AjaxJson handoverConfirmLabel(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.handoverConfirmLabel(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 揽收 - 标签
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/receiveConfirmLabel", method = RequestMethod.POST)
    public AjaxJson receiveConfirmLabel(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.receiveConfirmLabel(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 签收 - 标签
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/signConfirmLabel", method = RequestMethod.POST)
    public AjaxJson signConfirmLabel(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.signConfirmLabel(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 附件保存
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/saveTmAttachement", method = RequestMethod.POST)
    public AjaxJson saveTmAttachement(@RequestBody TmAppAttachementRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.saveTmAttachement(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 保存派车单现金费用信息
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/saveDispatchOrderCashAmount", method = RequestMethod.POST)
    public AjaxJson saveDispatchOrderCashAmount(@RequestBody TmAppDispatchOrderSaveRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.saveDispatchOrderCashAmount(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 保存维修工单信息
     */
    @ResponseBody
    @RequestMapping(value = "/appRepairOrder/saveRepairOrder", method = RequestMethod.POST)
    public AjaxJson saveRepairOrder(@RequestBody TmAppSaveRepairOrderRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.saveRepairOrder(request);
            LinkedHashMap<String, Object> returnMap = Maps.newLinkedHashMap();
            returnMap.put("order", message.getData());
            j.setBody(returnMap);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 维修完成确认
     */
    @ResponseBody
    @RequestMapping(value = "/appRepairOrder/finishRepairOrder", method = RequestMethod.POST)
    public AjaxJson finishRepairOrder(@RequestBody TmAppSaveRepairOrderRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.finishRepairOrder(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 保存派车装罐信息
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/saveDispatchTankInfo", method = RequestMethod.POST)
    public AjaxJson saveDispatchTankInfo(@RequestBody TmAppCreateDispatchTankInfoRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.saveDispatchTankInfo(request);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 异常上报
     */
    @ResponseBody
    @RequestMapping(value = "/appDispatchOrder/exceptionConfirm", method = RequestMethod.POST)
    public AjaxJson exceptionConfirm(@RequestBody TmAppExceptionConfirmRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = tmAppService.exceptionConfirm(request);
            LinkedHashMap<String, Object> returnMap = Maps.newLinkedHashMap();
            returnMap.put("order", message.getData());
            j.setBody(returnMap);
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}