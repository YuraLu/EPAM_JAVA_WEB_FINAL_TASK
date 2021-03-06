<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="locale"
       value="${not empty param.locale ? param.locale : not empty locale ? locale : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${locale}"/>

<fmt:setBundle basename="text"/>

<c:import url="/WEB-INF/form/updatePassword.jsp"/>

<c:import url="WEB-INF/jsp/common/header.jsp">
    <c:param name="page_title" value="title.personal_cabinet"/>
</c:import>
<body>
<c:import url="/WEB-INF/jsp/common/headerNav.jsp">
    <c:param name="paramRedirect" value="viewUserCabinet"/>
</c:import>

<!-- Begin page content -->
<div class="container-fluid">
    <div class="row no-gutter">
        <div class="col-md-8 col-lg-6">
            <div class="login d-flex align-items-center py-5">
                <div class="container">
                    <div class="row">
                        <div class="col-md-9 col-lg-8 mx-auto">
                            <h2><fmt:message key="title.personal_cabinet"/></h2>
                            <form action="controller" method="post">
                                <input type="hidden" name="userId" value="${user.id}">
                                <input type="hidden" name="roleId" value="${user.role.id}">
                                <div class="form-label-group mt-3">
                                    <label for="login">
                                        <fmt:message key="registration.enter_login_message"/>
                                    </label>
                                    <input type="text" id="login" name="login" class="form-control"
                                           placeholder="Login" value="${user.login}" readonly>
                                </div>
                                <div class="form-label-group mt-3">
                                    <label for="name">
                                        <fmt:message key="registration.enter_name_message"/>
                                    </label>
                                    <input type="text" id="name" name="name" class="form-control"
                                           value="${user.name}"/>
                                </div>
                                <div class="form-label-group mt-3">
                                    <label for="email">
                                        <fmt:message key="registration.enter_email_message"/>
                                    </label>
                                    <input type="text" id="email" name="email" class="form-control"
                                           value="${user.email}"/>
                                </div>
                                <button class="btn btn-lg btn-primary btn-block btn-login
                                                    text-uppercase font-weight-bold mb-2 mt-5"
                                        type="submit" name="command" value="editUser">
                                    <strong><fmt:message key="personal_cabinet.button_save"/></strong>
                                </button>
                            </form>
                        </div>
                        <div class="col-auto mx-auto">
                            <div class="mb-3">
                                <a href="" class="overlayLink btn btn-primary mb-3" role="button">
                                    <fmt:message key="button.change_password"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<c:if test="${errorMessage != null}">
    <script>
        showAlert("<fmt:message key="${errorMessage}"/>");
    </script>
</c:if>
<c:remove var="errorMessage"/>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
</body>
</html>