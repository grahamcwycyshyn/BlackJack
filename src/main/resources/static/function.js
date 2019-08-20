let betAmount = 0;


function addFive() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 5;
	betDisplay.innerText = betAmount;
	let betForm = document.getElementById("betDeal");
	betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\"" + betAmount + "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
}

function addTwentyFive() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 25;
	betDisplay.innerText = betAmount;
	let betForm = document.getElementById("betDeal");
	betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\"" + betAmount + "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
}


function addHundred() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 100;
	betDisplay.innerText = betAmount;
	let betForm = document.getElementById("betDeal");
	betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\"" + betAmount + "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
}


function addFiveHundred() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 500;
	betDisplay.innerText = betAmount;
	let betForm = document.getElementById("betDeal");
	betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\"" + betAmount + "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
}


function addThousand() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 1000;
	betDisplay.innerText = betAmount;
	let betForm = document.getElementById("betDeal");
	betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\"" + betAmount + "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
}
