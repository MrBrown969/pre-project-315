fetch("http://localhost:8080/api/thisUser")
    .then(res => res.json())
    .then(user => {

//todo fields name problem?
        document.getElementById('id').innerText = user.id;
        document.getElementById('firstname').innerText = user.name;
        document.getElementById('lastname').innerText = user.surname;
        document.getElementById('age').innerText = user.age;
        document.getElementById('email').innerText = user.email;
        document.getElementById('current_user').innerText = user.email;

    })
fetch("http://localhost:8080/api/userInfo")//todo: rename fields
    .then(res => res.text())
    .then(res => {
        document.getElementById("roles").innerText = res;
        document.getElementById("rolesTable").innerText = res;
        })



