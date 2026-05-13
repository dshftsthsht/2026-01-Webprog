package kr.hnu.ice;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

@WebServlet("/student")
public class StudentController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new StudentDAO();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            if (action.equals("insert")) {
                insert(request, response);
            } else if (action.equals("edit")) {
                edit(request, response);
            } else if (action.equals("update")) {
                update(request, response);
            } else if (action.equals("delete")) {
                delete(request, response);
            } else {
                list(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/studentInfo.jsp").forward(request, response);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> studentList = dao.getAll();
        request.setAttribute("studentList", studentList);

        request.getRequestDispatcher("/studentInfo.jsp").forward(request, response);
    }

    private void insert(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        Student student = new Student();

        BeanUtils.populate(student, request.getParameterMap());

        dao.insert(student);

        response.sendRedirect(request.getContextPath() + "/student?action=list");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Student student = dao.getStudent(id);
        List<Student> studentList = dao.getAll();

        request.setAttribute("student", student);
        request.setAttribute("studentList", studentList);
        request.setAttribute("mode", "edit");

        request.getRequestDispatcher("/studentInfo.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {

        Student student = new Student();

        BeanUtils.populate(student, request.getParameterMap());

        dao.update(student);

        response.sendRedirect(request.getContextPath() + "/student?action=list");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        dao.delete(id);

        response.sendRedirect(request.getContextPath() + "/student?action=list");
    }
}