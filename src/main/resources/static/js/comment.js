import {getUser} from "./loadUser.js";

const user = await getUser();
const urls = window.location.pathname.split('/');
const id = urls[2];
const submitButton = document.getElementById('comment-submit');
const commentText = document.getElementById('comment');

async function submitComment() {
    const text = commentText.value;

    if(!text)
        return;

    const response = await fetch(`/api/mods/${id}/comment`,  {
        method: 'POST',
        body: text
    })

    if(response.status === 400) return console.log('BAD REQUEST');
    if(response.status === 404) return console.log('MOD NOT FOUND');
    if(!response.ok)            return console.log("ERRO INESPERADO");

    window.location.reload();
}

if(user.loggedIn){
    submitButton.addEventListener("click", submitComment);
}
