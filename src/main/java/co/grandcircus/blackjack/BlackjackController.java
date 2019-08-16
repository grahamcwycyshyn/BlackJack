package co.grandcircus.blackjack;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.grandcircus.blackjack.ApiService;
import co.grandcircus.blackjack.dao.UserRepository;
import co.grandcircus.blackjack.entity.User;




@RestController
public class BlackjackController {
	
	@Autowired
	ApiService a;
	
	@Autowired
	private UserRepository dao;


	@RequestMapping("/signup")
	public ModelAndView showSignup() {
		return new ModelAndView("signup-form");
	}
	
	@RequestMapping("/signup-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session) {
		
		dao.save(user);
		
		session.setAttribute("user", user);
		
		ModelAndView mv = new ModelAndView("thanks");
		return mv;
	}
	
	@RequestMapping("/login")
		public ModelAndView showLogin() {
			return new ModelAndView("login-form");
		}
		
		
}