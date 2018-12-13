<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ taglib prefix="htmlformentryTag" tagdir="/WEB-INF/tags/module/htmlformentry" %>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page import="org.openmrs.web.WebConstants" %>
<%
	pageContext.setAttribute("msg", session.getAttribute(WebConstants.OPENMRS_MSG_ATTR));
	pageContext.setAttribute("msgArgs", session.getAttribute(WebConstants.OPENMRS_MSG_ARGS));
	pageContext.setAttribute("err", session.getAttribute(WebConstants.OPENMRS_ERROR_ATTR));
	pageContext.setAttribute("errArgs", session.getAttribute(WebConstants.OPENMRS_ERROR_ARGS));
	session.removeAttribute(WebConstants.OPENMRS_MSG_ATTR);
	session.removeAttribute(WebConstants.OPENMRS_MSG_ARGS);
	session.removeAttribute(WebConstants.OPENMRS_ERROR_ATTR);
	session.removeAttribute(WebConstants.OPENMRS_ERROR_ARGS);
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<openmrs:htmlInclude file="/openmrs.js" />
		<openmrs:htmlInclude file="/openmrs.css" />
		<openmrs:htmlInclude file="/style.css" />
		<openmrs:htmlInclude file="/dwr/engine.js" />
		<openmrs:htmlInclude file="/dwr/interface/DWRAlertService.js" />

		<c:choose>
			<c:when test="${!empty pageTitle}">
				<title>${pageTitle}</title>
			</c:when>
			<c:otherwise>
				<title><spring:message code="openmrs.title"/></title>
			</c:otherwise>
		</c:choose>

		<style>
			.box {width:auto;}
			.boxHeader {width:auto;}
		</style>

		<script type="text/javascript">
			/* variable used in js to know the context path */
			var openmrsContextPath = '${pageContext.request.contextPath}';
		</script>

		<openmrs:extensionPoint pointId="org.openmrs.headerFullIncludeExt" type="html" requiredClass="org.openmrs.module.web.extension.HeaderIncludeExt">
			<c:forEach var="file" items="${extension.headerFiles}">
				<openmrs:htmlInclude file="${file}" />
			</c:forEach>
		</openmrs:extensionPoint>

	</head>
	<body>
		<script type="text/javascript">
			// prevents users getting popup alerts when viewing pages
			var handler = function(msg, ex) {
				var div = document.getElementById("openmrs_dwr_error");
				div.style.display = ""; // show the error div
				var msgDiv = document.getElementById("openmrs_dwr_error_msg");
				msgDiv.innerHTML = '<spring:message code="error.dwr"/>' + " <b>" + msg + "</b>";

			};
			dwr.engine.setErrorHandler(handler);
			dwr.engine.setWarningHandler(handler);
		</script>

		<openmrs:forEachAlert>
			<c:if test="${varStatus.first}"><div id="alertOuterBox"><div id="alertInnerBox"></c:if>
				<div class="alert">
					<a href="#markRead" onClick="return markAlertRead(this, '${alert.alertId}')" HIDEFOCUS class="markAlertRead">
						<img src="${pageContext.request.contextPath}/images/markRead.gif" alt='<spring:message code="Alert.mark"/>' title='<spring:message code="Alert.mark"/>'/> <span class="markAlertText"><spring:message code="Alert.markAsRead"/></span>
					</a>
					${alert.text} ${alert.dateToExpire} <c:if test="${alert.satisfiedByAny}"><i class="smallMessage">(<spring:message code="Alert.mark.satisfiedByAny"/>)</i></c:if>
				</div>
			<c:if test="${varStatus.last}">
				</div>
				<div id="alertBar">
					<img src="${pageContext.request.contextPath}/images/alert.gif" align="center" alt='<spring:message code="Alert.unreadAlert"/>' title='<spring:message code="Alert.unreadAlert"/>'/>
					<c:if test="${varStatus.count == 1}"><spring:message code="Alert.unreadAlert"/></c:if>
					<c:if test="${varStatus.count != 1}"><spring:message code="Alert.unreadAlerts" arguments="${varStatus.count}" /></c:if>
				</div>
				</div>
			</c:if>
		</openmrs:forEachAlert>

		<c:if test="${msg != null}">
			<div id="openmrs_msg"><spring:message code="${msg}" text="${msg}" arguments="${msgArgs}" /></div>
		</c:if>
		<c:if test="${err != null}">
			<div id="openmrs_error"><spring:message code="${err}" text="${err}" arguments="${errArgs}"/></div>
		</c:if>
		<div id="openmrs_dwr_error" style="display:none" class="error">
			<div id="openmrs_dwr_error_msg"></div>
			<div id="openmrs_dwr_error_close" class="smallMessage">
				<i><spring:message code="error.dwr.stacktrace"/></i> &nbsp;
				<a href="#" onclick="this.parentNode.parentNode.style.display='none'"><spring:message code="error.dwr.hide"/></a>
			</div>
		</div>

		<style>
			tr,th {text-align:left; vertical-align:top;}
		</style>

		<openmrs:require privilege="Manage Forms" otherwise="/login.htm" redirect="/module/htmlformentry/htmlForm.list" />

		<c:if test="${ not empty message }">
			<span style="color:red;">${message}</span>
		</c:if>

		<h3>Form Schema Preview</h3>
		<b>${schema.name}</b>
		<br>

        <!-- todo: handle nested sections -->
		<c:forEach items="${schema.sections}" var="section">
			<div class="box">
				<div class="boxHeader" style="background-color:yellow; color:black; font-weight:bold;">${section.name}</div>
				<c:forEach items="${section.fields}" var="field">
					<div>
						<htmlformentryTag:viewSchemaField field="${field}"/>
					</div>
					<br/>
				</c:forEach>
			</div>
		</c:forEach>

        <div class="box">
            <div class="boxHeader" style="background-color:yellow; color:black; font-weight:bold;">${section.name}</div>
            <c:forEach items="${schema.fields}" var="field">
                <div>
                    <htmlformentryTag:viewSchemaField field="${field}"/>
                </div>
                <br/>
            </c:forEach>
        </div>

		<br>
		<br>
		<INPUT type="button" value="Close Schema" onClick="window.close()">
	</body>
</html>