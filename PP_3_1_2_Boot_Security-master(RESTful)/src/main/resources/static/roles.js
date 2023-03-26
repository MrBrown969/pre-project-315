function getRoles(user) {
    let roleList = ""
    for (let i = 0; i < 1; i++) {
        roleList += (user.roles[0].name).substring(5);
    }
    return roleList;
}

