function performSearch() {
    const query = document.getElementById('search-input').value;


    if (query.length > 0) {

        fetch(`http://localhost:8085/game-boot/game/search?name=${query}`)
            .then(response => response.json())
            .then(data => {

                displaySearchResults(data);
            });
    } else {

        displaySearchResults([]);
    }
}

function displaySearchResults(results) {
    const resultsContainer = document.getElementById('search-results');

    resultsContainer.innerHTML = '';

    if (results.length > 0) {
        results.forEach(result => {
            const row = document.createElement('tr');

            const nameCell = document.createElement('td');
            const link = document.createElement('a');


            let gameText;
            if (result.gameDetail.price === 0) {
                gameText = `${result.name}<br/>Free to use`;
            } else {
                gameText = `${result.name}<br/>${result.gameDetail.price}₴`;
            }

            link.innerHTML = gameText;
            link.href = `http://localhost:8085/game-boot/game/${result.gameId}`;
            nameCell.appendChild(link);
            row.appendChild(nameCell);

            const coverCell = document.createElement('td');
            const coverImage = document.createElement('img');
            coverImage.src = result.photoUrl;
            coverImage.alt = result.name;
            coverImage.style.maxWidth = '50px';
            coverImage.style.maxHeight = '50px';
            coverImage.style.margin = 'auto'
            coverImage.classList.add('game-cover');
            coverCell.appendChild(coverImage);
            row.appendChild(coverCell);
            resultsContainer.appendChild(row);
        });
    }
}