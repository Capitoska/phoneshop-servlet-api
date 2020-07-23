package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.services.CartService;
import com.es.phoneshop.services.RecentlyViewedService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ProductDao productDao;

    @Mock
    private CartService cartService;

    @Mock
    private RecentlyViewedService recentlyViewedService;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    Product testProduct = new Product();

    @Mock
    Cart cart = new Cart();

    public static final String DEFAULT_URL = "products/1";
    public static final String URL_WITH_WRONG_ID = "product/999999";
    public static final String DEFAULT_QUANTITY = "3";
    public static final String NOT_LONG_VALUE = "error_value";


    @InjectMocks
    public ProductDetailsPageServlet productDetailsPageServlet;

    @Before
    public void init() {
        when(request.getPathInfo()).thenReturn(DEFAULT_URL);
        when(productDao.getById(anyLong())).thenReturn(testProduct);
        when(request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp")).thenReturn(requestDispatcher);
        when(recentlyViewedService.getViewedProducts(request)).thenReturn(null);
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getParameter("quantity")).thenReturn(DEFAULT_QUANTITY);

    }

    @Test
    public void doGet() throws ServletException, IOException {
        productDetailsPageServlet.doGet(request, response);
        verify(request).setAttribute("product", testProduct);
        verify(request).setAttribute("viewedProducts", recentlyViewedService.getViewedProducts(request));
    }

    @Test
    public void doGetWithWrongId() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(URL_WITH_WRONG_ID);
        when(productDao.getById(anyLong())).thenThrow(new NoSuchElementException());
        productDetailsPageServlet.doGet(request, response);
        verify(response).sendError(eq(404), any(String.class));
    }

    @Test
    public void doPost() throws ServletException, IOException {
        productDetailsPageServlet.doPost(request, response);
        verify(request).setAttribute("congratulation", "Added to cart successfully");
    }

    @Test
    public void doPostWithThrowNumberFormatException() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn(NOT_LONG_VALUE);
        productDetailsPageServlet.doPost(request, response);
        verify(request).setAttribute("error", "Not a number");
    }

    @Test
    //Вот в этом методе проблемка.
    public void doPostWithNotEnoughElementsException() throws ServletException, IOException {
//    doThrow()
//            .when(cartService).add(cart,anyLong(),anyLong());

//        doThrow(new NotEnoughElementsException(anyString())).when(cartService).add(cart,any(Long.class),any(Long.class));

        //    when(cartService.add(cart,anyLong(),anyLong())).thenThrow(new NotEnoughElementsException(eq(anyString())));
        productDetailsPageServlet.doPost(request, response);
        verify(request).setAttribute("error", "Not enough stock");
    }
}
