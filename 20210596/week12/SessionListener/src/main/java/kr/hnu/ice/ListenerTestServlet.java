package kr.hnu.ice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ListenerTestServlet")
public class ListenerTestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();

        sc.setAttribute("scName", "홍길동");
        sc.setAttribute("scName", "김철수");
        sc.removeAttribute("scName");
        sc.setAttribute("scName", "홍길동");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();

        session.setAttribute("sName", "홍길동");
        session.setAttribute("sName", "김철수");
        session.removeAttribute("sName");
        session.setAttribute("sName", "홍길동");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Listener Test</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Listener Test Servlet 실행 완료</h2>");
        out.println("<p>ServletContext와 Session 속성 변경이 실행되었습니다.</p>");
        out.println("<p>Tomcat 로그 파일을 확인하세요.</p>");
        out.println("</body>");
        out.println("</html>");
    }
}