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

public class OrderCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        boolean html = HtmlResponses.prefersHtml(req);

        String customer = trimToNull(req.getParameter("customer"));
        String food = trimToNull(req.getParameter("food"));
        String quantityRaw = trimToNull(req.getParameter("quantity"));

        if (customer == null || food == null || quantityRaw == null) {
            respondError(resp, html, HttpServletResponse.SC_BAD_REQUEST,
                    "Error: missing required parameter (customer, food, quantity)");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityRaw.trim());
        } catch (NumberFormatException e) {
            respondError(resp, html, HttpServletResponse.SC_BAD_REQUEST,
                    "Error: quantity must be a valid number");
            return;
        }

        Order order = OrderStore.createOrder(customer, food, quantity);

        if (html) {
            resp.setContentType("text/html;charset=UTF-8");
            HtmlResponses.writeOrderCreated(resp.getWriter(), order);
        } else {
            resp.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("Order Created: " + order.getId());
        }
    }

    private static void respondError(HttpServletResponse resp, boolean html, int status, String message)
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

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
