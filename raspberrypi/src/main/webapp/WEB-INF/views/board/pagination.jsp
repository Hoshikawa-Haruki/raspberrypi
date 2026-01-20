<%-- 
    Document   : pagination
    Created on : 2026. 1. 19., 오전 4:13:39
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<div class="pagination">
    <!-- 맨 처음으로 -->
    <c:if test="${currentPage > 0}">
        <c:choose>
            <c:when test="${not empty keyword}">
                <a class="nav"
                   href="?page=0&searchType=${searchType}&keyword=${keyword}">
                    ◀
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav" href="?page=0">
                    ◀
                </a>
            </c:otherwise>
        </c:choose>

    </c:if>

    <!-- 이전 블록 -->
    <c:if test="${currentPage > 0}">
        <c:choose>
            <c:when test="${not empty keyword}">
                <a class="nav"
                   href="?page=${currentPage - 1}&searchType=${searchType}&keyword=${keyword}">
                    이전
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav" href="?page=${currentPage - 1}">이전</a>
            </c:otherwise>
        </c:choose>
    </c:if>

    <!-- 페이지 번호 -->
    <c:if test="${totalPages > 0}">
        <c:forEach var="i" begin="${startPage}" end="${endPage}">
            <c:choose>
                <c:when test="${i == currentPage}">
                    <span class="page active">${i + 1}</span>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${not empty keyword}">
                            <a class="page"
                               href="?searchType=${searchType}&keyword=${keyword}&page=${i}">
                                ${i + 1}
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="page" href="?page=${i}">
                                ${i + 1}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>

    <!-- 다음 블록 -->
    <c:if test="${currentPage < totalPages - 1}">
        <c:choose>
            <c:when test="${not empty keyword}">
                <a class="nav"
                   href="?page=${currentPage + 1}&searchType=${searchType}&keyword=${keyword}">
                    다음
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav" href="?page=${currentPage + 1}">다음</a>
            </c:otherwise>
        </c:choose>
    </c:if>

    <!-- 맨 끝으로 -->
    <c:if test="${currentPage < totalPages - 1}">
        <c:choose>

            <c:when test="${not empty keyword}">
                <a class="nav"
                   href="?page=${totalPages - 1}&searchType=${searchType}&keyword=${keyword}">
                    ▶
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav"
                   href="?page=${totalPages - 1}">
                    ▶
                </a>
            </c:otherwise>
        </c:choose>

    </c:if>

</div>

