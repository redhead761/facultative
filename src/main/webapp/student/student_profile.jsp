<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<fmt:setLocale value="${sessionScope.language}" scope="session"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title> Facultative </title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="wrapper">
    <jsp:include page="../parts/header.jsp"/>
    <jsp:include page="../parts/student_header.jsp"/>
    <main class="main">
        <tags:notification value_message="${requestScope.message}" value_error="${requestScope.error}"/>
        <section>
            <div class="container py-5">
                <div class="row">
                    <div class="col-lg-4">
                        <div class="card mb-4">
                            <div class="card-body text-center">
                                <c:if test="${sessionScope.user.avatar eq null}">
                                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp"
                                         alt="avatar"
                                         class="rounded-circle img-fluid" style="width: 150px;">
                                </c:if>
                                <c:if test="${sessionScope.user.avatar != null}">
                                    <img src="data:image/jpg;base64,${sessionScope.user.avatar}"
                                         width="240" height="300" alt="avatar">
                                </c:if>

                                <h5 class="my-3">${sessionScope.user.name} ${sessionScope.user.surname}</h5>
                                <p class="text-muted mb-1">${sessionScope.user.role}</p>
                                <form class="mx-1 mx-md-4" action="${pageContext.request.contextPath}/controller"
                                      method="post" enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="add_avatar"/>
                                    <input type="hidden" name="user_id" value="${sessionScope.user.id}"/>
                                    <input type="file" name="avatar" accept="image/*">
                                    <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
                                        <button type="submit" class="btn btn-primary btn-sm"><fmt:message
                                                key="submit"/></button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-8">
                        <div class="card mb-4">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <p class="mb-0"><fmt:message key="login"/></p>
                                    </div>
                                    <div class="col-sm-9">
                                        <p class="text-muted mb-0">${sessionScope.user.login}</p>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <p class="mb-0"><fmt:message key="full.name"/></p>
                                    </div>
                                    <div class="col-sm-9">
                                        <p class="text-muted mb-0">${sessionScope.user.name} ${sessionScope.user.surname}</p>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <p class="mb-0"><fmt:message key="email"/></p>
                                    </div>
                                    <div class="col-sm-9">
                                        <p class="text-muted mb-0">${sessionScope.user.email}</p>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <p class="mb-0"><fmt:message key="is.block"/></p>
                                    </div>
                                    <div class="col-sm-9">
                                        <p class="text-muted mb-0">${sessionScope.user.block}</p>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <p class="mb-0"><fmt:message key="course.number"/></p>
                                    </div>
                                    <div class="col-sm-9">
                                        <p class="text-muted mb-0">${sessionScope.user.courseNumber}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>
    <jsp:include page="/parts/footer.jsp"/>
</div>
</body>
</html>