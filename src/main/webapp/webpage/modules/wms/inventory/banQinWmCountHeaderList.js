<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinWmCountHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmCountHeader/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.isSerial = "N";
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'countNo',
            title: '盘点单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_COUNT_STATUS'))}, value, "-");
            }
        }, {
            field: 'countType',
            title: '盘点单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_COUNT_TYPE'))}, value, "-");
            }
        }, {
            field: 'countRange',
            title: '盘点范围',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_COUNT_RANGE'))}, value, "-");
            }
        }, {
            field: 'countMode',
            title: '盘点方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_COUNT_MODE'))}, value, "-");
            }
        }, {
            field: 'countMethod',
            title: '盘点方法',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_COUNT_METHOD'))}, value, "-");
            }
        }, {
            field: 'monitorOp',
            title: '监盘人',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'areaName',
            title: '区域',
            sortable: true
        }, {
            field: 'zoneName',
            title: '库区',
            sortable: true
        }, {
            field: 'fmLoc',
            title: '库位从',
            sortable: true
        }, {
            field: 'toLoc',
            title: '库位到',
            sortable: true
        }, {
            field: 'takeStartTime',
            title: '动盘时间从',
            sortable: true
        }, {
            field: 'takeEndTime',
            title: '动盘时间到',
            sortable: true
        }, {
            field: 'randomNum',
            title: '抽盘数量',
            sortable: true
        }, {
            field: 'randomRate',
            title: '抽盘比例',
            sortable: true
        }, {
            field: 'closeTime',
            title: '盘点关闭时间',
            sortable: true
        }, {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $('#banQinWmCountHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmCountHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#createCount').prop('disabled', !length);
        $('#createRecount').prop('disabled', !length);
        $('#cancelCountTask').prop('disabled', !length);
        $('#closeCount').prop('disabled', !length);
        $('#cancelCount').prop('disabled', !length);
        $('#createAd').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmCountHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmCountHeaderTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinWmCountHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmCountHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function deleteAll() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.confirm('确认要删除该库存盘点记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/inventory/banQinWmCountHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmCountHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增库存盘点', "${ctx}/wms/inventory/banQinWmCountHeader/form", '90%', '90%', $('#banQinWmCountHeaderTable'));
}

function view(id) {
    jp.openBQDialog('查看库存盘点', "${ctx}/wms/inventory/banQinWmCountHeader/form?id=" + id, '90%', '90%', $('#banQinWmCountHeaderTable'));
}

function edit() {
    jp.openBQDialog('编辑库存盘点', "${ctx}/wms/inventory/banQinWmCountHeader/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmCountHeaderTable'));
}

function createCount() {
    commonMethod('createCount');
}

function createRecount() {
    commonMethod('createRecount');
}

function cancelCountTask() {
    commonMethod('cancelCountTask');
}

function closeCount() {
    commonMethod('closeCount');
}

function cancelCount() {
    commonMethod('cancelCount');
}

function createAd() {
    // 只支持单条记录生成
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmCountHeader/createAd?id=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmCountHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function commonMethod(method) {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmCountHeader/" + method + "?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmCountHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function printCountTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inventory/banQinWmCountHeader/printCountTask", 'ids', rowIds.join(','), '打印盘点任务');
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inventory/banQinWmCountHeader/export", "库存盘点单记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

</script>