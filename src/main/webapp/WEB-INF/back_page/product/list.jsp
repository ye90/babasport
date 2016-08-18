<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/back_page/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>babasport-list</title>
<script type="text/javascript">
	//上架
	function isShow(pageNo, name, brandId, isShow) {
		if (Pn.checkedCount('ids') <= 0) {
			alert("请至少选择一个!");
			return;
		}
		if (!confirm("确定上架吗?")) {
			return;
		}

		$("#jvForm").attr(
				"action",
				"isShow.do?pageNo=" + pageNo + "&name=" + name + "&brandId="
						+ brandId + "&isShow=" + isShow);
		$("#jvForm").submit();
	}
</script>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 商品管理 - 列表</div>
		<form class="ropt">
			<input class="add" type="button" value="添加"
				onclick="javascript:window.location.href='toAdd.do'" />
		</form>
		<div class="clear"></div>
	</div>
	<div class="body-box">

		<!-- search -->
		<form action="/product/list.do" method="post"
			style="padding-top: 5px;">
			<input type="hidden" value="1" name="pageNo" /> 名称: <input
				type="text" value="${name }" name="name" /> <select name="brandId">
				<option value="">请选择品牌</option>
				<c:forEach items="${brands }" var="brand">
					<option value="${brand.id }"
						<c:if test="${brand.id==brandId }">selected="true"</c:if>>
						${brand.name }</option>
				</c:forEach>
			</select> <select name="isShow">
				<option value="1" <c:if test="${isShow ==1 }">selected="true"</c:if>>上架</option>
				<option value="0" <c:if test="${isShow ==0 }">selected="true"</c:if>>下架</option>
			</select> <input type="submit" class="query" value="查询" />
		</form>


		<form method="get" id="jvForm">
			<input type="hidden" value="" name="pageNo" /> <input type="hidden"
				value="" name="queryName" />
			<table cellspacing="1" cellpadding="0" width="100%" border="0"
				class="pn-ltable">
				<thead class="pn-lthead">
					<tr>
						<th width="20"><input type="checkbox"
							onclick="Pn.checkbox('ids',this.checked)" /></th>
						<th>商品编号</th>
						<th>商品名称</th>
						<th>图片</th>
						<th width="4%">新品</th>
						<th width="4%">热卖</th>
						<th width="4%">推荐</th>
						<th width="4%">上下架</th>
						<th width="12%">操作选项</th>
					</tr>
				</thead>
				<tbody class="pn-ltbody">
					<c:forEach items="${pagination.list }" var="entry">
						<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'"
							onmouseout="this.bgColor='#ffffff'">
							<td><input type="checkbox" name="ids" value="${entry.id }" /></td>
							<td align="center">${entry.id }--${entry.no }</td>
							<td align="center">${entry.name }</td>
							<td align="center"><img src="${entry.img.allUrl }"
								width="40" height="40" /></td>
							<td align="center">是</td>
							<td align="center">是</td>
							<td align="center">是</td>
							<td align="center"><c:choose>
									<c:when test="${entry.isShow == 1 }">上架</c:when>
									<c:otherwise>下架</c:otherwise>
								</c:choose></td>
							<td align="center">
							<a href="/product/detail.shtml?id=${entry.id }" target="_black" class="pn-opt">查看</a> |<a
								href="javascript:void(0);" class="pn-opt">修改</a> | <a
								href=" javascript:void(0);"
								onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>|
								<a href="/sku/list.do?productId=${entry.id }&pno=${entry.no}" class="pn-opt">库存</a>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div style="margin-top: 15px;">
				<input class="del-button" type="button" value="删除"
					onclick="optDelete();" /><input class="add" type="button"
					value="上架"
					onclick="isShow('${pagination.pageNo}','${name }','${brandId }','${isShow }');" /><input
					class="del-button" type="button" value="下架" onclick="optDelete();" />
			</div>

		</form>
	</div>
	<div class="page pb15">
		<span class="r inb_a page_b"> <c:forEach
				items="${pagination.pageView }" var="page">
			${page }
		</c:forEach>
		</span>
	</div>

</body>

</html>