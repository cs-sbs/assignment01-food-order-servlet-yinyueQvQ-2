package cs.sbs.web.util;

import cs.sbs.web.model.MenuItem;
import cs.sbs.web.model.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.util.List;

public final class HtmlResponses {

    private HtmlResponses() {
    }

    public static boolean prefersHtml(HttpServletRequest req) {
        String accept = req.getHeader("Accept");
        return accept != null && accept.toLowerCase().contains("text/html");
    }

    public static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static void writeShellHead(PrintWriter out, String title) {
        out.println("<!DOCTYPE html><html lang=\"zh-CN\"><head>");
        out.println("<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        out.printf("<title>%s</title>%n", esc(title));
        out.println("<link rel=\"stylesheet\" href=\"/static/app.css\">");
        out.println("</head><body>");
        out.println("<header class=\"site-header\"><div class=\"site-header__inner\">");
        out.println("<div class=\"brand\"><div class=\"brand__mark\" aria-hidden=\"true\"></div>");
        out.println("<span class=\"brand__text\">外卖点单</span></div>");
        out.println("<nav class=\"nav-links\"><a href=\"/\">首页</a><a href=\"/menu\">纯文本菜单</a></nav>");
        out.println("</div></header><main class=\"servlet-page\"><div class=\"card\">");
    }

    public static void writeShellFoot(PrintWriter out) {
        out.println("</div></main></body></html>");
    }

    public static void writeOrderCreated(PrintWriter out, Order order) {
        writeShellHead(out, "订单已创建");
        out.println("<p class=\"badge\">下单成功</p>");
        out.printf("<h1>订单 #%d 已记录</h1>%n", order.getId());
        out.println("<p class=\"lede\">Order Created: " + order.getId() + "</p>");
        out.println("<dl>");
        out.printf("<dt>顾客</dt><dd>%s</dd>%n", esc(order.getCustomer()));
        out.printf("<dt>餐品</dt><dd>%s</dd>%n", esc(order.getFood()));
        out.printf("<dt>数量</dt><dd>%d</dd>%n", order.getQuantity());
        out.println("</dl>");
        out.println("<div class=\"actions\">");
        out.printf("<a href=\"/order/%d\">查看详情</a>%n", order.getId());
        out.println("<a href=\"/\">返回首页</a>");
        out.println("</div>");
        writeShellFoot(out);
    }

    public static void writePlainErrorAsHtml(PrintWriter out, String plainMessage) {
        writeShellHead(out, "提示");
        out.printf("<p class=\"err\">%s</p>%n", esc(plainMessage));
        out.println("<div class=\"actions\"><a href=\"/\">返回首页</a></div>");
        writeShellFoot(out);
    }

    public static void writeOrderDetail(PrintWriter out, Order o) {
        writeShellHead(out, "订单详情");
        out.println("<h1>Order Detail</h1>");
        out.println("<dl>");
        out.printf("<dt>订单号</dt><dd>%d</dd>%n", o.getId());
        out.printf("<dt>顾客</dt><dd>%s</dd>%n", esc(o.getCustomer()));
        out.printf("<dt>餐品</dt><dd>%s</dd>%n", esc(o.getFood()));
        out.printf("<dt>数量</dt><dd>%d</dd>%n", o.getQuantity());
        out.println("</dl>");
        out.println("<div class=\"actions\"><a href=\"/\">返回首页</a></div>");
        writeShellFoot(out);
    }

    public static void writeMenuHtml(PrintWriter out, List<MenuItem> items, String searchQuery) {
        writeShellHead(out, "菜单");
        out.println("<h1>Menu List</h1>");
        if (items.isEmpty()) {
            out.println("<p class=\"err\">No matching menu items.</p>");
        } else {
            out.println("<ul class=\"menu-list\">");
            int i = 1;
            for (MenuItem m : items) {
                out.printf("<li class=\"menu-item\"><strong>%d. %s</strong><span class=\"price\">$%d</span></li>%n",
                        i++, esc(m.getName()), m.getPrice());
            }
            out.println("</ul>");
        }
        if (searchQuery != null && !searchQuery.isBlank()) {
            out.printf("<p class=\"hint\">当前筛选：<strong>%s</strong></p>%n", esc(searchQuery.trim()));
        }
        out.println("<div class=\"actions\"><a href=\"/\">返回首页</a></div>");
        writeShellFoot(out);
    }
}
