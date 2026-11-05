document.getElementById('login-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = {
        email: document.getElementById('email-login').value,
        password: document.getElementById('password-login').value
    };

    const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    });

    if (!response.ok) {
        showError(response.statusText || response.body || "Houve um erro interno");
        return;
    }

    document.getElementById('login-form').reset();
    window.location = response.url;
});

function showError(string){
    document.getElementById('responseMessage').innerText = "Erro enviando formulário: " + error.message;
    console.error('Error:', error);
}