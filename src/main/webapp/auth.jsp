<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--<c:set var="language"--%>
<%--       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"--%>
<%--       scope="session"/>--%>
<fmt:setLocale value="${sessionScope.language}" scope="session"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.language}">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title> Facultative </title>
</head>

<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>


<div align="center">
    <jsp:include page="/parts/header.jsp"/>
    <c:if test="${requestScope.message != null}">
    <div class="alert alert-warning alert-dismissible fade show col-lg-2" role="alert">
        <strong>${requestScope.message}</strong>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    </c:if>

    <section class="vh-100">
        <div class="container-fluid h-custom">
            <div class="row d-flex justify-content-center align-items-center h-80">
                <div class="col-md-9 col-lg-6 col-xl-5">
                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp"
                         class="img-fluid" alt="Sample image">
                </div>
                <div class="col-md-8 col-lg-6 col-xl-4 offset-xl-1">
                    <form action="controller" method="post">
                        <input type="hidden" name="action" value="auth"/>

                        <div class="form-floating mt-4 mb-3">
                            <input class="form-control form-control-lg" name="login" id="floatingInput"
                                   placeholder="login"
                                   value="${requestScope.login}"
                                   pattern="^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\d.-]{4,16}$"
                                   title="Login must contains 4 to 16 characters" required>
                            <label class="form-label" for="floatingInput"><fmt:message key="auth.login"/></label>
                        </div>

                        <div class="form-floating mb-1">
                            <input type="password" class="form-control form-control-lg" name="password"
                                   id="floatingPassword" placeholder="Password"
                                   pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,20}$"
                                   title="Password must be between 8 and 20 characters, capital letter and no special characters"
                                   required>
                            <label class="form-label" for="floatingPassword"><fmt:message key="auth.password"/></label>
                        </div>

                        <div class="d-flex justify-content-between align-items-center">
                            <button type="button" class="btn btn-primary btn-lg"
                                    style="padding-left: 2.5rem; padding-right: 2.5rem;"><fmt:message
                                    key="auth.button"/>
                            </button>
                            <a href="#!" class="text-body">Forgot password?</a>
                        </div>

                        <div class="text-center text-lg-start pt-2">
                            <p class="small fw-bold pt-1 mb-0">Don't have an account? <a href="register.jsp"
                                                                                              class="link-danger">Register</a>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="/parts/footer.jsp"/>
    </section>
</body>
</html>