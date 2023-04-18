package com.yunyou.modules.tms.common;

public class TmsConstants {

    public static final String YES = "Y";
    public static final String NO = "N";

    public static final String TRANSPORT_SCOPE_TYPE_1 = "1"; // 起始地
    public static final String TRANSPORT_SCOPE_TYPE_2 = "2"; // 目的地

    /*运输单状态*/
    public static final String TRANSPORT_ORDER_STATUS_00 = "00";// 新建
    public static final String TRANSPORT_ORDER_STATUS_10 = "10";// 已审核
    public static final String TRANSPORT_ORDER_STATUS_25 = "25";// 部分收货
    public static final String TRANSPORT_ORDER_STATUS_30 = "30";// 全部收货
    public static final String TRANSPORT_ORDER_STATUS_35 = "35";// 部分签收
    public static final String TRANSPORT_ORDER_STATUS_40 = "40";// 全部签收
    public static final String TRANSPORT_ORDER_STATUS_45 = "45";// 部分回单
    public static final String TRANSPORT_ORDER_STATUS_50 = "50";// 全部回单
    public static final String TRANSPORT_ORDER_STATUS_90 = "90";// 已取消
    public static final String TRANSPORT_ORDER_STATUS_99 = "99";// 关闭

    /*运输订单标签状态*/
    public static final String ORDER_LABEL_STATUS_00 = "00";// 新建
    public static final String ORDER_LABEL_STATUS_10 = "10";// 已收货
    public static final String ORDER_LABEL_STATUS_20 = "20";// 已签收
    public static final String ORDER_LABEL_STATUS_30 = "30";// 已回单

    /*派车单状态*/
    public static final String DISPATCH_ORDER_STATUS_00 = "00";// 新建
    public static final String DISPATCH_ORDER_STATUS_10 = "10";// 已审核
    public static final String DISPATCH_ORDER_STATUS_20 = "20";// 已派车
    public static final String DISPATCH_ORDER_STATUS_90 = "90";// 已取消
    public static final String DISPATCH_ORDER_STATUS_99 = "99";// 关闭

    /*派车单反馈状态*/
    public static final String DISPATCH_FEEDBACK_STATUS_00 = "00";  // 未反馈
    public static final String DISPATCH_FEEDBACK_STATUS_10 = "10";  //
    public static final String DISPATCH_FEEDBACK_STATUS_20 = "20";  //

    /*派车单标签状态*/
    public static final String DISPATCH_LABEL_STATUS_00 = "00";// 新建
    public static final String DISPATCH_LABEL_STATUS_05 = "05";// 已发货
    public static final String DISPATCH_LABEL_STATUS_10 = "10";// 已交接

    /*交接单状态*/
    public static final String HANDOVER_ORDER_STATUS_00 = "00"; // 新建
    public static final String HANDOVER_ORDER_STATUS_10 = "10"; // 部分交接
    public static final String HANDOVER_ORDER_STATUS_20 = "20"; // 已交接

    /*交接单标签状态*/
    public static final String HANDOVER_LABEL_STATUS_00 = "00"; // 新建
    public static final String HANDOVER_LABEL_STATUS_10 = "10"; // 已交接


    public static final String RECEIVE = "R";// 提货
    public static final String SHIP = "S";// 送货

    /*"*"代表空值*/
    public static final String NULL = "*";

    /*图片上传路径编码*/
    public static final String IMG_UPLOAD_PRE = "IMG_UPLOAD_PRE";

    /*业务对象类型*/
    public static final String TRANSPORT_OBJ_TYPE_1 = "CUSTOMER";// 委托方
    public static final String TRANSPORT_OBJ_TYPE_2 = "OWNER";// 客户
    public static final String TRANSPORT_OBJ_TYPE_3 = "CONSIGNEE";// 收货方
    public static final String TRANSPORT_OBJ_TYPE_4 = "SHIPPER";// 发货方
    public static final String TRANSPORT_OBJ_TYPE_5 = "SETTLEMENT";// 结算方
    public static final String TRANSPORT_OBJ_TYPE_6 = "CARRIER";// 承运商
    public static final String TRANSPORT_OBJ_TYPE_7 = "OUTLET";// 网点

    /*路由节点*/
    public static final String TRANSPORT_TRACK_NODE_RECEIVE = "揽收";
    public static final String TRANSPORT_TRACK_NODE_ARRIVE = "到件";
    public static final String TRANSPORT_TRACK_NODE_SHIP = "发件";
    public static final String TRANSPORT_TRACK_NODE_DELIVER = "配送";
    public static final String TRANSPORT_TRACK_NODE_SIGN = "签收";

    /*默认提货网点*/
    public static final String DEFAULT_DELIVERY_SITE = "DDS";
    /*默认送货网点*/
    public static final String DEFAULT_RECEIVE_SITE = "DRS";

    /*车辆状态*/
    public static final String VEHICLE_STATUS_00 = "00";// 可用
    public static final String VEHICLE_STATUS_01 = "01";// 不可用

    public static final String DATA_HAS_EXPIRED = "数据已过期";
    public static final String NULL_STRING = "";

    /*数据来源*/
    public static final String DS_00 = "00";// 手工单(默认)
    public static final String DS_01 = "01";// 调度计划

    /*维修工单状态*/
    public static final String REPAIR_STATUS_00 = "00";// 新建

    /*运输订单类型*/
    public static final String TRANSPORT_ORDER_TYPE_1 = "1";// 正常

    /*运输方式*/
    public static final String TRANSPORT_METHOD_1 = "1";// 铁路
    public static final String TRANSPORT_METHOD_2 = "2";// 航空
    public static final String TRANSPORT_METHOD_3 = "3";// 陆运
    public static final String TRANSPORT_METHOD_4 = "4";// 快递
    public static final String TRANSPORT_METHOD_5 = "5";// 船运

    /*图片附件上传类型*/
    public static final String IMP_UPLOAD_TYPE_HANDOVER = "HANDOVER";       // 交接图片
    public static final String IMP_UPLOAD_TYPE_EXCEPTION = "EXCEPTION";
    public static final String IMP_UPLOAD_TYPE_REPAIR = "REPAIR";

    /*异常单状态*/
    public static final String EXCEPTION_ORDER_STATUS_00 = "00";    //新建
    public static final String EXCEPTION_ORDER_STATUS_10 = "10";    //已处理

    /*备件入库单状态*/
    public static final String SPARE_ASN_STATUS_00 = "00";    // 新建
    public static final String SPARE_ASN_STATUS_10 = "10";    //

    /*备件入库单类型*/
    public static final String SPARE_ASN_TYPE_1 = "1";    // 正常入库
    public static final String SPARE_ASN_TYPE_2 = "2";    // 采购入库
    public static final String SPARE_ASN_TYPE_3 = "3";    // 退货入库

    /*备件出库单状态*/
    public static final String SPARE_SO_STATUS_00 = "00";    // 新建
    public static final String SPARE_SO_STATUS_10 = "10";    //

    /*备件出库单类型*/
    public static final String SPARE_SO_TYPE_1 = "1";    // 正常出库

    public static final String BUSINESS_AREA_CODE = "999999";// 区域管理-业务区域编码
}
