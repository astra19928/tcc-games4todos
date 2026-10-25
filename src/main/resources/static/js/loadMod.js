const urls = window.location.pathname.split('/');
const id = urls[2];
const message = document.getElementById('errorMessage');

async function loadMod() {
    const response = await fetch(`/api/mods/${id}`, {
        method: 'GET'
    });

    if(response.status === 404) return showError('Mod não encontrado.');
    if(response.status === 400) return showError('ID de mod inválido.');
    if(!response.ok)            return showError('Erro ao carregar mod.');

    const mod = await response.json();

    const gameTitle = mod.gameName + " (" + mod.modName + ")";
    const fileSize = mod.fileSize.toFixed(2);
    document.getElementById('game-title').textContent = gameTitle;
    //document.getElementById('mod-name').textContent = mod.modName;
    document.getElementById('dev-info').textContent = mod.developers;
    document.getElementById('uploader').textContent = mod.uploader;
    document.getElementById('release-date').textContent = mod.date;
    document.getElementById('description').textContent = mod.description;
    document.getElementById('downloads').textContent = mod.downloads;
    document.getElementById('file-size').textContent = fileSize;
    document.getElementById('mod-icon').src = `/api/mods/${id}/image`;

    document.getElementById('site').hidden = false;
    document.getElementById('download').href = `/api/mods/${id}/download`;

    window.document.title = mod.modName;
    message.hidden = true;
}

function showError(error){
    message.textContent = 'Erro! ' + error;
}

loadMod();
