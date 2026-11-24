import {getUser} from "./loadUser.js";

const urls = window.location.pathname.split('/');
const user = await getUser();
const id = urls[2];
const favoriteButton = document.getElementById('mod-favorite-button');
const favoriteIcon = document.getElementById('favorite-icon');
const responseMsg = document.getElementById('response-message');
let isFavorited = favoriteIcon.classList.contains('is-favorited');

async function load(){
    if(!user.loggedIn){
        favoriteIcon.classList.add('no-clicky');
        return;
    }

    favoriteButton.addEventListener("click", async () => {
        responseMsg.hidden = true;

        const response = await fetch(`/api/mods/${id}/favorite`,  {
            method: 'POST'
        })

        if(response.status === 400) return console.log('BAD REQUEST');
        if(response.status === 404) return console.log('NOT FOUND');
        if(!response.ok)            return console.log("ERRO DESCONHECIDO");

        const text = await response.text();

        responseMsg.hidden = false;
        responseMsg.textContent = text;

        if(isFavorited){
            favoriteIcon.classList.remove('is-favorited');
            isFavorited = false;
            return;
        }

        favoriteIcon.classList.add('is-favorited');
        isFavorited = true;
    })
}

await load();
