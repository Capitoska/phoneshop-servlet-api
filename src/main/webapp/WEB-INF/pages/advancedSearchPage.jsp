<%--
  Created by IntelliJ IDEA.
  User: Арсений Камадей
  Date: 29.07.2020
  Time: 10:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form method="post">
        <table>
            <tr>
                <td>Description:</td>
                <td><input name="description" value="${param.get("description")}"></td>
                <td class="error-message" style="border-color: white">
                    <c:forEach var="error" items="${errors.get('description')}">
                        ${error}
                        <br/>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td>Min price:</td>
                <td><input name="minPrice" value="${param.get("minPrice")}"></td>
                <td class="error-message" style="border-color: white">
                    <c:forEach var="error" items="${errors.get('minPrice')}">
                        ${error}
                        <br/>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td>Max price:</td>
                <td><input name="maxPrice" value="${param.get("maxPrice")}"></td>
                <td class="error-message" style="border-color: white">
                    <c:forEach var="error" items="${errors.get('maxPrice')}">
                        ${error}
                        <br/>
                    </c:forEach>
                </td class="error-message" style="border-color: white">
            </tr>
            <tr>
                <td>min stock:</td>
                <td><input name="minStock" value="${param.get("minStock")}"></td>
                <td class="error-message" style="border-color: white">
                    <c:forEach var="error" items="${errors.get('minStock')}">
                        ${error}
                    <br/>
                    </c:forEach>
            </tr>
        </table>
        <button>Find</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href="products/price-history/${product.id}">
                        <fmt:formatNumber value="${product.getCurrentPrice().cost}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>


            </tr>
        </c:forEach>
    </table>
    <jsp:include page="/cart/minicart"/>
    <tags:viewed-product/>
</tags:master>
