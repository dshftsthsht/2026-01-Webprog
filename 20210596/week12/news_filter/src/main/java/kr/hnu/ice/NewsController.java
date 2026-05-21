package kr.hnu.ice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;

@WebServlet(name = "NewsController", urlPatterns = {"/news.ice"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class NewsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private NewsDAO dao;
    private ServletContext context;
    private final String START_PAGE = "newsList.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new NewsDAO();
        context = getServletContext();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 필터 실습을 위해 주석 처리
        // 한글 인코딩 처리는 NewsFilter.java에서 처리한다.
        // request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String view = "";

        if (action == null) {
            action = "listNews";
        }

        try {
            Method m = this.getClass().getMethod(action, HttpServletRequest.class);
            view = (String) m.invoke(this, request);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            request.setAttribute("error", "action 요청이 잘못되었습니다: " + action);
            view = START_PAGE;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.toString());
            view = START_PAGE;
        }

        if (view.startsWith("redirect:/")) {
            String redirectPath = view.substring("redirect:/".length());
            response.sendRedirect(request.getContextPath() + "/" + redirectPath);
        } else {
            request.getRequestDispatcher(view).forward(request, response);
        }
    }

    public String addNews(HttpServletRequest request) {
        News news = new News();

        try {
            BeanUtils.populate(news, request.getParameterMap());

            Part part = request.getPart("file");
            String fileName = getFilename(part);

            if (fileName != null && !fileName.isEmpty()) {
                String imgPath = context.getRealPath("/img");

                File imgDir = new File(imgPath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }

                File saveFile = new File(imgDir, fileName);

                try (InputStream inputStream = part.getInputStream()) {
                    Files.copy(inputStream, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                news.setImg("img/" + fileName);
            } else {
                news.setImg("");
            }

            dao.addNews(news);

        } catch (Exception e) {
            e.printStackTrace();
            context.log("뉴스 추가 과정에서 문제가 발생했습니다.", e);
            request.setAttribute("error", e.toString());
            return listNews(request);
        }

        return "redirect:/news.ice?action=listNews";
    }

    public String listNews(HttpServletRequest request) {
        try {
            List<News> list = dao.getAll();
            request.setAttribute("newsList", list);
        } catch (Exception e) {
            e.printStackTrace();
            context.log("뉴스 리스트를 가져오는 과정에서 문제가 발생했습니다.", e);
            request.setAttribute("error", e.toString());
        }

        return START_PAGE;
    }

    public String getNews(HttpServletRequest request) {
        try {
            int aid = Integer.parseInt(request.getParameter("aid"));

            News news = dao.getNews(aid);

            if (news == null) {
                request.setAttribute("error", "해당 뉴스를 찾을 수 없습니다.");
                return listNews(request);
            }

            request.setAttribute("news", news);

        } catch (Exception e) {
            e.printStackTrace();
            context.log("뉴스를 가져오는 과정에서 문제가 발생했습니다.", e);
            request.setAttribute("error", e.toString());
            return listNews(request);
        }

        return "newsView.jsp";
    }

    public String editNews(HttpServletRequest request) {
        try {
            int aid = Integer.parseInt(request.getParameter("aid"));

            News news = dao.getNews(aid);

            if (news == null) {
                request.setAttribute("error", "수정할 뉴스를 찾을 수 없습니다.");
                return listNews(request);
            }

            request.setAttribute("news", news);

        } catch (Exception e) {
            e.printStackTrace();
            context.log("뉴스 수정 화면 이동 과정에서 문제가 발생했습니다.", e);
            request.setAttribute("error", e.toString());
            return listNews(request);
        }

        return "newsEdit.jsp";
    }

    public String updateNews(HttpServletRequest request) {
        News news = new News();

        try {
            BeanUtils.populate(news, request.getParameterMap());

            News oldNews = dao.getNews(news.getAid());

            if (oldNews == null) {
                request.setAttribute("error", "수정할 뉴스를 찾을 수 없습니다.");
                return listNews(request);
            }

            Part part = request.getPart("file");
            String fileName = getFilename(part);

            if (fileName != null && !fileName.isEmpty()) {
                String imgPath = context.getRealPath("/img");

                File imgDir = new File(imgPath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }

                File saveFile = new File(imgDir, fileName);

                try (InputStream inputStream = part.getInputStream()) {
                    Files.copy(inputStream, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                news.setImg("img/" + fileName);
            } else {
                news.setImg(oldNews.getImg());
            }

            dao.updateNews(news);

        } catch (Exception e) {
            e.printStackTrace();
            context.log("뉴스 수정 과정에서 문제가 발생했습니다.", e);
            request.setAttribute("error", e.toString());
            return listNews(request);
        }

        return "redirect:/news.ice?action=listNews";
    }

    public String delNews(HttpServletRequest request) {
        try {
            int aid = Integer.parseInt(request.getParameter("aid"));
            dao.delNews(aid);

        } catch (Exception e) {
            e.printStackTrace();
            context.log("뉴스를 삭제하는 과정에서 문제가 발생했습니다.", e);
            request.setAttribute("error", e.toString());
            return listNews(request);
        }

        return "redirect:/news.ice?action=listNews";
    }

    private String getFilename(Part part) {
        if (part == null) {
            return null;
        }

        String header = part.getHeader("content-disposition");

        if (header == null) {
            return null;
        }

        for (String content : header.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf("=") + 1).trim().replace("\"", "");
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                return fileName;
            }
        }

        return null;
    }
}