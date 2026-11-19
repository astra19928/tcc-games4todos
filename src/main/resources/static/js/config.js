
const form = document.getElementById('user-form');
const preview = document.getElementById('uploaded-user-image');
const responseMessage = document.getElementById('responseMessage');
const disabilityCheckboxes = document.querySelectorAll('.disability-checkbox');

const toggleDisabilitySelect = (checkbox) => {
    const select = document.getElementById(checkbox.dataset.target);

    select.hidden = !checkbox.checked;
    checkbox.setAttribute('aria-expanded', String(checkbox.checked));

    if (!checkbox.checked) {
        select.selectedIndex = 0; // volta para "Selecione o tipo"
    }
};

disabilityCheckboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', () => {
        toggleDisabilitySelect(checkbox);
        if (checkbox.checked) {
            document.getElementById(checkbox.dataset.target).focus();
        }
    });
});

// form.reset() desmarca os checkboxes, então os selects precisam ser escondidos de novo
form.addEventListener('reset', () => {
    setTimeout(() => disabilityCheckboxes.forEach(toggleDisabilitySelect), 0);
});

// Retorna [{ category, type }, ...] ou null se algum subtipo marcado não foi escolhido
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

    const formData = new FormData();

    formData.append("nickname", document.getElementById('name-create').value);
    formData.append("email", document.getElementById('email-create').value);
    formData.append("password", document.getElementById('password-create').value);
    disabilities.forEach(d => formData.append("disabilityTypeIds", d.type));

    const imageFile = document.getElementById('form-image-submit').files[0];
    if (imageFile) {
        formData.append("image", imageFile);
    }

    const response = await fetch('/api/users', {
        method: 'POST',
        body: formData
    });

    if (!response.ok) {
        throw new Error('Houve um problema com a requisição!');
    }

    form.reset();
    window.location = response.url;
});

const input = document.getElementById('form-image-submit');

const displayImage = () =>{
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
