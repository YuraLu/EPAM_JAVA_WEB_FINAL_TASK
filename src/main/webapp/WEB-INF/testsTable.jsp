<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="locale"
       value="${not empty param.locale ? param.locale : not empty locale ? locale : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${locale}"/>

<fmt:setBundle basename="text"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>
        <fmt:message key="table.test_title"/>
    </title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/sticky-footer-navbar.css">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo01"
            aria-controls="navbarTogglerDemo01" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarTogglerDemo01">
        <a class="navbar-brand" href="controller?command=viewIndex"><fmt:message key="title.main"/></a>
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <c:if test="${roleId == 1}">
                <li class="nav-item">
                    <a class="nav-link" href="controller?command=viewUserTable">User Table</a>
                </li>
            </c:if>
            <li class="nav-item">
                <a class="nav-link" href="controller?command=viewTestTable">Test Table</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="controller?command=viewSubjectTable">Subject Table</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="controller?command=viewQuestionTable">Question Table</a>
            </li>
        </ul>

        <div class="nav-tabs " id="localeDivNav">
            <form>
                <input type="hidden" name="command" value="viewTestTable"/>
                <label for="locale"></label>
                <select id="locale" name="locale" onchange="submit()">
                    <option value="en_EN" ${locale == 'en_EN' ? 'selected' : ''}>English</option>
                    <option value="ru_RU" ${locale == 'ru_RU' ? 'selected' : ''}>Русский</option>
                </select>
            </form>
        </div>

        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="controller?command=viewUserCabinet">
                    <fmt:message key="title.personal_cabinet"/>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="controller?command=signOut"><fmt:message key="button.signOut"/></a>
            </li>
        </ul>
    </div>
</nav>

<!-- Begin page content -->
<div class="container">
    <div class="m-t-1">
        <div class="col">

            <h2><fmt:message key="title.test_list"/></h2>
            <p><fmt:message key="title.testTable_intro"/></p>
            <div class="mb-3">
                <a href="controller?command=viewTestAddPage" class="btn btn-primary mb-3 " role="button"><fmt:message
                        key="button.add"/></a>
            </div>
        </div>
        <form action="controller" method="post">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th></th>
                    <th><fmt:message key="test_title"/></th>
                    <th><fmt:message key="test_description"/></th>
                    <th><fmt:message key="test_subject"/></th>
                    <th><fmt:message key="test_author"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${testList}" var="test">
                    <tr>
                        <td>
                            <a href="controller?command=viewTestWorkPage&testId=${test.id}" class="mt-2">
                                <strong><fmt:message key="more_detail"/></strong>
                            </a>
                        </td>
                        <td> ${test.title} </td>
                        <td> ${test.description} </td>
                        <td> ${test.subject.name} </td>
                        <td> ${test.author.name} </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </form>
    </div>
</div>

<footer class="footer">
    <div class="container">
        <span class="text-muted"><fmt:message key="footer.copyRight"/></span>
    </div>
</footer>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/jquery.slim.js"></script>
<script src="bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>