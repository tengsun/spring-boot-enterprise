package st.demo.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String index(Model model) {
		Message msg = new Message("test", "hello springboot!", "admin secret");
		model.addAttribute("msg", msg);
		return "home";
	}
}
