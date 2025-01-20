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

			const roles = data.roles || [];

			if (roles.includes('ROLE_ADMIN')) {
			                // Redirect to specific admin pages based on permissions
				if (roles.includes('ROLE_CREATE_ROLE')) {
			    	window.location.href = '/admin/createrole'; // Example redirect to create role page
			    } 
				else if (roles.includes('ROLE_MANAGE_USERS')) {
					window.location.href = '/admin/users'; // Example redirect to user management
			    } 
				else {
			        window.location.href = '/admin/dashboard'; // Default admin page
			    }
			 } 
			 else {
			    window.location.href = '/home'; // Redirect to home for regular users
			 }
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


const submitRole = document.getElementById("submitRole");
if (submitRole) {
    submitRole.addEventListener("click", function (event) {
        event.preventDefault();  // Prevent form submission

        const roleName = document.getElementById("roleName").value;

        // Send POST request to create the role
        fetch('/admin/createRole', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: roleName })  // Ensure correct key name
        })
        .then(response => {
            if (response.ok) {
                window.location.href = "/admin/dashboard";  // Redirect on success
            } else {
                console.error('Role creation failed');
            }
        })
        .catch(error => console.error('Error:', error));
    });
}

function openEditModal(userId, username, role) {
            document.getElementById('editUserId').value = userId;
            document.getElementById('editUsername').value = username;
            document.getElementById('editRole').value = role;
            document.getElementById('editModal').style.display = 'block';
        }

        // Close Edit Modal
        function closeEditModal() {
            document.getElementById('editModal').style.display = 'none';
        }

        // Delete User
        function deleteUser(userId) {
            if (confirm("Are you sure you want to delete this user?")) {
                fetch(`/admin/user/${userId}/delete`, {
                    method: 'DELETE',
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();  // Reload the page to update the user list
                    } else {
                        console.error('Error deleting user');
                    }
                })
                .catch(error => console.error('Error:', error));
            }
        }

        // Edit User
        document.getElementById('editUserForm').addEventListener('submit', function(event) {
            event.preventDefault();  // Prevent form submission

            const userId = document.getElementById('editUserId').value;
            const username = document.getElementById('editUsername').value;
            const role = document.getElementById('editRole').value;

            fetch(`/admin/user/${userId}/edit?username=${username}&role=${role}`, {
                method: 'POST',
            })
            .then(response => {
                if (response.ok) {
                    closeEditModal();
                    window.location.reload();  // Reload the page to update the user list
                } else {
                    console.error('Error editing user');
                }
            })
            .catch(error => console.error('Error:', error));
        });
