fetch("http://localhost:8080/api/currentUser")
    .then(res => res.json())
    .then(user => {
        document.getElementById('current_user').innerText = user.email;
    })
fetch("http://localhost:8080/api/users/roles")
    .then(res => res.text())
    .then(res => {
        document.getElementById("roles").innerText = res;
    })

