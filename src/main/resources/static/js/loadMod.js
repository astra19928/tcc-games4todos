const urls = window.location.pathname.split('/');
const id = urls[2];

try{
    const response = await fetch(`/api/mod/${id}`, {
        method: 'GET'
    });

    if (!response.ok) {
        throw new Error('Houve um problema com a requisição!');
    }

    const data = await response.json();
    document.getElementById('dev-info').innerText += data.developers;
    document.getElementById('uploader').innerText += data.uploader;
    document.getElementById('release-date').innerText += data.date;
    document.getElementById('file-size').innerText += data.fileSize;

} catch (error) {
    document.getElementById('responseMessage').innerText = "Erro enviando formulário: " + error.message;
    console.error('Error:', error);
}