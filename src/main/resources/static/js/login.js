document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = {
        email: document.getElementById('email-login').value,
        password: document.getElementById('password-login').value
    };

    try{
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error('Houve um problema com a requisição!');
        }

        const data = await response.text();

        document.getElementById('responseMessage').innerText = "Succeso: " + data;
        document.getElementById('loginForm').reset();

        window.location.replace("index.html");
    } catch (error) {
        document.getElementById('responseMessage').innerText = "Erro enviando formulário: " + error.message;
        console.error('Error:', error);
    }

});