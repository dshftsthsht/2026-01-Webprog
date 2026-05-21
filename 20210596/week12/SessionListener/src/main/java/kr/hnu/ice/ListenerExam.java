package kr.hnu.ice;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class ListenerExam implements ServletContextListener,
        ServletContextAttributeListener,
        HttpSessionListener,
        HttpSessionAttributeListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().log("[ServletContextListener] contextInitialized 호출");
        System.out.println("[ServletContextListener] contextInitialized 호출");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("[ServletContextListener] contextDestroyed 호출");
        System.out.println("[ServletContextListener] contextDestroyed 호출");
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        event.getServletContext().log(
                "[ServletContextAttributeListener] attributeAdded 호출 - name="
                + event.getName() + ", value=" + event.getValue()
        );

        System.out.println(
                "[ServletContextAttributeListener] attributeAdded 호출 - name="
                + event.getName() + ", value=" + event.getValue()
        );
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        event.getServletContext().log(
                "[ServletContextAttributeListener] attributeRemoved 호출 - name="
                + event.getName()
        );

        System.out.println(
                "[ServletContextAttributeListener] attributeRemoved 호출 - name="
                + event.getName()
        );
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        event.getServletContext().log(
                "[ServletContextAttributeListener] attributeReplaced 호출 - name="
                + event.getName()
        );

        System.out.println(
                "[ServletContextAttributeListener] attributeReplaced 호출 - name="
                + event.getName()
        );
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().getServletContext().log(
                "[SessionListener] Session 생성 - " + se.getSession().getId()
        );

        System.out.println(
                "[SessionListener] Session 생성 - " + se.getSession().getId()
        );
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        se.getSession().getServletContext().log(
                "[SessionListener] Session 종료 - " + se.getSession().getId()
        );

        System.out.println(
                "[SessionListener] Session 종료 - " + se.getSession().getId()
        );
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().log(
                "[SessionAttributeListener] Session 속성 추가 - name="
                + event.getName() + ", value=" + event.getValue()
        );

        System.out.println(
                "[SessionAttributeListener] Session 속성 추가 - name="
                + event.getName() + ", value=" + event.getValue()
        );
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().log(
                "[SessionAttributeListener] Session 속성 삭제 - name="
                + event.getName()
        );

        System.out.println(
                "[SessionAttributeListener] Session 속성 삭제 - name="
                + event.getName()
        );
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().log(
                "[SessionAttributeListener] Session 속성 변경 - name="
                + event.getName()
        );

        System.out.println(
                "[SessionAttributeListener] Session 속성 변경 - name="
                + event.getName()
        );
    }
}