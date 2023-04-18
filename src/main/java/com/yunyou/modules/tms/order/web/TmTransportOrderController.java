package com.yunyou.modules.tms.order.web;

import com.alibaba.fastjson.JSON;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.common.map.geo.Point;
import com.yunyou.modules.tms.order.action.TmDispatchOrderAction;
import com.yunyou.modules.tms.order.action.TmTransportOrderAction;
import com.yunyou.modules.tms.order.entity.TmDirectDispatch;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportReceiptInfo;
import com.yunyou.modules.tms.order.entity.extend.TmTransportSignInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运输订单信息Controller
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmTransportOrder")
public class TmTransportOrderController extends BaseController {
    @Autowired
    private TmTransportOrderAction tmTransportOrderAction;
    @Autowired
    private TmDispatchOrderAction tmDispatchOrderAction;

    @ModelAttribute
    public TmTransportOrderEntity get(@RequestParam(required = false) String id) {
        TmTransportOrderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportOrderAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmTransportOrderEntity();
        }
        return entity;
    }

    @RequiresPermissions("order:tmTransportOrder:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmTransportOrderList";
    }

    @RequiresPermissions(value = {"order:tmTransportOrder:view", "order:tmTransportOrder:add", "order:tmTransportOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmTransportOrderEntity entity, Model model) {
        model.addAttribute("tmTransportOrderEntity", entity);
        return "modules/tms/order/tmTransportOrderForm";
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmTransportOrderEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmTransportOrderAction.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:list")
    @RequestMapping(value = "getEachStatusOrderQty")
    public TmTransportOrderEntity getEachStatusOrderQty(TmTransportOrderEntity entity) {
        return tmTransportOrderAction.getEachStatusOrderQty(entity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"order:tmTransportOrder:add", "order:tmTransportOrder:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmTransportOrderEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.saveEntity(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:del")
    @RequestMapping(value = "deleteAll", method = RequestMethod.POST)
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.batchRemove(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:audit")
    @RequestMapping(value = "audit", method = RequestMethod.POST)
    public AjaxJson audit(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.batchAudit(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:cancelAudit")
    @RequestMapping(value = "cancelAudit", method = RequestMethod.POST)
    public AjaxJson cancelAudit(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.batchCancelAudit(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:receive")
    @RequestMapping(value = "receive", method = RequestMethod.POST)
    public AjaxJson receive(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmTransportOrderAction.batchReceive(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:sign")
    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public AjaxJson sign(String ids, TmTransportSignInfo tmTransportSignInfo) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        ResultMessage message = tmTransportOrderAction.sign(ids.split(","), tmTransportSignInfo);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:receipt")
    @RequestMapping(value = "receipt", method = RequestMethod.POST)
    public AjaxJson receipt(String ids, TmTransportReceiptInfo tmTransportReceiptInfo) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        ResultMessage message = tmTransportOrderAction.receipt(ids.split(","), tmTransportReceiptInfo);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:runTrack")
    @RequestMapping(value = "runTrack")
    public AjaxJson runTrackList(TmTransportOrderEntity entity) {
        AjaxJson j = new AjaxJson();
        // 获取订单派车车辆的行驶轨迹
        Map<String, List<Point>> tracks = tmDispatchOrderAction.findVehicleTracks(entity.getTransportNo(), entity.getBaseOrgId(), entity.getOrgId());
        if (MapUtil.isNotEmpty(tracks)) {
            j.setSuccess(true);
            j.put("tracks", tracks);
        } else {
            j.setSuccess(false);
            j.setMsg("未发现派车车辆行驶轨迹");
        }
        return j;
    }

    @RequiresPermissions("order:tmTransportOrder:vehicleLocation")
    @RequestMapping(value = "vehicleLocation")
    public String vehicleLocationList(TmTransportOrderEntity entity, Model model) {
        List<Map<String, String>> list = Lists.newArrayList();
        list.add(ImmutableMap.of("key", "116.403613,39.912916"));
        list.add(ImmutableMap.of("key", "114.403613,39.912916"));
        list.add(ImmutableMap.of("key", "113.403613,35.912916"));
        list.add(ImmutableMap.of("key", "115.403613,34.912916"));
        list.add(ImmutableMap.of("key", "116.413613,34.912916"));
        list.add(ImmutableMap.of("key", "115.423613,35.922916"));
        list.add(ImmutableMap.of("key", "115.433613,36.932916"));
        list.add(ImmutableMap.of("key", "117.443613,31.942916"));

        model.addAttribute("points", JSON.toJSONString(list));
        return "modules/tms/order/tmVehicleLocationList";
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:copy")
    @RequestMapping(value = "copy")
    public AjaxJson copy(String id) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmTransportOrderAction.copy(id);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:cancelReceive")
    @RequestMapping(value = "cancelReceive")
    public AjaxJson cancelReceive(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        ResultMessage msg = tmTransportOrderAction.cancelReceive(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("order:tmTransportOrder:directDispatch")
    @RequestMapping(value = "directDispatch")
    public AjaxJson directDispatch(TmDirectDispatch tmDirectDispatch) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmTransportOrderAction.directDispatch(tmDirectDispatch);
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @RequestMapping(value = "trackingForm")
    public String trackingForm(String id, Model model) {
        Map<String, Object> map = Maps.newHashMap();
        TmTransportOrderEntity entity = tmTransportOrderAction.getEntity(id);
        if (entity != null) {
            TmTransportOrderTrack qEntity = new TmTransportOrderTrack();
            qEntity.setTransportNo(entity.getTransportNo());
            qEntity.setOrgId(entity.getOrgId());
            List<TmTransportOrderTrack> trackList = tmTransportOrderAction.findTrackList(qEntity);
            if (CollectionUtil.isNotEmpty(trackList)) {
                trackList = trackList.stream().sorted(Comparator.comparing(TmTransportOrderTrack::getOpTime).reversed()).collect(Collectors.toList());
                String wayBillNo = trackList.stream().map(TmTransportOrderTrack::getLabelNo).distinct().collect(Collectors.joining(","));
                map.put("logisticsData", trackList);
                map.put("customerNo", entity.getCustomerNo());
                map.put("orderNo", entity.getTransportNo());
                map.put("wayBillNo", wayBillNo);
                map.put("customerName", entity.getCustomerName());
                map.put("orgId", entity.getOrgId());
            }
        }
        model.addAttribute("dataInfo", map);
        return "modules/tms/order/tmNodeTrackForm";
    }

    @ResponseBody
    @RequestMapping(value = "nodeTrackList")
    public AjaxJson nodeTrackList(String orderNo, String wayBillNo) {
        AjaxJson j = new AjaxJson();
        TmTransportOrderTrack qEntity = new TmTransportOrderTrack();
        qEntity.setTransportNo(orderNo);
        qEntity.setLabelNo(wayBillNo);
        List<TmTransportOrderTrack> trackList = tmTransportOrderAction.findTrackList(qEntity)
                .stream().sorted(Comparator.comparing(TmTransportOrderTrack::getOpTime).reversed())
                .collect(Collectors.toList());
        j.put("logisticsData", trackList);
        return j;
    }

    @RequiresPermissions("order:tmTransportOrder:checkDispatch")
    @RequestMapping(value = "checkDispatch")
    public String checkDispatch(TmTransportOrderEntity entity, Model model) {
        TmDispatchOrderEntity viewEntity = new TmDispatchOrderEntity();
        viewEntity.setTransportNo(entity.getTransportNo());
        viewEntity.setBaseOrgId(entity.getBaseOrgId());
        model.addAttribute("tmDispatchOrderEntity", viewEntity);
        return "modules/tms/order/tmTransportCheckDispatch";
    }

}