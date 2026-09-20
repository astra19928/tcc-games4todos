document.getElementById('userForm').addEventListener('submit', async (event) => {
    event.preventDefault(); 

    const formData = {
        username: document.getElementById('name-create').value,
        email: document.getElementById('email-create').value,
        password: document.getElementById('password-create').value,
        thingo: document.getElementById('thingo').checked,
        thingoB: document.getElementById('thingoB').checked,
        thingoC: document.getElementById('thingoC').checked
    };

    try{
        const response = await fetch('/cadastro', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData) 
        });

        if (!response.ok) {
            throw new Error('Houve um problmma com a requisição!');
        }

        const data = await response.text(); 

        document.getElementById('responseMessage').innerText = "Succeso: " + data;
        document.getElementById('userForm').reset(); 
    } catch (error) {
        document.getElementById('responseMessage').innerText = "Erro enviando formulário:" + error.message;
        console.error('Error:', error);
    }
});