package co.grandcircus.blackjack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import co.grandcircus.blackjack.ApiService;

@RestController
public class BlackjackController {
	
	@Autowired
	ApiService a;

}
