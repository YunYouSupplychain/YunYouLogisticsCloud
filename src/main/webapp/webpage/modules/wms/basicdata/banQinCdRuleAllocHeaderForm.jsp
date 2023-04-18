<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配规则管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab = false; // 是否显示右边tab页

        $(document).ready(function () {
            // 初始化
            init();
            // 初始化明细table
            initCdRuleAllocDetailTable();
            // 监听事件
            listener();
        });

        /**
         * 监听失去焦点事件
         */
        function listener() {
            $('.required').on('blur', function () {
                if ($(this).hasClass('form-error') && $(this).val()) {
                    $(this).removeClass('form-error');
                }
            });
        }

        /**
         * 单头保存
         */
        function save() {
            var isValidate = jp.validateForm('#inputForm');
            if (!isValidate) {
                return false;
            } else {
                jp.loading();
                jp.post("${ctx}/wms/basicdata/banQinCdRuleAllocHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/wms/basicdata/banQinCdRuleAllocHeader/form?id=" + data.body.entity.id;
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            }
        }
        
        /**
         * 初始化
         */
        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#addDetail').attr('disabled', true);
            } else {
                $('#ruleCode').prop('readonly', true);
            }
        }

        /**
         * 显示右边tab
         */
        function showTabRight() {
            $('#tab-right').show();
            $('#tab-left').addClass("div-left");
            isShowTab = true;
        }

        /**
         * 隐藏右边tab
         */
        function hideTabRight() {
            $('#tab-right').hide();
            $('#tab-left').removeClass("div-left");
            isShowTab = false;
        }

        /**
         * 初始化明细table
         */
        function initCdRuleAllocDetailTable() {
            $('#cdRuleAllocDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/basicdata/banQinCdRuleAllocDetail/data",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.headerId = !$('#id').val() == true ? "#" : $('#id').val();
                    searchParam.orgId = $('#orgId').val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function(row, $el) {
                    clickDetail(row);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row);
                },
                columns: [{
                    checkbox: true
                },{
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                },{
                    field: 'uom',
                    title: '包装单位',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_WARE_UNIT'))}, value, "-");
                    }
                },{
                    field: 'locUseType',
                    title: '库位使用类型',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LOC_USE_TYPE'))}, value, "-");
                    }
                },{
                    field: 'skuLocType',
                    title: '商品拣货位类型',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SKU_LOC_TYPE'))}, value, "-");
                    }
                },{
                    field: 'isClearFirst',
                    title: '是否清仓优先',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                }]
            });
        }

        /**
         * 单击表格事件
         * @param row 当前行
         */
        function clickDetail(row) {
            currentRow = row;
            if (isShowTab) {
                // 表单赋值
                evaluate();
            }
        }

        /**
         * 双击表格事件
         * @param row 当前行
         */
        function dbClickDetail(row) {
            currentRow = row;
            if (!isShowTab) {
                // 显示右边tab
                showTabRight();
                // 表单赋值
                evaluate();
            } else {
                // 隐藏右边tab
                hideTabRight();
            }
            $("#cdRuleAllocDetailTable").bootstrapTable('resetView');
        }

        /**
         * 赋值
         */
        function evaluate() {
            $("input[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
                if ($Id === 'detail_isClearFirst') {
                    $('#' + $Id).prop('checked', $('#' + $Id).val() === 'Y');
                }
            });

            $("select[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
            });
        }

        /**
         * 明细表单验证
         * @returns {boolean}
         */
        function detailSubmitCheck() {
            var flag = true;
            $('#tab-right').find(".required").each(function() {
                if (!$(this).val()) {
                    $(this).addClass('form-error');
                    $(this).attr("placeholder", "不能为空");
                    flag = false;
                }
            });
            return flag;
        }

        /**
         * 新增明细
         */
        function addDetail() {
            // 显示右边Tab
            showTabRight();
            // 清空表单
            $(':input', '#inputForm1').val('');
            // 清仓优先默认值
            $('#detail_isClearFirst').prop('checked', false).val('N');
            $('#detail_recVer').val('0');
        }

        /**
         * 保存明细
         */
        function saveDetail() {
            if (!isShowTab) {
                jp.warning("当前无保存记录!");
                return;
            }
            if (!$('#id').val()) {
                jp.warning("请先保存订单头!");
                return;
            }
            // 表单验证
            if (detailSubmitCheck()) {
                // 保存前赋值
                beforeSave();
                jp.post("${ctx}/wms/basicdata/banQinCdRuleAllocDetail/save", $('#inputForm1').bq_serialize(), function (data) {
                    if (data.success) {
                        $('#cdRuleAllocDetailTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            }
        }

        /**
         * 保存前赋值
         */
        function beforeSave() {
            if (!$('#detail_id').val()) {
                $('#detail_orgId').val($('#orgId').val());
                $('#detail_ruleCode').val($('#ruleCode').val());
                $('#detail_headerId').val($('#id').val());
            }
        }

        /**
         * 获取表格勾选行Ids
         */
        function getIdSelections() {
            return $.map($('#cdRuleAllocDetailTable').bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        /**
         * 删除明细
         */
        function removeDetail() {
            var rowIds = getIdSelections();
            if (rowIds.length === 0) {
                jp.bqError("请选择一条记录!");
                return;
            }
            jp.confirm('确认要删除吗？删除将不能恢复？', function () {
                jp.get("${ctx}/wms/basicdata/banQinCdRuleAllocDetail/deleteAll?ids=" + rowIds, function (data) {
                    if (data.success) {
                        $('#cdRuleAllocDetailTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

        /**
         * 清仓优先选择事件
         */
        function clearChange(flag) {
            $('#detail_isClearFirst').val(flag ? 'Y' : 'N');
        }

    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 20px;">
        <shiro:hasPermission name="basicdata:banQinCdRuleAllocHeader:edit">
            <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
        </shiro:hasPermission>
    </div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">基础信息</h3>
        </div>
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="banQinCdRuleAllocHeader" method="post" class="form-horizontal">
                <form:hidden path="id"/>
                <form:hidden path="orgId"/>
                <form:hidden path="recVer"/>
                <table class="table">
                    <tbody>
                    <tr>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>规则编码</label></td>
                        <td width="12%">
                            <form:input path="ruleCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>规则名称</label></td>
                        <td width="12%">
                            <form:input path="ruleName" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form:form>
        </div>
    </div>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">规则信息</h3>
        </div>
        <div class="panel-body">
            <div style="width: 100%; padding-left: 10px;">
                <div id="detailToolbar" style="width: 100%; padding: 10px 0px;">
                    <shiro:hasPermission name="basicdata:banQinCdRuleAllocDetail:addDetail">
                        <a class="btn btn-primary btn-sm" id="addDetail" onclick="addDetail()">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdRuleAllocDetail:saveDetail">
                        <a class="btn btn-primary btn-sm" id="saveDetail" onclick="saveDetail()">保存</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdRuleAllocDetail:removeDetail">
                        <a class="btn btn-danger btn-sm" id="removeDetail" onclick="removeDetail()">删除</a>
                    </shiro:hasPermission>
                </div>
                <div id="tab-left">
                    <table id="cdRuleAllocDetailTable" class="table text-nowrap"></table>
                </div>
                <div id="tab-right" style="overflow: scroll; height: 150px; border: 1px solid #dddddd; display: none;">
                    <form:form id="inputForm1" method="post" class="form-horizontal">
                    <input type="hidden" id="detail_id" name="id"/>
                    <input type="hidden" id="detail_ruleCode" name="ruleCode"/>
                    <input type="hidden" id="detail_orgId" name="orgId"/>
                    <input type="hidden" id="detail_headerId" name="headerId"/>
                    <input type="hidden" id="detail_recVer" name="recVer"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>行号</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>包装单位</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">库位使用类型</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">商品拣货位类型</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="detail_lineNo" name="lineNo" htmlEscape="false" class="form-control required" maxlength="4"/>
                            </td>
                            <td width="20%">
                                <select id="detail_uom" name="uom" class="form-control m-b required">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_WARE_UNIT')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="20%">
                                <select id="detail_locUseType" name="locUseType" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_LOC_USE_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="20%">
                                <select id="detail_skuLocType" name="skuLocType" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SKU_LOC_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <label class="pull-left">清仓优先</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="detail_isClearFirst" name="isClearFirst" type="checkbox" class="myCheckbox" onclick="clearChange(this.checked)"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>