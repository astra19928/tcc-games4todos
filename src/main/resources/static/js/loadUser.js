let cachedUser = null;

const loadUser = async () => {

    const response = await fetch('/api/login/me', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
    });

    if(response.redirected && response.url.includes('/login.html')) return { loggedIn: false }
    if(response.status === 401) return { loggedIn: false }
    if(!response.ok)            return { loggedIn: false }

    const data = await response.json();

    cachedUser = data;
    return data;
}

export async function getUser(){
    if(cachedUser)
        return cachedUser;
    return loadUser();
}