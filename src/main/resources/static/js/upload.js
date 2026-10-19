const form = document.getElementById('upload-form');
const preview = document.getElementById('uploaded-mod-image');

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    formData.append('gameName', document.getElementById('game-name').value);
    formData.append('modName', document.getElementById('mod-name').value);
    formData.append('developers', document.getElementById('mod-developers').value);
    formData.append('releaseDate', document.getElementById('mod-release-date').value);
    formData.append('image', document.getElementById('mod-image-submit').files[0]);
    formData.append('file', document.getElementById('mod-file').files[0]);
    

    try{
        const response = await fetch('/api/mods/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP! Status: ${response.status}`);
        }

        const data = await response.text();

        document.getElementById('responseMessage').innerText = "Succeso: " + data;
        document.getElementById('upload-form').reset();
        preview.setAttribute('src', "#");
    } catch (error) {
        document.getElementById('responseMessage').innerText = "Erro enviando formulário: " + error.message;
        console.error('Error:', error);
    }

});

const input = document.getElementById('mod-image-submit');

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
