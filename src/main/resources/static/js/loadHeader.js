fetch('header.html')
  .then(response => response.text())
  .then(html => {
    document.getElementById('header').innerHTML = html;
  })
  .catch(error => console.error("Erro carregando o Header: ", error));