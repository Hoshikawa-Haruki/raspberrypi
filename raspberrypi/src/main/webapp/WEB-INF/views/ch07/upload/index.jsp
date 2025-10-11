<%-- 
    Document   : index
    Created on : 2025. 10. 9., 오후 8:22:05
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewprot" content="width=device-width, initial-scale=1.0">
        <title>파일 업로드/다운로드 </title>
    </head>
    <body>
        <h1>파일 업로드/다운로드</h1>
        <hr>
        <c:if test="${!empty exec_message}"> <!-- 컨트롤러에서 exec_message를 받아서 띄워줌 -->
            <div class="box"> 실행 결과: ${exec_message} </div>
            <!-- ✅ 다운로드 링크 표시 -->
            <c:if test="${not empty downloadFile}">
                <p>
                    다운로드 링크:
                    <a href="${pageContext.request.contextPath}/ch07/download?filename=${downloadFile}">
                        ${downloadFile.substring(downloadFile.indexOf('_') + 1)}
                    </a>
                </p>
            </c:if>
        </c:if>

        <!-- 유저네임은 텍스트, 파일은 파일타입이므로 멀티파트 타입으로 설정 -->
        <form enctype="multipart/form-data" method="POST"
              action="${pageContext.request.contextPath}/ch07/upload.do"> <!-- submit 시 해당 url로 제어가 넘어감-->
            username: <input type="text" name="username"> <br><!-- comment -->
            upload 할 파일 선택: <input type="file" name="upfile"> <br><!-- comment -->
            <input type="submit" value="Upload">
        </form>
    </body>
</html>
