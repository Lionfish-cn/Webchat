package com.chat.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errorDo")
public class ErrorDo {

	@RequestMapping("error403")
	public String error403(){
		return "error/error403";
	}
}
