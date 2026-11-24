const form = document.getElementById('upload-form');
const preview = document.getElementById('uploaded-mod-image');
const input = document.getElementById('mod-image-submit');

function setupMultiSelect(selectId, listId) {
    const select = document.getElementById(selectId);
    const list = document.getElementById(listId);
    const selected = new Map(); // value -> <li>

    function add(option) {
        const value = option.value;
        if (selected.has(value)) return;

        const item = document.createElement('li');
        item.className = 'selected-item';

        const text = document.createElement('span');
        text.textContent = option.textContent;

        const remove = document.createElement('button');
        remove.type = 'button';
        remove.className = 'selected-item-remove';
        remove.textContent = '×';
        remove.setAttribute('aria-label', 'Remover ' + option.textContent);
        remove.addEventListener('click', () => removeValue(value));

        item.append(text, remove);
        list.appendChild(item);

        option.disabled = true;
        selected.set(value, item);
    }

    function removeValue(value) {
        const item = selected.get(value);
        if (!item) return;

        item.remove();
        selected.delete(value);

        const option = [...select.options].find(o => o.value === value);
        if (option) option.disabled = false;
    }

    select.addEventListener('change', () => {
        const option = select.selectedOptions[0];
        if (!option || !option.value) return;

        add(option);
        select.value = ''; // back to the placeholder
    });

    return {
        getValues: () => [...selected.keys()],
        clear: () => [...selected.keys()].forEach(removeValue)
    };
}

const localizationPicker = setupMultiSelect('localization-type-select', 'localization-selected');
const disabilityPicker = setupMultiSelect('disability-type-select', 'disability-selected');

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    formData.append('gameName', document.getElementById('game-name').value);
    formData.append('modName', document.getElementById('mod-name').value);
    formData.append('developers', document.getElementById('mod-developers').value);
    formData.append('description', document.getElementById('description').value);
    formData.append('releaseDate', document.getElementById('mod-release-date').value);
    formData.append('image', document.getElementById('mod-image-submit').files[0]);
    formData.append('file', document.getElementById('mod-file').files[0]);

    localizationPicker.getValues().forEach(id => formData.append('localizationTypes', id));
    disabilityPicker.getValues().forEach(id => formData.append('disabilityTypes', id));

    const response = await fetch('/api/mods/upload', {
        method: 'POST',
        body: formData
    });

    if (!response.ok) return showResponse('Erro ao realizar upload de mod: ', response.error?.message ?? "Erro interno");

    const data = await response.text();

    showResponse('Sucesso: ' + data, null);
    form.reset();
    localizationPicker.clear();
    disabilityPicker.clear();
    preview.setAttribute('src', '#');
    preview.classList.remove('uploaded-mod-image');
});


const displayImage = () =>{
    const file = input.files;
    if (file && file[0]) {
        const fileReader = new FileReader();

        fileReader.onload = event => {
            preview.setAttribute('src', event.target.result);
            preview.classList.add("uploaded-mod-image");
        }

        fileReader.readAsDataURL(file[0]);
    }
}

function showResponse(text, error){
    const display = text + (error != null ? error : '');
    document.getElementById('responseMessage').innerText = display;
}

input.addEventListener("change", displayImage);
