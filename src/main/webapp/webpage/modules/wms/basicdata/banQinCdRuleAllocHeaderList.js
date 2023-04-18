<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdRuleAllocHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdRuleAllocHeader/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'ruleCode',
            title: '规则编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'ruleName',
            title: '规则名称',
            sortable: true
        }]
    });
    $('#banQinCdRuleAllocHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdRuleAllocHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdRuleAllocHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdRuleAllocHeaderTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdRuleAllocHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该分配规则记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdRuleAllocHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdRuleAllocHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增分配规则', "${ctx}/wms/basicdata/banQinCdRuleAllocHeader/form", '80%', '80%', $('#banQinCdRuleAllocHeaderTable'));
}

function edit(id) {
    if(id == undefined){
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdRuleAllocHeader:edit">
        jp.openBQDialog('编辑分配规则', "${ctx}/wms/basicdata/banQinCdRuleAllocHeader/form?id=" + id,'80%', '80%', $('#banQinCdRuleAllocHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdRuleAllocHeader:edit">
        jp.openBQDialog('查看分配规则', "${ctx}/wms/basicdata/banQinCdRuleAllocHeader/form?id=" + id,'80%', '80%', $('#banQinCdRuleAllocHeaderTable'));
    </shiro:lacksPermission>
}

</script>