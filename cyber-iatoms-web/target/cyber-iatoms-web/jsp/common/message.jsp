<div id="op_title_name">
	<c:if test="${message!=null && message.code!=null}">
		<strong>
			<font color="red" size="3">
				<spring:message code="${message.code}"
				arguments="${message.argumentList}" argumentSeparator="," />
			</font>
		</strong>
	</c:if>
</div>