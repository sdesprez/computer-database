package com.excilys.computerdatabase.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.service.ComputerHttpService;

@WebServlet("/delete")
public class DeleteComputerController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {

		ComputerHttpService.delete(req);
		resp.sendRedirect("dashboard");
	}

	
	
}
