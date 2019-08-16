package co.grandcircus.blackjack;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.blackjack.entity.Card;
import co.grandcircus.blackjack.entity.Deck;

@Component
public class ApiService {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	{
		ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
	        request.getHeaders().add(HttpHeaders.USER_AGENT, "Spring");
	        return execution.execute(request, body);
	    };
	    restTemplate = new RestTemplateBuilder().additionalInterceptors(interceptor).build();
	}
	
	public Deck getDeck() {
		String url = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=6";
		Deck deck = restTemplate.getForObject(url, Deck.class);
		return deck;
	}
	
	public Card getCard(String id) {
		String url = "https://deckofcardsapi.com/api/deck/" + id + "/draw/?count=1";
		Card card = restTemplate.getForObject(url, Card.class);
		return card;
	}
	
	public Deck shuffle(Deck deck) {
		String url = "https://deckofcardsapi.com/api/deck/" + deck.getId() + "/shuffle/";
		Deck shuffledDeck = restTemplate.getForObject(url, Deck.class);
		return shuffledDeck;
	}

}
