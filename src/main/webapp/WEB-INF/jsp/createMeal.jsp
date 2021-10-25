<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <hr>
    <h2><spring:message code="meal.create"/></h2>
    <jsp:include page="fragments/mealForm.jsp"/>
</section>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
