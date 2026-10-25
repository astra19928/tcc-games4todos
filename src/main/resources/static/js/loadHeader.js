
const loadUser = async () => {

    const response = await fetch('/api/login/me', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
    });

    if(response.redirected && response.url.includes('/login.html')) return console.log("Usuário não autenticado.");
    if(response.status === 401) return console.log("Usuário não autenticado.");
    if(!response.ok)            return;

    const data = await response.json();

    return data.name;
}

const loadHeader = async () => {
    const response = await fetch('/header.html');
    if(!response.ok) return console.log("Erro ao carregar o header!");

    const data = await response.text();
    document.getElementById('header').innerHTML = data;
}

const load = async () => {
    const name = await loadUser();
    await loadHeader();

    if(name){
        let icon = document.getElementById("user-icon");
        icon.src = "/images/PLACEHOLDER_nagacuga.png";
        icon.style.display = "block";
        document.getElementById("user-symbol").style.display = "none";
        const nameElem = document.getElementById("user-name");
        nameElem.innerText = name;
    }
}

load();