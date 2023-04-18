<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存周转规则管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinCdRuleRotationHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库存周转规则列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinCdRuleRotationHeaderEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="规则编码：">规则编码：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="ruleCode" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="规则名称：">规则名称：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="ruleName" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="周转类型：">周转类型：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="rotationType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ROTATION_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="批次属性：">批次属性：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhLotHeader/grid" title="选择批次属性" cssClass="form-control"
                                                       fieldId="lotCode" fieldName="lotCode" fieldKeyName="lotCode" fieldValue=""
                                                       displayFieldId="lotName" displayFieldName="lotName" displayFieldKeyName="lotName" displayFieldValue=""
                                                       selectButtonId="lotSelectId" deleteButtonId="lotDeleteId"
                                                       fieldLabels="批次属性编码|批次属性名称" fieldKeys="lotCode|lotName" inputSearchKey="lotCodeAndName"
                                                       searchLabels="批次属性编码|批次属性名称" searchKeys="lotCode|lotName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basicdata:banQinCdRuleRotationHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdRuleRotationHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdRuleRotationHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinCdRuleRotationHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>