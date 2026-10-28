import {getUser} from "./loadUser.js";

const urls = window.location.pathname.split('/');
const id = urls[2];
const commentIcon = document.getElementById('comment-icon');
const submitButton = document.getElementById('comment-submit');
const commentText = document.getElementById('comment');

async function loadCommentIcon(){
    const user = await getUser();
    commentIcon.src = user.iconUrl;
}

async function submitComment() {
    const text = commentText.value;

    if(!text)
        return;

    const response = await fetch(`/api/mods/${id}/comment`,  {
        method: 'POST',
        body: text
    })

    if(response.status === 400) return console.log('fez ruim :(');
    if(response.status === 404) return console.log('nao eh isso nao...');
    if(!response.ok)            return console.log("coisas boas nao aconteceram...");

    console.log("yippeeeeeee!!");
}

document.addEventListener("DOMContentLoaded", loadCommentIcon);
submitButton.addEventListener("click", submitComment)