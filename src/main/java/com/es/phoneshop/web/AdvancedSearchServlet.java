package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.AdvancedSearch;
import com.es.phoneshop.services.RecentlyViewedService;
import com.es.phoneshop.services.impl.RecentlyViewedServiceImpl;
import com.es.phoneshop.utils.validator.SimpleValidator;
import com.es.phoneshop.utils.validator.SimpleValidatorImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class AdvancedSearchServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;
    private SimpleValidator simpleValidator;

    @Override
    public void init() throws ServletException {
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = RecentlyViewedServiceImpl.getInstance();
        simpleValidator = SimpleValidatorImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getAttribute("products") == null) {
            request.setAttribute("products", new ArrayList<>());
        }
        request.setAttribute("viewedProducts", recentlyViewedService.getViewedProducts(request));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearchPage.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, List<String>> errors = new HashMap<>();
        checkAdvancedSearchOnValid(request, errors);
        if (errors.isEmpty()) {
            AdvancedSearch advancedSearch = getAdvancedSearchFromRequest(request);
            request.setAttribute("products",
                    productDao.findProductsByAdvancedSearch(advancedSearch));
        }
        request.setAttribute("errors", errors);
        doGet(request, response);
    }

    private AdvancedSearch getAdvancedSearchFromRequest(HttpServletRequest request) {
        return new AdvancedSearch(
                request.getParameter("description"),
                request.getParameter("minPrice").equals("") ? null :
                        new BigDecimal(request.getParameter("minPrice")),
                request.getParameter("maxPrice").equals("") ? null :
                        new BigDecimal(request.getParameter("maxPrice")),
                request.getParameter("minStock").equals("") ? null :
                        Integer.parseInt(request.getParameter("minStock")));
    }

    private void checkAdvancedSearchOnValid(HttpServletRequest request, Map<String, List<String>> errors) {
        simpleValidator.newErrorList().setCheckedValue(request.getParameter("minPrice")).numberMoreThen(-1)
                .addToMapErrorsIfExist(errors, "minPrice");
        simpleValidator.newErrorList().setCheckedValue(request.getParameter("maxPrice")).numberMoreThen(-1)
                .addToMapErrorsIfExist(errors, "maxPrice");
        simpleValidator.newErrorList().setCheckedValue(request.getParameter("minStock")).numberMoreThen(-1)
                .addToMapErrorsIfExist(errors, "minStock");

    }
}
