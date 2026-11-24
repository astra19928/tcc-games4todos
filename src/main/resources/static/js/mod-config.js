import { setupMultiSelect } from '/js/multiSelect.js';

const form = document.getElementById('mod-config-form');
const modId = form.dataset.modId;
const preview = document.getElementById('uploaded-mod-image');
const imageInput = document.getElementById('mod-image-submit');
const fileInput = document.getElementById('mod-file');

const parseIds = (select) =>
    (select.dataset.initial || '').split(',').map(s => s.trim()).filter(Boolean);

const localizationSelect = document.getElementById('localization-type-select');
const disabilitySelect = document.getElementById('disability-type-select');

const localizationPicker = setupMultiSelect(
    'localization-type-select', 'localization-selected', parseIds(localizationSelect));
const disabilityPicker = setupMultiSelect(
    'disability-type-select', 'disability-selected', parseIds(disabilitySelect));

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = new FormData();
    formData.append('modName', document.getElementById('mod-name').value);
    formData.append('developers', document.getElementById('mod-developers').value);
    formData.append('description', document.getElementById('description').value);
    formData.append('releaseDate', document.getElementById('mod-release-date').value);

    if (imageInput.files[0]) formData.append('image', imageInput.files[0]);
    if (fileInput.files[0]) formData.append('file', fileInput.files[0]);

    localizationPicker.getValues().forEach(id => formData.append('localizationTypes', id));
    disabilityPicker.getValues().forEach(id => formData.append('disabilityTypes', id));

    try {
        const response = await fetch(`/api/mods/${modId}`, {
            method: 'PUT',
            body: formData
        });

        if (!response.ok) return showResponse('Erro ao salvar alterações do mod.');

        const data = await response.text();
        showResponse('Sucesso: ' + data);

        imageInput.value = '';
        fileInput.value = '';
    } catch (e) {
        showResponse('Erro de conexão ao salvar o mod.');
    }
});

imageInput.addEventListener('change', () => {
    const file = imageInput.files[0];
    if (!file) return;

    const fileReader = new FileReader();
    fileReader.onload = event => preview.setAttribute('src', event.target.result);
    fileReader.readAsDataURL(file);
});

function showResponse(text) {
    document.getElementById('responseMessage').innerText = text;
}
