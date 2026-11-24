import {getUser} from "./loadUser.js";

const form = document.getElementById('user-form');
const preview = document.getElementById('uploaded-user-image');
const responseMessage = document.getElementById('response-message');
const disabilityCheckboxes = document.querySelectorAll('.disability-checkbox');
const deleteButton = document.getElementById('user-delete');
const logoutButton = document.getElementById('user-logout');

const toggleDisabilitySelect = (checkbox) => {
    const select = document.getElementById(checkbox.dataset.target);

    select.style.visibility = checkbox.checked ? "visible" : "hidden";

    checkbox.setAttribute('aria-expanded', String(checkbox.checked));

    if (!checkbox.checked) {
        select.selectedIndex = 0; // volta para "Selecione o tipo"
    }
};

disabilityCheckboxes.forEach((checkbox) => {
    toggleDisabilitySelect(checkbox);

    checkbox.addEventListener('change', () => {
        toggleDisabilitySelect(checkbox);
        if (checkbox.checked) {
            document.getElementById(checkbox.dataset.target).focus();
        }
    });
});

form.addEventListener('reset', () => {
    setTimeout(() => disabilityCheckboxes.forEach(toggleDisabilitySelect), 0);
});

const collectDisabilities = () => {
    const disabilities = [];

    for (const checkbox of disabilityCheckboxes) {
        if (!checkbox.checked) continue;

        const select = document.getElementById(checkbox.dataset.target);
        if (!select.value) {
            select.focus();
            return null;
        }

        disabilities.push({
            category: Number(checkbox.dataset.category),
            type: Number(select.value)
        });
    }

    return disabilities;
};

form.addEventListener('submit', async (event) => {
    event.preventDefault();
    responseMessage.textContent = '';

    const disabilities = collectDisabilities();
    if (disabilities === null) {
        responseMessage.textContent = 'Selecione o tipo de cada necessidade de acessibilidade marcada.';
        return;
    }

    const password = document.getElementById('password-create').value
    const confirmPassword = document.getElementById('password-confirm').value

    if(password != confirmPassword){
        responseMessage.textContent = 'As senhas não são iguais!';
        return;
    }

    const formData = new FormData();

    formData.append("nickname", document.getElementById('name-create').value);
    formData.append("oldPassword", document.getElementById('old-password-create').value);
    formData.append("password", password);
    disabilities.forEach(d => formData.append("disabilityTypeIds", d.type));

    const imageFile = document.getElementById('form-image-submit').files[0];
    if (imageFile) {
        formData.append("image", imageFile);
    }

    const user = await getUser();
    const response = await fetch('/api/users/' + user.id, {
        method: 'PATCH',
        body: formData
    });

    if (!response.ok) {
        throw new Error('Houve um problema com a requisição!');
    }

    form.reset();
    window.location.reload();
});

let deleteReady = false;
let waiting = false;

deleteButton.addEventListener('click', async () => {
    responseMessage.textContent = 'VOCÊ TEM CERTEZA QUE QUER APAGAR SEU USUÁRIO?';

    if(waiting) {
        responseMessage.textContent = "VOCÊ DEVE ESPERAR 5 SEGUNDOS PARA PODER APAGAR SEU USUÁRIO!";
        return;
    }

    if(!deleteReady) {
        waiting = true;
        setTimeout(() => {
            deleteReady = true;
            waiting = false;
        }, 5000);
        return;
    }

    const isConfirmed = confirm("VOCÊ TEM CERTEZA ABSOLUTA QUE QUER APAGAR SEU USUÁRIO?");

    if(!isConfirmed){
        waiting = false;
        deleteReady = false;
        responseMessage.textContent = '';
        return;

    }

    const user = await getUser();
    const response = await fetch('/api/users/' + user.id, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error('Houve um problema com a requisição!');
    }

    form.reset();
    window.location.assign('/');
});

logoutButton.addEventListener('click', async () => {
    responseMessage.textContent = '';

    const response = await fetch('/api/login/logout', {
        method: 'POST'
    });

    if (!response.ok) {
        responseMessage.textContent = 'Houve um problema com a requisição!';
        return;
    }

    form.reset();
    window.location.reload();
});

const input = document.getElementById('form-image-submit');

const displayImage = () => {
    const file = input.files;
    if (file && file[0]) {
        const fileReader = new FileReader();

        fileReader.onload = event => {
            preview.setAttribute('src', event.target.result);
        }

        fileReader.readAsDataURL(file[0]);
    }
}

input.addEventListener("change", displayImage);
