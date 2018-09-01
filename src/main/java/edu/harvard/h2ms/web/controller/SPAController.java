package edu.harvard.h2ms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//TODO Remove this controller
/**
 * Demo for vue.js SPA using Spring JPA ORM
 */
@Controller
public class SPAController {

	@RequestMapping("/app")
	public String app() {
		return "spa";
	}
}
