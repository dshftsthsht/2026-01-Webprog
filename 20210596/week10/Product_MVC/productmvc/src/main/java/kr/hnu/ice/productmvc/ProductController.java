package kr.hnu.ice.productmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductController extends HttpServlet {
    private ProductService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String view;

        if ("info".
                equals(action)) {
            view = info(request, response);
        } else {
            view = list(request, response);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public String list(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("products", service.findAll());
        return "/WEB-INF/productList.jsp";
    }

    public String info(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Product product = service.find(id);
        request.setAttribute("product", product);
        return "/WEB-INF/productInfo.jsp";
    }
}
