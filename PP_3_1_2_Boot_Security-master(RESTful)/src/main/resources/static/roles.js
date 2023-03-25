function getRoles(user) {
    let roleList = ""
    for (let i = 0; i < user.roles.length; i++) {
        roleList += (user.roles[i].name + " ").substring(5);
    }
    return roleList;
}

