package cs.sbs.web;

/**
 * 本作业是 Servlet WAR 项目，Web 服务<strong>不是</strong>通过本类的 {@code main} 启动的。
 * <p>
 * 端口在 {@code pom.xml} 的 {@code jetty.http.port} 中配置（默认 <strong>8080</strong>），
 * 由 {@code jetty-maven-plugin} 在运行 {@code jetty:run} 时生效。
 * <p>
 * 本地部署到 8080：在项目根目录双击或执行 {@code .\run-web.cmd}（与作业二同类一键脚本），
 * 或执行 {@code mvnw.cmd jetty:run} / {@code mvn jetty:run}，浏览器访问 {@code http://localhost:8080/}。
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Food Order Servlet — Web 不在此 main 中启动。");
        System.out.println("请在项目根目录执行:  mvnw.cmd jetty:run   （或: mvn jetty:run）");
        System.out.println("默认端口见 pom.xml 中 <jetty.http.port>，当前为 8080。");
        System.out.println("浏览器打开: http://localhost:8080/");
        System.out.println("若 8080 被占用: mvnw.cmd jetty:run -Djetty.http.port=8081");
    }
}
