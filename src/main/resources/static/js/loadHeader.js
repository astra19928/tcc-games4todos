import {getUser} from "./loadUser.js";

const loadHeader = async () => {
    const response = await fetch('/header.html');
    if(!response.ok) return console.log("Erro ao carregar o header!");

    const data = await response.text();
    document.getElementById('header').innerHTML = data;
}

const load = async () => {
    const user = await getUser();
    await loadHeader();

    if(user.loggedIn){
        const icon = document.getElementById("user-icon");
        icon.src = user.iconUrl;
        icon.style.display = "block";

        const nameElem = document.getElementById("user-name");
        nameElem.innerText = user.name;

        document.getElementById("user-symbol").style.display = "none";
    }
}

document.addEventListener("DOMContentLoaded", load);