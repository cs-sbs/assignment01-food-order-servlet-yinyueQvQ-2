package cs.sbs.web.servlet;

import cs.sbs.web.data.OrderStore;
import cs.sbs.web.model.MenuItem;
import cs.sbs.web.util.HtmlResponses;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MenuListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        boolean html = HtmlResponses.prefersHtml(req);

        String nameParam = req.getParameter("name");
        List<MenuItem> items = OrderStore.searchByName(nameParam);

        if (html) {
            resp.setContentType("text/html;charset=UTF-8");
            HtmlResponses.writeMenuHtml(resp.getWriter(), items, nameParam);
            return;
        }

        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        if (items.isEmpty()) {
            out.println("No matching menu items.");
            return;
        }

        out.println("Menu List:");
        out.println();
        for (int i = 0; i < items.size(); i++) {
            MenuItem m = items.get(i);
            out.printf("%d. %s - $%d%n", i + 1, m.getName(), m.getPrice());
        }
    }
}
