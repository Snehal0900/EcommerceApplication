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

// Function to handle login
function login(username, password) {
	console.log(username)
	console.log(password)
	
    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password }),
        credentials: 'include' // Include cookies in the request
    })
    .then(response => {
        if (response.ok) {
            console.log("Login successful");
            return response.json(); // Parse the JSON response
        } else {
            throw new Error("Login failed with status: " + response.status);
        }
    })
    .then(data => {
        console.log("Received token:", data.authToken);
		if (data.authToken) {
			// Store token in cookie (if not already set by the server)
		    document.cookie = `authToken=${data.authToken}; path=/; HttpOnly=false`;

		    // Redirect to the home page
		    window.location.href = "/home";
		}			
		else {
			console.error("No token received from the server");
		}
    })
    .catch(error => {
        console.error("Error during login:", error);
    });
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
} else {
    console.log("No token found in cookies");
}

const loginForm = document.getElementById("loginForm");
if (loginForm) {
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent the default form submission
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        // Call the login function
        login(username, password);
    });
}

	
const logoutButton = document.getElementById("logoutButton");
if (logoutButton) {
    logoutButton.addEventListener("click", function () {
        fetch('/auth/logout', {
            method: 'POST',
            credentials: 'include', // Include cookies in the request
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Logged out successfully");
                    // Clear token manually (if needed)
                    document.cookie = "authToken=; path=/; Max-Age=0;";
                    // Redirect to login page
                    window.location.href = '/auth/login';
                } else {
                    console.error("Logout failed");
                }
            })
            .catch((error) => console.error("Error during logout:", error));
    });
}