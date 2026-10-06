
const loadUser = async () => {
    try{
        const response = await fetch('/login/me');
        if(!response.ok)
            return;

        const data = await response.json();

        return data.name;
    } catch(error){
        console.log("Autenticação: " + error);
    }

}

const loadHeader = async () => {
    try{
        const response = await fetch('/header.html');
        if(!response.ok)
            throw new Error("Requisição header erro");

        const data = await response.text();
        document.getElementById('header').innerHTML = data;

        name = data.name;
    } catch(error){
        console.error("Erro carregando o Header: ", error);
    }

}

const load = async () => {
    const name = await loadUser();
    await loadHeader();

    if(name){
        let icon = document.getElementById("user-icon");
        icon.src = "/images/PLACEHOLDER_nagacuga.png";
        icon.style.display = "block";
        document.getElementById("user-symbol").style.display = "none";
        let nameElem = document.getElementById("user-name");
        nameElem.innerText = name;
    }
}

load();