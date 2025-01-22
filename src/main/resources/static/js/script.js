console.log("Login Page");

// Function to retrieve a cookie by name
function getCookie(name) {
	let cookieArr = document.cookie.split(';');
	for (let i = 0; i < cookieArr.length; i++) {
		let cookie = cookieArr[i].trim();
		if (cookie.startsWith(name + "=")) {
			return cookie.substring(name.length + 1);
		}
	}
	return null;
}

// Check if the token exists in cookies
let token = getCookie("authToken");
if (token) {
    console.log("Token found in cookies:", token);
    // Make an authenticated request to /home
    fetch('http://localhost:8080/home', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        credentials: 'include' // Include cookies in the request
    })
    .then(response => response.json())
    .then(data => console.log("Home response:", data))
    .catch(error => console.error("Error accessing /home:", error));
} 
else {
    console.log("No token found in cookies");
}