package cs.sbs.web.servlet;

import cs.sbs.web.data.OrderStore;
import cs.sbs.web.model.Order;
import cs.sbs.web.util.HtmlResponses;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        boolean html = HtmlResponses.prefersHtml(req);

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isBlank() || "/".equals(pathInfo)) {
            respond(resp, html, HttpServletResponse.SC_BAD_REQUEST, "Error: order id is required in path");
            return;
        }

        String idPart = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        int id;
        try {
            id = Integer.parseInt(idPart.trim());
        } catch (NumberFormatException e) {
            respond(resp, html, HttpServletResponse.SC_BAD_REQUEST, "Error: invalid order id");
            return;
        }

        Optional<Order> found = OrderStore.findOrderById(id);
        if (found.isEmpty()) {
            respond(resp, html, HttpServletResponse.SC_NOT_FOUND, "Error: Order not found");
            return;
        }

        Order o = found.get();
        if (html) {
            resp.setContentType("text/html;charset=UTF-8");
            HtmlResponses.writeOrderDetail(resp.getWriter(), o);
        } else {
            resp.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("Order Detail");
            out.println();
            out.println("Order ID: " + o.getId());
            out.println("Customer: " + o.getCustomer());
            out.println("Food: " + o.getFood());
            out.println("Quantity: " + o.getQuantity());
        }
    }

    private static void respond(HttpServletResponse resp, boolean html, int status, String message)
            throws IOException {
        resp.setStatus(status);
        if (html) {
            resp.setContentType("text/html;charset=UTF-8");
            HtmlResponses.writePlainErrorAsHtml(resp.getWriter(), message);
        } else {
            resp.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(message);
        }
    }
}
