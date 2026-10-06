
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
        const response = await fetch('header.html');
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
        icon.src = "images/PLACEHOLDER_nagacuga.png";
        icon.style.display = "block";
        document.getElementById("user-symbol").style.display = "none";
        let nameElem = document.getElementById("user-name");
        nameElem.innerText = name;
    }
}

load();
/*
fetch('/login/me')
    .then(response => {
        if(!response.ok){
            throw new Error('Não autenticado');
        }
        return response.json();
    })
    .then(data => {
        name = data.name;
        console.log(data);
        console.log(name);
    })
    .catch(error => {
        console.log("Autenticação: " + error);
    });

fetch('header.html')
  .then(response => response.text())
  .then(html => {
    document.getElementById('header').innerHTML = html;
  })
  .catch(error => console.error("Erro carregando o Header: ", error));
*/
