const form = document.getElementById('user-form');
const preview = document.getElementById('uploaded-user-image');

form.addEventListener('submit', async (event) => {
    event.preventDefault(); 

    const formData = new FormData();

    formData.append("nickname", document.getElementById('name-create').value);
    formData.append("email", document.getElementById('email-create').value);
    formData.append("password", document.getElementById('password-create').value);
    formData.append("image", document.getElementById('form-image-submit').files[0]);

    const response = await fetch('/api/users', {
        method: 'POST',
        body: formData
    });

    if (!response.ok) {
        throw new Error('Houve um problema com a requisição!');
    }

    const data = await response.text();

    document.getElementById('responseMessage').innerText = 'Succeso: ' + data;
    form.reset();
});

const input = document.getElementById('form-image-submit');

const displayImage = () =>{
    const file = input.files;
    if (file) {
        const fileReader = new FileReader();

        fileReader.onload = event => {
            preview.setAttribute('src', event.target.result);
        }

        fileReader.readAsDataURL(file[0]);
    }
}

input.addEventListener("change", displayImage);