package com.example.springz23.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class logInCookie extends HttpServlet {
    public void doPost(HttpServletResponse res, HttpServletRequest req) throws IOException, ServletException {
        // create a cookie
        Cookie cookie = new Cookie("loggedIn", "True");

        //add a cookie to the response
        res.addCookie(cookie);
    }
}
